package com.arcadiaprep.app.arca.service;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.util.FileUtil;

public class DBManager extends SQLiteOpenHelper{
	private static final String LOG_TAG = "DBManager";
	
	private Context context;
	
	private static DBManager manager;
	private static DBManager managerRunTime;
	
	private String dataBaseName;
	
	public DBManager(Context context, String dataBaseName, CursorFactory factory,
			int version) {
		super(context, dataBaseName, factory, version);
		this.context = context.getApplicationContext();
		this.dataBaseName = dataBaseName;
	}
	
	
	/**
	 * 
	 * @param DataBaseName which defined in ConstantSystem 
	 * @return
	 */
	public static DBManager getInstance(Context context,String dataBaseName){
		
		
		if(dataBaseName.equals(ConstantSystem.DATABASE_NAME)){
			if(manager==null)manager = new DBManager(context,dataBaseName,null,3);
			return manager;
		}else if(dataBaseName.equals(ConstantSystem.DATABASE_NAME_RUNTIME)){
			if(managerRunTime==null)managerRunTime = new DBManager(context,dataBaseName,null,3);
			return managerRunTime;
		}
		return null;
		
	}
	

	public  SQLiteDatabase openDatabase() {
		Log.i(LOG_TAG, "open database ["+dataBaseName+"]");
		
		// check out the database file whether put on a right position,if not
		// copy original db file to right position.
		// the reason for duplication is that the original file which  placed in assets
		// folder can not be read by system.we must
		// place it to a readable place.
		String DB = ConstantSystem.getDataBaseDir() + dataBaseName;
		File f = new File(DB);
		if (!f.exists()) {
			if(dataBaseName.equals(ConstantSystem.DATABASE_NAME)){
				Log.i(LOG_TAG, "database is not exist,so copy one from original folder.");
				FileUtil.createDirectory(ConstantSystem.getDataBaseDir());
				try {// copy db file from raw
					if(
					FileUtil.copy(
							context.getResources().getAssets().open(ConstantSystem.DATABASE_ORIGINAL_FILE_PATH),
							new FileOutputStream(DB)))Log.i(LOG_TAG, "copy database success");
					else
						Log.i(LOG_TAG, "copy database failure.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		return getReadableDatabase();
//		return SQLiteDatabase.openOrCreateDatabase(DB, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(dataBaseName.equals(ConstantSystem.DATABASE_NAME_RUNTIME)){
			db.execSQL("CREATE TABLE bookmark( id integer primary key,ex_app_id integer,question_id integer,position integer,title text,date text);");
			db.execSQL("CREATE TABLE exam_result_info( id integer primary key,ex_app_id integer,ex_app_name text,score integer,finish text,progress integer,start_time text,end_time text, question_count text,section text);");
			db.execSQL("CREATE TABLE question_exam_result_info( id integer primary key,ex_app_id integer,question_id integer,choice text,correctChoice text,mark text);");
			db.execSQL("CREATE TABLE question_workspace_notes( id integer primary key,ex_app_id integer, question_id integer, note text);");
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
