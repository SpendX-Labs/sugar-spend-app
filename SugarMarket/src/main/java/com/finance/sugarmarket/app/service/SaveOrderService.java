package com.finance.sugarmarket.app.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.MarketData;
import com.finance.sugarmarket.app.model.OrderDetail;
import com.finance.sugarmarket.app.repo.MutualFundRepo;
import com.finance.sugarmarket.app.repo.OrderRepo;
import com.finance.sugarmarket.app.utils.MarketDataUtil;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.constants.MFConstants;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SaveOrderService {

	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private MutualFundRepo mfRepo;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	private final ExecutorService executorService = Executors.newCachedThreadPool();

	@Transactional
	public String saveInvestment(OrderDetail detail) throws Exception {
		detail.setDateOfEvent(sdf.parse(sdf.format(detail.getDateOfEvent())));
		if (detail.getDateOfEvent().compareTo(new Date()) >= 0) {
			// will update this feature
			return sdf.format(detail.getDateOfEvent()) + " is current or future date.";
		}
		if (StringUtils.isNotEmpty(detail.getMutualFund().getSchemeCode())) {
			detail.setMutualFund(mfRepo.findMutualFundBySchemeCode(detail.getMutualFund().getSchemeCode()));
		} else if (StringUtils.isNotEmpty(detail.getMutualFund().getSchemeName())) {
			detail.setMutualFund(mfRepo.findMutualFundBySchemeName(detail.getMutualFund().getSchemeName()));
		} else {
			return "Either Scheme Name or Code required.";
		}
		Double nav = detail.getNav();
		if (nav == null) {
			MarketData marketData = MarketDataUtil.getHistoricalData(detail.getMutualFund().getSchemeCode());
			if (marketData.getNavData().get(detail.getDateOfEvent()) == null) {
				return sdf.format(detail.getDateOfEvent()) + " is holiday.";
			}
			nav = Double.valueOf(marketData.getNavData().get(detail.getDateOfEvent()));
			detail.setNav(nav);
			detail.setUnits(detail.getAmount() / nav);
		}
		orderRepo.save(detail);
		return MFConstants.SUCCESS;
	}

	public void saveFile(File file, MFUser user, String password) throws Exception {
		executorService.submit(() -> {
			processFile(file, user, password);
			return null;
		});
	}

	public void processFile(File file, MFUser user, String password) throws Exception {

	}
}
