
package com.arcadiaprep.app.login.common;

import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.login.util.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserService {

	private static final String PREFS = "prefs";
	private static final String PREF_USERNAME = "prefUsername";
	private static final String PREF_PASSWORD = "prefPassword";
	private static final String PREF_PROFILE = "prefProfile";
	private static final String PREF_PICTURE_SRC = "prefPictureSrc";
	private static UserService service;

	private User user;

	/**
	 * Singleton - private constructor to prevent uncontrolled instantiating from outside of the class
	 */
	private UserService(Context ctx) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
		String username = shared.getString(PREF_USERNAME, StringUtil.EMPTY);
		String password = shared.getString(PREF_PASSWORD, StringUtil.EMPTY);
		int profile = shared.getInt(PREF_PROFILE, 0);

		// if username is not defined user is null (not logged in)
		if (StringUtil.isEmpty(username))
			return;

		user = new User(username, password, profile);
	}

	private static synchronized final UserService getInstance(Context ctx) {
		if (null == service)
			service = new UserService(ctx);
		return service;
	}

	/**
	 * Will never return null. Initially it returns empty User instance.
	 */
	public static User getUser(Context ctx) {
		return getInstance(ctx).user;
	}

	/**
	 * Set and persist user data to SharedPreferences.
	 * Set user to null to logout.
	 */
	public static void setUser(Context ctx, User user) {
		getInstance(ctx).user = user;
		persistUser(ctx);
	}

	private static void persistUser(Context ctx) {
		User user = getInstance(ctx).user;
		SharedPreferences shared = ctx.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
		Editor e = shared.edit();
		e.putString(PREF_USERNAME, null == user ? StringUtil.EMPTY : user.getUserName());
		e.putString(PREF_PASSWORD, null == user ? StringUtil.EMPTY : user.getPassword());
		e.putInt(PREF_PROFILE, null == user ? 0 : user.getProfile());
		e.commit();
	}

	/**
	 * Save to SharedPreferences.
	 */
	public static void persistPhotoSrc(Context ctx, String photoSrc) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
		Editor e = shared.edit();
		e.putString(PREF_PICTURE_SRC, photoSrc);
		e.commit();
	}

	/**
	 * Read from SharedPreferences.
	 */
	public static String getPhotoSrc(Context ctx) {
		SharedPreferences shared = ctx.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
		return shared.getString(PREF_PICTURE_SRC, StringUtil.EMPTY);
	}
}
