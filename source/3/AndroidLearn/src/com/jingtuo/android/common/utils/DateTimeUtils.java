package com.jingtuo.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 获取系统时间类，对一些的常用的时间操作的封装<br>
 * 按照指定的日期格式解析日期字符串<br>
 * yyyy表示年<br>
 * MM表示年中月<br>
 * dd表示月中天 HH表示小时(0-23)<br>
 * mm表示时中分<br>
 * ss表示分中秒<br>
 * SSS表示毫秒数<br>
 * w表示年中周<br>
 * W表示月中周<br>
 * D表示年中天<br>
 * F表示月中星期<br>
 * E表示星期几<br>
 * a/p表示am/pm<br>
 * k表示小时(1-24)<br>
 * K表示小时(0-11)am/pm<br>
 * h表示小时(1-12)am/pm<br>
 * z表示时区<br>
 * Z表示时区<br>
 * 比如，日期格式yyyy-MM-dd HH:mm:ss<br>
 * 
 * @author JingTuo
 */
public class DateTimeUtils {
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMM = "yyyyMM";
	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static final String yyyy_MM_dd1HH_mm_ss = "yyyy-MM-dd/HH:mm:ss";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String HH_mm = "HH:mm";
	public static final String HH_mm_ss = "HH:mm:ss";
	public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

	/**
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date toDate(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toString(Date date, String format) {
		if(date==null){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date currentDate(){
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * 如果start==end，则返回0<br>
	 * 如果start>end,则返回负值<br>
	 * 如果start<end,则返回正值
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDays(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Calendar startC = Calendar.getInstance();
		Calendar endC = Calendar.getInstance();
		startC.setTime(start);
		endC.setTime(end);

		startC.clear(Calendar.HOUR_OF_DAY);
		startC.clear(Calendar.MINUTE);
		startC.clear(Calendar.SECOND);
		startC.clear(Calendar.MILLISECOND);

		endC.clear(Calendar.HOUR_OF_DAY);
		endC.clear(Calendar.MINUTE);
		endC.clear(Calendar.SECOND);
		endC.clear(Calendar.MILLISECOND);
		if (startC.after(endC)) {
			int days = 0;
			while (endC.before(startC)) {
				endC.add(Calendar.DAY_OF_YEAR, 1);
				days++;
			}
			return 0 - days;
		} else {
			int days = 0;
			while (startC.before(endC)) {
				startC.add(Calendar.DAY_OF_YEAR, 1);
				days++;
			}
			return days;
		}
	}

	/**
	 * 将秒数转换成如下格式：<br>
	 * 00:00:00、00:00<br>
	 * 如果>=1天，则转换成：<br>
	 * >=1天
	 * 
	 * @param seconds
	 * @return
	 */
	public static String format(int seconds) {
		int hours = 0;
		int minutes = 0;
		if (seconds >= 0 && seconds < 60) {

		} else if (seconds >= 60 && seconds < 3600) {
			minutes = seconds / 60;
			seconds = seconds % 60;
		} else if (seconds >= 3600 && seconds < 24 * 3600) {
			hours = seconds / 3600;
			seconds = seconds % 3600;
			minutes = seconds / 60;
			seconds = seconds % 60;
		} else {
			return ">=1天";
		}
		String result = "";
		if (hours > 0 && hours <= 9) {
			result += "0" + hours + ":";
		} else if (hours >= 10) {
			result += hours + ":";
		}
		if (minutes >= 0 && minutes <= 9) {
			result += "0" + minutes + ":";
		} else {
			result += minutes + ":";
		}

		if (seconds >= 0 && seconds <= 9) {
			result += "0" + seconds;
		} else {
			result += seconds;
		}
		return result;
	}

	/**
	 * 由于业务需要，需要将时间转换成秒
	 * 
	 * @param time
	 *            HH:mm:ss
	 * @return
	 */
	public static int toSeconds(String time) {
		if (time == null || time.equals("")) {
			return 0;
		}
		String[] array = time.split(":");
		if (array.length == 1) {
			return Integer.parseInt(array[0]);
		} else if (array.length == 2) {
			return Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1]);
		} else {
			return Integer.parseInt(array[array.length - 3]) * 3600 + Integer.parseInt(array[array.length - 2]) * 60
					+ Integer.parseInt(array[array.length - 1]);
		}
	}
	
	/**
	 * 判断指定日期是哪一天
	 * @param date
	 * @param days	0:今天
	 * @return
	 */
	public static boolean isWhichDay(Date date, int days){
		if(date==null){
			return false;
		}
		Date today = new Date(System.currentTimeMillis());
		Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTime(today);
		calendarToday.add(Calendar.DAY_OF_YEAR, days);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(calendar.get(Calendar.YEAR)==calendarToday.get(Calendar.YEAR)
				&&calendar.get(Calendar.MONTH)==calendarToday.get(Calendar.MONTH)
				&&calendar.get(Calendar.DAY_OF_MONTH)==calendarToday.get(Calendar.DAY_OF_MONTH)){
			return true;
		}else{
			return false;
		}
	}

}
