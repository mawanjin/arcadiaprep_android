package com.arcadiaprep.app.scratchpad.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import us.gorges.viewaclue.TwoDScrollView;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.inperson.InPersonManager;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.samsung.samm.common.SAMMLibConstants;
import com.samsung.samm.common.SObject;
import com.samsung.samm.common.SObjectStroke;
import com.samsung.samm.common.SObjectText;
import com.samsung.samm.common.SOptionPlay;
import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spen.settings.SettingTextInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.AnimationProcessListener;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.samsung.spensdk.applistener.SCanvasLongPressListener;
import com.samsung.spensdk.applistener.SObjectUpdateListener;
import com.samsung.spensdk.applistener.SPenTouchListener;
import com.samsung.spensdk.applistener.SettingStrokeChangeListener;
import com.samsung.spensdk.applistener.SettingTextChangeListener;

public class WorkSpace extends RelativeLayout {
	public static final String DEFAULT_APP_IMAGEDATA_DIRECTORY = "ArcadiaPrep/images";
	public static final String DEFAULT_APP_CAPTURE_DIRECTORY = "ArcadiaPrep/capture";

	public static final String COMPOSITE_IMAGE_PATH = DEFAULT_APP_CAPTURE_DIRECTORY + "/composite_image.png";

	public static final String EXTRA_IMAGE_PATH = "path";
	public static final String EXTRA_IMAGE_NAME = "filename";

	public static final String SAVED_FILE_EXTENSION = "png";
	
	public static final String TAG = "WorkSpace";
	private int questionId = 0;
	
	private int initSCanvasWidth = 2000;
	private int initSCanvasHeight = 2000;
	private int initSCanvas2Width = 1000;
	private int initSCanvas2Height = 1000;
	
	private RelativeLayout mCanvasContainer;
	private RelativeLayout mCanvas2Container;
	private TwoDScrollView mScroller;
	private SCanvasView mSCanvas;  //main canvas
	private SCanvasView mSCanvas2; //webview canvas (the canvas over the webview)
	
	// inperson time stamps
	private long mRecordStartTime = 0 ;
	
	private ImageButton mPenBtn;
	private ImageButton mTextBtn;
	private ImageButton mEraserBtn;
	private ImageButton mUndoBtn;
	private ImageButton mRedoBtn; 
	private ImageButton mPenOnlyBtn; 
	private ImageButton mClearAllBtn;
	private File mFolder = null;
	
	private boolean mPenOnly = false;
	private boolean mScrollingOnly = false;
	private boolean mCanvasEnabled;
	private boolean mCanvasInitialized = false;
	private boolean mCanvasChanged = false;
	private MotionEvent mTouchEvent;
	private MyDragShadowBuilder mShadowBuilder;
	
	private Timer mTimer = null;  // timer for inperson
	private TimerTask mTask = null;

	
	private Activity parent;
	
	public MainWebView mWebView;
	
	public View choicePanel;
	
	List<Questions> questions;
	
	private InPersonManager inPersonManager = null;

	
	public WorkSpace(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public WorkSpace(Activity parent,Context context, MainWebView webView, int questionId, boolean bEnableCanvas) {
		super(context);
		this.parent = parent;
		mWebView = webView;
		this.mCanvasEnabled = bEnableCanvas;
		this.setQuestionId(questionId);
		onFinishInflate();
	}
	
	public WorkSpace(Activity parent, Context context, MainWebView mWebView, View choicePanel, List<Questions> questions, boolean bEnableCanvas) {
		super(context);
		this.parent = parent;
		this.mWebView = mWebView;
		this.mCanvasEnabled = bEnableCanvas;
		this.choicePanel = choicePanel;
		this.questions = questions; 
		this.setQuestionId( ((Questions)questions.get(0)).getId() );
		onFinishInflate();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.workspace, this);
		
		if(mWebView.getParent()!=null) 
	    	((ViewGroup)mWebView.getParent()).removeView(mWebView);
	    
	    ((FrameLayout)findViewById(R.id.web_view_container)).addView(mWebView);
	    registerListener(view);
		//add webview
//		addWebViewAndRefresh();
//	    
//
		mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);
		mCanvas2Container = (RelativeLayout) findViewById(R.id.canvas2_container);
		mScroller = (TwoDScrollView) findViewById(R.id.scene_scroller);
//		
//		
//		
//		//TODO mCanvasEnabled test is only to allow no canvas for emulator
	    if (mCanvasEnabled) {
	    	//add main canvas
		    RelativeLayout.LayoutParams canvasParams = new RelativeLayout.LayoutParams( initSCanvasWidth, initSCanvasHeight );
			mSCanvas = (SCanvasView) new SCanvasView(this.getContext());
			mCanvasContainer.addView(mSCanvas, canvasParams);
			
		    //add second canvas (over webview)
		    canvasParams = new RelativeLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT );
			mSCanvas2 = (SCanvasView) new SCanvasView(this.getContext());
			mCanvas2Container.addView(mSCanvas2, canvasParams);
	    }
