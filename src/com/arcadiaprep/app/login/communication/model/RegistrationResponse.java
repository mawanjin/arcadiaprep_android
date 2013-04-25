
package com.arcadiaprep.app.login.communication.model;

import org.json.JSONObject;

public class RegistrationResponse extends Response {

	private static final String REGISTER = "register";

	private boolean isRegistered;
	private String errorMessage;

	private RegistrationResponse(boolean isRegistered, String errorMessage) {
		this.isRegistered = isRegistered;
		this.errorMessage = errorMessage;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public static RegistrationResponse parse(JSONObject json) throws IllegalArgumentException {
		try {
			boolean isRegistered = json.getString(REGISTER).equalsIgnoreCase(YES);
			String error = json.getString(ERROR);
			return new RegistrationResponse(isRegistered, error);

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Can't parse json to RegistrationResponse instance! Exception: " + e);
		}
	}
}
