package com.pkmmte.logger;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logger {
	// Logging levels
	public enum Level { NONE, LOW, MEDIUM, HIGH, ERROR }

	// The initial amount of items for log arrays
	private static final int LOG_BUFFER = 500;

	// Master list of logger objects
	private static final List<Logger> loggers = Collections.synchronizedList(new ArrayList<Logger>());

	// Skip log retention?
	private static boolean keepLogs;

	// List of all logged messages
	private final List<Log> logs = new ArrayList<>(LOG_BUFFER);

	private final String tag;
	private Level logLevel;

	public Logger(@NonNull String tag, @NonNull Level logLevel) {
		this.tag = tag;
		this.logLevel = logLevel;
		loggers.add(this);
	}

	public void low(String log) {
		log(this, Level.LOW, log);
	}

	public void medium(String log) {
		log(this, Level.MEDIUM, log);
	}

	public void high(String log) {
		log(this, Level.HIGH, log);
	}

	public void error(String log) {
		log(this, Level.ERROR, log);
	}

	public String getTag() {
		return tag;
	}

	public Level getLevel() {
		return logLevel;
	}

	public void setLevel(Level level) {
		this.logLevel = level;
	}

	public List<Log> getLogs() {
		return new ArrayList<>(logs);
	}

	public static List<Logger> getLoggers() {
		return loggers;
	}

	public static void setKeepLogs(boolean keepLogs) {
		Logger.keepLogs = keepLogs;
	}

	private static void log(Logger logger, Level level, String log) {
		if(level == Level.NONE || level.ordinal() < logger.logLevel.ordinal())
			return;

		if(keepLogs)
			logger.logs.add(new Log(level, logger.tag, log));

		switch (level) {
			case LOW:
				android.util.Log.d(logger.tag, log);
				break;
			case MEDIUM:
				android.util.Log.i(logger.tag, log);
				break;
			case HIGH:
				android.util.Log.w(logger.tag, log);
				break;
			case ERROR:
				android.util.Log.e(logger.tag, log);
				break;
			default:
				android.util.Log.wtf(logger.tag, log);
				break;
		}
	}
}
