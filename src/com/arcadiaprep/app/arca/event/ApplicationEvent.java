
package com.arcadiaprep.app.arca.event;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {
	private static final long serialVersionUID = 3698095407091158499L;

	public static final String TYPE_LOGIN_SUCCESS = "loginSuccess";
	public static final String TYPE_LOGIN_FAIL = "loginFail";
	public static final String TYPE_REGISTER_SUCCESS = "registerSuccess";
	public static final String TYPE_REGISTER_FAIL = "registerFail";
	public static final String TYPE_PHOTO_UPLOAD = "photoUpload";
	public static final String TYPE_REGISTRATION_SHOW = "registrationShow";
	public static final String TYPE_PROGRESS_SHOW = "progressDialogShow";
	public static final String TYPE_PROGRESS_DISMISS = "progressDialogDismiss";
	public static final String TYPE_NO_CONNECTIVITY = "connectivity";
	public static final String TYPE_CANCEL = "cancel";

	public String type;
	public String data;

	public ApplicationEvent(Object source, String type, String data) {
		super(source);
		this.type = type;
		this.data = data;
	}

}
