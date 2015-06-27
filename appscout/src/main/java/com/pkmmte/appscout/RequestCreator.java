package com.pkmmte.appscout;

import android.os.AsyncTask;

class RequestCreator {
	private final AppScout singleton;
	private final Request.Builder data;

	protected RequestCreator(AppScout singleton, AppScout.Api api) {
		this.singleton = singleton;
		this.data = new Request.Builder(api);
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

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
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

				return null;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
}
