package com.arcadiaprep.app.arca;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.constants.ConstantDiscussion;
import com.arcadiaprep.app.arca.constants.ConstantMain;
import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.inperson.InPersonManager;
import com.arcadiaprep.app.arca.mo.BookMark;
import com.arcadiaprep.app.arca.mo.ExApps;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.service.BookMarkService;
import com.arcadiaprep.app.arca.service.DiscussionService;
import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.DiscussionFragmentView;
import com.arcadiaprep.app.arca.ui.MWebChromeClient;
import com.arcadiaprep.app.arca.ui.list.ListViewMenuQuestionSetAdapter;
import com.arcadiaprep.app.arca.util.HTTPUtils;
import com.arcadiaprep.app.arca.util.ListHeightUtils;
import com.arcadiaprep.app.arca.vo.DiscussionVO;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.arca.vo.QuestionExamStatus;
import com.arcadiaprep.app.scratchpad.component.ChoicePanel;
import com.arcadiaprep.app.scratchpad.component.MainWebView;
import com.arcadiaprep.app.scratchpad.component.MainWebView.CallBack;
import com.arcadiaprep.app.scratchpad.component.WorkSpace;

public class QuestionViewActivity extends Activity {


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			
			if(showDiscussion){
				if(!HTTPUtils.isConnectInternet(QuestionViewActivity.this))showNoNetErrorDialog();
				else
					btnInteraction.performClick();
					btnClock.setVisibility(View.INVISIBLE);
			}
				
		}
	}

	private String LOG_TAG = ""+QuestionViewActivity.class;
	
	private ImageButton btnClock,btnPre,btnNxt;
	private Button btnPage,btnMenu;
	private long mlCount = 0;
	private long mlTimerUnit = 1000;
	private TextView txtClock;
	private Timer timer = null;
	private TimerTask task = null;
	
	private Message msg = null;
	private PopupWindow popupWindowMenuQuestionSet,popupChoice,popupWindowMenu,popupDiscussion;
	private List<Questions> questions;
	private Button btnBookmark,btnInteraction;
	private ImageButton  btnVideo1,btnVideo2,btnReplay,whichBtnIsPlaying;
	
	private int screenWith,screenHeight;
	private boolean isVideoEnabled = false;
	private boolean trigerChoicePanel =false;
	private boolean isShowChoicePanel=true;
	private boolean isStopped = false;
	
	private AlertDialog errorDialog;
	
	private ExamResultInfo examResultInfo;
	private List<DiscussionVO> discussions = new ArrayList<DiscussionVO>();
	
	static int currentPageNO=1;
	int totalPageCount;
	int currentHintIndex=1;
	int hintCount=0;
	int totalSec = 0;
	int exAppID;
	int viewType;
	boolean showDiscussion=false;
	String section;
	
	boolean needreset=false;
	boolean loadChoice=false;
	Button bA;
	
	private WorkSpace mWorkSpace;
	private View mChoicePanel;
	private View viedoContainer;
	private View btnVideo1Container;
	private View btnVideo2Container;
	public DiscussionFragmentView discussionFragment;
	
	private WebView webViewVideo;
	
	Button btnDone;
	private AlertDialog bookMarkDialog;
	MainWebView mWebView;

	private int questionId;
	private String exAppName;
	private String totalTime;
	public static boolean timerOn = false;
	
	private Button btnReload;
	private MWebChromeClient mWebChromeClient = new MWebChromeClient(this);
	public boolean fullscreen=false;
	
	
	// private InPersonManager inPersonManager = null;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SystemService.setOrientation(this);
		Log.d(LOG_TAG, "invoke method onCreate");
		setContentView(R.layout.question_view);
		
		
		currentPageNO=1;
		screenWith = SystemService.getScreenWith(getWindowManager());
		screenHeight = SystemService.getScreenHeight(getWindowManager());
		exAppID = getIntent().getExtras().getInt("ex_app_id");
		questionId = getIntent().getExtras().getInt("question_id");
		totalTime=getIntent().getExtras().getString("ex_total_time");
		exAppName = getIntent().getExtras().getString("ex_app_name");
		
		section = getIntent().getExtras().getString("section");
		viewType = getIntent().getExtras().getInt("VIEW_TYPE");
		showDiscussion = getIntent().getExtras().getBoolean("show_discussion");
		
		questions = MainDataService.findQuestionsByExAppID(this,exAppID);

		btnClock = (ImageButton) findViewById(R.id.btnClock);
		txtClock = (TextView) findViewById(R.id.txtClock);
		btnPage = (Button) findViewById(R.id.btnPage);
		btnPre = (ImageButton) findViewById(R.id.btnPre);
		btnNxt = (ImageButton) findViewById(R.id.btnNxt);
		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnBookmark = (Button) findViewById(R.id.btnBookmark);
		btnInteraction = (Button) findViewById(R.id.btnInteraction);
		btnInteraction.setVisibility(View.VISIBLE);
		
		btnReload = (Button) findViewById(R.id.btnReload);
			
		
		viedoContainer = findViewById(R.id.viedo_container);
		
		if(!(ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW==viewType||ConstantQuestion.QUESTION_VIEW_TYPE_BOOKMARK==viewType))
			if(getString(R.string.device_screen).equals("hdpi")||getString(R.string.device_screen).equals("xhdpi"))findViewById(R.id.btnDone_container).setVisibility(View.GONE);
		
		View viedoContainerInner = findViewById(R.id.viedo_container_inner);
		LayoutParams vcip = new LayoutParams(screenWith,LayoutParams.WRAP_CONTENT);
		vcip.bottomMargin = (int)(screenHeight*0.05);
		vcip.gravity = Gravity.RIGHT;
		viedoContainerInner.setLayoutParams(vcip);
		viedoContainerInner.requestLayout();
		
		discussionFragment = new DiscussionFragmentView(this,new DiscussionFragmentView.Callback() {
			
			@Override
			public void onClose() {
				_handler.sendEmptyMessage(6);
			}
		});
		
		btnVideo1 = (ImageButton) findViewById(R.id.btnVideo1);
		btnVideo2 = (ImageButton) findViewById(R.id.btnVideo2);
		btnReplay = (ImageButton) findViewById(R.id.btnReplay);
		btnReplay.setVisibility(View.GONE);
		
		btnVideo1Container = findViewById(R.id.btnVideo1Container);
		btnVideo2Container = findViewById(R.id.btnVideo2Container);
		whichBtnIsPlaying = btnVideo1;
		
		isVideoEnabled = false;
		if (isVideoEnabled == true) {
			webViewVideo = (WebView) findViewById(R.id.webViewVideo);
		} else {
			webViewVideo = null;
		}
		if (webViewVideo != null) {
			webViewVideo.getSettings().setJavaScriptEnabled(true);
			webViewVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			webViewVideo.getSettings().setPluginsEnabled(true);
			webViewVideo.getSettings().setPluginState(PluginState.ON);
		
		
			final Activity activity = this;
			webViewVideo.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
				}
			});
			webViewVideo.setWebViewClient(new WebViewClient() {

			public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
					Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		webViewVideo.getSettings().setUseWideViewPort(true); 

//		webViewVideo.getSettings().setLoadWithOverviewMode(true);
			webViewVideo.setScrollbarFadingEnabled(true);
			webViewVideo.setHorizontalScrollBarEnabled(false);
			webViewVideo.setVerticalScrollBarEnabled(false);
		
			LayoutParams params = new LayoutParams(640,480);
			params.gravity = Gravity.CENTER;
			webViewVideo.setLayoutParams(params);
			//webViewVideo.loadUrl("file:///android_asset/VideoTemplate-iPad.html");

			webViewVideo.loadUrl("file:///android_asset/VideoTemplate-Android.html");
		}
		initTimer();
		initErrorDialog();
		
		display(viewType);
		
		//workspace begin
		mWebView = new MainWebView(this,questions,new CallBack() {
			
			@Override
			public void onSizeChanged() {
				
				if(msg==null)
					msg = new Message();
				else
					msg = Message.obtain();
				
				msg.what = 2;
				_handler.sendMessage(msg);
			}
		});
		
		if(viewType==ConstantQuestion.QUESTION_VIEW_TYPE_BEGINPRACTICE||viewType==ConstantQuestion.QUESTION_VIEW_TYPE_RESUME)
			mChoicePanel = ChoicePanel.getInstance(this,this, btnNxt, examResultInfo, questions, mWebView, currentPageNO);
		
		initPop();
//		btnReload.performClick();
	}
	
	
