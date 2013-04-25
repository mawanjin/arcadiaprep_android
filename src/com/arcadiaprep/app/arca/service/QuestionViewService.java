package com.arcadiaprep.app.arca.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.mo.ExApps;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.util.TimeUtil;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.arca.vo.ListItemExamStatVO;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;
import com.arcadiaprep.app.arca.vo.QuestionExamStatus;
import com.arcadiaprep.app.arca.vo.ListItemWorkspaceNotesVO;

public class QuestionViewService {
	
	private static String LOG_TAG = "QuestionViewService";

	/**
	 * Get the exam information whatever user has completed or semi-finished the
	 * exam.
	 * 
	 * @return
	 */
	public static ExamResultInfo getExamResultInfoByExAppID(Context context,int exAppID) {
		ExamResultInfo info = null;
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from exam_result_info where ex_app_id ="+exAppID +" order by start_time desc", null);
		int c = 0;
		if(cursor!=null){
			cursor.moveToFirst();
			if(!cursor.isAfterLast())
			info = new ExamResultInfo();
		while(!cursor.isAfterLast()){
			if(c>0)break;
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
			info.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			info.setExAppName(cursor.getString(cursor.getColumnIndex("ex_app_name")));
			info.setScore(cursor.getInt(cursor.getColumnIndex("score")));
			info.setFinish(Boolean.parseBoolean(cursor.getString(cursor
					.getColumnIndex("finish"))));
			info.setSection(cursor.getString(cursor.getColumnIndex("section")));
			info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
			info.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
			info.setEndTime(cursor.getInt(cursor.getColumnIndex("end_time")));
			info.setQuestionCount(cursor.getInt(cursor.getColumnIndex("question_count")));
			cursor.moveToNext();
			c++;
		}
		
		cursor.close();
		}
		//find question_exam_result_info
		cursor = db.rawQuery("select * from question_exam_result_info where ex_app_id ="+exAppID, null);
		if(cursor!=null){
			cursor.moveToFirst(); 
		while(!cursor.isAfterLast()){
			QuestionExamStatus qes = new QuestionExamStatus();
			info.getQuestions().add(qes);
			
			qes.setId(cursor.getInt(cursor.getColumnIndex("id")));
			qes.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			qes.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
			qes.setChoice(cursor.getString(cursor.getColumnIndex("choice")));
			qes.setCorrectChoice(cursor.getString(cursor.getColumnIndex("correctChoice")));
			qes.setMark(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("mark"))));
			cursor.moveToNext();
		}
		
