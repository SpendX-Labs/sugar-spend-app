package com.finance.sugarmarket.app.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.budgetview.dto.ExpenseReportDto;
import com.finance.sugarmarket.app.budgetview.dto.TimeBasedSummary;
import com.finance.sugarmarket.app.enums.BudgetCalendar;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.model.BudgetView;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.repo.BudgetViewRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;

import io.micrometer.common.util.StringUtils;

@Service
public class BudgetViewerService {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private BudgetViewRepo budgetViewRepo;
	@Autowired
	private ExpenseRepo expenseRepo;

	public ExpenseReportDto getExpenseReport(Long userId, String month, Integer year) {
		ExpenseReportDto reportDto = new ExpenseReportDto();

		List<Object[]> expenseList = new ArrayList<>();
		if (month != null) {
			expenseList = expenseRepo.getMonthlyExpenseSummaryWithCashFlow(year, month, userId);
		} else {
			expenseList = expenseRepo.getYearlyExpenseSummaryWithCashFlow(year, userId);
		}

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal autoDebitAmount = BigDecimal.ZERO;
		BigDecimal manualSpendAmount = BigDecimal.ZERO;
		BigDecimal cardSpendAmount = BigDecimal.ZERO;

		Map<String, TimeBasedSummary> timeBasedmap = new HashMap<>();

		for (Object[] obj : expenseList) {
			String key = obj[0].toString();
			CashFlowType expenseType = CashFlowType.valueOf(obj[1].toString());
			BigDecimal amount = (BigDecimal) obj[2];

			TimeBasedSummary summary = null;
			if (timeBasedmap.containsKey(key)) {
				summary = timeBasedmap.get(key);
			} else {
				summary = new TimeBasedSummary();
				summary.setDataKey(key);
			}

			if (expenseType.equals(CashFlowType.CrediCard)) {
				cardSpendAmount = cardSpendAmount.add(amount);
				summary.setCreditCardAmount(amount);
			} else {
				manualSpendAmount = manualSpendAmount.add(amount);
				summary.setManualAmount(amount);
			}

			timeBasedmap.put(key, summary);

		}
		
		

		return reportDto;
	}

	public List<BudgetView> findAllBudget(Integer budgetYear, String budgetMonth, Long userId) {
		List<BudgetView> budgets = new ArrayList<>();
		if (StringUtils.isNotEmpty(budgetMonth)) {
			budgets = budgetViewRepo.findAllByMonthAndYear(budgetYear, budgetMonth, userId);
		} else {
			List<Object[]> list = budgetViewRepo.findAllByCreditCardYear(budgetYear, userId);
			Long id = 1L;
			for (Object[] ob : list) {
				BudgetView budget = new BudgetView(id, (CreditCard) ob[0], "All", budgetYear, (BigDecimal) ob[1],
						(BigDecimal) ob[2], null, null);
				budgets.add(budget);
				id++;
			}
		}
		budgets.forEach(x -> x.getCreditCard().setUser(null));
		return budgets;
	}

	public BudgetView findBudgetById(Long id) {
		return budgetViewRepo.findById(id).get();
	}

	public void saveBudget(BudgetView budget) {
		budgetViewRepo.save(budget);
	}

	public List<TimeBasedSummary> getBudgetChart(Integer budgetYear, String budgetMonth, Long userId) {
		List<TimeBasedSummary> budgetViewList = new ArrayList<>();
		Map<String, BigDecimal> map = new HashMap<>();
		List<Object[]> list = StringUtils.isNotEmpty(budgetMonth)
				? expenseRepo.getMonthlyExpenseSummary(budgetYear, budgetMonth, userId)
				: expenseRepo.getYearlyExpenseSummary(budgetYear, userId);
		for (Object[] ob : list) {
			String key = null;
			if (ob[0] instanceof Date) {
				key = dateFormat.format((Date) ob[0]);
			} else {
				key = (String) ob[0];
			}
			map.put(key, (BigDecimal) ob[1]);
		}
		if (StringUtils.isNotEmpty(budgetMonth)) {
			int noOfDays = BudgetCalendar.getDaysInMonth(budgetMonth, budgetYear);
			for (int day = 1; day <= noOfDays; day++) {
				String date = dateFormat.format(BudgetCalendar.constructDate(day, budgetMonth, budgetYear));
//				budgetViewList.add(new TimeBasedSummary(date, map.get(date) != null ? map.get(date) : BigDecimal.ZERO));
			}
		} else {
			for (String month : BudgetCalendar.getMonths()) {
//				budgetViewList
//						.add(new TimeBasedSummary(month, map.get(month) != null ? map.get(month) : BigDecimal.ZERO));
			}
		}
		return budgetViewList;
	}

	public List<TimeBasedSummary> getPieChart(Integer budgetYear, String budgetMonth, Long userId) {
		List<TimeBasedSummary> pieList = new ArrayList<>();
		List<Object[]> list = expenseRepo.getCardExpenseSummary(budgetYear, budgetMonth, userId);
//		list.forEach(x -> pieList.add(new TimeBasedSummary(
//				((CreditCard) x[0]).getBankName() + " " + ((CreditCard) x[0]).getCreditCardName(), (BigDecimal) x[1])));
		return pieList;
	}

	public Map<String, BigDecimal> getTotalAmount(Integer budgetYear, String budgetMonth, Long userId) {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("totalSpend", expenseRepo.getSumAmount(budgetYear, budgetMonth, userId));
		return map;
	}

	public void updateBudgetView(Date date, Long userId) {
		budgetViewRepo.updateBudgetView(date, userId);
	}

}
