package com.dyj.untils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GetTimeUtil {

	public static String getDate(String month, String day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24Сʱ��
		java.util.Date d = new java.util.Date();
		;
		String str = sdf.format(d);
		String nowmonth = str.substring(5, 7);
		String nowday = str.substring(8, 10);
		String result = null;

		int temp = Integer.parseInt(nowday) - Integer.parseInt(day);
		switch (temp) {
		case 0:
			result = "����";
			break;
		case 1:
			result = "����";
			break;
		case 2:
			result = "ǰ��";
			break;
		default:
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.parseInt(month) + "��");
			sb.append(Integer.parseInt(day) + "��");
			result = sb.toString();
			break;
		}
		return result;
	}

	public static String getTime(int timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		try {
			java.util.Date currentdate = new java.util.Date();// ��ǰʱ��
			long i = (currentdate.getTime() / 1000 - timestamp) / (60);
			Timestamp now = new Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
			String str = sdf.format(new Timestamp(IntToLong(timestamp)));
			time = str.substring(11, 16);
			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			time = getDate(month, day) + time;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	// java Timestamp���캯���贫��Long��
	public static long IntToLong(int i) {
		long result = (long) i;
		result *= 1000;
		return result;
	}

	public static void main(String[] args) {
		int timestamp = 1310457552; // ������Ѷ΢������ʱ���Ϊ��
		String time = GetTimeUtil.getTime(timestamp);
		System.out.println("timestamp-->" + time);

		// print timestamp-->7��12��15:59
	}

}
