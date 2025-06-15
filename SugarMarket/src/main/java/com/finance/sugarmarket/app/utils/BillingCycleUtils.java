package com.finance.sugarmarket.app.utils;

import org.springframework.data.util.Pair;

import java.util.Calendar;
import java.util.Date;

public final class BillingCycleUtils {

    private static final String[] monthNames = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private BillingCycleUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Pair<Date, Date> findDateRangeByTransactionDate(Date transactionDate, Integer statementDay) {
        validateInputs(transactionDate, statementDay);

        Calendar cal = Calendar.getInstance();
        cal.setTime(transactionDate);

        int transactionDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.setTime(transactionDate);
        endCal.setTime(transactionDate);

        if (transactionDay >= statementDay) {
            calculateCurrentMonthCycle(startCal, endCal, statementDay);
        } else {
            calculatePreviousMonthCycle(startCal, endCal, statementDay);
        }

        setTimeToStartOfDay(startCal);
        setTimeToEndOfDay(endCal);

        return Pair.of(startCal.getTime(), endCal.getTime());
    }

    public static Date getBillingCycleStartDate(Date transactionDate, Integer statementDay) {
        return findDateRangeByTransactionDate(transactionDate, statementDay).getFirst();
    }

    public static Date getBillingCycleEndDate(Date transactionDate, Integer statementDay) {
        return findDateRangeByTransactionDate(transactionDate, statementDay).getSecond();
    }

    public static boolean isDateInBillingCycle(Date dateToCheck, Date cycleStartDate, Date cycleEndDate) {
        if (dateToCheck == null || cycleStartDate == null || cycleEndDate == null) {
            throw new IllegalArgumentException("All dates must be non-null");
        }

        return !dateToCheck.before(cycleStartDate) && !dateToCheck.after(cycleEndDate);
    }

    public static long getBillingCycleDays(Date cycleStartDate, Date cycleEndDate) {
        if (cycleStartDate == null || cycleEndDate == null) {
            throw new IllegalArgumentException("Cycle dates must be non-null");
        }

        long diffInMillis = cycleEndDate.getTime() - cycleStartDate.getTime();
        return (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
    }

    public static Date getDueDate(Date statementDate, Integer billDay) {
        if (statementDate == null || billDay == null) {
            throw new IllegalArgumentException("Statement date and bill day cannot be null");
        }

        if (billDay < 1 || billDay > 31) {
            throw new IllegalArgumentException("Bill day must be between 1 and 31");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(statementDate);

        int statementDay = cal.get(Calendar.DAY_OF_MONTH);

        if (billDay <= statementDay) {
            cal.add(Calendar.MONTH, 1);
        }

        adjustForMonthEnd(cal, billDay);
        setTimeToStartOfDay(cal);
        return cal.getTime();
    }

    public static Integer getYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static String getMonthByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return monthNames[cal.get(Calendar.MONTH)];
    }

    private static void validateInputs(Date transactionDate, Integer statementDay) {
        if (transactionDate == null || statementDay == null) {
            throw new IllegalArgumentException("Transaction date and statement day cannot be null");
        }

        if (statementDay < 1 || statementDay > 31) {
            throw new IllegalArgumentException("Statement day must be between 1 and 31");
        }
    }

    private static void calculateCurrentMonthCycle(Calendar startCal, Calendar endCal, int statementDay) {
        startCal.set(Calendar.DAY_OF_MONTH, statementDay);
        adjustForMonthEnd(startCal, statementDay);

        endCal.add(Calendar.MONTH, 1);
        endCal.set(Calendar.DAY_OF_MONTH, statementDay - 1);
        adjustForMonthEnd(endCal, statementDay - 1);
    }

    private static void calculatePreviousMonthCycle(Calendar startCal, Calendar endCal, int statementDay) {
        startCal.add(Calendar.MONTH, -1);
        startCal.set(Calendar.DAY_OF_MONTH, statementDay);
        adjustForMonthEnd(startCal, statementDay);

        endCal.set(Calendar.DAY_OF_MONTH, statementDay - 1);
        adjustForMonthEnd(endCal, statementDay - 1);
    }

    private static void adjustForMonthEnd(Calendar cal, int targetDay) {
        int maxDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (targetDay > maxDayInMonth) {
            cal.set(Calendar.DAY_OF_MONTH, maxDayInMonth);
        } else cal.set(Calendar.DAY_OF_MONTH, Math.max(targetDay, 1));
    }

    private static void setTimeToStartOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }

    public static Pair<String, Integer> getPreviousMonthByMonthAndYear(String month, Integer year) {
        int monthIndex = -1;

        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equalsIgnoreCase(month)) {
                monthIndex = i;
                break;
            }
        }

        if (monthIndex == -1) {
            throw new IllegalArgumentException("Invalid month name: " + month);
        }

        if (monthIndex == 0) {
            return Pair.of("December", year - 1);
        } else {
            return Pair.of(monthNames[monthIndex - 1], year);
        }
    }
}