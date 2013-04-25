package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.login.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Result {
	private static final String TAG = Logger.getClassTag(Result.class);
	private static final boolean debug = Logger.isDebugMode(Result.class);
	
	public static final int OK = 0;
	public static final int INTERNET_NOT_AVAILABLE = 1;
	public static final int SOME_ERROR = 2;
    public static final int DOES_NOT_EXIST = 404;
	
	private String response;
	private JSONObject result;
	private int status;
	
	public Result(int status) {
		this.status = status;
	}
	
	public Result(String response, int status) throws Exception {
		
		try {
			result = new JSONObject(response);		
		} catch (JSONException e) {
			if (debug) Log.w(TAG, Logger.desc() + "we received a non JSON object:\n" + response);
		} catch (Exception e) {
			Log.e(TAG, Logger.desc() + "error parsing result! error: " + e.getMessage(), e);
			throw e;
		}

		this.response = response;
		this.status = status;
	}
	
	public JSONObject getResult() {
		return result;
	}
	public int getStatus() {
		return status;
	}
	
	public String getResponse() {
		return response;
	}	
	
}
