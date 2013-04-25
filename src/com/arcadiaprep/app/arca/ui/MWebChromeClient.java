package com.arcadiaprep.app.arca.ui;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.arcadiaprep.app.arca.ProblemSetIntroductionActivity;
import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;

public class MWebChromeClient extends WebChromeClient implements OnCompletionListener,OnErrorListener {
	private Activity interfazWeb;
	private View mCustomVideoView;

    private FrameLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private LinearLayout mErrorConsoleContainer;
    static final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
	

    
    public MWebChromeClient(Activity iwi) {
        super();
        this.interfazWeb = iwi;
    }
    
    
    
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		super.onShowCustomView(view, callback);
		if (view instanceof FrameLayout) {
			mCustomViewContainer = (FrameLayout) view;
            mCustomViewCallback = callback;
            mContentView = (FrameLayout) interfazWeb.findViewById(R.id.fullscreenContainer);
            mContentView.setVisibility(View.VISIBLE);
//            View v = mCustomViewContainer.getFocusedChild();
            
            mCustomVideoView =  mCustomViewContainer.getFocusedChild();
            // frame.removeView(video);
            
            mCustomViewContainer.setVisibility(View.VISIBLE);
            mCustomViewContainer.setBackgroundColor(Color.TRANSPARENT);
            mContentView.addView(mCustomViewContainer);
            mContentView.setVisibility(View.VISIBLE);
//            interfazWeb.setContentView(mContentView);
            
            
//            mCustomVideoView.setOnCompletionListener(this);
//            mCustomVideoView.setOnErrorListener(this);
//            mCustomVideoView.start();
            if(interfazWeb instanceof ProblemSetIntroductionActivity)
            	((ProblemSetIntroductionActivity)interfazWeb).fullscreen = true;
            else 
            	((QuestionViewActivity)interfazWeb).fullscreen = true;
        }
	}

	@Override
	public void onHideCustomView() {
		super.onHideCustomView();
		 if (mCustomVideoView == null)
	            return;
		 
	        // Hide the custom view.
	        mCustomVideoView.setVisibility(View.GONE);
	        // Remove the custom view from its container.
	        mCustomViewContainer.removeView(mCustomVideoView);
	        mCustomVideoView = null;
	        mCustomViewContainer.setVisibility(View.GONE);
	        mCustomViewCallback.onCustomViewHidden();
	        // Show the content view.
	        mContentView.setVisibility(View.GONE);
//	        interfazWeb.setContentView(mContentView);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.stop();
		 mCustomViewContainer.setVisibility(View.GONE);
	        onHideCustomView();
	        interfazWeb.setContentView(mContentView);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		 interfazWeb.setContentView(R.layout.main);
	        return true;
	}

}
