package com.android.ysq.utils.logger;

/**
 * Logger is a wrapper of {@link android.util.Log} But more pretty, simple and
 * powerful
 */
public final class Logger {
	private static final String DEFAULT_TAG = "prettylogger";

	private static Printer printer;

	// no instance
	private Logger() {
	}

	/**
	 * It is used to get the settings object in order to change settings
	 *
	 * @return the settings object
	 */
	public static Settings init() {
		return init(DEFAULT_TAG);
	}

	/**
	 * It is used to change the tag
	 *
	 * @param tag
	 *            is the given string which will be used in Logger as TAG
	 */
	public static Settings init(String tag) {
		printer = new LoggerPrinter();
		return printer.init(tag);
	}

	public static void clear() {
		if (printer != null) {
			printer.clear();
			printer = null;
		}
	}

	public static Printer t(String tag) {
		if (printer == null) {
			init();
		}
		return printer.t(tag, printer.getSettings().getMethodCount());
	}

	public static Printer t(int methodCount) {
		if (printer == null) {
			init();
		}
		return printer.t(null, methodCount);
	}

	public static Printer t(String tag, int methodCount) {
		if (printer == null) {
			init();
		}
		return printer.t(tag, methodCount);
	}

	public static void d(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.d(message, args);
	}

	public static void e(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.e(null, message, args);
	}

	public static void e(Throwable throwable, String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.e(throwable, message, args);
	}

	public static void i(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.i(message, args);
	}

	public static void v(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.v(message, args);
	}

	public static void w(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.w(message, args);
	}

	public static void wtf(String message, Object... args) {
		if (printer == null) {
			init();
		}
		printer.wtf(message, args);
	}

	/**
	 * Formats the json content and print it
	 *
	 * @param json
	 *            the json content
	 */
	public static void json(String json) {
		if (printer == null) {
			init();
		}
		printer.json(json);
	}

	/**
	 * Formats the json content and print it
	 *
	 * @param xml
	 *            the xml content
	 */
	public static void xml(String xml) {
		if (printer == null) {
			init();
		}
		printer.xml(xml);
	}

}
