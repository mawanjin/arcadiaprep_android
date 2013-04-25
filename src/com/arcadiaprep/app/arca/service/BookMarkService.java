package com.arcadiaprep.app.arca.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.mo.BookMark;

public class BookMarkService {
	
	/**
	 * From all data from table bookmark. 
	 * @param context
	 * @param orderby the column name of table bookmark.
	 */
	public static List<BookMark> findAll(Context context,String orderby){
		List<BookMark> marks = new ArrayList<BookMark>();
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
//		Cursor cursor = db.rawQuery("select k.* from bookmark k,iphone_ex_apps app where k.ex_app_id=app.id order by "+orderby+" desc",null);
		Cursor cursor = db.rawQuery("select k.* from bookmark k  order by "+orderby+" desc",null);
		if(cursor!=null){
			cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			BookMark mark = new BookMark();
			marks.add(mark);
			 
			mark.setId(cursor.getInt(cursor.getColumnIndex("id")));
			mark.setExappid(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			mark.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
			mark.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			mark.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			mark.setDate(cursor.getString(cursor.getColumnIndex("date")));
			
			cursor.moveToNext();
		}
		
		cursor.close();
		}
		db.close();
		return marks;
	}
	
	public static BookMark get(Context context,int exAppId,int questionId){
		BookMark mark = null;
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		
		Cursor cursor = db.rawQuery("select k.* from bookmark k where k.ex_app_id=? and k.question_id=? ",new String[]{exAppId+"",questionId+""});
		if(cursor!=null){
			cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			mark = new BookMark();
			mark.setId(cursor.getInt(cursor.getColumnIndex("id")));
			mark.setExappid(cursor.getInt(cursor.getColumnIndex("ex_app_id")));
			mark.setQuestionId(cursor.getInt(cursor.getColumnIndex("question_id")));
			mark.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
			mark.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			mark.setDate(cursor.getString(cursor.getColumnIndex("date")));
			
			break;
		}
		
		cursor.close();
		}
		db.close();
		return mark;
	}

	
	public static boolean save(Context context,BookMark bookMark){
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		db.execSQL("delete from bookmark where ex_app_id=? and question_id=?",new Integer[]{bookMark.getExappid(),bookMark.getQuestionId()});
		db.execSQL("insert into bookmark(ex_app_id,question_id,position,title,date) values(?,?,?,?,?)",new String[]{bookMark.getExappid()+"",bookMark.getQuestionId()+"",bookMark.getPosition()+"",bookMark.getTitle(),bookMark.getDate()});
		db.close();
		return true;
	}


	public static void del(Context context,
			BookMark bookMark) {
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME_RUNTIME).openDatabase();
		db.execSQL("delete from bookmark where ex_app_id=? and question_id=?",new Integer[]{bookMark.getExappid(),bookMark.getQuestionId()});
		db.close();
	}
	
}
