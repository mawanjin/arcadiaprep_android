package com.arcadiaprep.app.arca;

import com.arcadiaprep.app.arca.service.SystemService;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class ExamReviewWebViewLayout extends LinearLayout {
	
	public static WebView webviewWidget;

	public ExamReviewWebViewLayout(Context context) {
		super(context);
	}

	public ExamReviewWebViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ExamReviewWebViewLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public static ExamReviewWebViewLayout getInstance(Activity context,View.OnClickListener callback) {
		ExamReviewWebViewLayout webview = new ExamReviewWebViewLayout(context);
		View view = LayoutInflater.from(context.getApplicationContext())
				.inflate(R.layout.exam_review_webview, null);
		
		view.setLayoutParams(new LayoutParams(SystemService.getScreenWith(context.getWindowManager()),SystemService.getScreenHeight(context.getWindowManager())));
		webviewWidget = (WebView) view.findViewById(R.id.webView);
		Button btn = (Button) view.findViewById(R.id.btnExit);
		btn.setOnClickListener(callback);
		
		webview.addView(view);

		return webview;
	}

}
