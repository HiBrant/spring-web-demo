package com.netease.yuedu.weekly.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getMondayOfThisWeek() {
		return getDateStrOfThisWeek(Calendar.MONDAY);
	}
	
	public static String getFridayOfThisWeek() {
		return getDateStrOfThisWeek(Calendar.FRIDAY);
	}
	
	private static String getDateStrOfThisWeek(int calDateValue) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, calDateValue);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(cal.getTime());
	}
	
	public static void main(String[] args) {
		System.out.println(getDateStrOfThisWeek(Calendar.WEDNESDAY));
	}
}