//	    
//	   
//
	    if(choicePanel!=null){
	    	//choice panel
		    LinearLayout choicePanelContainer = (LinearLayout) findViewById(R.id.choicePanel);

		    choicePanelContainer.removeAllViews();
		    LinearLayout.LayoutParams cp;

//		    cp = new LinearLayout.LayoutParams(400,150);
		    cp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		    //cp.bottomMargin = (int)(SystemService.getScreenHeight(parent.getWindowManager())*0.02);
		    choicePanel.setAlpha(150); 
		    if(choicePanel.getParent()!=null)
			    ((ViewGroup)choicePanel.getParent()).removeView(choicePanel);
		   
		    choicePanelContainer.addView(choicePanel,cp);	
		    choicePanel.setAlpha(0.8f); 
	    }
//	    
//	    
//		// Assign the drag listeners
		mScroller.setOnDragListener(new MyDragListener());
//		
		SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.drawer);
		drawer.setOnDrawerOpenListener(mOnDrawerOpenListener);
		drawer.setOnDrawerCloseListener(mOnDrawerCloseListener);
//		
		initialize();
//		
//		// Init Inperson controller
		inPersonManager = new InPersonManager(this.getContext().getApplicationContext());
		
		View inPersonControllerView = inPersonManager.getInPersonView();
		
		
		putInpersonControllerViewPosition(inPersonControllerView);
		
		inPersonManager.setCurrentWorkSpace(this);

	}
	
	private void addWebViewAndRefresh() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( initSCanvas2Width, initSCanvas2Height );
	    FrameLayout container = (FrameLayout) findViewById(R.id.web_view_container);
	    if(mWebView.getParent()!=null) 
	    	((ViewGroup)mWebView.getParent()).removeView(mWebView);
	    container.addView(mWebView, params);
	    
	}

	private boolean trigerChoicePanel =false;
	private boolean isShowChoicePanel=true;

	public void clearCanvases() {
		//TODO mCanvasEnabled test is only to allow no canvas for emulator
	    if (mCanvasEnabled && mCanvasInitialized){
	    	Log.d(TAG, "clearCanvases");
	    	
	    	mSCanvas.doAnimationClose();
	    	mSCanvas2.doAnimationClose();

		    mSCanvas.clearSCanvasView();
		    mSCanvas2.clearSCanvasView();
			System.gc();
	    }
	}

	private void registerListener(View view) {
		
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(choicePanel!=null){
					if(event.getAction()==MotionEvent.ACTION_MOVE){
						trigerChoicePanel = false;
					}else if(event.getAction()==MotionEvent.ACTION_UP&&!trigerChoicePanel){
						trigerChoicePanel = false;
					}else
						trigerChoicePanel = true;
						
					
					if(trigerChoicePanel&&event.getAction()!=MotionEvent.ACTION_MOVE&&event.getAction()==MotionEvent.ACTION_UP){
						if (choicePanel.getVisibility()==View.VISIBLE&&isShowChoicePanel){
							isShowChoicePanel=false;
							choicePanel.setVisibility(View.INVISIBLE);
						}else{
							isShowChoicePanel=true;
							choicePanel.setVisibility(View.VISIBLE);
						}
						
					}
				}
//				Toast.makeText(parent, getHeight()+"..", 1000).show();
				
				return false;
			}
		});
	}

	public void releaseWorkSpace() {
		//TODO mCanvasEnabled test is only to allow no canvas for emulator
	    if (mCanvasEnabled) {
	    	Log.d(TAG, "releaseWorkSpace");
	    	
	    	stopReplay();
	    	
	    	mSCanvas.doAnimationClose();
	    	mSCanvas2.doAnimationClose();

		    mSCanvas.closeSCanvasView();
		    mSCanvas2.closeSCanvasView();
			System.gc();
	    }
	}

	SlidingDrawer.OnDrawerOpenListener mOnDrawerOpenListener = new SlidingDrawer.OnDrawerOpenListener() {

		@Override
		public void onDrawerOpened() {
			mScroller.setScrollEnabled( mScrollingOnly = false );
		    LinearLayout choicePanelContainer = (LinearLayout) findViewById(R.id.choicePanel);
		    choicePanelContainer.setVisibility(INVISIBLE);
		    ImageView slider_arrow = (ImageView) findViewById(R.id.slider_arrow);
		    slider_arrow.setSelected(true);
		    
			LinearLayout layout = (LinearLayout) findViewById(R.id.in_person_container);
			layout.setVisibility(VISIBLE);
		    
		}
		
	};
	
	SlidingDrawer.OnDrawerCloseListener mOnDrawerCloseListener = new SlidingDrawer.OnDrawerCloseListener() {
		
		@Override
		public void onDrawerClosed() {
			mScroller.setScrollEnabled( mScrollingOnly = true );
		    LinearLayout choicePanelContainer = (LinearLayout) findViewById(R.id.choicePanel);
		    choicePanelContainer.setVisibility(VISIBLE);
		    ImageView slider_arrow = (ImageView) findViewById(R.id.slider_arrow);
		    slider_arrow.setSelected(false);
		    
			LinearLayout layout = (LinearLayout) findViewById(R.id.in_person_container);
			layout.setVisibility(INVISIBLE);

		}
	};
	
	private int xLoc, yLoc, initX, initY;
	class MyDragListener implements OnDragListener {
		  @Override
		  public boolean onDrag(View v, DragEvent event) {
		    //int action = event.getAction();
		    switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		    	//Log.d(TAG, "drag started");
		    	xLoc = 0;
		    	yLoc = 0;
		    	break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		    	//v.setBackgroundDrawable(enterShape);
		    	break;
		    case DragEvent.ACTION_DRAG_EXITED:        
		    case DragEvent.ACTION_DROP:
		    	//Log.d(TAG, "drag exited x,y,shadowX,shadowY - " + (int)event.getX()+","+(int)event.getY()+","+mShadowBuilder.getTouchEvent().getX()+","+mShadowBuilder.getTouchEvent().getY());
		    	xLoc = (int) Math.round(event.getX() - initX);
		    	yLoc = (int) Math.round(event.getY() - initY);
		    	//Log.d(TAG, "drag exited, xLoc, yLoc - " + xLoc + ", " + yLoc);
		    	break;
		    case DragEvent.ACTION_DRAG_ENDED:
		    	xLoc = xLoc < 0 ? 0 : xLoc;
		    	yLoc = yLoc < 0 ? 0 : yLoc;
		    	//Log.d(TAG, "drag ended, final position at xLoc, yLoc - " + xLoc + ", " + yLoc);
		    	positionWebView(xLoc, yLoc);
		      	break;
		    default:
		    	break;
		    }
		    return true;
		  }
		} 
	
	public void notifyWebViewSizeChanged(int width ,int height){

		
//		mWebView.getLayoutParams().height = height;
//		mWebView.getLayoutParams().width = width;
		
		mWebView.getLayoutParams().height = android.widget.FrameLayout.LayoutParams.WRAP_CONTENT;
		
//		mWebView.invalidate();
//		mWebView.requestLayout();
	    View view = findViewById(R.id.canvas_container);
	    
	    /* if the webview is longer than the canvas, adjust the canvas */
	    if (view.getLayoutParams().height < height) {
	    	view.getLayoutParams().height = height + 30;
	    	if (mCanvasEnabled)
	    		mSCanvas.getLayoutParams().height = height + 30;

	    }

		//TODO mCanvasEnabled test is only to allow no canvas for emulator
	    if (mCanvasEnabled) {
	    	mSCanvas2.getLayoutParams().width = width;
	    	mSCanvas2.getLayoutParams().height =height;
		    //mSCanvas2.getLayoutParams().width = mWebView.getLayoutParams().width;
		    //mSCanvas2.getLayoutParams().height = mWebView.getLayoutParams().height;
		    
	    }
	}
	
	public void positionWebView(int x, int y) {
		RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	relativeLayoutParams.leftMargin = x;
    	relativeLayoutParams.topMargin = y;
    	Log.d(TAG, "layoutParams - " + relativeLayoutParams.leftMargin+","+relativeLayoutParams.topMargin+","+relativeLayoutParams.width+","+relativeLayoutParams.height+",");
    	mCanvas2Container.setLayoutParams(relativeLayoutParams);

    	mCanvas2Container.setVisibility(VISIBLE);

    	mScroller.requestLayout();
	}
	
	public void resize(int w, int h) {
		int canvasWidth = (int) Math.round(w); // 
		int canvasHeight = (int) Math.round(h * 1.25); 
		
		//set the scrollable scene width to 125%
		View view = findViewById(R.id.scene_container);
	    view.getLayoutParams().width = canvasWidth;
	    view.getLayoutParams().height = canvasHeight;
	    
		//set the main canvas to match scrollable scene
	    view = findViewById(R.id.canvas_container);
	    view.getLayoutParams().width = canvasWidth;
	    view.getLayoutParams().height = canvasHeight;
//	    
//		
	    mWebView.getLayoutParams().width = w;
	    mWebView.getLayoutParams().height = h;
//		//TODO mCanvasEnabled test is only to allow no canvas for emulator
		if (mCanvasEnabled) {
		    mSCanvas2.getLayoutParams().width = mWebView.getLayoutParams().width;
		    mSCanvas2.getLayoutParams().height = mWebView.getLayoutParams().height;
		    mSCanvas.getLayoutParams().width = canvasWidth;
		    mSCanvas.getLayoutParams().height = canvasHeight;

		}
	}

	public boolean saveCurrentQuestionCanvases() {
		boolean result = true;
				
		//save the main and webview canvas
		result = saveAsSammFile(mSCanvas, getQuestionId() + "_image.png");
		result = result ? saveAsSammFile(mSCanvas2, getQuestionId() + "_webview_image.png") : result;
		
		mCanvasChanged = false;

		return result;
	}
	
	public boolean saveCurrentQuestionWorkspace() {
		boolean result = true;

		result = saveWorkSpaceText(getQuestionId());
		result = result ? saveCompositeCanvas(false, false) : result;  //saveCompositeCanvas( boolean bSaveCompositeXML, boolean bSaveCompositeImage )
		
		return result;
	}
	
	public void goQuestion(int position,String choice,boolean showSolution) {
		//TODO mCanvasEnabled test is only to allow no canvas for emulator
		if (mCanvasEnabled && mCanvasInitialized) {  //safeguard against unintialized question
			
			// stop any InPerson related stuff
			stopInPerson();

			saveCurrentQuestionCanvases();
			saveCurrentQuestionWorkspace();
			
			clearCanvases();
			
	        mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
	        mSCanvas2.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
	        if ( mSCanvas.isSettingViewVisible(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN) ) {
				mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);	
	        }
	        else if ( mSCanvas.isSettingViewVisible(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT) ) {
	        	mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, false);
	        }
	        else if ( mSCanvas.isSettingViewVisible(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER) ) {
	        	mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
	        }
			updateModeState();
		}
				
		setQuestionId( questions.get(position).getId() );	
        mWebView.changeContent(this.parent,position, choice, showSolution);

        //reset tools to closed and make sure answers are visible
        mScroller.setScrollEnabled( mScrollingOnly = true );
		SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.drawer);
		drawer.close();
	    LinearLayout choicePanelContainer = (LinearLayout) findViewById(R.id.choicePanel);
	    choicePanelContainer.setVisibility(VISIBLE);
		
		mScroller.scrollTo(0,  0);

        //if existing bitmap, show it in scanvas
		boolean ret = loadCanvas(mSCanvas, getQuestionId() + "_image.png");
		boolean ret2 = loadCanvas(mSCanvas2, getQuestionId() + "_webview_image.png");
		
		if (ret || ret2)
			inPersonManager.enableReplay();
		else
			inPersonManager.disableReplay();
		
		positionWebView(10, 10); // put the webview in (10,10) for now. 
	}
	
	private void initialize() {
		// create basic save/road file path
		File sdcard_path = Environment.getExternalStorageDirectory();
		File cFolder =  new File(sdcard_path, DEFAULT_APP_CAPTURE_DIRECTORY);
		if(!cFolder.exists()){
			if(cFolder.mkdirs()==false){
				Log.e(TAG, "Default Capture Path Creation Error");
				return ;
			}
		}
		mFolder =  new File(sdcard_path, DEFAULT_APP_IMAGEDATA_DIRECTORY);
		if(!mFolder.exists()){
			if(mFolder.mkdirs()==false){
				Log.e(TAG, "Default Save Path Creation Error");
				return ;
			}
		}
		
		if (mCanvasEnabled) {
	        initButtons();
			
			mSCanvas.setSCanvasInitializeListener(mSCanvasInitializeListener);
			
			mSCanvas2.setSCanvasInitializeListener(mSCanvas2InitializeListener);
		}
		
		mScroller.setScrollEnabled( mScrollingOnly = true );
		
	}
    
    private void initButtons() {
		mPenBtn = (ImageButton) findViewById(R.id.settingBtn);
		mPenBtn.setOnClickListener(mBtnClickListener);
		mPenBtn.setSelected(true);
		mTextBtn = (ImageButton) findViewById(R.id.textBtn);
		mTextBtn.setOnClickListener(mBtnClickListener);
		mTextBtn.setSelected(false);
		mEraserBtn = (ImageButton) findViewById(R.id.eraseBtn);
		mEraserBtn.setOnClickListener(mBtnClickListener);
		mEraserBtn.setSelected(false);
		mUndoBtn = (ImageButton) findViewById(R.id.undoBtn);
		mUndoBtn.setOnClickListener(undoNredoBtnClickListener);
		mUndoBtn.setSelected(false);
		mRedoBtn = (ImageButton) findViewById(R.id.redoBtn);
		mRedoBtn.setOnClickListener(undoNredoBtnClickListener);
		mRedoBtn.setSelected(false);
		mPenOnlyBtn = (ImageButton) findViewById(R.id.penOnlyBtn);
		mPenOnlyBtn.setOnClickListener(mBtnClickListener);
		mPenOnlyBtn.setSelected(false);

		String device = android.os.Build.MODEL;
		if (!device.contains("GT-N7000") && !device.contains("GT-N7100")
				&& !device.contains("GT-N8000") && !device.contains("GT-N801")) {
			mPenOnlyBtn.setVisibility(GONE);
		}
		
		mClearAllBtn = (ImageButton) findViewById(R.id.clearAllBtn);
		mClearAllBtn.setOnClickListener(mBtnClickListener);
    }
    
	OnClickListener mBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int nBtnID = v.getId();
			// If the mode is not changed, open the setting view. If the mode is same, close the setting view. 
			if(nBtnID == mPenBtn.getId()){				
				if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN){
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
				}
				else{
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
					mSCanvas2.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
					mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);					
					updateModeState();
				}
			}
			else if(nBtnID == mTextBtn.getId()){
				if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_TEXT){
					mSCanvas.toggleShowSettingView( SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT);
				}
				else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
					mSCanvas2.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
					mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, false);
					updateModeState();
				}
			}
			else if(nBtnID == mEraserBtn.getId()){
				if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
				}
				else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
					mSCanvas2.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
					mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					updateModeState();
				}
			}
			else if(nBtnID == mPenOnlyBtn.getId()) {
				mPenOnly = !mPenOnly;
				updateModeState();
			}
			else if(nBtnID == mClearAllBtn.getId()) {
				mSCanvas.clearSCanvasView();
				mSCanvas2.clearSCanvasView();
		    	mSCanvas.doAnimationClose();
		    	mSCanvas2.doAnimationClose();
				inPersonManager.disableReplay();
				mCanvasChanged =true;
			}
		}
	};

	private void updateModeState(){
		int nCurMode = mSCanvas.getCanvasMode();
		if(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
			mPenBtn.setSelected(true);
			mEraserBtn.setSelected(false);
			mTextBtn.setSelected(false);
		}
		else if(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_TEXT) {
			mPenBtn.setSelected(false);
			mEraserBtn.setSelected(false);
			mTextBtn.setSelected(true);
		}
		else if(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER) {
			mPenBtn.setSelected(false);
			mEraserBtn.setSelected(true);
			mTextBtn.setSelected(false);
		}

		if (mPenOnly) {
			mPenOnlyBtn.setSelected(true);
		} else {
			mPenOnlyBtn.setSelected(false);

		}
		//mPenOnlyBtn.setTextColor(mPenOnly ? Color.WHITE : Color.BLACK);
	}
	
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	//------------------------------------------------
	// SettingView Listener 
	//------------------------------------------------				
	SettingStrokeChangeListener mSettingStrokeChangeListener = new SettingStrokeChangeListener() {
		@Override
		public void onClearAll(boolean bClearAllCompleted) {
			if(bClearAllCompleted) Log.d(TAG, "Clear All is completed");
		}

		@Override
		public void onEraserWidthChanged(int eraserWidth) {				
			Log.d(TAG, "Eraser width is changed : " + eraserWidth);				
			mSCanvas2.setEraserStrokeSetting(eraserWidth);
		}

		@Override
		public void onStrokeColorChanged(int strokeColor) {				
			Log.d(TAG, "Pen Color is changed : " + strokeColor);				
			SettingStrokeInfo ssi = mSCanvas.getSettingViewStrokeInfo();
			mSCanvas2.setSettingStrokeInfo(ssi);
		}

		@Override
		public void onStrokeStyleChanged(int strokeStyle) {

			SettingStrokeInfo ssi = mSCanvas.getSettingViewStrokeInfo();
			mSCanvas2.setSettingStrokeInfo(ssi);

			/*
			if (strokeStyle == SObjectStroke.SAMM_STROKE_STYLE_PENCIL) {
				Log.d(TAG, "Stroke Style = Pen");
			}
			else if (strokeStyle == SObjectStroke.SAMM_STROKE_STYLE_BRUSH) {
				Log.d(TAG, "Stroke Style = Brush");
			}
			else if (strokeStyle == SObjectStroke.SAMM_STROKE_STYLE_CRAYON) {
				Log.d(TAG, "Stroke Style = Pencil Crayon");    		
			}
			else if (strokeStyle == SObjectStroke.SAMM_STROKE_STYLE_MARKER) {
				Log.d(TAG, "Stroke Style = Marker");	
			}
			*/
		}

		@Override
		public void onStrokeWidthChanged(int strokeWidth) {					
			Log.d(TAG, "Pen width is changed : " + strokeWidth);				
			SettingStrokeInfo ssi = mSCanvas.getSettingViewStrokeInfo();
			mSCanvas2.setSettingStrokeInfo(ssi);
		}

		@Override
		public void onStrokeAlphaChanged(int strokeAlpha) {					
			Log.d(TAG, "Pen alpha is changed : " + strokeAlpha);				
			SettingStrokeInfo ssi = mSCanvas.getSettingViewStrokeInfo();
			mSCanvas2.setSettingStrokeInfo(ssi);
		}
	};

	//------------------------------------------------
	// SettingTextChange Listener 
	//------------------------------------------------				
	SettingTextChangeListener mSettingTextChangeListener = new SettingTextChangeListener() {

		@Override
		public void onTextColorChanged(int color) {
			Log.d(TAG, "text color is changed : " + color);				
			SettingTextInfo sti = mSCanvas.getSettingViewTextInfo();
			mSCanvas2.setSettingTextInfo(sti);
		}

		@Override
		public void onTextFontChanged(String font) {
			Log.d(TAG, "text font is changed : " + font);				
			SettingTextInfo sti = mSCanvas.getSettingViewTextInfo();
			mSCanvas2.setSettingTextInfo(sti);
		}

		@Override
		public void onTextSizeChanged(int size) {
			Log.d(TAG, "text size is changed : " + size);				
			SettingTextInfo sti = mSCanvas.getSettingViewTextInfo();
			mSCanvas2.setSettingTextInfo(sti);
		}

		@Override
		public void onTextStyleChanged(int style) {
			Log.d(TAG, "text style is changed : " + style);				
			SettingTextInfo sti = mSCanvas.getSettingViewTextInfo();
			mSCanvas2.setSettingTextInfo(sti);
		}

		//@Override
		public void onTextAlignmentChanged(int arg0) {
			// TODO Auto-generated method stub
		}
	};

	
	SCanvasInitializeListener mSCanvasInitializeListener = new SCanvasInitializeListener() {
		@Override
		public void onInitialized() {
			// Set the background color to white 
			//int nSetColor = 0xFFFFFFFF; mSCanvas.setBGColor(nSetColor);	
			
			mCanvasInitialized = true;
			
			//load save samm file for first question
			// loadCanvas(mSCanvas, getQuestionId() + "_image.png");
			
			mPenBtn.setSelected(true);
			mUndoBtn.setEnabled(mSCanvas.isUndoable());
			mRedoBtn.setEnabled(mSCanvas.isRedoable());
	
			// Resource
			HashMap<String,Integer> settingResourceMap = new HashMap<String, Integer>();
			// Locale
			settingResourceMap.put(SCanvasConstants.LOCALE_PEN_SETTING_TITLE, R.string.pen_settings);
			settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_TITLE, R.string.eraser_settings);
			settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL, R.string.clear_all);   
			settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL_MESSAGE, R.string.confirm_clear_all); 
	
			// Create Setting View
			boolean bClearAllVisibileInEraserSetting = true;
			mSCanvas.createSettingView(mCanvas2Container, settingResourceMap, bClearAllVisibileInEraserSetting);    	  

			mSCanvas.setHistoryUpdateListener(mHistoryUpdateListener);    	
			mSCanvas.setSettingStrokeChangeListener(mSettingStrokeChangeListener);
			mSCanvas.setSettingTextChangeListener(mSettingTextChangeListener);
			
			mSCanvas.setSPenTouchListener( new SPenTouchListener() {

				@Override
				public void onTouchButtonDown(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTouchButtonUp(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onTouchFinger(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;
					return mPenOnly || mScrollingOnly;  //set to true to turn off finger input
				}

				@Override
				public boolean onTouchPen(View arg0, MotionEvent arg1) {
					//return false; //not handled
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;
					return mScrollingOnly;  //set to true if in scroll only mode
				}

				@Override
				public boolean onTouchPenEraser(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;

					return false;
				}
				
			});
			
			mSCanvas.setAnimationProcessListener( new AnimationProcessListener() {

				@Override
				public void onChangeProgress(int arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPlayComplete() {
					// TODO Auto-generated method stub
					
					int  nAnimationState = mSCanvas2.getAnimationState( );
					
					// stop when the other one is stoped too.
					if( nAnimationState == SAMMLibConstants.ANIMATION_STATE_ON_STOP ) 
						stopInPerson();
				}
				
			});

			mSCanvas.setSObjectUpdateListener( new SObjectUpdateListener() {

				@Override
				public void onSObjectChanged(SObject arg0, boolean arg1,
						boolean arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSObjectDeleted(SObject arg0, boolean arg1,
						boolean arg2, boolean arg3) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSObjectInserted(SObject sObj, boolean bUndo,
						boolean bRedo) {
					// TODO Auto-generated method stub
						Log.d(TAG, "onSObjectInserted - " + sObj.getDescription());
				}

				@Override
				public void onSObjectSelected(SObject arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onSObjectStrokeInserting(SObjectStroke arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				//@Override
				public void onSObjectClearAll(boolean arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		};
	};

	private HistoryUpdateListener mHistoryUpdateListener = new HistoryUpdateListener() {

		@Override
		public void onHistoryChanged(boolean bUndoable, boolean bRedoable) {

			mUndoBtn.setEnabled(bUndoable);
			mRedoBtn.setEnabled(bRedoable);

		}
	};

	SCanvasInitializeListener mSCanvas2InitializeListener = new SCanvasInitializeListener() {

		@Override
		public void onInitialized() {
			mSCanvas2.setLongClickable(true);
			
			//load save samm file for first question
			// loadCanvas(mSCanvas2, getQuestionId() + "_webview_image.png");
			
			mSCanvas2.setSCanvasLongPressListener( new SCanvasLongPressListener() {

				@Override
				public void onLongPressed() {
		    		View view = mCanvas2Container; 
		    		ClipData data = ClipData.newPlainText("", "");
		    		mShadowBuilder = new MyDragShadowBuilder(mWebView);
		    		mShadowBuilder.setTouchEvent(mTouchEvent);
		    		view.startDrag(data, mShadowBuilder, view, 0);
		    		view.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onLongPressed(float arg0, float arg1) {
				}
				
			});
			
			mSCanvas2.setAnimationProcessListener( new AnimationProcessListener() {

				@Override
				public void onChangeProgress(int arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPlayComplete() {
					// TODO Auto-generated method stub
				int  nAnimationState = mSCanvas.getAnimationState( );
					
					// stop when the other one is stoped too.
					if( nAnimationState== SAMMLibConstants.ANIMATION_STATE_ON_STOP ) 
						stopInPerson();
				}
				
			});
			
			mSCanvas2.setSPenTouchListener( new SPenTouchListener() {

				@Override
				public void onTouchButtonDown(View arg0, MotionEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTouchButtonUp(View arg0, MotionEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onTouchFinger(View arg0, MotionEvent e) {
					// TODO Auto-generated method stub
			    	mTouchEvent = e;
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;

					return mPenOnly || mScrollingOnly;  //set to true to turn off finger input
				}

				@Override
				public boolean onTouchPen(View arg0, MotionEvent e) {
					//return false; //not handled
			    	mTouchEvent = e;
			    	
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;

					return mScrollingOnly;  //set to true if in scroll only mode
				}

				@Override
				public boolean onTouchPenEraser(View arg0, MotionEvent e) {
					// TODO Auto-generated method stub
					inPersonManager.enableReplay(); 
					mCanvasChanged = true;

					return false;
				}
				
			});
		}
		
	};
	private OnClickListener undoNredoBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.equals(mUndoBtn)) {
				if (mSCanvas.undo() == false) {
					mSCanvas2.undo();
					mCanvasChanged =true;
				}
			} else if (v.equals(mRedoBtn)) {
				if (mSCanvas.redo() == false) {
					mSCanvas2.redo();
					mCanvasChanged =true;
				}
			}

			mUndoBtn.setEnabled(mSCanvas.isUndoable() || mSCanvas2.isUndoable());
			mRedoBtn.setEnabled(mSCanvas.isRedoable() || mSCanvas2.isRedoable());
		}
	};

	private boolean saveWorkSpaceText(int qid) {
		SObject sObj;
		SCanvasView sCanvas;
		List<SCanvasView> canvases = Arrays.asList(mSCanvas, mSCanvas2);
		
		List<String> notes = new ArrayList<String>();
		
		Iterator<SCanvasView> iterator = canvases.iterator();
		while (iterator.hasNext()) {
			sCanvas = iterator.next();
			
			int numObjects = sCanvas.getSAMMObjectNum();
			for (int i=0; i< numObjects; i++) {
				sObj = sCanvas.getSAMMObject(i);
				Log.d(TAG, "sObj - " + i + ", " + ( (sObj instanceof SObjectStroke) ? "stroke" : (sObj instanceof SObjectText) ? "text" : "unknown") );
				if ( (sObj instanceof SObjectText) ) {
					notes.add(((SObjectText) sObj).getText());
				}
			}
		}
		
		QuestionViewService.saveWorkspaceNotes((Activity)getContext(), qid, notes);
		
		/* 
		 * debug only
		if ( !notes.isEmpty() ) {
			List<ListItemWorkspaceNotesVO> rs = QuestionViewService.searchWorkspaceNotesByKey((Activity)getContext(), "test");
			Log.d(TAG, "rs - " + rs.toString() );
		}
		*/
		
		return true;
	}
	
	public boolean saveCompositeCanvasObjects(String path) {
		SObject sObj;
		SCanvasView sCanvas;
		List<SCanvasView> canvases = Arrays.asList(mSCanvas, mSCanvas2);
		
		Iterator<SCanvasView> iterator = canvases.iterator();
		while (iterator.hasNext()) {
			sCanvas = iterator.next();
			
			int numObjects = sCanvas.getSAMMObjectNum();
			for (int i=0; i< numObjects; i++) {
				sObj = sCanvas.getSAMMObject(i);
				Log.d(TAG, "sObj - " + i + ", " + ( (sObj instanceof SObjectStroke) ? "stroke" : (sObj instanceof SObjectText) ? "text" : "unknown") );
			}
		}
		
		return true;
	}
	
	public boolean saveCompositeCanvas(boolean bSaveCanvasXml, boolean bSaveCompositeImage) {
		boolean result = true;
		String savePath;
		
		if (!mCanvasEnabled)
			return false;
		
		//Make sure the folder exists
		if (!(mFolder.exists()))
			if(!mFolder.mkdirs())
				return false;
		
		//This is the section that saves the composite image. 
		if (bSaveCompositeImage && result) {
			savePath = mFolder.getPath() + '/' + getQuestionId() + "_composite_image.png";
			Bitmap bmCanvas = mSCanvas.getCanvasBitmap(true);
		    RelativeLayout view = (RelativeLayout) findViewById(R.id.scene_container);
		    bmCanvas = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
	        view.draw(new Canvas(bmCanvas));
			result = saveBitmapPNG(savePath, bmCanvas);
		}
		
		if (bSaveCanvasXml && result) {
			savePath = mFolder.getPath() + '/' + getQuestionId() + "_composite_objects.xml";
			saveCompositeCanvasObjects(savePath);
		}
		
		return result;
	}

	public boolean saveCompositeImage() {

		if (!(mFolder.exists()))
			if(!mFolder.mkdirs())
				return false;
		
		Bitmap bmCanvas = mSCanvas.getCanvasBitmap(true);
	    RelativeLayout view = (RelativeLayout) findViewById(R.id.scene_container);
	    bmCanvas = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);	    
        view.draw(new Canvas(bmCanvas));
        
        File file = new File(Environment.getExternalStorageDirectory(), WorkSpace.COMPOSITE_IMAGE_PATH);
        return saveBitmapPNG(file.getAbsolutePath(), bmCanvas);
	}

	boolean saveBitmapPNG(String strFileName, Bitmap bitmap){		
		if(strFileName == null || bitmap == null)
			return false;

		boolean bSuccess1 = false;	
		boolean bSuccess2;
		boolean bSuccess3;
		File saveFile = new File(strFileName);			

		if(saveFile.exists()) {
			if(!saveFile.delete())
				return false;
		}

		try {
			bSuccess1 = saveFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(saveFile);
			bSuccess2 = bitmap.compress(CompressFormat.PNG, 100, out);			
		} catch (Exception e) {
			e.printStackTrace();			
			bSuccess2 = false;
		}
		try {
			if(out!=null)
			{
				out.flush();
				out.close();
				bSuccess3 = true;
			}
			else
				bSuccess3 = false;

		} catch (IOException e) {
			e.printStackTrace();
			bSuccess3 = false;
		}finally
		{
			if(out != null)
			{
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		}		

		return (bSuccess1 && bSuccess2 && bSuccess3);
	}	

	public boolean saveAsSammFile(SCanvasView sCanvas, String fileName) {
		boolean result = true;
		
		if (!mCanvasEnabled)
			return false;
		
		if (mCanvasChanged == false)
			return false;

		String path = mFolder.getPath() + '/' + fileName;
		
		if (sCanvas.getSAMMObjectNum() > 0) {
			Log.d(TAG, "save samm image -  " + path);
			result = sCanvas.saveSAMMFile(path);
		} else {
			//remove any pre-existing file since the objects have all been removed 
			File curFile = new File(path);
			if (curFile.exists()) 
				curFile.delete();
		}
	
		return result;
	}
	
	public void stopInPerson() {
		stopReplay();
    	
		inPersonManager.resetContentView();
		
	}
	
	public void startReplay(){
		
		if (!mCanvasEnabled || !mCanvasInitialized)
			return ;
		
		saveCurrentQuestionCanvases(); // save the Canvases first because after animation all SAMM objects are not in Canvas any more BUG in SDK??
/*				
		SOptionSCanvas canvasOption = new SOptionSCanvas();
		mSCanvas.setOption(canvasOption);
		mSCanvas2.setOption(canvasOption);
*/
		// startAnimationTimer();
						
		mSCanvas.setAnimationMode(true);
		
		mSCanvas2.setAnimationMode(true);
		
		mSCanvas.setAnimationSpeed(SOptionPlay.ANIMATION_SPEED_NORMAL);
		mSCanvas2.setAnimationSpeed(SOptionPlay.ANIMATION_SPEED_NORMAL);
		
		mSCanvas.doAnimationStart();
		mSCanvas2.doAnimationStart();

	}
	
	public void pauseReplay(){
		
		if (!mCanvasEnabled || !mCanvasInitialized)
			return ;
				
		mSCanvas.doAnimationPause();
		
		mSCanvas2.doAnimationPause();
	}
	
	public void resumeReplay(){
		
		if (!mCanvasEnabled || !mCanvasInitialized)
			return ;
		
		mSCanvas.doAnimationResume();
		
		mSCanvas2.doAnimationResume();
	}
	
	public void stopReplay(){
		
		if (!mCanvasEnabled || !mCanvasInitialized)
			return ;
		
		mSCanvas.doAnimationStop(true);
		mSCanvas2.doAnimationStop(true);
		
		//mSCanvas.doAnimationClose();
		//mSCanvas2.doAnimationClose();
		
		mSCanvas.setAnimationMode(false);
		mSCanvas2.setAnimationMode(false);
		
		stopAnimationTimer();
	}
	
	public void startRecord() {
		mRecordStartTime = System.currentTimeMillis();
		
		inPersonManager.enableReplay(); 
		
		clearCanvases(); // restart the canvas
		
	}
	
	public boolean loadCanvas(SCanvasView sCanvas, String fileName) {
		
		if (!mCanvasEnabled || !mCanvasInitialized)
			return false;

		File curFile = new File(mFolder.getPath(), fileName);
		String loadPath = mFolder.getPath() + '/' + fileName;
		
		if (curFile.exists() && SCanvasView.isSAMMFile(loadPath)) {
			Log.d(TAG, "Load Path = " + loadPath);
			sCanvas.loadSAMMFile(loadPath, true);
		} else {
			// disabled play back
			return false;
		}
	
		return true;
	}
	
	private class MyDragShadowBuilder extends View.DragShadowBuilder {
		 private MotionEvent touchEvent; 
		 private Float fW, fH;

		 public MyDragShadowBuilder(View v) {
		 	super(v);
		 	fW = Float.valueOf(v.getWidth());
		 	fH = Float.valueOf(v.getHeight());
		 }
		 
		 @Override
		 public void onProvideShadowMetrics (Point size, Point touch){
			 int width = getView().getWidth();
			 int height = getView().getHeight();

			 size.set(width, height);
			 initX = (int) getTouchEvent().getX();	
			 initY = (int) getTouchEvent().getY();	
			 //Log.d(TAG, "shadowX,shadowY - " + getTouchEvent().getX()+","+getTouchEvent().getY());
			 touch.set( (int) Math.round(getTouchEvent().getX()), (int) Math.round(getTouchEvent().getY()) );
		 }

		 @Override
		 public void onDrawShadow(Canvas canvas) {
			 //shadow.draw(canvas);
			 Paint p = new Paint();
			 p.setColor(0xFF0000FF);
			 p.setStrokeWidth(8.0F);
			 getView().draw(canvas);
			 canvas.drawLine(0f, 0f, fW, 0f, p);
			 canvas.drawLine(fW, 0f, fW, fH, p);
			 canvas.drawLine(fW, fH, 0, fH, p);
			 canvas.drawLine(0, fH, 0, 0, p);
		 }

		public MotionEvent getTouchEvent() {
			return this.touchEvent;
		}

		public void setTouchEvent(MotionEvent touchEvent) {
			this.touchEvent = touchEvent;
		}

	}
	
	private void stopAnimationTimer(){
		 if (null != mTimer) {  
            mTask.cancel();  
            mTask = null;  
            mTimer.cancel(); // Cancel timer  
            mTimer.purge();  
            mTimer = null;  
        }
	}
	
	private void startAnimationTimer(){
		if(null==mTimer){
			if(null==mTask){
				mTask = new TimerTask() {
					
					@Override
					public void run() {
						
						Message msg = new Message();
						
						msg.what = 1;
						_handler.sendMessage(msg);
					
					}
					
				};
			}
			mTimer = new Timer(true);
			mTimer.schedule(mTask, 100,100);
		}
	}
	
	Handler _handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				break;
			case 2:
	
				break;
			case 44:

			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	private void putInpersonControllerViewPosition(View inPersonView){
		
		
		//FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//params.setMargins(40, -1, -1, 60);
		
		params.gravity = Gravity.BOTTOM;
		inPersonView.setLayoutParams(params);
		
		// FrameLayout layout = (FrameLayout) findViewById(R.id.fullscreenContainer);
		LinearLayout layout = (LinearLayout) findViewById(R.id.in_person_container);	
		layout.addView(inPersonView);
		
		layout.setVisibility(View.INVISIBLE);

	}
	
	
}