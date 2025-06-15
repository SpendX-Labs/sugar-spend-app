package com.finance.sugarmarket.app.service;

import com.finance.sugarmarket.app.budgetview.dto.AutoDebitDto;
import com.finance.sugarmarket.app.model.BudgetView;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.utils.BillingCycleUtils;
import com.finance.sugarmarket.base.util.DateUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class BudgetViewerService extends BudgetBaseService {

    public AutoDebitDto getNextMonthBudget(Long userId) {
        LocalDate currentDate = DateUtil.getCurrentLocalDate();
        LocalDate nextMonthDate = currentDate.plusMonths(1);

        String month = nextMonthDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        Integer year = nextMonthDate.getYear();

        List<BudgetView> budgetList = budgetViewRepo.findAllByMonth(year, month, userId);

        return getAutoDebit(budgetList);
    }

    public void updateBudgetView(Date date, Long creditCardId) {
        CreditCard creditCard = creditCardRepo.findById(creditCardId).orElse(null);
        if (creditCard != null) {
            Pair<Date, Date> billingRange = BillingCycleUtils.findDateRangeByTransactionDate(date, creditCard.getStatementDate());
            BigDecimal totalExpense = transactionRepo.getTotalExpenseOfCreditCardByDateRange(billingRange.getFirst(), billingRange.getSecond(), creditCardId);
            Date dueDate = BillingCycleUtils.getDueDate(billingRange.getSecond(), creditCard.getDueDate());
            Integer dueYear = BillingCycleUtils.getYearByDate(dueDate);
            String dueMonth = BillingCycleUtils.getMonthByDate(dueDate);

            BudgetView budgetView = budgetViewRepo.findByCreditCardIdAndMonthAndYear(creditCardId, dueMonth, dueYear);

            if (budgetView != null) {
                budgetView.setActualAmount(totalExpense);
                budgetView.setRemainingAmount(totalExpense);
                budgetView.setUpdateDate(date);
            } else {
                budgetView = new BudgetView();
                budgetView.setUser(creditCard.getUser());
                budgetView.setCreditCard(creditCard);
                budgetView.setBudgetYear(BillingCycleUtils.getYearByDate(dueDate));
                budgetView.setBudgetMonth(BillingCycleUtils.getMonthByDate(dueDate));
                budgetView.setActualAmount(totalExpense);
                budgetView.setRemainingAmount(totalExpense);
                budgetView.setDueDate(dueDate);
                budgetView.setUpdateDate(date);
            }
            budgetViewRepo.save(budgetView);
        }
    }

}