		cursor.close();
		}
		db.close();
		
		return info;
	}
	
	/**
	 * 
	 * @param context
	 * @param exAppID e.g: 1,2,3,4
	 * @return
	 */
	public static List<ExamResultInfo> getExamResultInfoByExAppIDs(Context context,String exAppID) {
		List<ExamResultInfo> infos = new ArrayList<ExamResultInfo>();
		Map<Integer,String> exists = new HashMap<Integer,String>();
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from exam_result_info where ex_app_id in ("+exAppID +") order by start_time desc", null);
		if(cursor!=null){
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()){
				if(!exists.containsKey(cursor.getInt(cursor.getColumnIndex("ex_app_id")))){
					ExamResultInfo info = new ExamResultInfo();
					info.setId(cursor.getInt(cursor.getColumnIndex("id")));
					info.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
					info.setExAppName(cursor.getString(cursor.getColumnIndex("ex_app_name")));
					info.setScore(cursor.getInt(cursor.getColumnIndex("score")));
					info.setFinish(Boolean.parseBoolean(cursor.getString(cursor
							.getColumnIndex("finish"))));
					info.setSection(cursor.getString(cursor.getColumnIndex("section")));
					info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
					info.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
					info.setEndTime(cursor.getInt(cursor.getColumnIndex("end_time")));
					info.setQuestionCount(cursor.getInt(cursor.getColumnIndex("question_count")));
					exists.put(info.getExAppID(), "");
					infos.add(info);
				}
				cursor.moveToNext();
			}
		
		cursor.close();
		}
		for(ExamResultInfo info: infos){
			//find question_exam_result_info
			cursor = db.rawQuery("select * from question_exam_result_info where ex_app_id ="+info.getExAppID(), null);
			if(cursor!=null){
				cursor.moveToFirst(); 
			while(!cursor.isAfterLast()){
				QuestionExamStatus qes = new QuestionExamStatus();
				info.getQuestions().add(qes);
				
				qes.setId(cursor.getInt(cursor.getColumnIndex("id")));
				qes.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
				qes.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
				qes.setChoice(cursor.getString(cursor.getColumnIndex("choice")));
				qes.setCorrectChoice(cursor.getString(cursor.getColumnIndex("correctChoice")));
				qes.setMark(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("mark"))));
				cursor.moveToNext();
			}
		}
		
		
		cursor.close();
		}
		db.close();
		
		return infos;
	}
	
	public static List<ListItemExamStatVO> getAverageExamResultInfosBySection(Context context,String sectionName){
		List<ListItemExamStatVO> rs = new ArrayList<ListItemExamStatVO>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
//		Cursor cursor = db.rawQuery("select o.ex_app_id ex_app_id ,o.ex_app_name ex_app_name,avg(o.score) score,avg(o.end_time) time, avg(o.end_time)/o.question_count per from exam_result_info o ,iphone_ex_apps app where finish='true' and app.id=o.ex_app_id and app.section=? group by ex_app_id  order by start_time desc", new String[]{sectionName});
		Cursor cursor = db.rawQuery("select o.ex_app_id ex_app_id ,o.ex_app_name ex_app_name,avg(o.score) score,avg(o.end_time) time, avg(o.end_time)/o.question_count per, o.question_count question_count from exam_result_info o  where finish='true' and section=? group by ex_app_id  order by start_time desc", new String[]{sectionName});
		if(cursor!=null){
			cursor.moveToFirst();
			
		while(!cursor.isAfterLast()){
			ListItemExamStatVO vo = new ListItemExamStatVO();
			rs.add(vo);
			vo.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			vo.setName(cursor.getString(cursor.getColumnIndex("ex_app_name")));
			vo.setScore(""+cursor.getInt(cursor.getColumnIndex("score")));
			vo.setTotalTime(TimeUtil.getTime(cursor.getInt(cursor.getColumnIndex("time"))));
			vo.setPer(TimeUtil.getTime(cursor.getInt(cursor.getColumnIndex("per"))));
			vo.setNumQuestion(cursor.getInt(cursor.getColumnIndex("question_count")));

			cursor.moveToNext();
		}
		
		cursor.close();
		}
		db.close();
		return rs;
	}
	
	public static List<ExamResultInfo> getExamResultInfosBySection(Context context,String sectionName){
		List<ExamResultInfo> rs = new ArrayList<ExamResultInfo>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
		Cursor cursor = db.rawQuery("select o.* from exam_result_info o  where finish='true' and section=? order by start_time desc", new String[]{sectionName});
		if(cursor!=null){
			cursor.moveToFirst();
			
		while(!cursor.isAfterLast()){
			ExamResultInfo info = new ExamResultInfo();
			rs.add(info);
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
			info.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			info.setExAppName(cursor.getString(cursor.getColumnIndex("ex_app_name")));
			info.setScore(cursor.getInt(cursor.getColumnIndex("score")));
			info.setFinish(Boolean.parseBoolean(cursor.getString(cursor
					.getColumnIndex("finish"))));
			info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
			info.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
			info.setEndTime(cursor.getInt(cursor.getColumnIndex("end_time")));
			info.setQuestionCount(cursor.getInt(cursor.getColumnIndex("question_count")));
			cursor.moveToNext();
		}
		
		cursor.close();
		}
		db.close();
		return rs;
	}
	
	public static List<ExamResultInfo> getExamResultInfosByProblemSets(Context context,List<ListItemMyQuestionVO> problemsets) {
		String exAppIDS ="";
		List<ExamResultInfo> infos = new ArrayList<ExamResultInfo>();
		for(ListItemMyQuestionVO set : problemsets){
			getExamResultInfoNew(context,set.getExAppId(), set.getTitle()+":"+set.getInfo(), MainDataService.findQuestionsByExAppID(context, set.getExAppId()));
			exAppIDS+=set.getExAppId()+",";
		}
		if(exAppIDS.endsWith(","))
			exAppIDS = exAppIDS.substring(0,exAppIDS.length()-1);
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from exam_result_info where ex_app_id in ("+exAppIDS+")", null);
		if(cursor!=null){
			cursor.moveToFirst(); 
			
		while(!cursor.isAfterLast()){
			ExamResultInfo info = new ExamResultInfo();
			infos.add(info);
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
			info.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			info.setExAppName(cursor.getString(cursor.getColumnIndex("ex_app_name")));
			info.setScore(cursor.getInt(cursor.getColumnIndex("score")));
			info.setFinish(Boolean.parseBoolean(cursor.getString(cursor
					.getColumnIndex("finish"))));
			info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
			info.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
			info.setEndTime(cursor.getInt(cursor.getColumnIndex("end_time")));
			info.setQuestionCount(cursor.getInt(cursor.getColumnIndex("question_count")));
			cursor.moveToNext();
		}
		
		cursor.close();
		}
		
		List<QuestionExamStatus> examStatusList = new ArrayList<QuestionExamStatus>();
		//find question_exam_result_info
		cursor = db.rawQuery("select * from question_exam_result_info where ex_app_id in ("+exAppIDS+") ",null);
		if(cursor!=null){
			cursor.moveToFirst(); 
		while(!cursor.isAfterLast()){
			QuestionExamStatus qes = new QuestionExamStatus();
			examStatusList.add(qes);
			qes.setId(cursor.getInt(cursor.getColumnIndex("id")));
			qes.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			qes.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
			qes.setChoice(cursor.getString(cursor.getColumnIndex("choice")));
			qes.setCorrectChoice(cursor.getString(cursor.getColumnIndex("correctChoice")));
			qes.setMark(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("mark"))));
			cursor.moveToNext();
		}
		
		cursor.close();
		}
		db.close();
		
		//Bring examStatus to the right place in  the List of ExamResultInfo.
		for(QuestionExamStatus s : examStatusList){
			for(ExamResultInfo info :infos){
				if(info.getExAppID()==s.getExAppID()){
					info.getQuestions().add(s);
				}
			}
		}
		
		return infos;
	}

	/**
	 * Create a examination information object when user begin a new practice.
	 * 
	 * @return
	 */
	public static ExamResultInfo getExamResultInfoNew(Context context,int exAppID,
			String exAppName, List<Questions> questions) {
		ExamResultInfo info = new ExamResultInfo();
		info.setExAppID(exAppID);
		info.setExAppName(exAppName);
		info.setStartTime(System.currentTimeMillis() + "");
		info.setQuestionCount(questions.size());
		
		for (Questions q : questions) {
			QuestionExamStatus qes = new QuestionExamStatus();
			info.getQuestions().add(qes);
			qes.setQuestionId(q.getId());
			qes.setCorrectChoice(q.getSolution());
			qes.setChoice("-1");
		}
		
		//find question_exam_result_info
			SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
			  Cursor cursor = db.rawQuery("select * from question_exam_result_info where ex_app_id ="+exAppID, null);
				if(cursor!=null){
					cursor.moveToFirst(); 
				while(!cursor.isAfterLast()){
					QuestionExamStatus qes = new QuestionExamStatus();
					
					qes.setId(cursor.getInt(cursor.getColumnIndex("id")));
					qes.setExAppID(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
					qes.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
					qes.setChoice(cursor.getString(cursor.getColumnIndex("choice")));
					qes.setCorrectChoice(cursor.getString(cursor.getColumnIndex("correctChoice")));
					qes.setMark(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("mark"))));
					
					for(QuestionExamStatus q : info.getQuestions()){
						if(q.getId() == qes.getId()){
							q = qes;
							break;
						}
					}
					
					cursor.moveToNext();
				}
				
				cursor.close();
				}
		
		return info;
	};

	/**
	 * When finishing an exam to keep the examination information into database.
	 * @param context
	 * @param info
	 * @return
	 */
	public static boolean saveExamResultInfo(Context context,
			ExamResultInfo info) {
		Log.i(LOG_TAG, "save exam result info..");
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		db.beginTransaction();
		try{
//			if(!info.isFinish()){
//				Log.d(LOG_TAG, "delete from exam_result_info where ex_app_id ="+info.getExAppID());
//				db.execSQL("delete from exam_result_info where ex_app_id ="
//						+ info.getExAppID());
//			}
			Log.d(LOG_TAG, "delete from question_exam_result_info where ex_app_id ="+info.getExAppID());
			db.execSQL("delete from question_exam_result_info where ex_app_id ="
					+ info.getExAppID());
			
			Log.d(LOG_TAG,"insert into exam_result_info...");
			db.execSQL(
					"insert into exam_result_info (ex_app_id,ex_app_name,score,finish,progress,start_time,end_time,question_count,section) values (?,?,?,?,?,?,?,?,?)",
					
					new String[] { info.getExAppID() + "", info.getExAppName(),
							info.getScore() + "", info.isFinish() + "",
							info.getProgress()+"",
							info.getStartTime(), info.getEndTime()+"",info.getQuestionCount()+"",info.getSection() });
			
			for(QuestionExamStatus q:info.getQuestions()){
				Log.d(LOG_TAG,"insert into question_exam_result_info,and question_id="+q.getQuestionId()+",and solution="+q.getCorrectChoice());
				db.execSQL(
						"insert into question_exam_result_info (ex_app_id,question_id,choice,correctChoice,mark) values (?,?,?,?,?)",
						new String[] { info.getExAppID() + "", q.getQuestionId()+"",q.getChoice()+"",q.getCorrectChoice()+"",q.isMark()+"" });
			}
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();

		return true;
	}


	/**
	 * 
	 * @param context
	 * @param section
	 * @param type 1 - last 2 - average 3 - best 
	 * @return
	 */
	public static List<ListItemExamStatVO> getExamStatsBySectionAndType(
			Context context, String section,int type) {
		List<ListItemExamStatVO> rs = new ArrayList<ListItemExamStatVO>();
		Map<Integer, String> examResultInfoMap = new HashMap<Integer,String>();
		
		List<ExamResultInfo> infos = getExamResultInfosBySection(context,section);
		if(infos.size()==0)return rs;
		switch (type) {
		case 1:
			for(ExamResultInfo o : infos){
				if(!examResultInfoMap.containsKey(o.getExAppID())){
					ListItemExamStatVO vo = new ListItemExamStatVO();
					vo.setExAppID(o.getExAppID());
					vo.setName(o.getExAppName());
					vo.setScore(o.getScore()+"");
					vo.setTotalTime(TimeUtil.getTime(o.getEndTime()));
					if(o.getQuestionCount()>0)
					vo.setPer(TimeUtil.getTime(o.getEndTime()/o.getQuestionCount()));
					vo.setNumQuestion(o.getQuestionCount());
					rs.add(vo);
					examResultInfoMap.put(o.getExAppID(), "");
				}
			}
			 
			break;
		case 2:
			return getAverageExamResultInfosBySection(context,section);
			
		case 3:
			Collections.sort(infos, new Comparator<ExamResultInfo>() {
				@Override
				public int compare(ExamResultInfo lhs, ExamResultInfo rhs) {
					if(lhs.getScore()>rhs.getScore())return -1;
					else if(lhs.getScore()<rhs.getScore())return 1;
					else{
						if(lhs.getEndTime()>rhs.getEndTime())return -1;
						else if(lhs.getEndTime()<rhs.getEndTime())return 1;
						else
							return 0;
					}
						
				}
			});
			for(ExamResultInfo o : infos){
				if(!examResultInfoMap.containsKey(o.getExAppID())){
					ListItemExamStatVO vo1 = new ListItemExamStatVO();
					vo1.setExAppID(o.getExAppID());
					vo1.setName(o.getExAppName());
					vo1.setScore(o.getScore()+"");
					vo1.setTotalTime(TimeUtil.getTime(o.getEndTime()));
					vo1.setPer(TimeUtil.getTime(o.getEndTime()/o.getQuestionCount()));
					vo1.setNumQuestion(o.getQuestionCount());
					rs.add(vo1);
					examResultInfoMap.put(o.getExAppID(), "");
				}
			}
			
			break;

		default:
			
			break;
		}
		
		
		return rs;
	}

	public static void updateQuestionExamResultInfoMark(Context context,int exAppID,
			int questionId, boolean mark) {
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		db.beginTransaction();
		db.execSQL("update question_exam_result_info set mark=? where ex_app_id=? and question_id=?",new Object[]{mark+"",exAppID,questionId});
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		
	}
	
	public static int getNumOfQuestions(Context context, int exAppID) {
		int ret = 0;
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select count(*) as q_count from iphone_questions where ex_app_id ="+exAppID +" ", null);
		if(cursor!=null){
			cursor.moveToFirst();
			ret = cursor.getInt(cursor.getColumnIndex("q_count"));
			cursor.close();
	
		}
		db.close();

		return ret;
	}

	public static ExApps getExappByQuestionId(Context context,int questionId) {
		ExApps exapp = new ExApps();
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select app.id,app.section,app.subsection,app.name,app.date,app.duration,app.solving_title,app.evaluation_title,app.price,app.total_time,app.difficulty,app.video_url from iphone_ex_apps app,iphone_questions q where app.id=q.ex_app_id and q.id=?", new String[]{questionId+""});
		if(cursor!=null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				exapp.setId(cursor.getInt(cursor.getColumnIndex("id")));
				exapp.setSection(cursor.getString(cursor.getColumnIndex("section")));
				exapp.setSubSection(cursor.getString(cursor.getColumnIndex("subsection")));
				exapp.setName(cursor.getString(cursor.getColumnIndex("name")));
				exapp.setDate(cursor.getString(cursor.getColumnIndex("date")));
				exapp.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
				exapp.setSolvingTitle(cursor.getString(cursor.getColumnIndex("solving_title")));
				exapp.setEvaluationTitle(cursor.getString(cursor.getColumnIndex("evaluation_title")));
				exapp.setPrice(cursor.getString(cursor.getColumnIndex("price")));
				exapp.setTotalTime(cursor.getString(cursor.getColumnIndex("total_time")));
				exapp.setDifficulty(cursor.getInt(cursor.getColumnIndex("difficulty")));
				exapp.setVideoUrl(cursor.getString(cursor.getColumnIndex("video_url")));
				break;
			}
		
		cursor.close();
		}
		db.close();
		
		return exapp;
		
	}	
	
	/**
	 * When finishing an exam to keep the workspace notes into database.
	 * @param context
	 * @param qid: question_id
	 * @param notes
	 * @return
	 */
	public static boolean saveWorkspaceNotes(Context context, int qid,
			List<String> notes) {
		Log.i(LOG_TAG, "save workspace notes ...");
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		db.beginTransaction();
		try{

			Log.d(LOG_TAG, "delete from question_exam_result_info where question_id ="+qid);
			db.execSQL("delete from question_workspace_notes where question_id ="
					+ qid);
			

			
			for(String n:notes){
				Log.d(LOG_TAG,"insert into question_workspace_notes, question_id="+qid+",and note="+n);
				db.execSQL(
						"insert into question_workspace_notes (question_id,note) values (?,?)",
						new String[] { qid+"",n});
			}
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();

		return true;
	}

	
	/**
	 * Create a examination information object when user begin a new practice.
	 * 
	 * @return
	 */
	public static List<ListItemWorkspaceNotesVO> searchWorkspaceNotesByKey(Context context, String keyword) {
		
		List<ListItemWorkspaceNotesVO> rs = new ArrayList<ListItemWorkspaceNotesVO>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
//		Cursor cursor = db.rawQuery("select o.ex_app_id ex_app_id ,o.ex_app_name ex_app_name,avg(o.score) score,avg(o.end_time) time, avg(o.end_time)/o.question_count per from exam_result_info o ,iphone_ex_apps app where finish='true' and app.id=o.ex_app_id and app.section=? group by ex_app_id  order by start_time desc", new String[]{sectionName});
		Cursor cursor = db.rawQuery("select question_id, note from question_workspace_notes where note like '%"+keyword+"%' order by question_id", null);
//		Cursor cursor = db.rawQuery("select question_id, note from question_workspace_notes where note like '%hello%' order by question_id", null);

		if(cursor!=null){
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()){
				ListItemWorkspaceNotesVO vo = new ListItemWorkspaceNotesVO();
				rs.add(vo);
				vo.setQuestionID(cursor.getInt(cursor.getColumnIndex("question_id")));
				vo.setNote(""+cursor.getString(cursor.getColumnIndex("note")));			
				cursor.moveToNext();
			}
		
			cursor.close();
		}
		db.close();
		return rs;
		
	};

}
