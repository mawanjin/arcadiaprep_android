
package com.arcadiaprep.app.login.common;


public class Constants {

	/** Default is true. Set to false during development. Overrides DEVELOPER_MODE. */
	public static final boolean PROD = !true;

	public static final String VERSION = "1.0";

	/** Default value is false. Set to true only during development. Enable to show logging. */
	public static final boolean DEVELOPER_MODE = !true;

	public static final boolean USE_FULL_UPLOAD_SCENARIO_ON_REGISTRATION = true;
	
	/** Default value is false. Set to true only during development. Enable to populate login and registration forms. */
	public static final boolean TEST_POPUPLATE_FORMS = !true;

	/** Default value is false. Set to true only during development. Enable to show "login successful" etc. */
	public static final boolean TEST_SHOW_TOASTS = !true;

	/** Default value is false. Set to true only during development to log responses. */
	public static final boolean LOG_HTTP_RESPONSES = !false && DEVELOPER_MODE;

	public static final String AP_DEBUG_CLASSES = ""
			+ "com.arcadiaprep.app.scratchpad.component.popup.communication.AbstractHttpService,"
			+ "";

	public static final String APPLICATION_TAG = "Popup";

	/** Default HTTP request timeout of 20s.  */
	public static final int HTTP_TIMEOUT = 25000;

	/**
	 * Default is <b>true</b></br>
	 * </br>
	 * Related to:</br>
	 * - untrusted certificate on production</br>
	 * - SSL problems with Tattoo</br>
	 * Set to true on production.
	 * You can set to <b>false</b> on stage.
	 */
	public static final boolean USE_SSL_BACKWARD_COMPATIBILITY = !true;

	public static final String BASE_SERVER_URL = "arcadia.witagg.com";

	/* For testing purposes only. */

	// login
	public static final String TEST_USERNAME = "michael.kaye@gmail.com";
	public static final String TEST_PASSWORD = "aaaaaa";
	// register
	public static final String TEST_FIRST_NAME = "Michael";
	public static final String TEST_LAST_NAME = "Kaye";
	public static final String TEST_EMAIL_NAME = "michael.kaye@gmail.com";
	public static final String TEST_REPEAT_PASSWORD = "aaaaaa";
	
	// photo
	public static final int PICTURE_WIDTH_PX = 200;
	public static final int PICTURE_HEIGHT_PX = 200;
	public static final String PHOTO_NAME = "photo.jpg";
	public static final String PHOTO_DIRECTORY = "/ArcadiaPrep/photos/";
}
