
package com.arcadiaprep.app.login.util;

import com.arcadiaprep.app.login.log.Logger;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {
	private static final String TAG = Logger.getClassTag(JsonUtil.class);
	private static final boolean debug = Logger.isDebugMode(JsonUtil.class);

	public static JSONObject getObject(JSONObject object, String name) {

		JSONObject result = null;

		try {
			result = object.getJSONObject(name);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc(), e);
		} catch (NullPointerException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "npe", e);
		}

		return result;
	}

	public static JSONArray getArray(JSONObject object, String name) {

		JSONArray result = null;

		try {
			result = object.getJSONArray(name);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc(), e);
		} catch (NullPointerException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "npe", e);
		}

		return result;
	}

	public static JSONObject getArrayObject(JSONArray array, int index) {

		JSONObject result = null;

		try {
			result = (JSONObject) array.get(index);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "error: " + e.getMessage(), e);
		}

		return result;
	}

	public static String getString(JSONObject object, String name) {

		String result = StringUtil.EMPTY;

		try {
			result = object.getString(name);
			if (debug)
				Log.d(TAG, Logger.desc() + name + ": " + result);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "error: " + e.getMessage(), e);
		} catch (NullPointerException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "npe", e);
		}

		return result;
	}

	public static int getInteger(JSONObject object, String name) {

		int result = 0;

		try {
			result = object.getInt(name);
			if (debug)
				Log.d(TAG, Logger.desc() + name + ": " + result);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "error: " + e.getMessage(), e);
		}

		return result;
	}

	public static double getDouble(JSONObject object, String name) {

		double result = 0;

		try {
			result = object.getDouble(name);
			if (debug)
				Log.d(TAG, Logger.desc() + name + ": " + result);
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "error: " + e.getMessage(), e);
		}

		return result;
	}

	public static ArrayList<Integer> getIntegerArrayList(JSONObject object, String name) {

		ArrayList<Integer> result = new ArrayList<Integer>();

		try {
			JSONArray jsona = object.getJSONArray(name);
			for (int i = 0; i < jsona.length(); i++) {
				result.add(jsona.getInt(i));
				if (debug)
					Log.d(TAG, Logger.desc() + i + ": " + jsona.getInt(i));
			}
		} catch (JSONException e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "error: " + e.getMessage(), e);
		}

		return result;
	}
}
