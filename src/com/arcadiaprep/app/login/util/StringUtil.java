
package com.arcadiaprep.app.login.util;

public class StringUtil {

	public static final String EMPTY = "";
	public static final String NULL = "null";
	public static final String HTTP_SLASHES = "://";
	public static final String SLASH = "/";

	/**
	 * Use to normalize URL parameters by returning
	 * empty string instead of null or "null".
	 * @param url URL parameter to normalize
	 * @return Normalized parameter
	 */
	public static String nullToEmpty(String url) {
		if (null == url || url.equals(NULL))
			return EMPTY;
		return url;
	}

	/**
	 * Check if string is null or empty.
	 * @return <b>true</b> if string is null or empty
	 */
	public static boolean isEmpty(String s) {
		if (null == s || s.trim().equals(EMPTY) || s.trim().equals(NULL))
			return true;
		return false;
	}
}
