package com.pkmmte.appscout;

class RequestCreator {
	private final AppScout singleton;
	private final Request.Builder data;

	protected RequestCreator(AppScout singleton, AppScout.API api) {
		this.singleton = singleton;
		this.data = new Request.Builder(api);
	}

	public void sync() {
		// TODO
	}

	public void async() {
		// TODO
	}
}