//	private void changeViewMode() {
//		if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}else{
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
//	}


	private boolean canvas = true;
	
	private void initWorkSpace() {
		 
		mWorkSpace = new WorkSpace(this,this,mWebView,mChoicePanel,questions,true);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.question_view);
	    layout.addView(mWorkSpace);
	    mWorkSpace.resize(screenWith,screenHeight);
	    
	    layout.requestLayout();
		//workspace end
	    
	    /**
<<<<<<< local
			@Override
			public void onApplicationEvent(ApplicationEvent event) {
				// TODO Auto-generated method stub
				// Feel free to change type to integer and define event constants
				if (event.type.equals("web")) {
					Log.d(LOG_TAG, "onApplicationEvent -> web page loaded - " + event.data);
				}
			}
	    	
	    });
	   
		registerListener();
		
		
=======
	    registerListener();
		initPop();
>>>>>>> other
		*/
	    registerListener();
//		initPop();
	    
		if(getIntent().getExtras().get("pageno")!=null)
		{
			currentPageNO = getIntent().getExtras().getInt("pageno");
		}
		// A bug  occurred when component-webview finished loading page.
		// The javascript can't execute correctly,use the following way to rectify it.
//		btnNxt.performClick();
//		btnPre.performClick();
//		btnReload.performClick();
		
		// inPersonManager.setCurrentWorkSpace(mWorkSpace);
		
	}
	
	private void initErrorDialog() {
		errorDialog = new AlertDialog.Builder(QuestionViewActivity.this)
				.setTitle("Network Error")
				.setMessage("Cannot process your request now,please try later!")
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(showDiscussion)showDiscussion=false;
						dialog.dismiss();
					}
				}).create();
	}


	private void display(int viewType) {
		
		switch (viewType) {
		case ConstantQuestion.QUESTION_VIEW_TYPE_BEGINPRACTICE:		       
		        if (totalTime != null && Integer.parseInt(totalTime)>0) {
		            //popup dialog want to timer or not
		            AlertDialog.Builder builder = new AlertDialog.Builder(this);
		            builder.setTitle("Practice Test")
		                   .setMessage("Do you want to time this practice?")
		                   .setCancelable(true)
		                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		                       public void onClick(DialogInterface dialog, int id) {
		                           btnClock.setVisibility(View.GONE);
		                           txtClock.setVisibility(View.VISIBLE);
		                           timerOn = true;
	                               btnInteraction.setVisibility(View.INVISIBLE);		                           
		                           // start timing
		                           startTimer();
		                       }
		                   })
		                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
		                       public void onClick(DialogInterface dialog, int id) {
		                            dialog.cancel();
		                            // stop timing in case timerOn
		                            if (timerOn == true) {
	                                    btnInteraction.setVisibility(View.VISIBLE);
	                                    pauseTimer();
	                                }    
		                       }
		                   });
		            AlertDialog alert = builder.create();
		            alert.show();
		        }
		        else {
		            startTimer();
		        }
			
			
			//get data
			totalPageCount = questions.size();
			btnPage.setText(currentPageNO+"/"+totalPageCount);
			
			examResultInfo = QuestionViewService.getExamResultInfoNew(this,exAppID,exAppName, questions);
			examResultInfo.setStartTime(System.currentTimeMillis()+"");
			
			break;
		case ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW:
			if(getString(R.string.device_screen).equals("hdpi"))findViewById(R.id.btnDone_container).setVisibility(View.VISIBLE);
			examResultInfo = QuestionViewService.getExamResultInfoByExAppID(this, exAppID);
			questions = MainDataService.findQuestionsByExAppID(this,exAppID);
			
			btnDone = (Button) findViewById(R.id.btnDone);
			if (btnDone != null) {
				btnDone.setVisibility(View.VISIBLE);
				btnDone.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Back to the ExamReviewActivity
						Intent i = new Intent(QuestionViewActivity.this,ExamReviewActivity.class);
						i.putExtra("ex_app_id", exAppID);
						startActivity(i);
						finish();
					}
				});
			}
			
			if(getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi"))
				btnMenu.setVisibility(View.GONE);
			else	
				btnMenu.setVisibility(View.INVISIBLE);				
			
			mlCount = examResultInfo.getEndTime();
			totalPageCount = questions.size();
			btnPage.setText(currentPageNO+"/"+totalPageCount);
			msg = new Message();
			msg.what=1;
			_handler.sendMessage(msg);
			
			break;
		case ConstantQuestion.QUESTION_VIEW_TYPE_RESUME: 
			questions = MainDataService.findQuestionsByExAppID(this,exAppID);
			totalPageCount = questions.size();
			btnPage.setText(currentPageNO+"/"+totalPageCount);
			
			examResultInfo = QuestionViewService.getExamResultInfoByExAppID(this, exAppID);
			mlCount = examResultInfo.getEndTime();
			startTimer();
			needreset = true;
			
			break;
			
		case ConstantQuestion.QUESTION_VIEW_TYPE_BOOKMARK:
			if(getString(R.string.device_screen).equals("hdpi"))findViewById(R.id.btnDone_container).setVisibility(View.VISIBLE);
			questions = MainDataService.findQuestionsByExAppID(this,exAppID);
			
			examResultInfo = QuestionViewService.getExamResultInfoByExAppID(this, exAppID);
			if (examResultInfo == null)
				examResultInfo = QuestionViewService.getExamResultInfoNew(
						this,exAppID, exAppName, questions);
			
			btnDone = (Button) findViewById(R.id.btnDone);
			
			if (btnDone != null) {
				btnDone.setVisibility(View.VISIBLE);
				btnDone.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Back to the ExamReviewActivity
						Intent i = new Intent(QuestionViewActivity.this,BookMarkActivity.class);
						startActivity(i);
						finish();
					}
				});
			}
			if(getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi"))
				btnMenu.setVisibility(View.GONE);
			else	
			btnMenu.setVisibility(View.INVISIBLE);
			btnPre.setVisibility(View.INVISIBLE);
			btnNxt.setVisibility(View.INVISIBLE);
			changeMark(true);
			

			
			mlCount = examResultInfo.getEndTime();
			totalPageCount = questions.size();
			btnPage.setText(currentPageNO+"/"+totalPageCount);
			msg = new Message();
			msg.what=1;
			_handler.sendMessage(msg);
			break;
		case ConstantQuestion.QUESTION_VIEW_TYPE_DISCUSSION:
		case ConstantQuestion.QUESTION_VIEW_TYPE_SEARCHNOTES:
			if(getString(R.string.device_screen).equals("hdpi"))findViewById(R.id.btnDone_container).setVisibility(View.VISIBLE);
			ExApps app = QuestionViewService.getExappByQuestionId(this,questionId);
			exAppName = app.getName();
			section = app.getSection();
			exAppID = app.getId();
			questions = MainDataService.findQuestionsByExAppID(this,exAppID);
			totalPageCount = questions.size();
			for(int i=0;i<questions.size();i++){
				if(questions.get(i).getId()==questionId){
					currentPageNO = i+1;
					break;
				}
			}
			
			btnPage.setText(currentPageNO+"/"+totalPageCount);
			
			btnDone = (Button) findViewById(R.id.btnDone);
			
			if (btnDone != null) {
				btnDone.setVisibility(View.VISIBLE);
				btnDone.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Back to the DiscussionActivity
						finish();
					}
				});
			}
			
			if(getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi"))
				btnMenu.setVisibility(View.GONE);
			else	
				btnMenu.setVisibility(View.INVISIBLE);
			btnNxt.setVisibility(View.INVISIBLE);
			btnPre.setVisibility(View.INVISIBLE);
						
			examResultInfo = QuestionViewService.getExamResultInfoByExAppID(this, exAppID);
			if(examResultInfo==null)
				examResultInfo = QuestionViewService.getExamResultInfoNew(this,exAppID,exAppName, questions);
			break;
		default:
			break;
		}
	}
	
	private void startTimer(){
		if(null==timer){
			if(null==task){
				task = new TimerTask() {
					
					@Override
					public void run() {
						if(msg==null)
							msg = new Message();
						else
							msg = Message.obtain();
						
						msg.what = 1;
						_handler.sendMessage(msg);
					}
					
				};
			}
			timer = new Timer(true);
			timer.schedule(task, mlTimerUnit,mlTimerUnit);
		}
	}	

	private void initTimer() {
		

	}
	
	
	private void registerListener() {
		btnClock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnClock.setVisibility(View.GONE);
				txtClock.setVisibility(View.VISIBLE);
			}
		});
		
		txtClock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txtClock.setVisibility(View.GONE);
				btnClock.setVisibility(View.VISIBLE);
			}
		});
		
		btnPage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show pop window
				showPopWinForQuestoins();
				
			}
		});
		
		btnPre.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changePage(0);
