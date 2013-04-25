
package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.login.common.EntityPart;
import com.arcadiaprep.app.login.log.Logger;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;

class HttpPostService extends AbstractHttpService {
	private static final String TAG = Logger.getClassTag(HttpPostService.class);
	private static final boolean debug = Logger.isDebugMode(HttpPostService.class);

	protected List<EntityPart> parts;

	public HttpPostService(Context ctx, Callback<Result> callback, List<EntityPart> parts) {
		super(ctx, callback, TYPE_HTTP_POST);
		this.ctx = ctx;
		this.parts = parts;
	}

	@Override
	protected final HttpUriRequest getHttpGetRequest(String uri) {
		throw new UnsupportedOperationException(
				"Can't create GET request in HttpPostService class! Use HttpGetService instead.");
	}

	protected MultipartEntity getMultipartEntity() throws UnsupportedEncodingException {
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		//		multipartEntity.addPart("Filename", new StringBody(filename));
		//        multipartEntity.addPart("ImageData",
		//        		new FileBody(new File(FileUtil.getBarcodeDirectory(ctx) + filename), "image/jpeg"));

		//		//http://arcadia.witagg.com/jsonlogin/?login_user=dragan.marjanovic@gmail.com&login_pass=aaaaaa&login=login
		//		multipartEntity.addPart("login_user", new StringBody("dragan.marjanovic@gmail.com"));
		//		multipartEntity.addPart("login_pass", new StringBody("aaaaaa"));
		//		multipartEntity.addPart("login", new StringBody("login"));

		for (EntityPart part : parts) {
			multipartEntity.addPart(part.getName(), new StringBody(part.getBody()));
		}

		return multipartEntity;
	}

	@Override
	protected final HttpPost getHttpPostRequest(String uri) throws UnsupportedEncodingException {
		if (debug)
			Log.d(TAG, Logger.descName());
		
		MultipartEntity multipartEntity = getMultipartEntity();
		HttpPost httpPost = new HttpPost(uri);

		
		
		
//		String username ="dragan.marjanovic@gmail.com";
//		String password = "aaaaaa";
//		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
//		httpPost.addHeader(BasicScheme.authenticate(creds,"US-ASCII",false));
		
		
		
		httpPost.setEntity(multipartEntity);
		return httpPost;
	}

}
