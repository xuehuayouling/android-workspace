package com.android.ysq.utils.crash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class CrashLogHandlerUtils implements UncaughtExceptionHandler {

	private static CrashLogHandlerUtils mSelf;

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> nDeviceInfos;
	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat mFormatter;
	private File mSavePath;

	private CrashLogHandlerUtils(Application application) {
		mContext = application;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		nDeviceInfos = new HashMap<String, String>();
		mFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mSavePath = application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		} else {
		}
	}

	public static synchronized CrashLogHandlerUtils getInstance(Application application) {
		if (mSelf == null) {
			mSelf = new CrashLogHandlerUtils(application);
		}
		return mSelf;
	}

	/**
	 * @param path
	 *            文件路径，不包含文件名
	 */
	public void setSavePath(String path) {
		mSavePath = new File(path);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex != null) {
			collectDeviceInfo(mContext);
			saveCrashInfo2File(ex);
		}
		return false;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				nDeviceInfos.put("versionName", versionName);
				nDeviceInfos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Throwable ex) {
		Calendar calendar = Calendar.getInstance();
		
		StringBuffer sb = new StringBuffer();
		sb.append(getStartInfo());
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		
		sb.append(getEndInfo());
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = mSavePath;
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileWriter fWriter = null;
			try {
				fWriter = new FileWriter(getFileNameByCalendar(calendar), true);
				fWriter.write(sb.toString());
				fWriter.flush();
				fWriter.close();
				deleteOldFile(calendar);
			} catch (IOException e) {
				e.printStackTrace();
				if (fWriter != null) {
					try {
						fWriter.flush();
						fWriter.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	private String getStartInfo() {
		StringBuffer sb = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(calendar.getTimeInMillis());
		long timestamp = calendar.getTimeInMillis();
		String time = mFormatter.format(date);
		sb.append("本次错误开始\n");
		sb.append(time).append("_").append(timestamp).append("\n");
		for (Map.Entry<String, String> entry : nDeviceInfos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		return sb.toString();
	}
	
	private String getEndInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("本次错误结束").append("\n\n");
		return sb.toString();
	}
	
	private void deleteOldFile(Calendar todayCalendar) {
		Calendar oldCalendar = (Calendar) todayCalendar.clone();
		oldCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH) - 7);
		String oldFilePath = getFileNameByCalendar(oldCalendar);
		try {
			File file = new File(oldFilePath);
			if (file.isFile() && file.exists()) {
			    file.delete();  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFileNameByCalendar(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		Date date = new Date(calendar.getTimeInMillis());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = mSavePath + "/crash_log" + "_" + formatter.format(date) + ".log";
		return fileName;
	}
}
