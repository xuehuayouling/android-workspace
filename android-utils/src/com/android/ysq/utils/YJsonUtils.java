package com.android.ysq.utils;

public final class YJsonUtils {

	private static IJson mJson;
	public static synchronized IJson getJson() {
		if (mJson == null) {
			mJson = new FastJsonImpl();
		}
		return mJson;
	}
}
