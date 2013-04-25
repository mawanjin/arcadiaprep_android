package com.arcadiaprep.app.arca.util.http;


import com.arcadiaprep.app.arca.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class HttpActivity extends Activity {

	private ImageView image;
	private TextView text;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect2);
        
        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);
        
        downloadTwitterIcon();
        downloadTwitterStream();
    }
    
	public void downloadTwitterIcon() {
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						Bitmap response = (Bitmap) message.obj;
						image.setImageBitmap(response);
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						break;
					}
				}
			}
		};
		new HttpConnection(handler).bitmap
		("http://www.bogotobogo.com/images/HTML5/HTML5-Number5.png");
	}

	public void downloadTwitterStream() {
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				switch (message.what) {
					case HttpConnection.DID_START: {
						text.setText("Starting connection...");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						String response = (String) message.obj;
						text.setText(response);
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						text.setText("Connection failed.");
						break;
					}
				}
			}
		};
		new HttpConnection(handler).get
		("http://www.bogotobogo.com/HTML5/HTML5_Tutorial.html");
	}
}