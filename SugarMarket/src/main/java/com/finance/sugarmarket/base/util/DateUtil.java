package com.finance.sugarmarket.base.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static String formatDateInTimeZone(Date date, String timeZoneId) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	public static Date getCurrentDate() {
		Date date = new Date();
		try {
			String timeZoneId = "Asia/Kolkata";
			TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(timeZone);
			return sdf.parse(sdf.format(date));
		} catch (Exception e) {
			return date;
		}
	}

	public static LocalDate getCurrentLocalDate() {
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		LocalDate currentDate = LocalDate.now(zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = currentDate.format(formatter);
		return LocalDate.parse(formattedDate, formatter);
	}

}
