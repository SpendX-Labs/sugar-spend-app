package com.finance.sugarmarket.app.budgetview.dto;

import com.finance.sugarmarket.app.model.BudgetView;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseData {
    private List<Object[]> expenseList;
    private List<BudgetView> budgetList;
    private BigDecimal totalIncome;

    public List<Object[]> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Object[]> expenseList) {
        this.expenseList = expenseList;
    }

    public List<BudgetView> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List<BudgetView> budgetList) {
        this.budgetList = budgetList;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
}