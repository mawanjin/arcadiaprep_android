package com.arcadiaprep.app.arca.service;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class AsyncImageLoader {
	 public Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	    private ExecutorService executorService = Executors.newFixedThreadPool(5);    //Make five threads to execute task
	    private final Handler handler=new Handler();
	    private static AsyncImageLoader loader ; 
	    
	    private  AsyncImageLoader(){}
	    
	    public static synchronized AsyncImageLoader getInstance(){
	    	if(loader==null)loader=new AsyncImageLoader();
	    	return loader;
	    }
	    
	    public Bitmap loadDrawable(final String imageUrl, final ImageCallback callback) {
	        //get from cache if exists there
	        if (imageCache.containsKey(imageUrl)) {
	            SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
	            if (softReference.get() != null) {
	                return softReference.get();
	            }
	        }
	        //get from net only if not exists in cache,and push it into cache
	         executorService.submit(new Runnable() {
	            public void run() {
	                try {
	                	
	                	HttpClient httpClient = new DefaultHttpClient();
	                	HttpConnectionParams.setSoTimeout(httpClient.getParams(), 25000);
	                	HttpResponse response = null;
	                	response = httpClient.execute(new HttpGet(imageUrl));
	                	BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(response.getEntity());
	            		final Bitmap bm = BitmapFactory.decodeStream(bufHttpEntity.getContent());
//	                    final Drawable drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.png");
	            		
	                    imageCache.put(imageUrl, new SoftReference<Bitmap>(bm));

	                    handler.post(new Runnable() {
	                        public void run() {
	                           callback.imageLoaded(bm);
	                        }
	                    });
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        });
	        return null;
	    }

	    /**
	     * Get image from net
	     * @param imageUrl
	     * @return
	     */
	    protected Drawable loadImageFromUrl(String imageUrl) {
	        try {
	            return Drawable.createFromStream(new URL(imageUrl).openStream(), "image.png");
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	    public interface ImageCallback {
	        public void imageLoaded(Bitmap imageDrawable);
	    }
}
