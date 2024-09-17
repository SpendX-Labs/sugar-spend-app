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

import com.finance.sugarmarket.app.dto.BudgetDto;
import com.finance.sugarmarket.app.enums.BudgetCalendar;
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
	
	public List<BudgetView> findAllBudget(Integer budgetYear, String budgetMonth, String userName) {
		List<BudgetView> budgets = new ArrayList<>();
		if(StringUtils.isNotEmpty(budgetMonth)) {
			budgets = budgetViewRepo.findAllByMonthAndYear(budgetYear, budgetMonth, userName);
		}
		else {
			List<Object[]> list = budgetViewRepo.findAllByYear(budgetYear, userName);
			int id = 1;
			for(Object[] ob : list) {
				BudgetView budget = new BudgetView(id, 
						(CreditCard) ob[0], "All", budgetYear, 
						(BigDecimal) ob[1], (BigDecimal) ob[2], null, null);
				budgets.add(budget);
				id++;
			}
		}
		budgets.forEach(x->x.getCreditCard().setUser(null));
		return budgets;
	}
	
	public BudgetView findBudgetById(Integer id) {
		return budgetViewRepo.findById(id).get();
	}
		 
	public void saveBudget(BudgetView budget) {
		budgetViewRepo.save(budget);
	}
	
	public List<BudgetDto> getBudgetChart(Integer budgetYear, String budgetMonth, String userName) {
		List<BudgetDto> budgetViewList = new ArrayList<>();
		Map<String, BigDecimal> map = new HashMap<>();
		List<Object[]> list = StringUtils.isNotEmpty(budgetMonth) ? 
				expenseRepo.getMonthlyExpenseSummary(budgetYear, budgetMonth, userName)
				: expenseRepo.getYearlyExpenseSummary(budgetYear, userName);
		for(Object[] ob : list) {
			String key = null;
			if(ob[0] instanceof Date) {
				key = dateFormat.format((Date) ob[0]);
			}
			else {
				key = (String) ob[0];
			}
			map.put(key, (BigDecimal) ob[1]); 
		}
		if(StringUtils.isNotEmpty(budgetMonth)) {
			int noOfDays = BudgetCalendar.getDaysInMonth(budgetMonth, budgetYear);
			for(int day=1;day<=noOfDays;day++) {
				String date = dateFormat.format(BudgetCalendar.constructDate(day, budgetMonth, budgetYear));
				budgetViewList.add(new BudgetDto(date, map.get(date) != null ? map.get(date) : BigDecimal.ZERO));
			}
		}
		else {
			for(String month : BudgetCalendar.getMonths()) {
				budgetViewList.add(new BudgetDto(month, map.get(month) != null ? map.get(month) : BigDecimal.ZERO));
			}
		}
		return budgetViewList;
	}
	
	public List<BudgetDto> getPieChart(Integer budgetYear, String budgetMonth, String userName) {
		List<BudgetDto> pieList = new ArrayList<>();
		List<Object[]> list = expenseRepo.getCardExpenseSummary(budgetYear, budgetMonth, userName);
		list.forEach(x->pieList.add(new BudgetDto(
				((CreditCard)x[0]).getBankName() + " " + ((CreditCard)x[0]).getCreditCardName()
				, (BigDecimal) x[1])));
		return pieList;
	}
	
	public Map<String, BigDecimal> getTotalAmount(Integer budgetYear, String budgetMonth, String userName) {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("totalSpend", expenseRepo.getSumAmount(budgetYear, budgetMonth, userName));
		return map;
	}
	
	public void updateBudgetView(Date date, Integer userId) {
		budgetViewRepo.updateBudgetView(date, userId);
	}
	
	
}
