package com.finance.sugarmarket.app.cache;

import com.finance.sugarmarket.app.budgetview.dto.BudgetDetails;
import com.finance.sugarmarket.app.enums.TransactionType;
import com.finance.sugarmarket.app.repo.BudgetViewRepo;
import com.finance.sugarmarket.app.repo.TransactionRepo;
import com.finance.sugarmarket.base.cache.AbstractCacheProvider;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.RedisConstants;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class BudgetDetailsCacheProvider extends AbstractCacheProvider<BudgetDetails> {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private BudgetViewRepo budgetViewRepo;
    @Autowired
    private ModelMapper modelMapper;

    public BudgetDetailsCacheProvider() {
        super(BudgetDetails.class);
    }

    @Override
    public String getFinalKey(String key) {
        return RedisConstants.PREVIOUS_DETAIL + AppConstants.HASH_DELIMITER + key;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(12);
    }

    private String buildCacheKey(Long userId, Integer year, String month) {
        return String.format("%d_%d_%s", userId, year,
                StringUtils.isNotBlank(month) ? month : "year");
    }

    public BudgetDetails getBudgetDetails(Long userId, Integer year, String month) {
        BudgetDetails budgetDetails = get(buildCacheKey(userId, year, month));
        if (budgetDetails != null) {
            return budgetDetails;
        }
        budgetDetails = new BudgetDetails();

        List<TransactionType> manualSpendTypes = List.of(TransactionType.BANK, TransactionType.CASH);
        List<TransactionType> credCardSpendTypes = List.of(TransactionType.CREDITCARD);
        if (StringUtils.isNotBlank(month)) {
            budgetDetails.setManualSpendAmount(transactionRepo.getTotalMonthlyExpenseByTransactionType(year, month, manualSpendTypes, userId));
            budgetDetails.setCardSpendAmount(transactionRepo.getTotalMonthlyExpenseByTransactionType(year, month, credCardSpendTypes, userId));
        } else {
            budgetDetails.setManualSpendAmount(transactionRepo.getTotalYearlyExpenseByTransactionType(year, manualSpendTypes, userId));
            budgetDetails.setCardSpendAmount(transactionRepo.getTotalYearlyExpenseByTransactionType(year, credCardSpendTypes, userId));
        }

        return budgetDetails;
    }

    public void invalidateCache(Long userId, Integer year, String month) {
        String key = buildCacheKey(userId, year, month);
        remove(key);
    }
}