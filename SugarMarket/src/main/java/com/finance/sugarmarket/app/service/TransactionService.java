package com.finance.sugarmarket.app.service;

import com.finance.sugarmarket.agent.constants.ScheduledJob;
import com.finance.sugarmarket.agent.service.SchedulerService;
import com.finance.sugarmarket.app.dto.CashFlowDetailDto;
import com.finance.sugarmarket.app.dto.TransactionDto;
import com.finance.sugarmarket.app.dto.TransactionMobileInputDto;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.enums.TransactionType;
import com.finance.sugarmarket.app.model.BankAccount;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.model.Transaction;
import com.finance.sugarmarket.app.repo.BankAccountRepo;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.TransactionRepo;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionService extends SpecificationService<Transaction> {

    @Autowired
    private CreditCardRepo creditCardRepo;
    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private MFUserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SchedulerService schedulerService;

    private static final Map<String, String> filterMap = new HashMap<>();

    static {
        filterMap.put(FieldConstant.USER_ID, "user.id");
        filterMap.put(FieldConstant.CREDIT_CARD_NAME, "creditCard.creditCardName");
        filterMap.put(FieldConstant.BANK_ACCOUNT_NAME, "bankAccount.bankName");
        filterMap.put(FieldConstant.BANK_NAME_FOR_CARD, "creditCard.bankName");
        filterMap.put(FieldConstant.BANK_LAST_4_DIGIT, "bankAccount.last4Digit");
        filterMap.put(FieldConstant.CARD_LAST_4_DIGIT, "creditCard.last4Digit");
    }

    public ListViewDto<TransactionDto> findAllTransactions(PageRequest pageRequest, List<Filter> filters) throws Exception {
        Specification<Transaction> specificationFilters = getSpecificationFilters(filters, filterMap);
        Page<Transaction> pages = transactionRepo.findAll(specificationFilters, pageRequest);
        List<TransactionDto> listDto = new ArrayList<>();
        for (Transaction transaction : pages.getContent()) {
            TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
            if (transaction.getCreditCard() != null) {
                String creditCardName = transaction.getCreditCard().getBankName() + " "
                        + transaction.getCreditCard().getCreditCardName() + " (XXXX"
                        + transaction.getCreditCard().getLast4Digit() + ") ";
                transactionDto.setCashFlowDetails(new CashFlowDetailDto(transaction.getCreditCard().getId(), creditCardName, transaction.getCreditCard().getLast4Digit()));
            } else if (transaction.getBankAccount() != null) {
                String bankAccountName = transaction.getBankAccount().getBankName() + " (XXXX"
                        + transaction.getBankAccount().getLast4Digit() + ") ";
                transactionDto.setCashFlowDetails(new CashFlowDetailDto(transaction.getBankAccount().getId(), bankAccountName, transaction.getBankAccount().getLast4Digit()));
            }
            listDto.add(transactionDto);
        }
        return new ListViewDto<>(listDto, pages.getTotalElements(), pageRequest.getOffset(),
                pageRequest.getPageSize());
    }

    public void saveTransaction(TransactionDto transactionDto, Long userId) throws Exception {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        MFUser user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            transaction.setUser(user);
            persistTransaction(transactionDto, transaction, userId);
        }
    }

    public void updateTransaction(TransactionDto transactionDto, Long id, Long userId) throws Exception {
        Specification<Transaction> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
        List<Transaction> transactionList = transactionRepo.findAll(specificationFilters);
        if (transactionList.isEmpty()) {
            throw new Exception("You are not authorised to modify");
        }
        Transaction existingTransaction = transactionList.get(0);
        modelMapper.map(transactionDto, existingTransaction);
        existingTransaction.setId(id);
        persistTransaction(transactionDto, existingTransaction, userId);
    }

    private void persistTransaction(TransactionDto transactionDto, Transaction transaction, Long userId) throws Exception {
        Long cashFlowId = transactionDto.getCashFlowDetails().getCashFlowId();
        if (cashFlowId != null) {
            if (transactionDto.getTransactionType().equals(TransactionType.CREDITCARD)) {
                CreditCard creditCard = creditCardRepo.findById(cashFlowId).orElse(null);
                if (creditCard == null || !Objects.equals(creditCard.getUser().getId(), userId)) {
                    throw new Exception("user is different from the credit card.");
                }
                transaction.setCreditCard(creditCard);
            } else if (transactionDto.getTransactionType().equals(TransactionType.BANK)) {
                BankAccount bankAccount = bankAccountRepo.findById(cashFlowId).orElse(null);
                if (bankAccount == null || !Objects.equals(bankAccount.getUser().getId(), userId)) {
                    throw new Exception("user is different from the Bank Account.");
                }
                transaction.setBankAccount(bankAccount);
            }
        }
        transactionRepo.saveAndFlush(transaction);
        postCreditCardTransaction(transaction);
    }

    public void deleteTransaction(Long id, Long userId) throws Exception {
        Specification<Transaction> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
        List<Transaction> transactionList = transactionRepo.findAll(specificationFilters);
        if (transactionList.isEmpty()) {
            throw new Exception("You are not authorised to delete");
        }
        Transaction existingTransaction = transactionList.get(0);
        transactionRepo.deleteById(existingTransaction.getId());
        postCreditCardTransaction(existingTransaction);
    }

    public void saveTransactionForMobile(TransactionMobileInputDto transaction, Long userId) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(transaction.getEventDateTime(), formatter);
        Date date = Date.from(zonedDateTime.toInstant());
        String last4Digits = "%" + transaction.getLast4Digit();
        Transaction newTransaction = new Transaction();
        newTransaction.setCashFlowType(transaction.getCashFlowType());
        newTransaction.setAmount(transaction.getTransactionAmount());
        newTransaction.setTransactionDate(date);
        CreditCard creditCard = creditCardRepo.findByUserIdAndLast4Digit(userId, last4Digits);
        if (creditCard != null) {
            newTransaction.setUser(creditCard.getUser());
            newTransaction.setTransactionType(TransactionType.CREDITCARD);
            newTransaction.setCreditCard(creditCard);
        } else {
            BankAccount bankAccount = bankAccountRepo.findByUserIdAndLast4Digit(userId, last4Digits);
            if (bankAccount != null) {
                newTransaction.setUser(bankAccount.getUser());
                newTransaction.setTransactionType(TransactionType.BANK);
                newTransaction.setBankAccount(bankAccount);
            } else {
                throw new Exception("Please add that card ending with: " + transaction.getLast4Digit());
            }
        }
        transactionRepo.saveAndFlush(newTransaction);
        postCreditCardTransaction(newTransaction);
    }

    private void postCreditCardTransaction(Transaction transaction) throws Exception {
        //post persist trigger agent to update budget view
        if (transaction.getTransactionType().equals(TransactionType.CREDITCARD) && transaction.getCashFlowType().equals(CashFlowType.DEBIT)) {
            postTransactionUpdateUpdateBudget(transaction);
        }
    }

    private void postTransactionUpdateUpdateBudget(Transaction transaction) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(AppConstants.DATE, transaction.getTransactionDate());
        dataMap.put(AppConstants.CREDIT_CARD_ID, transaction.getCreditCard().getId());
        schedulerService.triggerAgentByClass(ScheduledJob.UPDATE_BUDGET_AGENT, dataMap);
    }
}
