
package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;
import com.arcadiaprep.app.login.common.ConnectivityUtil;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.util.StringUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class AbstractHttpService extends AsyncTask<String, Void, Result> {
	private static final String TAG = Logger.getClassTag(AbstractHttpService.class);
	private static final boolean debug = Logger.isDebugMode(AbstractHttpService.class);

	protected static final String SCHEME = "http";

	protected static final int TYPE_HTTP_GET = 0;
	protected static final int TYPE_HTTP_POST = 1;

	protected Context ctx;
	private Callback<Result> callback;
	private int type;
	private int timeout = Constants.HTTP_TIMEOUT;

	protected String getUrl() {
		return new StringBuilder(SCHEME)
				.append(StringUtil.HTTP_SLASHES)
				.append(SystemService.parseModuelInfo(ctx).getServerURL())
				.append(StringUtil.SLASH)
				.toString();
	}

	protected abstract HttpUriRequest getHttpGetRequest(String uri)
			throws UnsupportedEncodingException;

	protected abstract HttpPost getHttpPostRequest(String uri) throws UnsupportedEncodingException;

	public AbstractHttpService(Context ctx, Callback<Result> callback, int type) {
		this.ctx = ctx;
		this.callback = callback;
		this.type = type;
	}

	public AbstractHttpService(Context ctx, Callback<Result> callback) {
		this.ctx = ctx;
		this.callback = callback;
	}

	protected Result doInBackground(String... urls) {
		if (!ConnectivityUtil.isConnected(ctx)) {
			return new Result(Result.INTERNET_NOT_AVAILABLE);
		}

		Result result = null;
		try {
			if (debug)
				Log.d(TAG, Logger.desc() + "URL: " + getUrl() + urls[0]);

			String response = pullDataFromServer(getUrl() + urls[0]);

			if (Constants.LOG_HTTP_RESPONSES)
				Log.d(TAG, Logger.desc() + "Response: " + response);

			if (null != response) {
				result = new Result(response, Result.OK);
				return result;

			} else {
				Log.e(TAG, Logger.desc() + "error: response must not be null!");
				return new Result(Result.SOME_ERROR);
			}

		} catch (Exception e) {
			Log.e(TAG, Logger.desc() + "error: " + e.getMessage(), e);
			return new Result(Result.SOME_ERROR);
		}

	}

	protected void onPostExecute(Result result) {
		if (null != callback) {
			callback.onFinish(result);
		} else {
			Log.w(TAG, Logger.desc() + "callback is null");
		}
	}

	@SuppressWarnings("unused")
	private String pullDataFromServer(String uri)
			throws ParseException, IOException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException,
			SocketTimeoutException {

		HttpClient httpclient = null;
		if (Constants.USE_SSL_BACKWARD_COMPATIBILITY && uri.startsWith("https")) {
			httpclient = createTrustAllHttpClient(timeout);
		} else {
			httpclient = createHttpClient(timeout);
		}

		HttpResponse response = null;
		switch (type) {
			case TYPE_HTTP_GET:
				response = httpclient.execute(getHttpGetRequest(uri));
				break;

			case TYPE_HTTP_POST:
				response = httpclient.execute(getHttpPostRequest(uri));
				break;
		}

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			return EntityUtils.toString(httpEntity, "utf-8");
		}

		return "{}";
	}

	private static HttpClient createHttpClient(int timeout) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		return new ContentEncodingHttpClient(httpParams);
	}

	/**
	 * Create DefaultHttpClient for SSL purposes that trusts all certificates. 
	 * @return DefaultHttpClient instance.
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 */
	public static HttpClient createTrustAllHttpClient(int timeout)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, KeyManagementException, UnrecoverableKeyException {

		if (debug)
			Log.i(TAG, Logger.descName());

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", sf, 443));

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
		return new DefaultHttpClient(ccm, httpParams);
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
				KeyManagementException, KeyStoreException, UnrecoverableKeyException {

			super(truststore);
			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] {
					tm
			}, new java.security.SecureRandom());
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {

			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
