
package com.arcadiaprep.app.login.communication;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;

import java.io.UnsupportedEncodingException;

class HttpGetService extends AbstractHttpService {

	public HttpGetService(Context ctx, Callback<Result> callback) {
		super(ctx, callback);
	}

	@Override
	protected HttpUriRequest getHttpGetRequest(String uri) throws UnsupportedEncodingException {
		return new HttpGet(uri);
	}

	@Override
	protected final HttpPost getHttpPostRequest(String uri) {
		throw new UnsupportedOperationException(
				"Can't create POST request in HttpGetService class! Use HttpPostService instead.");
	}

}
