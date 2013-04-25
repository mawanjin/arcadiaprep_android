package com.arcadiaprep.app.arca;

import com.arcadiaprep.app.arca.service.AsyncImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageLoaderActivity extends Activity{
	private AsyncImageLoader loader = AsyncImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
	}
	
	private void loadImage(final String url,final int id){
		Bitmap cacheImage = loader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap imageDrawable) {
				((ImageView)findViewById(id)).setImageBitmap(imageDrawable);
			}
		}); 
		if(cacheImage!=null)((ImageView)findViewById(id)).setImageBitmap(cacheImage);
	}

}
