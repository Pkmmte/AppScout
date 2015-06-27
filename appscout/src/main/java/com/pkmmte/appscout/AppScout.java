package com.pkmmte.appscout;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pkmmte.logger.Logger;

public class AppScout {
	// For logging purposes
	private static final String TAG = AppScout.class.getSimpleName();

	// Special logger
	protected static final Logger logger = new Logger(TAG, Logger.Level.NONE);

	// Main singleton instance
	private static volatile AppScout singleton = null;

	// Application context... because of reasons
	private final Context context;

	/**
	 * The global default {@link AppScout} instance.
	 * Call this whenever you want to use this library.
	 */
	public static AppScout with(@NonNull Context context) {
		if(singleton == null)
			singleton = new AppScout(context.getApplicationContext());

		return singleton;
	}

	/* Hidden constructor */
	private AppScout(Context context) {
		this.context = context;
	}

	public RequestCreator auto() {
		return new RequestCreator(this, Api.AUTO);
	}

	public RequestCreator load() {
		return new RequestCreator(this, Api.LOAD);
	}

	public RequestCreator send() {
		return new RequestCreator(this, Api.SEND);
	}

	protected void auto(Request request) {
		logger.medium("auto(" + request + ')');
		//final CallbackHandler handler = request.handler != null ? request.handler : this.handler;
		//final boolean safe = request.safe != null ? request.safe : this.safe;

		// TODO
	}

	protected void load(Request request) {
		logger.medium("load(" + request + ')');
		//final CallbackHandler handler = request.handler != null ? request.handler : this.handler;
		//final boolean safe = request.safe != null ? request.safe : this.safe;

		// TODO
	}

	protected void send(Request request) {
		logger.medium("send(" + request + ')');
		//final CallbackHandler handler = request.handler != null ? request.handler : this.handler;
		//final boolean safe = request.safe != null ? request.safe : this.safe;

		// TODO
	}

	protected enum Api { AUTO, LOAD, SEND }
}
