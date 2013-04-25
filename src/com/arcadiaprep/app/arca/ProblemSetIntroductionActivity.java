package com.arcadiaprep.app.arca;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.MainListenerService;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.MWebChromeClient;
import com.arcadiaprep.app.arca.ui.list.ListViewMenuProblemSetAdapter;
import com.arcadiaprep.app.arca.util.ListHeightUtils;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;

public class ProblemSetIntroductionActivity extends Activity implements View.OnClickListener{
	

	@Override
	protected void onDestroy() {
		popupWindow.dismiss();
		popupWindow = null;
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		webContainer.loadUrl("file:///android_asset/blank.html");
		
		super.onStop();
	}

	private String LOG_TAG = "ProblemSetIntroductionActivity";
	
	ListItemMyQuestionVO vo ;
	TextView txtSetName;
	WebView webContainer;
	Button btnMenu,btnReview,btnResume,btnPractice,btnBack;
	Fragment menuFragment;
	PopupWindow popupWindow;
	ExamResultInfo info;
	boolean vedio=false;
	String content ;
	public boolean fullscreen=false;
	private MWebChromeClient mWebChromeClient = new MWebChromeClient(this);
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problemset_introduction);
		int width = SystemService.getScreenWith(getWindowManager());
		int height = SystemService.getScreenHeight(getWindowManager());
		View container_button = findViewById(R.id.container_button);
		LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT,(int)(height*0.1));
		
		p.setMargins((int)(height*0.1),0, (int)(height*0.1), 0);
		container_button.setLayoutParams(p);
		
		//View container_brief = findViewById(R.id.container_brief);
		//android.widget.LinearLayout.LayoutParams pb = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,(int)(height*0.7));
		//android.widget.RelativeLayout.LayoutParams pb = new android.widget.RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,(int)(height*0.7));
		//pb.setMargins((int)(width*0.05), (int)(width*0.05), (int)(width*0.05), 0);
		//container_brief.setLayoutParams(pb);
		
		
		vo = getIntent().getParcelableExtra("problemset");
		
		txtSetName = (TextView) findViewById(R.id.setname);
		txtSetName.setText(vo.getTitle()+":"+vo.getInfo());
		
		webContainer = (WebView) findViewById(R.id.webContainer);
		
		final Activity activity = this;

		webContainer.getSettings().setJavaScriptEnabled(true);
		webContainer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webContainer.getSettings().setPluginsEnabled(true);
		webContainer.getSettings().setPluginState(PluginState.ON);
		webContainer.setWebChromeClient(mWebChromeClient);
		//webContainer.setWebChromeClient(new WebChromeClient());

