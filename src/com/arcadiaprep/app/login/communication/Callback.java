package com.arcadiaprep.app.login.communication;

public interface Callback<T> {

    /** Runs in main UI thread when used with AbstractHttpService. */
	public void onFinish(T result);
	
}
