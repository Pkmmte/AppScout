package com.pkmmte.appscout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** Miscellaneous convenience methods */
class Utils {

	/**
	 * @param stream Stream containing some sort of XML document.
	 * @param tag Tag for which to find matching elements for.
	 * @return The number of elements found matching the specified tag name or -1 if an exception is thrown.
	 */
	public static int getElementCount(InputStream stream, String tag) {
		try {
			return DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(stream)
					.getElementsByTagName(tag)
					.getLength();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			AppScout.logger.error("Could not find number of <" + tag + "> elements in stream!");
			return -1;
		}
	}

	/**
	 * Deletes files one by one until the entire directory is gone.
	 *
	 * @param path File directory to delete.
	 * @return {@code true} if fully deleted.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static boolean delete(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (file.isDirectory())
					delete(file);
				else
					file.delete();
			}
		}

		return(path.delete());
	}

	/**
	 * Creates a new {@link ComponentName} out of an {@link App}'s component value.
	 */
	public static ComponentName getComponent(App app) {
		return new ComponentName(app.component.split("/")[0], app.component.split("/")[1]);
	}

	/**
	 * @return A String representing the Intent's component.
	 */
	public static String getComponentString(Intent intent) {
		final String launchString = intent.toString().split("cmp=")[1];
		String component = launchString.substring(0, launchString.length() - 1);
		String[] split = component.split("/");
		if (split[1].startsWith("."))
			component = split[0] + '/' + split[0] + split[1];

		return component.trim();
	}

	/**
	 * Finds the highest resolution drawable possible for the specified app.
	 *
	 * @return A high-res drawable or null if nothing was found.
	 */
	@SuppressWarnings("deprecation")
	public static Drawable getHighDrawable(Context context, App app) {
		final PackageManager packageManager = context.getPackageManager();
		ActivityInfo activityInfo;
		Resources resources;

		try {
			activityInfo = packageManager.getActivityInfo(getComponent(app), PackageManager.GET_META_DATA);
			resources = packageManager.getResourcesForApplication(activityInfo.applicationInfo);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		if (resources == null)
			return null;

		int iconId = activityInfo.getIconResource();
		if (iconId == 0)
			return null;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return resources.getDrawableForDensity(iconId, DisplayMetrics.DENSITY_XXXHIGH, null);
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			return resources.getDrawableForDensity(iconId, DisplayMetrics.DENSITY_XXXHIGH);
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			return resources.getDrawableForDensity(iconId, DisplayMetrics.DENSITY_XXHIGH);
		else
			return resources.getDrawableForDensity(iconId, DisplayMetrics.DENSITY_XHIGH);
	}

	public static <K extends Enum<K>, V> Map<K, List<V>> enumMultiMap(Class<K> type, K keys[]) {
		final Map<K, List<V>> map = new EnumMap<>(type);
		for (K key : keys)
			map.put(key, Collections.synchronizedList(new ArrayList<V>()));

		return Collections.unmodifiableMap(map);
	}

	/**
	 * Zips a file or file directory.
	 *
	 * @param file File or directory to zip.
	 * @param outputStream Output stream.
	 * @param buffer Buffer size.
	 * @throws IOException
	 */
	public static void zip(File file, ZipOutputStream outputStream, int buffer) throws IOException {
		byte[] data = new byte[buffer];
		if (file.isFile()) {
			ZipEntry entry = new ZipEntry(file.getName());
			outputStream.putNextEntry(entry);
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			try {
				int read;
				while ((read = inputStream.read(data, 0, buffer)) != -1)
					outputStream.write(data, 0, read);
			} finally {
				outputStream.closeEntry();
				inputStream.close();
			}
		}
		else if (file.isDirectory()) {
			String[] fileList = file.list();
			for (String list : fileList)
				zip(new File(file.getPath() + '/' + list), outputStream, buffer);
		}
	}
}
