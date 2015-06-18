package com.pkmmte.appscout;

import android.graphics.drawable.Drawable;

/** Immutable application metadata model class. */
public class App {
	/** Name belonging to this application as displayed by launchers */
	public final String name;

	/** Identifier for a specific component available from this app */
	public final String component;

	/** Drawable representing the icon bitmap for this application */
	public final Drawable icon;

	/* Hidden Constructor */
	private App(Builder builder) {
		this.name = builder.name;
		this.component = builder.component;
		this.icon = builder.icon;
	}

	public Builder buildUpon() {
		return new Builder(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof App)) return false;

		App that = (App) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (component != null ? !component.equals(that.component) : that.component != null) return false;
		return !(icon != null ? !icon.equals(that.icon) : that.icon != null);

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (component != null ? component.hashCode() : 0);
		result = 31 * result + (icon != null ? icon.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "App{" +
				"name='" + name + '\'' +
				", component='" + component + '\'' +
				", icon=" + icon +
				'}';
	}

	public static class Builder {
		private String name;
		private String component;
		private Drawable icon;

		public Builder() {
			this.name = null;
			this.component = null;
			this.icon = null;
		}

		public Builder(App app) {
			this.name = app.name;
			this.component = app.component;
			this.icon = app.icon;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder component(String component) {
			this.component = component;
			return this;
		}

		public Builder icon(Drawable icon) {
			this.icon = icon;
			return this;
		}

		public App build() {
			return new App(this);
		}
	}
}
