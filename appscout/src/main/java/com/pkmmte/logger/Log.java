package com.pkmmte.logger;

import android.support.annotation.NonNull;

public class Log {
	public final Logger.Level level;
	public final String tag;
	public final String log;
	public final long timestamp;

	public Log(@NonNull Logger.Level level, @NonNull String tag, @NonNull String log) {
		this.level = level;
		this.tag = tag;
		this.log = log;
		this.timestamp = System.currentTimeMillis();
	}
}
