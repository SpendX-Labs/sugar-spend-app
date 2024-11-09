package com.finance.sugarmarket.agent.jobs;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.model.Loan;
import com.finance.sugarmarket.app.repo.BudgetViewRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.app.repo.LoanRepo;
import com.finance.sugarmarket.base.util.DateUtil;

public class LoanSchedulerAgent implements Job {

	@Autowired
	private LoanRepo loanRepo;
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private BudgetViewRepo budgetViewRepo;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		List<Loan> loans = loanRepo.findAll();

		for (Loan loan : loans) {
			if (loan.getCreditCard() != null) {
				// principal
				BigDecimal interest = loan.getRemainingPrincipalAmount();
				Expense expense = new Expense();
				expense.setUser(loan.getUser());
				expense.setExpenseType(CashFlowType.CREDITCARD);
				expense.setCreditCard(loan.getCreditCard());
				expense.setExpenseDate(DateUtil.getCurrentDate());
				expense.setExpenseTime(LocalTime.now());
				expense.setAmount(null);
			}
		}
	}

}
