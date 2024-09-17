package com.finance.sugarmarket.app.enums;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public enum BudgetCalendar {
	JANUARY(1, "January"),
    FEBRUARY(2, "February"),
    MARCH(3, "March"),
    APRIL(4, "April"),
    MAY(5, "May"),
    JUNE(6, "June"),
    JULY(7, "July"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "October"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "December");

    private final int monthNumber;
    private final String monthName;

    BudgetCalendar(int monthNumber, String monthName) {
        this.monthNumber = monthNumber;
        this.monthName = monthName;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }
    
    public static BudgetCalendar getBudgetMonth(Date date) {
    	if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthNumber = calendar.get(Calendar.MONTH) + 1;
    	for(BudgetCalendar month : values()) {
    		if(month.getMonthNumber() == monthNumber) {
    			return month;
    		}
    	}
    	return null;
    }
    
    public static Integer getBudgetYear(Date date) {
    	if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    
    public static List<String> getMonths() {
    	return Arrays.asList(BudgetCalendar.values()).stream().map(BudgetCalendar::getMonthName)
    			.collect(Collectors.toList());
    }
    
    public static int getDaysInMonth(String monthName, int year) {
        Month month = Month.valueOf(monthName.toUpperCase());
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }
    
    public static Date constructDate(int day, String month, int year) {
    	BudgetCalendar budgetMonth = BudgetCalendar.valueOf(month.toUpperCase());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, budgetMonth.getMonthNumber() - 1); // Adjust for zero-based month
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }
	
}
