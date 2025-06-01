package com.finance.sugarmarket.app.service;

import com.finance.sugarmarket.app.dto.TransactionDto;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.enums.TransactionType;
import com.finance.sugarmarket.app.model.BankAccount;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.model.Income;
import com.finance.sugarmarket.app.repo.BankAccountRepo;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.app.repo.IncomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private CreditCardRepo creditCardRepo;
    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private IncomeRepo incomeRepo;

    public void saveTransaction(TransactionDto transaction, Long userId) throws Exception {
        //find from credit card
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(transaction.getEventDateTime(), formatter);
        Date date = Date.from(zonedDateTime.toInstant());
        LocalTime time = zonedDateTime.toLocalTime();
        String last4Digits = "%" + transaction.getLast4Digit();
        CreditCard creditCard = creditCardRepo.findByUserIdAndLast4Digit(userId, last4Digits);
        if (creditCard != null) {
            if(TransactionType.DEBIT.equals(transaction.getTransactionType())) {
                Expense expense = new Expense();
                expense.setUser(creditCard.getUser());
                expense.setExpenseType(CashFlowType.CREDITCARD);
                expense.setCreditCard(creditCard);
                expense.setAmount(transaction.getTransactionAmount());
                expense.setExpenseDate(date);
                expense.setExpenseTime(time);
                expenseRepo.saveAndFlush(expense);
            } else {
                Income income = new Income();
                income.setUser(creditCard.getUser());
                income.setIncomeType(CashFlowType.CREDITCARD);
                income.setCreditCard(creditCard);
                income.setAmount(transaction.getTransactionAmount());
                income.setDateOfEvent(date);
                income.setTimeOfEvent(time);
                incomeRepo.saveAndFlush(income);
            }
        } else {
            BankAccount bankAccount = bankAccountRepo.findByUserIdAndLast4Digit(userId, last4Digits);
            if (bankAccount != null) {
                if(TransactionType.DEBIT.equals(transaction.getTransactionType())) {
                    Expense expense = new Expense();
                    expense.setUser(bankAccount.getUser());
                    expense.setExpenseType(CashFlowType.BANK);
                    expense.setBankAccount(bankAccount);
                    expense.setAmount(transaction.getTransactionAmount());
                    expense.setExpenseDate(date);
                    expense.setExpenseTime(time);
                    expenseRepo.saveAndFlush(expense);
                } else {
                    Income income = new Income();
                    income.setUser(bankAccount.getUser());
                    income.setIncomeType(CashFlowType.BANK);
                    income.setBankAccount(bankAccount);
                    income.setAmount(transaction.getTransactionAmount());
                    income.setDateOfEvent(date);
                    income.setTimeOfEvent(time);
                    incomeRepo.saveAndFlush(income);
                }
            } else {
                throw new Exception("Please add that card ending with: " + transaction.getLast4Digit());
            }
        }
    }
}
