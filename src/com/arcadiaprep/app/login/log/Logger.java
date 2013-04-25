
package com.arcadiaprep.app.login.log;

import com.arcadiaprep.app.login.common.Constants;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Logger {

	static {
		debugClasses = new ArrayList<String>();

		// put all classes here
		log("com.arcadiaprep.app.scratchpad.component.popup.common.ConnectivityUtil");
		log("com.arcadiaprep.app.scratchpad.component.popup.common.PackagesUtil");
		log("com.arcadiaprep.app.scratchpad.component.popup.communication.HttpPostService");
		log("com.arcadiaprep.app.scratchpad.component.popup.communication.AbstractHttpService");
		log("com.arcadiaprep.app.scratchpad.component.popup.dialog.CustomDialog");
		log("com.arcadiaprep.app.scratchpad.component.popup.dialog.LoginDialog");
		log("com.arcadiaprep.app.scratchpad.component.popup.dialog.LoginDialogFragment");
		log("com.arcadiaprep.app.scratchpad.component.popup.dialog.RegistrationDialog");
		log("com.arcadiaprep.app.scratchpad.component.popup.dialog.RegistrationDialogFragment");
		log("com.arcadiaprep.app.arca.LoginBasicActivity");
	}

	private static final String TAG = getClassTag(Logger.class);
	private static final String DELIMITER = " -> ";
	private static List<String> debugClasses;

	/**
	 * Calculate class descriptor that will show in log
	 * by appending class name to application tag.</br>
	 * </br>
	 * Usage:</br>
	 * <b><i>private static final String TAG = Logger.getClassTag(Example.class);</i></b>
	 * 
	 * @param className Class name ending with .class
	 * @return Formatted descriptor e.g. "APP_TAG/Example"
	 */
	public static String getClassTag(Class<?> className) {
		return new StringBuilder(Constants.APPLICATION_TAG).append(DELIMITER)
				.append(className.getSimpleName())
				.toString();
	}

	public static String desc() {
		StringBuilder builder = new StringBuilder("[");
		builder.append(Thread.currentThread().getStackTrace()[3].getLineNumber());
		builder.append("] ");
		return builder.toString();
	}

	public static String descName() {
		StringBuilder builder = new StringBuilder("[");
		builder.append(Thread.currentThread().getStackTrace()[3].getLineNumber());
		builder.append(" ");
		builder.append(Thread.currentThread().getStackTrace()[3].getMethodName());
		builder.append("] ");
		return builder.toString();
	}

	/**
	 * Use to set what class will be logged in debug mode.
	 * Usage:</br>
	 * <i>Logger.log("com.example.Example")</i>
	 * @param fullClassName
	 */
	private static void log(String fullClassName) {
		if (null == fullClassName) {
			Log.w(Constants.APPLICATION_TAG, "Class name must not be null! Not set yet.");
			return;
		}

		if (fullClassName.equals("")) {
			Log.w(Constants.APPLICATION_TAG, "Class name must not be empty string!");
			return;
		}

		debugClasses.add(fullClassName.toLowerCase());
	}

	/**
	 * Check if class needs to be debugged.</br>
	 * </br>
	 * Usage:</br>
	 * <b><i>private static final boolean debug = isDebugMode(Example.class);</i></b></br>
	 * </br> You have to add every class with <b><i>Logger.addDebuggable("com.example.Example");</i></b></br>
	 * </br> In every class you need to declare debug variable e.g. in class Example we define:</br>
	 * <b><i>private static final boolean debug = Logger.isDebugMode(Example.class);</i></b>
	 * 
	 * @param className
	 * @return true if class need to be debugged
	 */
	public static boolean isDebugMode(Class<?> c) {
		String className = c.getName();
		boolean debugClass = debugClasses.contains(className.toLowerCase())
				&& Constants.DEVELOPER_MODE;
		if (debugClass) {
			if (Constants.DEVELOPER_MODE)
				Log.d(TAG, desc() + "debug mode ENABLED for " + className);
		} else {
			if (Constants.DEVELOPER_MODE)
				Log.d(TAG, desc() + "debug mode DISABLED for " + className);
		}
		return debugClass;
	}

}