//				adjustWebview();
			}
			
		});
		btnNxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changePage(1);
//				adjustWebview();
			}
			
		});
		
		btnMenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindowMenu.showAsDropDown(btnMenu);
			}
			
		});
		
		btnBookmark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (examResultInfo.getQuestions().get(currentPageNO - 1)
						.isMark()) {
					examResultInfo.getQuestions().get(currentPageNO - 1)
							.setMark(false);
					changeMark(false);
					saveBookMark(false);
				} else {
					examResultInfo.getQuestions().get(currentPageNO - 1)
							.setMark(true);
					changeMark(true);
					saveBookMark(true);
				}
					
			}
			
		});
		
		btnInteraction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!HTTPUtils.isConnectInternet(QuestionViewActivity.this))showNoNetErrorDialog();
//				else if(btnInteraction.getText().toString().equals("0")){
//					btnInteraction.setEnabled(false);
//				}
				else{
//					btnInteraction.setEnabled(true);
					if(popupDiscussion.isShowing())
						popupDiscussion.dismiss();
					else{//test begin -- after test uncomment here
						mWorkSpace.saveCompositeImage();
						//test end
						
						if(getResources().getString(R.string.device_screen).equals("xhdpi")||getResources().getString(R.string.device_screen).equals("hdpi")){
							Intent intent = new Intent(QuestionViewActivity.this,DiscussionActivity.class);
							intent.putExtra("from", "question_view");
							intent.putExtra("question_id", questionId);
							startActivity(intent);
						}else{
							popupDiscussion.showAsDropDown(btnInteraction, (-(int)popupDiscussion.getWidth()/2+5), 0);
							discussionFragment.showDiscussionDialog(questionId);	
						}
						
					}
				}
				
				
			}
		});
		
		
		
		//Video Buttons
		if (btnReplay != null)
		btnReplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!HTTPUtils.isConnectInternet(QuestionViewActivity.this))showNoNetErrorDialog();
				else{
					webViewVideo.setVisibility(View.VISIBLE);
					isStopped = false;
					webViewVideo.loadUrl("javascript:replay()");
				}
			}
			
		}); 
		
		btnVideo1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!HTTPUtils.isConnectInternet(QuestionViewActivity.this))showNoNetErrorDialog();
				else if(btnVideo1==whichBtnIsPlaying&&webViewVideo.getVisibility()==View.VISIBLE){
					webViewVideo.loadUrl("javascript:stopVideo()");
					webViewVideo.setVisibility(View.INVISIBLE);
					isStopped = true;
				}else {
						webViewVideo.setVisibility(View.VISIBLE);
						if(!isStopped){
							webViewVideo.loadUrl("javascript:fillContent('"+questions.get(currentPageNO-1).getVideo1()+"')");
							webViewVideo.loadUrl("javascript:replay()");
						}
						webViewVideo.loadUrl("javascript:playVideo()");
						whichBtnIsPlaying = btnVideo1;
						isStopped=false;
					}
			}
			
		});
		
		btnVideo2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!HTTPUtils.isConnectInternet(QuestionViewActivity.this))showNoNetErrorDialog();
				else if(btnVideo2==whichBtnIsPlaying&&webViewVideo.getVisibility()==View.VISIBLE){
					webViewVideo.loadUrl("javascript:stopVideo()");
					webViewVideo.setVisibility(View.INVISIBLE);
					isStopped = true;
				}else{
						webViewVideo.setVisibility(View.VISIBLE);
						if(!isStopped){
							webViewVideo.loadUrl("javascript:fillContent('"+questions.get(currentPageNO-1).getVideo2()+"')");
							webViewVideo.loadUrl("javascript:replay()");
						}
						webViewVideo.loadUrl("javascript:playVideo()");
						whichBtnIsPlaying = btnVideo2;
						isStopped = false;
				}
			}
			
		});
		
		btnReload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changePage(-1);
			}
		});	
		
	}
	
	protected void adjustWebview() {
		if(mWorkSpace!=null){
			mWorkSpace.resize(screenWith,screenHeight);
//			mWorkSpace.mWebView.reload();
			btnReload.performClick();
		}
		
	}

	protected void showNoNetErrorDialog() {
		errorDialog.show();
	}

	private void changePage(int i) {
		mWebView.hasJSReload = false;
		
		if(totalPageCount<1)return;
		if(i==1){//next page
			if(totalPageCount>currentPageNO){
				currentPageNO++;
			}
		}else if(i==0){//previous page
			if(currentPageNO>1){
				currentPageNO--;
			}
		} 
		ChoicePanel.currentPageNO=currentPageNO;
		btnPage.setText(currentPageNO+"/"+totalPageCount);
		Questions question = questions.get(currentPageNO-1);
		
		questionId = question.getId();
		
		examResultInfo.getQuestions().get(currentPageNO-1).setCorrectChoice(question.getSolution());
		
		if(ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW==viewType||ConstantQuestion.QUESTION_VIEW_TYPE_BOOKMARK==viewType){
			mWorkSpace.goQuestion(currentPageNO-1,examResultInfo.getQuestions().get(currentPageNO-1).getChoice(),true);
		}else
			mWorkSpace.goQuestion(currentPageNO-1,examResultInfo.getQuestions().get(currentPageNO-1).getChoice(),false);
		
		if(ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW==viewType||ConstantQuestion.QUESTION_VIEW_TYPE_DISCUSSION==viewType){
			mWorkSpace.mWebView.showSolution();
			mWorkSpace.mWebView.refreshScreenWidthHeight(false);
		}
		
		
//		if(ConstantQuestion.QUESTION_VIEW_TYPE_DISCUSSION==viewType)
//			changeMark((BookMarkService.get(this, exAppID, questionId)==null)?false:true);
//		else
//			changeMark(examResultInfo.getQuestions().get(currentPageNO-1).isMark());
		changeMark((BookMarkService.get(this, exAppID, questionId)==null)?false:true);
		
		//Get discussion
		
		
		ChoicePanel.adjustButtons(question.getAnswerLetters().length);
		ChoicePanel.resetBGForPress(null);
		ChoicePanel.setPreviousSelect(examResultInfo.getQuestions().get(currentPageNO-1).getChoice());
		ChoicePanel.refreshHint();
		
		if (isVideoEnabled == true)
			changeVideo();
		
		getDiscussion();
		
		
	}

	public void getDiscussion() {
		DiscussionService.getInstance(QuestionViewActivity.this).findDiscussionsCountByQuestionid(questionId,_handler);
		
	}

	private void changeVideo() {
		if (webViewVideo!= null) {
			webViewVideo.loadUrl("javascript:stopVideo()");
			webViewVideo.setVisibility(View.INVISIBLE);
		}
		isStopped = false;

		
		 Questions q = questions.get(currentPageNO-1);
		 if(q.getVideo1()==null||q.getVideo1().equals("")){
			 viedoContainer.setVisibility(View.GONE);
		 }else if(q.getVideo2()==null||q.getVideo2().equals("")){
			 viedoContainer.setVisibility(View.VISIBLE);
			 btnVideo1Container.setVisibility(View.VISIBLE);
			 btnVideo2Container.setVisibility(View.INVISIBLE);
		 }else {
			 viedoContainer.setVisibility(View.VISIBLE);
			 btnVideo1Container.setVisibility(View.VISIBLE);
			 btnVideo2Container.setVisibility(View.VISIBLE);
		 }
	}

	private void pauseTimer(){
	     timerOn = false;
		 if (null != timer) {  
             task.cancel();  
             task = null;  
             timer.cancel(); // Cancel timer  
             timer.purge();  
             timer = null;  
         }
	}
	
	private void showPopWinForQuestoins(){
		if(getString(R.string.device_screen).equals("hdpi"))popupWindowMenuQuestionSet.showAsDropDown(btnPage,0,0);
	else
		popupWindowMenuQuestionSet.showAsDropDown(btnPage,(-(int)btnPage.getWidth()/2),0);
	}
	
	
	private void initPop() {
		//question set pop
		View v = LayoutInflater.from(getApplicationContext())  
                .inflate(R.layout.menu_questionset, null);  
		
		popupWindowMenuQuestionSet = new PopupWindow(findViewById(R.id.question_view),WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		
		popupWindowMenuQuestionSet.setContentView(v);
		popupWindowMenuQuestionSet.setFocusable(true);
		popupWindowMenuQuestionSet.setOutsideTouchable(true);
		
		popupWindowMenuQuestionSet.getContentView().setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindowMenuQuestionSet.dismiss();
				return false;
			}
		});
		
		Button btnProblemsetName = (Button) v.findViewById(R.id.btnProblemsetName);
		btnProblemsetName.setText(Html.fromHtml(exAppName));
		

		ListView listView = (ListView) v.findViewById(R.id.list);
		listView.setAdapter(new ListViewMenuQuestionSetAdapter(getApplicationContext(),
				questions,examResultInfo));
		
		ListHeightUtils.setListViewHeightBasedOnChildren(listView);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				currentPageNO = position+1;
				changePage(-1);
				popupWindowMenuQuestionSet.dismiss();

			}
		});
		
		//popupWindowMenu
		View vmenu = LayoutInflater.from(getApplicationContext())  
                .inflate(R.layout.pop_questionset_menu, null);  
		
		popupWindowMenu = new PopupWindow(findViewById(R.id.question_view),WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		
		popupWindowMenu.setContentView(vmenu);
		popupWindowMenu.setFocusable(true);
		popupWindowMenu.setOutsideTouchable(true);
		
		popupWindowMenu.getContentView().setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindowMenu.dismiss();
				return false;
			}
		});
		
		final String[] options = getResources().getStringArray(R.array.pop_question_menu_list);
		ListView  listQuestionMenu = (ListView) vmenu.findViewById(R.id.list_question_menu);
		listQuestionMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item,options));
		
		listQuestionMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if((options.length-1)==position){//Cancel
					popupWindowMenu.dismiss();
				}else if(position==0){//Exit the Exam
					Log.i(LOG_TAG, "Exit the exam...and save exam status into DB.");
					mWorkSpace.saveCurrentQuestionCanvases();
					saveExamStatus(false);
					
					mWorkSpace.releaseWorkSpace();
					
					redirectToMain();
				}else if(position==1){//Finish&Review the exam
					Log.i(LOG_TAG, "Finish the exam...");
					mWorkSpace.saveCurrentQuestionCanvases();
					saveExamStatus(true);
				}else if(position==2){//Report a problem
					Log.i(LOG_TAG, "Report a problem");
					//invoke sytem service - mail to send a mail .
					SystemService.sendMail(QuestionViewActivity.this, ConstantSystem.application_mail_address, ConstantSystem.application_mail_subject, ConstantSystem.application_mail_content);
					
				}else if(position==3){//Bookmark
					Log.i(LOG_TAG, "mark a bookmark");
					//save to db
					saveBookMark(true);
					changeMark(true);
					//show a dialog a notice user the question has been added into Bookmarks.
					if(bookMarkDialog==null)
					bookMarkDialog =  new AlertDialog.Builder(QuestionViewActivity.this).setTitle("Bookmarks")
					.setMessage("Question is added into Bookmarks.")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							bookMarkDialog.dismiss();
						}
					}).create();
					bookMarkDialog.show();
				}
			}

		});
		
