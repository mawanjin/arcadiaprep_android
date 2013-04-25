
package com.arcadiaprep.app.login.communication;

import android.content.Context;

public class SecureHttpGetService extends HttpGetService {

	protected static final String SCHEME = "https";

	public SecureHttpGetService(Context ctx, Callback<Result> callback) {
		super(ctx, callback);
	}

}
