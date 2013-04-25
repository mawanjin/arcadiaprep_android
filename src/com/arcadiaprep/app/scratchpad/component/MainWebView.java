package com.arcadiaprep.app.scratchpad.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.event.EventDispatcher;

public class MainWebView extends WebView {
	
	
	

	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		callBack.onSizeChanged();
//		loadContent();
//		if(!choice.equals("-1")&&!choice.equals("") ){    
//			char[] qs = choice.toCharArray();
//			for(int i=0;i<qs.length;i++){
//				choice(qs[i]);
////				loadUrl("javascript:choice("+qs[i]+")");
//			}
//		}
//		if(showSolution){
//			showSolution();
//		}
//		refreshScreenWidthHeight();
	}

	String LOG_TAG =""+MainWebView.class;
	private int webScaleInPercent = 100;
    private Handler mHandler = new Handler();
    private List<Questions> questions;
    public String url;
    private Activity parent;
    private boolean fullScreen = true;
    public static boolean hasJSReload = false;
    
    private List<Thread> threads = new ArrayList<Thread>();
    
    private  boolean  pageFinished=false;
    private boolean isRunning = false;

    private synchronized void playThread(){
    	
		if (pageFinished && !isRunning) {
			if (threads.size() > 0) {
				isRunning = true;
				threads.get(0).start();
				threads.remove(0);
			}

		}
    }
    
	@SuppressLint("SetJavaScriptEnabled")
	public MainWebView(Context context,List<Questions> questions,CallBack callBack) {
		super(context);
		this.callBack = callBack; 
        WebSettings webSettings = this.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(false);
        
//        this.setBackgroundColor(0);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); //moves scrollbar outside the view
        this.setWebChromeClient(new MyWebChromeClient());
        this.setWebViewClient(new MyWebViewClient());
        this.getSettings().setJavaScriptEnabled(true);
        
        if(getResources().getString(R.string.device_screen).equals("xhdpi")){
        	webScaleInPercent = 165; // increase the scale to 165%, so the font in xhdpi can look bigger
        } else
        	webScaleInPercent = 100;
        
        addJavascriptInterface(new Object(){
        	public void notifyHeightChanged(final int height){
        		adjustSize(height * webScaleInPercent / 100);
        	}
        	
        	public void notifyJSReload(String _hasReload){
        		MainWebView.hasJSReload = Boolean.parseBoolean(_hasReload);
        	}
        }, "webview");
        
        
        this.questions = questions;
        adjust(1);
        url = "file:///android_asset/problemfull.html";
        
        this.setInitialScale(webScaleInPercent);
        this.loadUrl(url);
        new Thread(){

			@Override
			public void run() {
				super.run();
				while(true){
					playThread();
				}
				
			}}.start();
	}
	
	
    /**
     * Places the webView loadUrl onto a separate thread. 
     * <p>
     * This method always returns immediately
     * @param  url  the url to call on the current webView
     * @return      none
     */
    public void threadSafeLoadUrl ( String url ) {
    		final String _url = url;
    		final WebView webView = this;
    		mHandler.post(new Runnable() {
            public void run() {
            		webView.loadUrl(_url);
            }
        });
    }
    private String choice="";
    boolean showSolution;
    CallBack callBack;
    
    private void adjustSize(final int height){
    	if(parent==null)return;
    	mHandler.post(
        		new Thread(){

					@Override
					public void run() {
							
						adjust(height);
					}
        			
        		});
    	
    }
    protected void adjust(int height) {
    	if (!fullScreen) {
			if(getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi")) {
				int w = SystemService.getScreenWith(parent.getWindowManager());
				int h = SystemService.getScreenHeight(parent.getWindowManager());
				// Use the small value for the width
				setLayoutParams(new FrameLayout.LayoutParams((int) (((h>w)?w:h)*ConstantQuestion.QUESTION_WEBVIEW_FULL_WIDTH_RATE), height ));
			}
			else
				setLayoutParams(new FrameLayout.LayoutParams((int) (400*ConstantQuestion.QUESTION_WEBVIEW_FULL_WIDTH_RATE), height ));
			requestLayout();
		}else{
			/*
			//setLayoutParams(new FrameLayout.LayoutParams( (int)(SystemService.getScreenWith(parent.getWindowManager())*ConstantQuestion.QUESTION_WEBVIEW_FULL_WIDTH_RATE), height ));
			if(parent.getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi")){
				getLayoutParams().width=(int)(SystemService.getScreenWith(parent.getWindowManager())*ConstantQuestion.QUESTION_WEBVIEW_FULL_WIDTH_RATE);
				getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
				requestLayout();
			}
			else
			*/
				setLayoutParams(new FrameLayout.LayoutParams((int) (800*ConstantQuestion.QUESTION_WEBVIEW_FULL_WIDTH_RATE), height ));
				requestLayout();
				
		}
		Log.d(LOG_TAG, "heightheightheightheightheight=="+height+";;"+getMeasuredHeight());
		callBack.onSizeChanged();
		
	}

	Questions question;
    /**
     * 
     * @param position
     * @param choice indicate whether a choice had been marked.if -1 means no choose before.others corresponds to A.B.C.D.E...
     */
    public void changeContent(Activity parent,int position,String choice,boolean showSolution){
    	this.parent = parent;
    	this.choice = choice;
    	this.showSolution = showSolution;
    	question = questions.get(position);
    	if(question.getTextBlock1A().length()<ConstantSystem.HTML_CHAR_LIMIT||getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi")){
    		fullScreen = false;
    	}else{
    		fullScreen = true;
    	}
		loadContent();		
    }
    
    
    private void loadContent() {
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				//reset
				loadUrl("javascript:reset()");

				char[] qs = question.getSolution().toCharArray();
				String s = "[";
				for(int i=0;i<qs.length;i++){
					if(i==qs.length-1)
						s+=qs[i];
					else
						s+=qs[i]+",";
				}
				s+="]";
				
				freshPassageStemChoices(question.getTextBlock1A(),question.getQuestionStemA(),question.getAnswers(),s,question.getSolutionText());
				
				if(!choice.equals("-1")&&!choice.equals("")){         
						char[] qs1 = choice.toCharArray();
						for(int i=0;i<qs1.length;i++){
//							loadUrl("javascript:choice("+qs1[i]+")");
							choice(qs1[i]);
						}
				}
				if(showSolution){
					showSolution();
				}
				refreshScreenWidthHeight(false);
		    	isRunning = false;
			}
    		
    	});
    	
    	
		
	}
    
    public static boolean reload = false;

	public void refreshScreenWidthHeight(boolean reload){
		
		loadUrl("javascript:screenWidthHeight()");
    	MainWebView.reload = reload;
    	
    }
    
    public void showSolution(){
    	loadUrl("javascript:showSolution()");
    	showSolution = true;
    }
    
    public void clearChoice(){
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				choice = "";
		    	isRunning = false;
			}
    		
    	});
    	
    }
    
    public void js_reload(){
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				loadUrl("javascript:reload()");
		    	isRunning = false;
			}
    		
    	});
    	
    }
    
    public void choice(char which){
    	loadUrl("javascript:choice("+which+")");
    	choice = choice+""+which;
//    	final char _w = which;
//    	threads.add(new Thread(){
//
//			@Override
//			public void run() {
//				super.run();
//				loadUrl("javascript:choice("+_w+")");
//		    	choice = choice+""+_w;
//		    	isRunning = false;
//			}
//    		
//    	});
    	
    }
    
    public void reset(){
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				loadUrl("javascript:reset()");
		    	isRunning = false;
			}
    		
    	});
    	
    }
    
    public void freshPassageHtml(String html){
    	final String _h = html;
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				loadUrl("javascript:freshPassageHtml('"+_h.replaceAll("\'", "\\\\'")+"')");
		    	isRunning = false;
			}
    		
    	});
    }
    
    public void freshQuestionContent(String html){
    	final String _h = html;
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				loadUrl("javascript:freshQuestionContent('"+_h.replaceAll("\'", "\\\\'")+"')");
		    	isRunning = false;
			}
    		
    	});
    }
    
    public void setChoices(String answersJson,String solution, String solDetails){
    	final String _a = answersJson;
    	final String _s = solution;
    	final String _sol = solDetails;
    	
    	threads.add(new Thread(){

			@Override
			public void run() {
				super.run();
				loadUrl("javascript:setChoices("+_a+","+_s+",'"+_sol.replaceAll("\'", "\\\\'")+"')");
		    	isRunning = false;
			}
    		
    	});
    	
    }
    
    public void freshPassageStemChoices(String passage, String stem, String choicesJson, String solution,String solDetails) {
    	
    	final String urlString = "javascript:freshPassageStemChoices("
    			+ fullScreen +",'"
    			+ passage.replaceAll("\'", "\\\\'") + "','"
    			+ stem.replaceAll("\'", "\\\\'") + "',"
    			+ choicesJson+"," 
    			+ solution+",'"
    			+ solDetails.replaceAll("\'", "\\\\'") + "')";
    	
    	loadUrl(urlString);
    }
    
    
    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
    		final static String TAG = "WebChromeClient";
    		
    		
    	
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("MyWebChromeClient", message);
            result.confirm();
            return true;
        }
         
         @Override
         public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
	        	 Log.d(TAG, "js console - " + consoleMessage.message());
	        	 return true;
         }
    }

    /**
     * WebView client utilities.
     */
    final class MyWebViewClient extends WebViewClient {

			@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			pageFinished = false;
			
		}


			final static String TAG = "WebViewClient";
	    	
			
        /**
         * onPageFinished called each time a page loaded
         */
	    	@Override  
	    	public void onPageFinished(WebView webView, String url) {
	    		Log.d(TAG, "onPageFinished - " + url);
	    		pageFinished = true;
//	    		loadContent();
//	    		
//	    		if(!choice.equals("-1")&&!choice.equals("") ){    
//	    			char[] qs = choice.toCharArray();
//	    			for(int i=0;i<qs.length;i++){
////	    				loadUrl("javascript:choice("+qs[i]+")");
//	    				choice(qs[i]);
//	    			}
//	    		}
//	    		if(showSolution){
//	    			showSolution();
//	    		}
//	    			refreshScreenWidthHeight(false);
//		    		EventDispatcher.getInstance().fireEvent(new ApplicationEvent(this, "web", url));
	    		
	    	}  
    }
    
    public interface  CallBack{
    	public void onSizeChanged();
    }
    
}