//		//popupDiscussion
//		View vDiscussion = LayoutInflater.from(getApplicationContext())
//				.inflate(R.layout.discussion, null);
		popupDiscussion = new PopupWindow(findViewById(R.id.question_view),WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		popupDiscussion.setContentView(discussionFragment);
		popupDiscussion.setFocusable(true);
		popupDiscussion.setOutsideTouchable(false);
		
//		popupDiscussion.getContentView().setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				popupDiscussion.dismiss();
//				return false;
//			}
//		});
		
//		discussionService = DiscussionService.getInstance(this);
//		if(questions.size()>0){
//			discussions = discussionService.findDiscussionsByQuestionid(questions.get(currentPageNO-1).getId());
//	        
//	        TextView tv = (TextView) vDiscussion.findViewById(R.id.txtCommentCount);
//	        tv.setText(discussions.size()+" comments");
//	        
//	        ListView list = (ListView) vDiscussion.findViewById(R.id.list);
//	        list.setAdapter(new ListExamDiscussionAdapter(getApplicationContext(),discussions,new ListExamDiscussionAdapter.Callback() {
//				
//				@Override
//				public void onReply(int position) {
//					popupDiscussion.dismiss();
//					popupDiscussionReply.showAsDropDown(btnInteraction, 0, 0);	
//				}
//
//				@Override
//				public void onUserInfo(int position) {
//					// TODO Auto-generated method stub
//					
//				}
//			}));
//		}
		
		
		//popDiscussionReply
//				View discussionReply = LayoutInflater.from(getApplicationContext())
//						.inflate(R.layout.discussion_reply, null);
//				popupDiscussionReply = new PopupWindow(findViewById(R.id.question_view),500,700);
//				popupDiscussionReply.setContentView(discussionReply);
//				popupDiscussionReply.setFocusable(true);
//				popupDiscussionReply.setOutsideTouchable(true);
//				
//				popupDiscussionReply.getContentView().setOnTouchListener(new View.OnTouchListener() {
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						popupDiscussionReply.dismiss();
//						return false;
//					}
//				});
        
		
	}
	
	/**
	 * keep exam status into DB when Finish or exit exam action is triggered.
	 * @param finish 
	 */
	public void saveExamStatus(boolean finish){
		//save it to DB
		pauseTimer();
		examResultInfo.setEndTime(totalSec);
		examResultInfo.setStartTime(System.currentTimeMillis()+"");
		if(examResultInfo.getProgress()<currentPageNO)
			examResultInfo.setProgress(currentPageNO);
		
		int choiceCount=0;
		
		examResultInfo.setFinish(finish);
		 int c =0;
		 for(QuestionExamStatus q : examResultInfo.getQuestions()){
			 if(q.getChoice().equals(q.getCorrectChoice()))c++;
			 if(!q.getChoice().equals("-1"))choiceCount++;
		 }
		 if(choiceCount>examResultInfo.getProgress())examResultInfo.setProgress(choiceCount);
		examResultInfo.setScore(c);
		examResultInfo.setQuestionCount(examResultInfo.getQuestions().size());
		examResultInfo.setSection(section);
		QuestionViewService.saveExamResultInfo(getApplicationContext(), examResultInfo);
		
		if(finish){
			//redirect to exam review
			Intent i =new Intent(QuestionViewActivity.this,ExamReviewActivity.class);
			i.putExtra("ex_app_id", exAppID);
			startActivity(i);
			finish();
		}
		
	}
	
	
	private void changeMark(boolean f){
		if(f){
			btnBookmark.setBackgroundDrawable(getResources().getDrawable(R.drawable.bookmark2));
		}else{
			btnBookmark.setBackgroundDrawable(getResources().getDrawable(R.drawable.bookmark));
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		popupWindowMenuQuestionSet.dismiss();
		if(popupChoice!=null)
			popupChoice.dismiss();
	}

	@Override
	protected void onStart() {
    	//Log.d(LOG_TAG, "onStart... start");
			super.onStart();
			screenWith = SystemService.getScreenWith(getWindowManager());
			screenHeight = SystemService.getScreenHeight(getWindowManager());
			
//			mWebView.requestLayout();
			
			Message m = new Message();
			m.what=7;
			_handler.sendMessage(m);
			
			
//			mWebView.requestLayout();
			
//			mWebView.requestLayout();
    	//Log.d(LOG_TAG, "onStart... end");
	}
	

	@Override
	protected void onStop() {
		if (webViewVideo != null) {
			webViewVideo.loadUrl("javascript:stopVideo()");
			webViewVideo.setVisibility(View.INVISIBLE);
			webViewVideo.loadUrl("file:///android_asset/blank.html");

		}
		isStopped = false;
		webViewVideo=null;

    	//Log.d(LOG_TAG, "onStop... start");
		super.onStop();
		if (mWorkSpace != null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.question_view);
		    layout.removeView(mWorkSpace);
			mWorkSpace.releaseWorkSpace();
			mWorkSpace = null;
		}
    	//Log.d(LOG_TAG, "onStop... end");
	}

	@Override
	protected void onDestroy() {
		super.onPause();
		if(mWorkSpace!=null)
			mWorkSpace.releaseWorkSpace();
		
		if (webViewVideo!= null)
			webViewVideo.loadUrl("file:///android_asset/blank.html");
		
	}
	
	/**
	 * 
	 * @param saveordel true -save false-delete
	 */
	private void saveBookMark(boolean saveordel) {
		BookMark bookMark = new BookMark();
		bookMark.setExappid(exAppID);
		bookMark.setQuestionId(questions.get(currentPageNO - 1).getId());
		bookMark.setPosition(currentPageNO);
		bookMark.setTitle(examResultInfo.getExAppName());
		bookMark.setDate(System.currentTimeMillis()+"");
		if(saveordel){
			BookMarkService.save(QuestionViewActivity.this, bookMark);
			
		}
		else{
			BookMarkService.del(QuestionViewActivity.this, bookMark);
		}
		
		if(viewType==ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW){
			QuestionViewService.updateQuestionExamResultInfoMark(this,exAppID,bookMark.getQuestionId(),saveordel);
		}
			
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(LOG_TAG, "this onConfigurationChanged... ");
		
		super.onConfigurationChanged(newConfig);
//		SystemService.setOrientation(this);
		
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.d(LOG_TAG, "isChangingConfigurations... ORIENTATION_LANDSCAPE ");

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.d(LOG_TAG, "isChangingConfigurations... ORIENTATION_PORTRAIT ");

		}
		
		screenWith = SystemService.getScreenWith(getWindowManager());
		screenHeight = SystemService.getScreenHeight(getWindowManager());
			
		adjustWebview();
		
	}
	
	@Override
	public void onBackPressed() {
		if(fullscreen){
			mWebChromeClient.onHideCustomView();
			resizeVideoWebView();
			webViewVideo.loadUrl("javascript:play()");
			fullscreen=false;
		}else{
			if(viewType==ConstantQuestion.QUESTION_VIEW_TYPE_BEGINPRACTICE||viewType==ConstantQuestion.QUESTION_VIEW_TYPE_RESUME){
				saveExamStatus(false);
				redirectToIntroduction();
			}else
				finish();
		}
		
		
	}
	
