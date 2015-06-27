package com.pkmmte.appscout;

import android.content.Context;
import android.support.annotation.NonNull;

public class AppScout {
	// For logging purposes
	private static final String TAG = AppScout.class.getSimpleName();

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

	protected enum API { AUTO, LOAD, SEND }
}
