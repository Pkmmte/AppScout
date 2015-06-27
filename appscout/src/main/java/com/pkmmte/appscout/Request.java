package com.pkmmte.appscout;

class Request {
	public final AppScout.API api;

	private Request(Builder builder) {
		this.api = builder.api;
	}

	protected static class Builder {
		private AppScout.API api;

		public Builder(AppScout.API api) {
			this.api = api;
		}

		public Request build() {
			return new Request(this);
		}
	}
}
