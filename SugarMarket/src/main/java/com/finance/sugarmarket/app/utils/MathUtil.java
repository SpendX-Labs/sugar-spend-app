package com.finance.sugarmarket.app.utils;

import java.util.Date;
import java.util.List;


public class MathUtil {
	
	public static final double tolerance = 0.001;
	
	public static Double getReturnPercentage(List<Double> investedValues, Double currentValue) {
		Double totalInvestedValue = investedValues.stream().mapToDouble(Double::doubleValue).sum();
		return (currentValue / totalInvestedValue - 1) * 100;
	}

	public static Double findXIRR(double guess, List<Double> payments, List<Date> days) {
		double x0 = guess;
		double x1 = 0.0;
		double err = 1e+100;

		while (err > tolerance) {
			x1 = x0 - sumfXIRR(payments, days, x0) / sumdfXIRR(payments, days, x0);
			err = Math.abs(x1 - x0);
			x0 = x1;
		}

		return Math.ceil(x0 * 100 * 10) / 10; //make it to percentage and round up to one decimal 
	}
	
	private static double dateDiff(Date d1, Date d2) {
		long day = 24 * 60 * 60 * 1000;

		return (d1.getTime() - d2.getTime()) / day;
	}

	private static Double fXIRR(Double p, Date dt, Date dt0, double x) {
		return p * Math.pow((1.0 + x), (dateDiff(dt0, dt) / 365.0));
	}

	private static Double dfXIRR(Double p, Date dt, Date dt0, double x) {
		return (1.0 / 365.0) * dateDiff(dt0, dt) * p * Math.pow((x + 1.0), ((dateDiff(dt0, dt) / 365.0) - 1.0));
	}

	private static Double sumfXIRR(List<Double> payments, List<Date> days, double x) {
		double resf = 0.0;

		for (int i = 0; i < payments.size(); i++) {
			resf = resf + fXIRR(payments.get(i), days.get(i), days.get(0), x);
		}

		return resf;
	}

	private static Double sumdfXIRR(List<Double> payments, List<Date> days, double x) {
		double resf = 0.0;

		for (int i = 0; i < payments.size(); i++) {
			resf = resf + dfXIRR(payments.get(i), days.get(i), days.get(0), x);
		}

		return resf;
	}
	
	
}
