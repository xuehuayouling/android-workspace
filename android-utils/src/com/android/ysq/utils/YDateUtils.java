package com.android.ysq.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class YDateUtils {

	public static String getDateString(long milliseconds) {
		Date date = new Date(milliseconds);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String data = format.format(date);
		return data;
	}
}
