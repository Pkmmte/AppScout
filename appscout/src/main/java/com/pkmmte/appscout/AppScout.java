package com.pkmmte.appscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.pkmmte.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class AppScout {
	// For logging purposes
	private static final String TAG = AppScout.class.getSimpleName();

	// Special logger
	protected static final Logger logger = new Logger(TAG, Logger.Level.LOW);

	// Component matcher
	private static final Pattern pattern = Pattern.compile("^.*?\\{(.*)\\}$");

	// Main singleton instance
	private static volatile AppScout singleton = null;

	// Application context... because of reasons
	private final Context context;

	// Cache for loaded apps
	private volatile List<App> apps = new ArrayList<>();
	private volatile List<App> appsInstalled = new ArrayList<>();
	private volatile List<String> appsDefined = new ArrayList<>();

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

	// TODO
	protected void load(Request request) throws IOException, XmlPullParserException {
		logger.medium("load(" + request + ')');
		//final CallbackHandler handler = request.handler != null ? request.handler : this.handler;
		//final boolean safe = request.safe != null ? request.safe : this.safe;

		// TODO: Notify callback

		// Get list of all installed applications and sort them by name
		final PackageManager packageManager = context.getPackageManager();
		final List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		Collections.sort(packages, new ApplicationInfo.DisplayNameComparator(packageManager));

		// Create new local list for installed apps
		final int numPackages = packages.size();
		final List<App> installed = new ArrayList<>(numPackages);
		logger.medium("Found " + numPackages + " installed packages");

		for (int index = 0; index < numPackages; index++) {
			// Get basic application info
			final ApplicationInfo packageInfo = packages.get(index);
			final Intent launchIntent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);

			// Skip invalid intents
			if (launchIntent == null)
				continue;

			// TODO: Notify callback

			// Build custom immutable App object
			App app = new App.Builder()
					.name(packageInfo.loadLabel(packageManager))
					.component(Utils.getComponentString(launchIntent))
					.icon(packageInfo.loadIcon(packageManager))
					.build();

			// Append to list
			installed.add(app);
			logger.low(app.toString());
		}

		// Replace global installed list with local list
		appsInstalled = installed;
		logger.medium("Loaded " + installed.size() + " installed apps");

		// TODO: Notify callback

		// Filter apps if needed
		if (request.filter) {
			logger.medium("Starting appfilter loading process...");

			// Create new XmlPullParser instance and set input to appfilter
			final XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(context.getAssets().open(request.appfilter), "UTF-8");

			// Find the number of elements in tbe appfilter (for logging & progress)
			final int numElements = Utils.getElementCount(context.getAssets().open(request.appfilter), "item");
			final List<String> defined = new ArrayList<>(numElements);
			logger.medium("Found " + numElements + " <item> elements in " + request.appfilter);

			// Loop through entire appfilter to find defined components
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					try {
						// Parse component names from <item> tags
						final String elementName = parser.getName();
						if (elementName != null && elementName.equals("item")) {
							final String component = parser.getAttributeValue(null, "component");
							if (component != null)
								defined.add(pattern.matcher(component).replaceAll("$1"));
						}
					} catch (Exception e) {
						logger.high("Error parsing appfilter item! [" + e.getMessage() + ']');
					}
				}

				// Move on to next parsing event
				eventType = parser.next();
			}

			// Replace global defined list with local list
			appsDefined = defined;
			logger.medium("Loaded " + defined.size() + " defined components");
			logger.medium("Starting filtering process...");
		}
	}

	protected void send(Request request) {
		logger.medium("send(" + request + ')');
		//final CallbackHandler handler = request.handler != null ? request.handler : this.handler;
		//final boolean safe = request.safe != null ? request.safe : this.safe;

		// TODO
	}

	protected enum Api { AUTO, LOAD, SEND }
}
