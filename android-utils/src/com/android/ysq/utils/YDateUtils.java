package com.android.ysq.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class YDateUtils {

	public static String getDateString(long milliseconds) {
		return getDateString(milliseconds, "yyyy-MM-dd");
	}
	
	public static String getDateString(long milliseconds, String formatStr) {
		Date date = new Date(milliseconds);
		SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.US);
		String data = format.format(date);
		return data;
	}
}
