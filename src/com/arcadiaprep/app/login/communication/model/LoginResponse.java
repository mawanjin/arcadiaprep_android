
package com.arcadiaprep.app.login.communication.model;

import com.arcadiaprep.app.login.util.JsonUtil;

import org.json.JSONObject;

public class LoginResponse extends Response {

	private static final String LOGIN = "login";
	private static final String PROFILE = "profile";

	private boolean isLoggedIn;
	private int profile;
	private String errorMessage;

	private LoginResponse(boolean isLoggedIn, int profile, String errorMessage) {
		this.isLoggedIn = isLoggedIn;
		this.profile = profile;
		this.errorMessage = errorMessage;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public int getProfile() {
		return profile;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public static LoginResponse parse(JSONObject json) throws IllegalArgumentException {
		try {
			boolean isLoggedIn = json.getString(LOGIN).equalsIgnoreCase(YES);
			int profile = JsonUtil.getInteger(json, PROFILE);
			String error = json.getString(ERROR);
			return new LoginResponse(isLoggedIn, profile, error);

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Can't parse json to LoginResponse instance! Exception: " + e);
		}
	}
}
