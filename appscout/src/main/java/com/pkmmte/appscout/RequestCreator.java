package com.pkmmte.appscout;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pkmmte.appscout.Utils.enumMultiMap;

public class RequestCreator {
	// TODO: Experimental!
	private static final Map<AppScout.Api, List<AsyncTask>> activeThreads = enumMultiMap(AppScout.Api.class, AppScout.Api.values());
	private static final List<AppScout.Api> activeRequests = Collections.synchronizedList(new ArrayList<AppScout.Api>());

	private final AppScout singleton;
	private final Request.Builder data;

	private long delay = 0;
	private boolean ignoreIfRunning = true;
	private boolean override = false;

	protected RequestCreator(AppScout singleton, AppScout.Api api) {
		this.singleton = singleton;
		this.data = new Request.Builder(api);
	}

	public RequestCreator delay(long delay) {
		this.delay = delay;
		return this;
	}

	public RequestCreator ignoreIfRunning(boolean ignoreIfRunning) {
		this.ignoreIfRunning = ignoreIfRunning;
		return this;
	}

	public RequestCreator override(boolean override) {
		this.override = override;
		return this;
	}

	public void sync() {
		final Request request = data.build();
		switch (request.api) {
			case AUTO:
				singleton.auto(request);
				break;
			case LOAD:
				singleton.load(request);
				break;
			case SEND:
				singleton.send(request);
				break;
		}
	}

	public void async() {
		final Request request = data.build();

		synchronized (activeThreads) {
			if (override && !activeThreads.get(request.api).isEmpty()) {
				final List<AsyncTask> tasks = activeThreads.get(request.api);
				final int size = tasks.size();
				for (AsyncTask task : tasks)
					task.cancel(true);
				AppScout.logger.high("Cancelled " + size + ' ' + request.api.name() + " threads via override()");
			}
		}

		synchronized (activeRequests) {
			if (ignoreIfRunning && activeRequests.contains(request.api)) {
				AppScout.logger.high(request.api.name() + " request already running! Ignoring...");
				return;
			}
			activeRequests.add(request.api);
		}

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				synchronized (activeThreads) {
					activeThreads.get(request.api).add(this);
				}

				try {
					if (delay > 0)
						Thread.sleep(delay);
				} catch (InterruptedException e) {
					AppScout.logger.high(request.api.name() + " thread interrupted during delay!");
				}

				switch (request.api) {
					case AUTO:
						singleton.auto(request);
						break;
					case LOAD:
						singleton.load(request);
						break;
					case SEND:
						singleton.send(request);
						break;
				}

				synchronized (activeThreads) {
					activeThreads.get(request.api).remove(this);
				}

				return null;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
}
