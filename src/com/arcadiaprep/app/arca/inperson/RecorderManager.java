package com.arcadiaprep.app.arca.inperson;

import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class RecorderManager {
	
	private static final String LOG_TAG = "RecorderManager";
	
	private String curProblemId;
	private int		curPage;
	
	private MediaRecorder audioRecorder = null;
	
	private MediaPlayer   audioPlayer = null;
	
	public void startRecorder(String problemId,int pageNum){
		
		audioRecorder = new MediaRecorder();
		
		audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		
		audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		
		curProblemId = problemId;
		curPage = pageNum;
		
		String audioPath = getRecordingFileName(problemId,pageNum);
		
		audioRecorder.setOutputFile(audioPath);
		
		try{
			audioRecorder.prepare();
		}
		catch(IOException e){
			
			Log.e(LOG_TAG, "prepare failed");
		}
		
		audioRecorder.start();
		
	}
	
	public boolean startReplay(String problemId, int pageNum){
		
		audioPlayer = new MediaPlayer();
		
		try{
			
			String filePath = getRecordingFileName(problemId,pageNum);
			audioPlayer.setDataSource(filePath);
			
			audioPlayer.prepare();
			
			audioPlayer.start();
		}
		catch(IOException e){
			
			Log.e(LOG_TAG, "Replay failed");
		}
		
		return true;
	}
	
	public void pause(){
		
		
	}
	
	public void stop(){
		
		audioRecorder.stop();
		audioRecorder.release();
		audioRecorder = null;
		
		audioPlayer.stop();
		audioPlayer.release();
		audioPlayer = null;
	}
	
	private String getRecordingFileName(String problemId, int pageNum){
		
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "_" + problemId + "_" + pageNum;
	}
}
