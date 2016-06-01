package com.android.ysq.utils.application;

import com.android.ysq.utils.crash.CrashLogHandlerUtils;
import com.android.ysq.utils.logger.LogLevel;
import com.android.ysq.utils.logger.Logger;

import android.app.Application;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.init(getLogTag()).logLevel(LogLevel.FULL);
		CrashLogHandlerUtils.getInstance(this);
	}
	
	protected String getLogTag() {
		return "YLogTag";
	}
}
