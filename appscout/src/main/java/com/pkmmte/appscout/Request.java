package com.pkmmte.appscout;

class Request {
	public final AppScout.Api api;
	public final String appfilter;
	public final boolean filter;

	private Request(Builder builder) {
		this.api = builder.api;
		this.appfilter = builder.appfilter;
		this.filter = builder.filter;
	}

	protected static class Builder {
		private AppScout.Api api;
		private String appfilter;
		private boolean filter;

		public Builder(AppScout.Api api) {
			this.api = api;
			this.appfilter = null;
			this.filter = true;
		}

		public Builder appfilter(String appfilter) {
			this.appfilter = appfilter;
			return this;
		}

		public Builder filter(boolean filter) {
			this.filter = filter;
			return this;
		}

		public Request build() {
			if (filter && appfilter == null)
				appfilter = "appfilter.xml";
			return new Request(this);
		}
	}
}
