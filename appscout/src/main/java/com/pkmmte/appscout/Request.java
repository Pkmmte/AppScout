package com.pkmmte.appscout;

class Request {
	public final AppScout.Api api;
	public final boolean filter;

	private Request(Builder builder) {
		this.api = builder.api;
		this.filter = builder.filter;
	}

	protected static class Builder {
		private AppScout.Api api;
		private boolean filter;

		public Builder(AppScout.Api api) {
			this.api = api;
			this.filter = true;
		}

		public Request build() {
			return new Request(this);
		}
	}
}
