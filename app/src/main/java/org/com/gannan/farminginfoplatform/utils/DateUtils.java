package org.com.gannan.farminginfoplatform.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DateUtils {


	public static String currentTimeStrToDate(String currentTime){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(Long.parseLong(currentTime));
		String strDate=formatter.format(date);
		return strDate;
	}
	public static String dateFormatDIY(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 Date date1 = null;
		try {
			date1 = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatter.format(date1);
	}
	public static String dateFormatDateTime(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date1 = null;
		try {
			date1 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sdf.format(date1);
	}
	// 获取当前日期
	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	public static int[] getYMDArray(String datetime, String splite) {
		int[] date = { 0, 0, 0, 0, 0 };
		if (datetime != null && datetime.length() > 0) {
			String[] dates = datetime.split(splite);
			int position = 0;
			for (String temp : dates) {
				date[position] = Integer.valueOf(temp);
				position++;
			}
		}
		return date;
	}
	/* 将字符串转为时间戳 */
	public static String getTimeToStamp(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
				Locale.CHINA);
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String tmptime = String.valueOf(date.getTime()).substring(0, 10);

		return tmptime;
	}
}
