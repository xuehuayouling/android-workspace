package com.android.ysq.utils.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

public class FileOperator {

	private static final File SAVE_PATH = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private static FileOperator mSelf;

	private FileOperator() {

	}

	public static synchronized FileOperator getInstance() {
		if (mSelf == null) {
			mSelf = new FileOperator();
		}
		return mSelf;
	}

	public void saveToFile(Throwable ex, String message, Object... args) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Calendar calendar = Calendar.getInstance();

			StringBuffer sb = new StringBuffer();
			sb.append(getStartInfo());
			sb.append(args.length == 0 ? message : String.format(message, args));
			if (ex != null) {
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
			}
			sb.append(getEndInfo());
			File dir = SAVE_PATH;
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
		return sb.toString();
	}

	private String getEndInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("本次错误结束").append("\n\n");
		return sb.toString();
	}
	
	private String getFileNameByCalendar(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		Date date = new Date(calendar.getTimeInMillis());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = SAVE_PATH + "/y_log" + "_" + formatter.format(date) + ".log";
		return fileName;
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
}
