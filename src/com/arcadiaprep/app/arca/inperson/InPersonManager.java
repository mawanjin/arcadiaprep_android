package com.arcadiaprep.app.arca.inperson;

import java.lang.ref.SoftReference;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.scratchpad.component.WorkSpace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

public class InPersonManager {
	
	private Context curContext = null;
	private InpersonState curState = InpersonState.INPERSON_IDLE;
	
	LinearLayout containerView = null;
	
	View pannelView = null;
	
	View recordingView = null;
	
	View replayView = null;
	
	String  problemId = null;
	
	int 	pageNum	 = 0;
	
	boolean  curPauseState = false;
	
	SoftReference<WorkSpace>  curWorkSpaceRef = null;
	
	public InPersonManager(Context ctx){
		
		curContext = ctx;
		
		containerView = (LinearLayout)LayoutInflater.from(curContext.getApplicationContext()).inflate(
				R.layout.in_person_container, null);
		
		initContentView();
		
		changeContentView();
		
		
	}
	
	public void showContent(){
		
		View contentView = null;
				
		switch(curState){
	
			case INPERSON_IDLE:
				
				contentView = pannelView;
				break;
				
			case INPERSON_RECORD:
				
				contentView = recordingView;
				break;
				
			case INPERSON_REPLAY:
				contentView = replayView;
				
				break;
		}
		
		containerView.addView(contentView);
		
	}
	
	public void hideContent(){
		
		View contentView = null;
				
		switch(curState){
	
			case INPERSON_IDLE:
				
				contentView = pannelView;
				break;
				
			case INPERSON_RECORD:
				
				contentView = recordingView;
				break;
				
			case INPERSON_REPLAY:
				contentView = replayView;
				
				break;
		}
		
		containerView.removeView(contentView);
		
	}
	
	public void setCurrentWorkSpace(WorkSpace workSpace){
		
		curWorkSpaceRef = new SoftReference<WorkSpace>(workSpace);
	}
	
	public View getInPersonView(){
		
		return containerView;
	}
	
	private void initContentView(){
		
		// Init ContentView and add listener
		pannelView = LayoutInflater.from(curContext.getApplicationContext()).inflate(
				R.layout.in_person_pannel, null);
		
		ImageButton recordingButton = (ImageButton)pannelView.findViewById(R.id.inperson_record);
		
		if (recordingButton != null)
			recordingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				curState = InpersonState.INPERSON_RECORD;
				
				changeContentView();
				
				// TODO start recording
				curWorkSpaceRef.get().startRecord();
			}
		});
		
		ImageButton replayButton = (ImageButton)pannelView.findViewById(R.id.inperson_play);
		
		//replayButton.setEnabled(false);
		
		replayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				curState = InpersonState.INPERSON_REPLAY;
				
				changeContentView();
				
				curWorkSpaceRef.get().startReplay();
				// TODO start recording
			}
		});
		
		
		recordingView = LayoutInflater.from(curContext.getApplicationContext()).inflate(
				R.layout.in_persone_recording, null);
		
		ImageButton recordingStopButton = (ImageButton)recordingView.findViewById(R.id.inperson_record_stop);	
		
		recordingStopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				curState = InpersonState.INPERSON_IDLE;
				
				changeContentView();
				
			}
		});
		
		replayView = LayoutInflater.from(curContext.getApplicationContext()).inflate(
				R.layout.in_person_replay, null);
		
		final ImageButton  replayPause = (ImageButton)replayView.findViewById(R.id.inperson_replay_pause);
		
		replayPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(curPauseState){
					
					curWorkSpaceRef.get().resumeReplay();
					
					curPauseState = false;
					
					replayPause.setImageDrawable(curContext.getResources().getDrawable(R.drawable.inperson_pause_play));
				}
				else{
					//replayPause.setBackgroundDrawable(curContext.getResources().getDrawable(R.drawable.inperson_pause));
					replayPause.setImageDrawable(curContext.getResources().getDrawable(R.drawable.inperson_play_bttn));

					curWorkSpaceRef.get().pauseReplay();
					
					curPauseState = true;
				}
				
			}
		});
		
		ImageButton	replayStop = (ImageButton)replayView.findViewById(R.id.inperson_replay_done);
		
		
		replayStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				curWorkSpaceRef.get().stopReplay();
				
				// TODO Auto-generated method stub
				curState = InpersonState.INPERSON_IDLE;
				
				changeContentView();
				
				// TODO start recording
			}
		});
		
		ProgressBar replayProgress = (ProgressBar)replayView.findViewById(R.id.inperson_replay_progress);
	}
	
	private void changeContentView(){
		
		View contentView = null;
		
		containerView.removeAllViews();
		
		switch(curState){
	
			case INPERSON_IDLE:
				
				contentView = pannelView;
				break;
				
			case INPERSON_RECORD:
				
				contentView = recordingView;
				break;
				
			case INPERSON_REPLAY:
				contentView = replayView;
				
				break;
		}
		
		containerView.addView(contentView);
		
	}
	
	public void resetContentView() {
		View contentView = null;

		containerView.removeAllViews();
		curState = InpersonState.INPERSON_IDLE;
		
		contentView = pannelView;

		containerView.addView(contentView);

	}
	
	public void disableReplay() {
		pannelView.setVisibility(View.INVISIBLE);
		ImageButton replayButton = (ImageButton)pannelView.findViewById(R.id.inperson_play);
		
		replayButton.setEnabled(false);
		replayButton.setAlpha(50);
	}
	
	public void enableReplay() {
		pannelView.setVisibility(View.VISIBLE);

		ImageButton replayButton = (ImageButton)pannelView.findViewById(R.id.inperson_play);
		
		replayButton.setEnabled(true);
		replayButton.setAlpha(255);

	}
}