//		webContainer.setWebChromeClient(new WebChromeClient()  {
//			@Override
//			public void onShowCustomView(View view, CustomViewCallback callback) {
//				Log.d(LOG_TAG, "this is the method of onShowCustomView");
//				super.onShowCustomView(view, callback);
//			}
//
//			@Override
//			public void onHideCustomView() {
//				Log.d(LOG_TAG, "this is the method of onHideCustomView");
//				super.onHideCustomView();
//			}
//
//			public void onProgressChanged(WebView view, int progress) {
//				// Activities and WebViews measure progress with different
//				// scales.
//				// The progress meter will automatically disappear when we reach
//				// 100%
//				activity.setProgress(progress * 1000);
//			}
//		});
		webContainer.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(vedio){
					webContainer.loadUrl("javascript:fillContent('"+content+"')");
//					webContainer.loadUrl("javascript:playVideo()");
					
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
//		webViewVideo.getSettings().setUseWideViewPort(true); 
//		webViewVideo.getSettings().setLoadWithOverviewMode(true);
		webContainer.setScrollbarFadingEnabled(true);
		webContainer.setHorizontalScrollBarEnabled(false);
		webContainer.setVerticalScrollBarEnabled(false);
		webContainer.getSettings().setUseWideViewPort(false); 
		
//		Log.d(LOG_TAG, "load data [problemset introduction]::"+MainDataService.findProblemSetIntroductionByExAppId(this,vo.getExAppId()));
		content = MainDataService.findProblemSetIntroductionByExAppId(this,vo.getExAppId());
				
		if(content.contains("<video")){
			vedio = true;
			resizeVideoWebView();
			// webContainer.loadUrl("file:///android_asset/Video.html");
			StringBuffer source = new StringBuffer(); 

			try {
			    BufferedReader reader = new BufferedReader(
			        new InputStreamReader(getAssets().open("Video.html")));
			    	
				int len = 0, ch; 
				// read the file char by char 
				while( (ch = reader.read()) != -1)     
				source.append((char)ch); 			    // do reading

			    reader.close();
			} catch (Exception e) {
			    //log the exception
			}
		
			webContainer.loadDataWithBaseURL("http://www.arcadiaprep.com/_upload/questions/",source.toString(), "text/html", "utf-8",null);
			
			webContainer.setInitialScale(100); // set the page fit into the current width

		}else{
			vedio = false;
			webContainer.loadDataWithBaseURL("file:///android_asset/",content, "text/html", "utf-8",null);
		}
			
//		<video width=\"100%\" height=\"100%\" id=\"vid\" controls=\"\" tabindex=\"0\" onclick=\"this.play();\" poster=\"sbvideos/DSF-1.jpg\" preload=\"none\" src=\"sbvideos/DSF-1.mp4\"> Your browser does not support the video tag. </video>
		
		
		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnReview = (Button) findViewById(R.id.btnReview);
		
		btnMenu.setOnClickListener(this);
		btnReview.setOnClickListener(this);
		
		btnResume = (Button) findViewById(R.id.btnResume);
		
		btnPractice = (Button) findViewById(R.id.btnPractice);
		
		btnPractice.setOnClickListener(this);
		btnResume.setOnClickListener(this);
		
		btnBack =  (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		initPop();
		
		// get examResultInfo by exappid
		info = QuestionViewService.getExamResultInfoByExAppID(
				this, vo.getExAppId());
		
		if (QuestionViewService.getNumOfQuestions(this, vo.getExAppId()) == 0)
		{
			btnPractice.setVisibility(View.GONE);
			btnResume.setVisibility(View.GONE);
			btnReview.setVisibility(View.GONE);
		}
		else if(info==null){
			btnResume.setVisibility(View.GONE);
			btnReview.setVisibility(View.GONE);
		}else if (!info.isFinish()) {
			btnReview.setVisibility(View.GONE);
		}else if(info.isFinish())
			btnResume.setVisibility(View.GONE);
			
	}
	@Override
	public void onClick(View v) {
		String which="nothing";
		if(v.getId()==btnMenu.getId()){
			which="menu";
			if(popupWindow.isShowing())popupWindow.dismiss();
			else{
				popupWindow.showAsDropDown(btnMenu);
			}
				
		}else if(v.getId()==btnReview.getId()){
			which="review";
			//redirect to exam review
			Intent i =new Intent(ProblemSetIntroductionActivity.this,ExamReviewActivity.class);
			i.putExtra("ex_app_id", vo.getExAppId());
			startActivity(i);
			finish();
		}else if(v.getId()==btnPractice.getId()){
			Intent i = new Intent(this,QuestionViewActivity.class);
			i.putExtra("VIEW_TYPE", ConstantQuestion.QUESTION_VIEW_TYPE_BEGINPRACTICE);
			i.putExtra("ex_app_id", vo.getExAppId());
			i.putExtra("ex_app_name", vo.getTitle()+":"+vo.getInfo());
			i.putExtra("section", vo.getSection());
			i.putExtra("problemset", vo);
			i.putExtra("ex_total_time", vo.getTotalTime());
			startActivity(i);
			finish();
			
		}else if(v.getId()==btnResume.getId()){
			Intent i = new Intent(this,QuestionViewActivity.class);
			i.putExtra("VIEW_TYPE", ConstantQuestion.QUESTION_VIEW_TYPE_RESUME);
			i.putExtra("ex_app_id", vo.getExAppId());
			i.putExtra("ex_app_name", vo.getTitle()+":"+vo.getInfo());
			i.putExtra("section", vo.getSection());
			i.putExtra("pageno", info.getProgress());
			i.putExtra("problemset", vo);
			startActivity(i);
			finish();
		}
		else if (v.getId() == btnBack.getId()) {
			
			if(fullscreen){
				mWebChromeClient.onHideCustomView();
				resizeVideoWebView();
				webContainer.loadUrl("javascript:playVideo()");
				fullscreen=false;
			}
			
		}
	}
	
	private void initPop() {
		View v = LayoutInflater.from(getApplicationContext())  
                .inflate(R.layout.menu_problemset, null);  
		
		popupWindow = new PopupWindow(findViewById(R.id.problemset_introduction),WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		
		popupWindow.setContentView(v);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		
		Button btnBack = (Button) v.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ArcadiaprepActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

		ListView listView = (ListView) v.findViewById(R.id.list);
		List<ListItemMyQuestionVO> questionSets = new ArrayList<ListItemMyQuestionVO>();
		for(ListItemMyQuestionVO vo : MainDataService.questionSets){
			if(!vo.isRecommends())questionSets.add(vo);
		}
		
		listView.setAdapter(new ListViewMenuProblemSetAdapter(getApplicationContext(),
				questionSets));
		
		ListHeightUtils.setListViewHeightBasedOnChildren(listView);
		
		MainListenerService.getInstance().registerMyQuestionSetsListener(
				this, listView);
		
	}
	
	private void resizeVideoWebView(){
		
//		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(640,480); 
		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		webContainer.setLayoutParams(params);
	}
	
	@Override
	public void onBackPressed() {
		if(fullscreen){
			mWebChromeClient.onHideCustomView();
			resizeVideoWebView();
			webContainer.loadUrl("javascript:playVideo()");

			
			fullscreen=false;
//			mWebChromeClient.onCloseWindow(webContainer);
		}else{
			Intent i = new Intent(this, ArcadiaprepActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			finish();
		}
		
	}
	
}
