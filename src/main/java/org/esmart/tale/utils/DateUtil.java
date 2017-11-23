package org.esmart.tale.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Integer currentDateInt(){
		long current=new Date().getTime()/1000;
		return (int)current;
	}
	public static int getTimeByDate(Date date) {
		return (int) (date.getTime() / 1000);
	}
	public static Date getTimeByInt(int time) {
		return new Date(time * 1000L);
	}
	public static Date dateFormat(String date, String dateFormat) {
		if (date == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		if (date != null) {
			try {
				return format.parse(date);
			} catch (Exception ex) {
			}
		}
		return null;
	}
	public static String dateFormat(Date date, String dateFormat) {
		if (date != null){
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			if (date != null) {
				return format.format(date);
			}
		}
		return "";
	}
}
