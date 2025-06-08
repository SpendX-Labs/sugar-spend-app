package com.finance.sugarmarket.test;


import com.finance.sugarmarket.app.utils.BillingCycleUtils;
import org.springframework.data.util.Pair;

import java.util.Date;

public class BillingTest {
    public static void main(String[] args) {
        Pair<Date, Date> dates = BillingCycleUtils.findDateRangeByTransactionDate(new Date(), 11);
        System.out.println(dates.getFirst());
        System.out.println(dates.getSecond());
        System.out.println(BillingCycleUtils.getDueDate(dates.getSecond(), 31));
    }
}
