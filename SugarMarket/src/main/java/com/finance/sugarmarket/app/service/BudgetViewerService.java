package com.finance.sugarmarket.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.budgetview.dto.AutoDebitDto;
import com.finance.sugarmarket.app.budgetview.dto.BudgetViewDto;
import com.finance.sugarmarket.app.budgetview.dto.ExpenseReportDto;
import com.finance.sugarmarket.app.budgetview.dto.TimeBasedSummary;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.model.BudgetView;
import com.finance.sugarmarket.app.repo.BudgetViewRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.app.repo.IncomeRepo;
import com.finance.sugarmarket.base.util.DateUtil;

@Service
public class BudgetViewerService {

	@Autowired
	private BudgetViewRepo budgetViewRepo;
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private IncomeRepo incomeRepo;
	@Autowired
	private ModelMapper modelMapper;

	public ExpenseReportDto getExpenseReport(Long userId, String month, Integer year) {
		Date today = DateUtil.getCurrentDate();
		ExpenseReportDto reportDto = new ExpenseReportDto();

		List<Object[]> expenseList = new ArrayList<>();
		List<BudgetView> budgetList = new ArrayList<>();
		BigDecimal totalIncome = BigDecimal.ZERO;
		if (StringUtils.isNotBlank(month)) {
			expenseList = expenseRepo.getMonthlyExpenseSummaryWithCashFlow(year, month, userId);
			budgetList = budgetViewRepo.findAllByMonth(year, month, userId);
			totalIncome = incomeRepo.getTotalMonthlyIncome(year, month, userId);

		} else {
			expenseList = expenseRepo.getYearlyExpenseSummaryWithCashFlow(year, userId);
			budgetList = budgetViewRepo.findAllByYear(year, userId);
			totalIncome = incomeRepo.getTotalYearlyIncome(year, userId);
		}

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

			if (expenseType.equals(CashFlowType.CREDITCARD)) {
				cardSpendAmount = cardSpendAmount.add(amount);
				summary.setCreditCardAmount(amount);
			} else {
				manualSpendAmount = manualSpendAmount.add(amount);
				summary.setManualAmount(amount);
			}

			timeBasedmap.put(key, summary);
		}

		reportDto.setManualSpendAmount(manualSpendAmount);
		reportDto.setCardSpendAmount(cardSpendAmount);
		reportDto.setTimeBasedSummary(new ArrayList<>(timeBasedmap.values()));

		setAutoDebit(budgetList, reportDto, today);
		reportDto.setTotalExpense(manualSpendAmount.add(reportDto.getAutoDebitAmount()));
		reportDto.setTotalImcome(totalIncome);
		reportDto.setAvailableAmount(totalIncome.subtract(reportDto.getTotalExpense()));

		return reportDto;
	}

	// in future we need to modify this based on the transaction sms
	private void setAutoDebit(List<BudgetView> budgetList, ExpenseReportDto reportDto, Date today) {
		int dueDateIndex = getDueDateIndex(budgetList, today);

		if (dueDateIndex < 0) {
			reportDto.setAutoDebitAmount(BigDecimal.ZERO);
			reportDto.setRemainingAutoDebit(getAutoDebit(budgetList));
		} else if (0 <= dueDateIndex && dueDateIndex < budgetList.size()) {
			reportDto.setAutoDebitAmount(budgetList.subList(0, dueDateIndex + 1).stream()
					.map(BudgetView::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
			if (dueDateIndex + 1 < budgetList.size()) {
				reportDto.setRemainingAutoDebit(getAutoDebit(budgetList.subList(dueDateIndex + 1, budgetList.size())));
			} else {
				reportDto.setRemainingAutoDebit(new AutoDebitDto(BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>()));
			}
		} else {
			reportDto.setAutoDebitAmount(
					budgetList.stream().map(BudgetView::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
			reportDto.setRemainingAutoDebit(new AutoDebitDto(BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>()));
		}
	}

	private int getDueDateIndex(List<BudgetView> list, Date dueDate) {
		int left = 0;
		int right = list.size() - 1;
		int splitIndex = -1;

		while (left <= right) {
			int mid = left + (right - left) / 2;

			if (list.get(mid).getDueDate().compareTo(dueDate) < 0) {
				splitIndex = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}

		return splitIndex;

	}

	private AutoDebitDto getAutoDebit(List<BudgetView> list) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		List<BudgetViewDto> details = new ArrayList<>();

		for (BudgetView view : list) {
			BudgetViewDto dto = modelMapper.map(view, BudgetViewDto.class);
			totalAmount = totalAmount.add(dto.getActualAmount());
			remainingAmount = remainingAmount.add(dto.getRemainingAmount());
			if (view.getCreditCard() != null) {
				dto.setLender(view.getCreditCard().getBankName() + " " + view.getCreditCard().getCreditCardName());
				dto.setLast4Digit(view.getCreditCard().getLast4Digit());
			} else if (view.getLoan() != null) {
				dto.setLender(view.getLoan().getLenderName());
				dto.setLast4Digit(view.getLoan().getLast4Digit());
			}
			details.add(dto);
		}
		return new AutoDebitDto(totalAmount, remainingAmount, details);
	}

	public AutoDebitDto getNextMonthBudget(Long userId) {
		LocalDate currentDate = DateUtil.getCurrentLocalDate();
		LocalDate nextMonthDate = currentDate.plusMonths(1);

		String month = nextMonthDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		Integer year = nextMonthDate.getYear();

		List<BudgetView> budgetList = budgetViewRepo.findAllByMonth(year, month, userId);

		return getAutoDebit(budgetList);

	}

	@Deprecated
	public BudgetView findBudgetById(Long id) {
		return budgetViewRepo.findById(id).get();
	}

	@Deprecated
	public void saveBudget(BudgetView budget) {
		budgetViewRepo.save(budget);
	}

	public void updateBudgetView(Date date, Long userId) {
		budgetViewRepo.updateBudgetView(date, userId);
	}

}
