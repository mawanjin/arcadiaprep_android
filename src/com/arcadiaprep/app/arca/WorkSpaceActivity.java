package com.arcadiaprep.app.arca;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arcadiaprep.app.scratchpad.component.MainWebView;
import com.arcadiaprep.app.scratchpad.component.WorkSpace;

public class WorkSpaceActivity extends Activity {
	public static final String TAG = "WorkSpaceActivity";
	public static final String TAG_LIFE_CYCLE = "LifeCycle";

    private Handler mHandler;
    private Context mContext;
    
    private WorkSpace mWorkSpace;
	private Button mPreviousBtn;
	private Button mNextBtn;
	private int mQuestionId = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_LIFE_CYCLE, "onCreate start");
        
        mHandler = new Handler();

        setContentView(R.layout.main_workspace);

		MainWebView mWebView = new MainWebView(this, null,null);
		
        mWorkSpace = new WorkSpace(this,this, mWebView, mQuestionId, true);
	    LinearLayout layout = (LinearLayout) findViewById(R.id.main_layout);
	    layout.addView(mWorkSpace);

		WindowManager w = getWindowManager();
    	
		Display d = w.getDefaultDisplay(); 
	    mWorkSpace.resize(d.getWidth(), d.getHeight());
	    layout.requestLayout();

	    mPreviousBtn = (Button) findViewById(R.id.previousBtn);
	    mPreviousBtn.setOnClickListener(mBtnClickListener);
	    mNextBtn = (Button) findViewById(R.id.nextBtn);
	    mNextBtn.setOnClickListener(mBtnClickListener);
		
	    Log.d(TAG_LIFE_CYCLE, "onCreate start");
    }
    
    public void onStart() {
        Log.d(TAG_LIFE_CYCLE, "onStart start");
    	super.onStart();
        mContext = this;
	    Log.d(TAG_LIFE_CYCLE, "onStart end");
    }
    
    protected void onPause() {
		Log.d(TAG_LIFE_CYCLE, "onPause start");
        super.onPause();
        Log.d(TAG_LIFE_CYCLE, "onPause end");
    }

    protected void onResume() {
		Log.d(TAG_LIFE_CYCLE, "onResume start");
        super.onResume();
        Log.d(TAG_LIFE_CYCLE, "onResume end");
    }

    protected void onStop() {
		Log.d(TAG_LIFE_CYCLE, "onStop start");
        super.onStop();
        Log.d(TAG_LIFE_CYCLE, "onStop end");
    }
    
    public void toastIt(final String toast) {
    	final Context context = getApplicationContext();
     	mHandler.post( new Runnable() {
 		   public void run() {
 	            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
 		   }
		});
    }

	OnClickListener mBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int nBtnID = v.getId();
			// If the mode is not changed, open the setting view. If the mode is same, close the setting view. 
			if(nBtnID == mPreviousBtn.getId()){
				if (mQuestionId > 1) {
					mWorkSpace.goQuestion(--mQuestionId,"-1",false);
				}
				else {
					toastIt("This is the first question. No previous questions");
				}
			}
			else if(nBtnID == mNextBtn.getId()){
				if (mQuestionId < 3) {
					mWorkSpace.goQuestion(++mQuestionId,"-1",false);
				}
				else {
					toastIt("This is the third question. No more questions");
				}
			}
		}
	};
}
