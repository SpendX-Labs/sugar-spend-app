package com.finance.sugarmarket.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.FundData;
import com.finance.sugarmarket.app.dto.MutualFundPortfolio;
import com.finance.sugarmarket.app.model.MutualFund;
import com.finance.sugarmarket.app.model.OrderDetail;
import com.finance.sugarmarket.app.repo.OrderRepo;
import com.finance.sugarmarket.app.utils.MathUtil;
import com.finance.sugarmarket.constants.AppConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

	@Autowired
	private OrderRepo orderRepo;

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	public MutualFundPortfolio getMutualFundPortfolio(String userName) {
		log.info("Start logging for " + userName);
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		List<OrderDetail> orderDetails = orderRepo.findByUserUsernameOrderByDateOfEventAsc(userName);

		List<FundData> fundData = getAllFundDataByUser(orderDetails, executorService);

		Double currentAmount = 0.0;
		Double investedAmount = 0.0;
		Double dayChange = 0.0;
		Double preveousDayAmount = 0.0;
		for (FundData fund : fundData) {
			currentAmount += fund.getCurrentAmount();
			investedAmount += fund.getInvestedAmount();
			dayChange += fund.getDay1ChangeAmount();
			preveousDayAmount = preveousDayAmount + fund.getCurrentAmount() - fund.getDay1ChangeAmount();
		}

		Double returnAmount = currentAmount - investedAmount;
		Double returnPercentage = returnAmount / investedAmount * 100;
		Double xirr = findAvgXIRR(fundData, currentAmount);
		Double day1Return = (currentAmount - preveousDayAmount) / preveousDayAmount * 100;

		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		return new MutualFundPortfolio(currentAmount, investedAmount, returnAmount, returnPercentage, xirr, dayChange,
				day1Return, fundData);

	}

	private List<FundData> getAllFundDataByUser(List<OrderDetail> orderDetails, ExecutorService executorService) {
		List<FundData> list = new ArrayList<>();

		Map<String, List<OrderDetail>> orderDetailMap = orderDetails.stream()
				.collect(Collectors.groupingBy(ord -> ord.getMutualFund().getSchemeCode()));

		List<Future<FundData>> futures = new ArrayList<>();

		for (String key : orderDetailMap.keySet()) {
			Future<FundData> future = executorService.submit(() -> createFundData(orderDetailMap.get(key), key));
			futures.add(future);
		}

		Integer id = 1;
		for (Future<FundData> future : futures) {
			try {
				FundData data = future.get();
				data.setId(id++);
				list.add(data);
			} catch (Exception e) {
				// Handle exceptions
				e.printStackTrace();
			}
		}

		return list;
	}

	private FundData createFundData(List<OrderDetail> orderDetails, String schemeCode) throws Exception {

		// fetch market data

		FundData data = new FundData();
		MutualFund fund = orderDetails.get(0).getMutualFund();
		data.setFundName(fund.getSchemeName());
		
		Date updatedDate = fund.getLastNavDate();
		Double currentNav = fund.getCurrentNav();
		

		Double investedAmount = 0.0;
		Double totalUnits = 0.0;
		List<Double> amounts = new ArrayList<>();
		List<Date> dates = new ArrayList<>();
		Queue<OrderDetail> queue = new LinkedList<>();
		for (OrderDetail ord : orderDetails) {
			ord.setUser(null);
			if (ord.getSide().equals(AppConstants.BUY)) {
				queue.offer(ord);
				investedAmount += ord.getAmount();
				totalUnits += ord.getUnits();
				amounts.add(ord.getAmount());
			}
			if (ord.getSide().equals(AppConstants.SELL)) {
				investedAmount -= getSellInvestedAmount(ord.getAmount(), queue, ord.getUnits());
				totalUnits -= ord.getUnits();
				amounts.add(-1 * ord.getAmount());
			}
			dates.add(ord.getDateOfEvent());

			Double returnP = ((currentNav - ord.getNav()) / ord.getNav()) * 100;
			Double returnValue = (ord.getAmount() * returnP) / 100;
			ord.setTotalReturn(String.format("%.2f", returnValue) + "(" + String.format("%.2f", returnP) + "%)");
			ord.setCurrenValue(ord.getAmount() + returnValue);
		}
		
		//data.setMeta(marketData.getMeta());

		Double currentValue = currentNav * totalUnits;
		Double returnPercentage = totalUnits > 0.0 ? MathUtil.getReturnPercentage(amounts, currentValue) : null;
		Double temp = currentValue * -1;
		amounts.add(temp);
		dates.add(updatedDate);
		Double xirr = totalUnits > 0.0 ? MathUtil.findXIRR(0.1, amounts, dates) : null;

		// set values
		data.setCurrentAmount(currentValue);
		data.setInvestedAmount(investedAmount);
		data.setTotalUnits(totalUnits);
		data.setReturnPercentage(returnPercentage);
		data.setReturnAmount(currentValue - investedAmount);
		data.setXirrValue(xirr);
		data.setCurrentNav(currentNav);
		data.setOrderDetails(orderDetails);
		data.setUpdatedDate(updatedDate);

		// set historical data
		// 1 day
		if (fund.getDay1Nav() != null) {	
			data.setDay1Change(((currentNav - fund.getDay1Nav()) * 100) / fund.getDay1Nav());
			data.setDay1ChangeAmount(currentValue - (totalUnits * fund.getDay1Nav()));
		}

		// 1 W
		if (fund.getWeek1Nav() != null) {
			data.setWeek1Change(((currentNav - fund.getWeek1Nav()) * 100) / fund.getWeek1Nav());
		}

		// 1 M
		if (fund.getMonth1Nav() != null) {
			data.setMonth1Change(((currentNav - fund.getMonth1Nav()) * 100) / fund.getMonth1Nav());
		}

		// 6 M
		if (fund.getMonth6Nav() != null) {
			data.setMonth6Change(((currentNav - fund.getMonth6Nav()) * 100) / fund.getMonth6Nav());
		}

		// 1 Y
		if (fund.getYear1Nav() != null) {
			data.setYear1Change(((currentNav - fund.getYear1Nav()) * 100) / fund.getYear1Nav());
		}

		// 3 Y
		if (fund.getYear3Nav() != null) {
			data.setYear3Change(((currentNav - fund.getYear3Nav()) * 100) / fund.getYear3Nav());
		}

		// 5 Y
		if (fund.getYear5Nav() != null) {
			data.setYear5Change(((currentNav - fund.getYear5Nav()) * 100) / fund.getYear5Nav());
		}

		// All
		data.setAllTimeChange(((currentNav - fund.getFirstNav()) * 100) / fund.getFirstNav());

		return data;
	}

	private Double getSellInvestedAmount(Double amount, Queue<OrderDetail> queue, Double units) {
		Double sellInvestedAmount = 0.0;

		while (!queue.isEmpty() && units > 0.0) {
			if (queue.peek().getUnits().compareTo(units) == 1) {
				sellInvestedAmount += units * queue.peek().getNav();
				units = 0.0;
				queue.peek().setUnits(queue.peek().getUnits() - units);
			} else if (queue.peek().getUnits().equals(units)) {
				sellInvestedAmount += units * queue.peek().getNav();
				units = 0.0;
				queue.poll();
			} else { // queue.peek().getUnits() < units
				sellInvestedAmount += queue.peek().getUnits() * queue.peek().getNav();
				units = units - queue.peek().getUnits();
				queue.poll();
			}
		}

		return sellInvestedAmount;
	}

	private Double findAvgXIRR(List<FundData> fundData, Double currentAmount) {
		TreeMap<Date, Double> dateToTotalAmount = new TreeMap<>();

		for (FundData data : fundData) {
			if (data.getTotalUnits().equals(0.0))
				continue;
			for (OrderDetail orderDetail : data.getOrderDetails()) {
				dateToTotalAmount.merge(orderDetail.getDateOfEvent(),
						orderDetail.getSide().equals(AppConstants.BUY) ? orderDetail.getAmount()
								: -1 * orderDetail.getAmount(),
						Double::sum);
			}
		}

		List<Date> dates = new ArrayList<>(dateToTotalAmount.keySet());
		List<Double> amounts = new ArrayList<>(dateToTotalAmount.values());

		dates.add(new Date(System.currentTimeMillis() - AppConstants.ONE_DAY_IN_MILLIS));
		amounts.add(currentAmount * -1);

		return MathUtil.findXIRR(0.1, amounts, dates);
	}

}
