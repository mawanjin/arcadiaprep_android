package com.arcadiaprep.app.arca.ui.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.arcadiaprep.app.arca.R;

public class VideoDialogFragment extends DialogFragment{
	
	WebView webViewVideo;
	MediaController mMediaController;
	
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		webViewVideo.loadUrl("javascript:stopVideo()");
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}

	
	
	public static VideoDialogFragment newInstance() {
		VideoDialogFragment f = new VideoDialogFragment();
		Bundle args = new Bundle();
//		args.putString("folderPath", folderPath);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.video_view_fragment, container, false);
//		VideoView videoView = (VideoView) v.findViewById(R.id.videoView);
//		//Create media controller
//		mMediaController = new MediaController(getActivity());
//		//设置MediaController
//		videoView.setMediaController(mMediaController);
////		videoView.setVideoURI(Uri.parse("http://view.vzaar.com/935244/video"));
//		videoView.setVideoURI(Uri.parse("/mnt/sdcard/Samsung/Videos/Art of Flight.mp4"));
//		videoView.start();
		

		
		webViewVideo = (WebView) v.findViewById(R.id.webview);
		webViewVideo.getSettings().setJavaScriptEnabled(true);
		webViewVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webViewVideo.getSettings().setPluginsEnabled(true);
		webViewVideo.getSettings().setPluginState(PluginState.ON);
		
		webViewVideo.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				getActivity().setProgress(progress * 1000);
			}
		});
		webViewVideo.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		webViewVideo.setScrollbarFadingEnabled(true);
		webViewVideo.setHorizontalScrollBarEnabled(true);
		webViewVideo.setVerticalScrollBarEnabled(true);
		webViewVideo.getSettings().setDomStorageEnabled(true);
		webViewVideo.requestFocus();
		
		
//		LayoutParams params = new LayoutParams(640,480);
//		webViewVideo.setLayoutParams(params);
		
		webViewVideo.loadUrl("file:///android_asset/VideoTemplate-iPad.html");
		webViewVideo.loadUrl("javascript:playVideo()");
		return v;
	}
	

}