private void resizeVideoWebView(){
		
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(640,480); 
		webViewVideo.setLayoutParams(params);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
//		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case ConstantSystem.ACTIVITY_SELECT_IMAGE:
				if(resultCode==RESULT_OK){
					Uri selectedImage = data.getData();
		            String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String filePath = cursor.getString(columnIndex);
		            cursor.close();

		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
		            discussionFragment.setImagAttach(filePath,yourSelectedImage);
		            showDiscussion = false;
				}
			break;
		}
	}
	
	Handler _handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mlCount++;
				int yushu = 0;
				totalSec = (int) (mlCount);
				
				// Set time display
				int min = (totalSec / 60);	
				if (totalTime == null) {
					// do nothing
				    txtClock.setTextColor(Color.WHITE);
				}
				else if ((Integer.parseInt(totalTime) - min) < 5) {      
				    txtClock.setTextColor(Color.RED);
				} else if ((Integer.parseInt(totalTime) - min) == 0 && (totalSec % 60 == 0)) {   				    
				    //popup dialog want to stop or continue
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionViewActivity.this);
                    builder.setTitle("Time is up")
                           .setMessage("Do you want to continue exam?")
                           .setCancelable(true)
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   // continue;
                                   dialog.cancel();
                               }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
	                                btnInteraction.setVisibility(View.VISIBLE);
                                    pauseTimer();
                                    //redirect to exam review
                                    saveExamStatus(true);
                               }
                           });
                    AlertDialog alert = builder.create();
                    alert.show();
				}
				int sec = (totalSec % 60);
				int hour = (min / 60);
				try {
					// second(1000ms)
					txtClock.setText(String.format("%1$02d:%2$02d:%3$02d", hour,min, sec));
				} catch (Exception e) {
					txtClock.setText("" + min + ":" + sec + ":" + yushu);
					e.printStackTrace();
					Log.e("QuestionViewActivity", "Format string error.");
				}
				break;
			case 2:
				//notify worksapce to resize
				if (mWorkSpace != null){
//					if(!mWebView.hasJSReload){
//						mWebView.js_reload();
//						ConstantMain.html_px = mWebView.getHeight();
//						ConstantMain.measure_px = mWebView.getMeasuredHeight();
//					}
					mWebView.measure(0,0);
					mWorkSpace.notifyWebViewSizeChanged(mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
				}
					
				break;
			case 44:
				 discussions = (List<DiscussionVO>) msg.obj; 
				 if(discussions.size()>0){
					 btnInteraction.setText(discussions.size()+"");
					 btnInteraction.setEnabled(true);}
				 else{
					 btnInteraction.setText("0");
				 }

				break;
			case 5:
				 if(((Integer)msg.obj)>0){
					 btnInteraction.setText((Integer)msg.obj+"");
					 btnInteraction.setEnabled(true);
				 }
				 else{
					 btnInteraction.setText("0");
				 }

				break;
				
			case 6:	
				popupDiscussion.dismiss();
				break;
			case 7:
//				mWebView.reload();
//				mWebView.requestLayout();
//				btnReload.performClick();
//				mWebView.requestLayout();
				
//				mWorkSpace.resize(screenWith,screenHeight);
				initWorkSpace();
				btnReload.performClick();
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};
	
	public void setShowDiscussion(boolean showDiscussion){
		this.showDiscussion = showDiscussion;
	}
	
	private void redirectToMain(){
		popupWindowMenu.dismiss();
		popupWindowMenu=null;
		Intent intent = new Intent(QuestionViewActivity.this,ArcadiaprepActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void redirectToIntroduction(){
		Intent intent = new Intent(QuestionViewActivity.this,ProblemSetIntroductionActivity.class);
		intent.putExtra("problemset", getIntent().getParcelableExtra("problemset"));
		startActivity(intent);
		finish();
	}
	
}
