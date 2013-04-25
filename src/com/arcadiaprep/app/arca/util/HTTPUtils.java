package com.arcadiaprep.app.arca.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HTTPUtils {
	
	public static boolean isConnectInternet(Context context) {
        ConnectivityManager conManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null ){ 
                  return networkInfo.isAvailable();
        }
        return false ;
}
	
	public static String getStringByGet(String server_url){
		HttpGet httpRequest = new HttpGet(server_url);
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				String strResult;
				strResult = EntityUtils.toString(httpResponse.getEntity());
				return strResult;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "-1"+e.getMessage();
		}
         return "";
	}

	public static JSONObject getJsonByGet(String server_url){
		if(!server_url.startsWith("http://"))server_url="http://"+server_url;
		JSONObject o = null;
		HttpGet httpRequest = new HttpGet(server_url);
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				String strResult;
				strResult = EntityUtils.toString(httpResponse.getEntity());
				o = new JSONObject(strResult);
				
				return o;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return o;
		}
		return o;
	}
	
	
	public static String getStringByPost(String server_url,List<NameValuePair> params,String urlEncode){
		if(urlEncode==null)urlEncode="UTF-8";
		 //HttpPost连接对象  
        HttpPost httpRequest = new HttpPost(server_url);  
        //使用NameValuePair来保存要传递的Post参数  
//        //添加要传递的参数  
//        params.add(new BasicNameValuePair("par", "HttpClient_android_Post"));  
        //设置字符集  
        try{
        	 HttpEntity httpentity = new UrlEncodedFormEntity(params, urlEncode);  
             //请求httpRequest  
             httpRequest.setEntity(httpentity);  
             //取得默认的HttpClient  
             HttpClient httpclient = new DefaultHttpClient();  
             //取得HttpResponse  
             HttpResponse httpResponse = httpclient.execute(httpRequest);  
             //HttpStatus.SC_OK表示连接成功  
             if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)  
             {  
                 //取得返回的字符串  
                 String strResult = EntityUtils.toString(httpResponse.getEntity());
                 return strResult;
             }
        }catch(Exception e){
        	e.printStackTrace();
        	return "-1";
        }
             
           return "";
	}
	
	public static String getUrl(String url) throws IOException {  
        HttpGet request = new HttpGet(url);  
        HttpClient httpClient = new DefaultHttpClient();  
        HttpResponse response = httpClient.execute(request);  
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
            return EntityUtils.toString(response.getEntity());  
        } else {  
            return "error";  
        }  
    } 
	
}
