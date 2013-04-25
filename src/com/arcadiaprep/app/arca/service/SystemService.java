package com.arcadiaprep.app.arca.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.arcadiaprep.app.arca.ArcadiaprepActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.mo.ExApps;
import com.arcadiaprep.app.arca.mo.ExAppsPages;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.arca.vo.ExamSection;
import com.arcadiaprep.app.arca.vo.InformationVO;
import com.arcadiaprep.app.arca.vo.MinItemVO;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;
import com.arcadiaprep.app.arca.vo.ProblemPackageVO;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.market.MyApplication;


public class SystemService {
	private static String lOG_TAG = "SystemService";
	
	private static ModuleInfoVO moduleInfo;
	private static List<ProblemPackageVO> problemPackages;
	private static InformationVO information;
	
	
	public static InformationVO parseInformation(Context context){
		if(information==null){
			Log.d(lOG_TAG, "initializing information from xml...");
			information = new InformationVO();
			SAXBuilder builder = new SAXBuilder(); 
			InputStream ins = null;  
			try {

				ins = context.getResources().getAssets()
						.open("plist/information.plist");
				Document doc = null;
				doc = builder.build(ins);
				Element root = doc.getRootElement().getChild("array");
				
				List<Element> elements = root.getChildren();
				
				for(Element e : elements){
					MinItemVO vo = new MinItemVO();
					List<Element> _elements =  e.getChildren();
					for(int i=0;i<_elements.size();i++){
						if(_elements.get(i).getName().equals("key")&&_elements.get(i).getValue().equals("content")){
							vo.setText(_elements.get(i+1).getValue());
						}else if(_elements.get(i).getName().equals("key")&&_elements.get(i).getValue().equals("name")){
							vo.setTitle(_elements.get(i+1).getValue());
						} 
					}
					information.getItems().add(vo);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return information;
	}
	
	
	public static ModuleInfoVO parseModuelInfo(Context context) {
		
		if(moduleInfo==null){
			Log.d(lOG_TAG, "initializing ModuleInfo from xml...");
			moduleInfo = new ModuleInfoVO();
			SAXBuilder builder = new SAXBuilder(); 
			InputStream ins = null;  
			try {  
				ins = context.getResources().getAssets().open("plist/moduleinfo.plist");
			    Document doc = null;  
			    doc = builder.build(ins);  
			    Element root = doc.getRootElement().getChild("dict");  
			    
			    List<Element> elements = root.getChildren();
			    int i=0;
			    for(Element e: elements) {
			    	if(e.getName().equals("key")&&e.getValue().equals("examType")){
			    		moduleInfo.setExamType(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("resetPassURL")){
			    		moduleInfo.setResetPassURL(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("ServerURL")){
			    		moduleInfo.setServerURL(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("SecureServerURL")){
			    		moduleInfo.setSecureServerURL(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("appCode")){
			    		moduleInfo.setAppCode(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("websiteName")){
			    		moduleInfo.setWebsiteName(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("websiteURL")){
			    		moduleInfo.setWebsiteURL(root.getChildren().get(i+1).getValue());
	                }else if(e.getName().equals("key")&&e.getValue().equals("Samsung Market GroupID")){
	                    moduleInfo.setMarketGroupId(root.getChildren().get(i+1).getValue());
	                } else if (e.getName().equals("key")&&e.getValue().equals("Developer")) {
	                	moduleInfo.setDeveloperFlag(root.getChildren().get(i+1).getValue());
	                }
	                else if(e.getName().equals("key")&&e.getValue().equals("IntroPage")){
	                    moduleInfo.setIntroPage(root.getChildren().get(i+1).getValue());
			    	}else if(e.getName().equals("key")&&e.getValue().equals("examSection")){
			    		 List<Element> dicts = root.getChildren().get(i+1).getChildren();
			    		 for(Element d: dicts){
			    			 ExamSection examSection = new ExamSection();
			    			 int index=0;
			    			 List<Element> items = d.getChildren();
			    			 for(Element item : items){
			    				 if(item.getName().equals("key")&&item.getValue().equals("secName")){
			    					 examSection.setSecName(d.getChildren().get(index+1).getValue());
			    				 }else if(item.getName().equals("key")&&item.getValue().equals("secNameShort")){
			    					 examSection.setSecNameShort(d.getChildren().get(index+1).getValue());
			    				 }else if(item.getName().equals("key")&&item.getValue().equals("secFile")){
			    					 examSection.setSecFile(d.getChildren().get(index+1).getValue());
			    				 } 
			    				 index++;
			    			 }
			    			 moduleInfo.getExamSection().add(examSection);
			    		 }
			    	}
			    	
			    	i++;
			    }  
			} catch (Exception e1) {  
			    e1.printStackTrace();  
			} 
			finally {  
			    try {  
			        ins.close();  
			    } catch (IOException e) {
			    	Log.i(lOG_TAG, "initializing ModuleInfo from xml failed.");
			        e.printStackTrace();  
			    }  
			} 
			Log.i(lOG_TAG, "initializing ModuleInfo from xml success.");
		}
		
		return moduleInfo;
	}

	public static List<ProblemPackageVO> parseProblemPackage(Context context){
		if(problemPackages==null){
			Log.i(lOG_TAG, "initializing ProblemPackage from plst...");
			problemPackages = new ArrayList<ProblemPackageVO>();
			//begin
			SAXBuilder builder = new SAXBuilder(); 
			InputStream ins = null;  
			try {  
				ins = context.getResources().getAssets().open("plist/problempackages.plist");
			    Document doc = null;  
			    doc = builder.build(ins);  
			    Element root = doc.getRootElement().getChild("array");  
			    
			    List<Element> elements = root.getChildren();
			    for(Element e: elements) {
			    	ProblemPackageVO problemPackageVO = new ProblemPackageVO();
			    	problemPackages.add(problemPackageVO);
			    	 int index=0;
			    	 List<Element> dict = e.getChildren();
			    	 for(Element d: dict){
			    		 if(d.getName().equals("key")&&d.getValue().equals("Attributes")){
			    			 List<Element> attrDict = e.getChildren().get(index+1).getChildren();
			    			 int adIndex=0;
			    			 for(Element ad : attrDict){
			    				 if(adIndex+1==attrDict.size())break;
			    				 List<Element> items = attrDict.get(adIndex+1).getChildren();
			    				 if(ad.getName().equals("key")&&ad.getValue().equals("EvaluationAndImprovementPage")){
			    					 problemPackageVO.setEvaluationAndImprovementPage(new MinItemVO());
			    					
			    					 for(int itemIndex=0;itemIndex<items.size();itemIndex++){
			    						 if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Title")){
			    							 problemPackageVO.getEvaluationAndImprovementPage().setTitle(items.get(itemIndex+1).getValue());
			    						 }else if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Text")){
			    							 problemPackageVO.getEvaluationAndImprovementPage().setText(items.get(itemIndex+1).getValue());
			    						 } 
			    					 }
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("IdentifyingAndSolvingPage")){
			    					 problemPackageVO.setIdentifyingAndSolvingPage(new MinItemVO());
//			    					 List<Element> items = attrDict.get(adIndex+1).getChildren();
			    					 for(int itemIndex=0;itemIndex<items.size();itemIndex++){
			    						 if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Title")){
			    							 problemPackageVO.getIdentifyingAndSolvingPage().setTitle(items.get(itemIndex+1).getValue());
			    						 }else if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Text")){
			    							 problemPackageVO.getIdentifyingAndSolvingPage().setText(items.get(itemIndex+1).getValue());
			    						 } 
			    					 }
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("AboutPage")){
			    					 problemPackageVO.setAboutPage(new MinItemVO());
//			    					 List<Element> items = attrDict.get(adIndex+1).getChildren();
			    					 for(int itemIndex=0;itemIndex<items.size();itemIndex++){
			    						 if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Title")){
			    							 problemPackageVO.getAboutPage().setTitle(items.get(itemIndex+1).getValue());
			    						 }else if(items.get(itemIndex).getName().equals("key")&&items.get(itemIndex).getValue().equals("Text")){
			    							 problemPackageVO.getAboutPage().setText(items.get(itemIndex+1).getValue());
			    						 } 
			    					 }
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("Local")){
			    					 
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("WebAppUnlocked")){
			    					 
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("Difficulty")){
			    					 problemPackageVO.setDifficulty(attrDict.get(adIndex+1).getValue());
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("Price")){
			    					 problemPackageVO.setPrice(attrDict.get(adIndex+1).getValue());
			    				 }else if(ad.getName().equals("key")&&ad.getValue().equals("Icon")){
			    					 problemPackageVO.setIcon(attrDict.get(adIndex+1).getValue());
			    				 }
			    				 adIndex++;
			    			 }
			    			 
			    			 
			    		 }else if(d.getName().equals("key")&&d.getValue().equals("Data")){
			    			 List<Element> dataDict = e.getChildren().get(index+1).getChildren();
			    			 for(int dataDicIndex=0;dataDicIndex<dataDict.size();dataDicIndex++){
			    				 if(dataDict.get(dataDicIndex).getName().equals("key")&&dataDict.get(dataDicIndex).getValue().equals("PackageSetName")){
			    					 problemPackageVO.setPackageSetName(dataDict.get(dataDicIndex+1).getValue());  
			    				 }else if(dataDict.get(dataDicIndex).getName().equals("key")&&dataDict.get(dataDicIndex).getValue().equals("ProblemSets")){
//			    					 problemPackageVO.setProblemSets(dataDict.get(dataDicIndex+1).getValue());
			    					 if(dataDict.get(dataDicIndex+1).getName().equals("array")){
			    						 for(Element t : dataDict.get(dataDicIndex+1).getChildren()){
			    							 problemPackageVO.getProblemSets().add(t.getValue());
			    						 }
			    					 }
			    					 
			    				 }
			    			 }
			    			 
			    		 }else if(d.getName().equals("key")&&d.getValue().equals("Name")){
			    			 List<Element> nameDict = e.getChildren().get(index+1).getChildren();
			    			 for(int nameIndex=0;nameIndex<nameDict.size();nameIndex++){
			    				 if(nameDict.get(nameIndex).getName().equals("key")&&nameDict.get(nameIndex).getValue().equals("ProblemSetName")){
			    					 problemPackageVO.setProblemSetName(nameDict.get(nameIndex+1).getValue());
			    				 }else if(nameDict.get(nameIndex).getName().equals("key")&&nameDict.get(nameIndex).getValue().equals("ExamSectionName")){
			    					 problemPackageVO.setExamSectionName(nameDict.get(nameIndex+1).getValue());
			    				 }else if(nameDict.get(nameIndex).getName().equals("key")&&nameDict.get(nameIndex).getValue().equals("ExamName")){
			    					 problemPackageVO.setExamName(nameDict.get(nameIndex+1).getValue());
			    				 }
			    			 }
			    		 }
			    		 index++;
			    	 }
			    }  
			} catch (Exception e1) {  
			    e1.printStackTrace();  
			} 
			finally {  
			    try {  
			        ins.close();  
			    } catch (IOException e) {
			    	Log.i(lOG_TAG, "initializing ModuleInfo from xml failed.");
			        e.printStackTrace();  
			    }  
			} 
			//end
			
		}
		return problemPackages;
	}

	public static List<ExApps> findAllExApps(Context context){
		List<ExApps> apps = new ArrayList<ExApps>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from iphone_ex_apps", null);
		if(cursor!=null){
		while(!cursor.isAfterLast()){
			ExApps exapp = new ExApps();
			apps.add(exapp);
			cursor.moveToFirst(); 
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
		}
		cursor.close();}
		db.close();
		return apps;
	}

	public static List<ExApps> findExAppsBySection(Context context ,String section){
		List<ExApps> apps = new ArrayList<ExApps>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
//		Cursor cursor = db.rawQuery("select problemset.id pid, problemset.*,eri.* from iphone_ex_apps problemset  left join exam_result_info eri on eri.ex_app_id=problemset.id  where problemset.section=? order by eri.start_time desc", new String[]{section});
		Cursor cursor = db.rawQuery("select problemset.id pid, problemset.* from iphone_ex_apps problemset  where problemset.section=? order by name", new String[]{section});
		if(cursor!=null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				boolean exists = false;
				for(ExApps app : apps){
					if(app.getId()==cursor.getInt(cursor.getColumnIndex("pid"))){
						exists = true;
						break;
					}
				}
				if(!exists){
				ExApps exapp = new ExApps();
				exapp.setId(cursor.getInt(cursor.getColumnIndex("pid")));
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
				
				// Only show purchased and 0 priced problem sets
				// -- Sharon
				String exappId = String.valueOf(exapp.getId());
				if (MyApplication._purchasedProblemSetIds.contains(exappId))
				    apps.add(exapp);
				else {
				    Double price = Double.valueOf(exapp.getPrice());
				    User mUser = UserService.getUser(context);
				    
				    if ((mUser != null && mUser.getProfile() >= 5) || (MyApplication._developer == true) ) {  // unlock the questions for user with Profile>=5 
				    	apps.add(exapp);
				    }
				    else if (price.doubleValue() == 0.0)
				        apps.add(exapp);
				}
//				 apps.add(exapp);
				}  // end of if (!exists)
				
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		cursor=null;
		db.close();
		
		//fill with exam_result_info
		fillExamResultInfo(apps,context);
		return apps;
	}
	
	
	
	private static void fillExamResultInfo(List<ExApps> apps,Context context) {
		StringBuffer ids = new StringBuffer();
		for(ExApps p : apps)
			ids.append(p.getId()+",");
		if(ids.toString().endsWith(","))
			ids.deleteCharAt(ids.lastIndexOf(","));
		
		List<ExamResultInfo> infos =  QuestionViewService.getExamResultInfoByExAppIDs(context, ids.toString());
		
		for(ExApps p : apps){
			for(ExamResultInfo info : infos){
				if(info.getExAppID()==p.getId())p.setExamResultInfo(info);
			}
		}
		
	}


//	public static List<ExApps> findExAppsBySection(Context context ,String section){
//		List<ExApps> apps = new ArrayList<ExApps>();
//		SQLiteDatabase db = DBManager.getInstance().openDatabase(context);
//		
//		Cursor cursor = db.rawQuery("select * from iphone_ex_apps where section=?", new String[]{section});
//		if(cursor!=null){
//			cursor.moveToFirst();
//			while(!cursor.isAfterLast()){
//				ExApps exapp = new ExApps();
//				apps.add(exapp);
//				exapp.setId(cursor.getInt(cursor.getColumnIndex("id")));
//				exapp.setSection(cursor.getString(cursor.getColumnIndex("section")));
//				exapp.setSubSection(cursor.getString(cursor.getColumnIndex("subsection")));
//				exapp.setName(cursor.getString(cursor.getColumnIndex("name")));
//				exapp.setDate(cursor.getString(cursor.getColumnIndex("date")));
//				exapp.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
//				exapp.setSolvingTitle(cursor.getString(cursor.getColumnIndex("solving_title")));
//				exapp.setEvaluationTitle(cursor.getString(cursor.getColumnIndex("evaluation_title")));
//				exapp.setPrice(cursor.getString(cursor.getColumnIndex("price")));
//				exapp.setTotalTime(cursor.getString(cursor.getColumnIndex("total_time")));
//				exapp.setDifficulty(cursor.getInt(cursor.getColumnIndex("difficulty")));
//				exapp.setVideoUrl(cursor.getString(cursor.getColumnIndex("video_url")));
//				cursor.moveToNext();
//			}
//		}
//		
//		cursor.close();
//		cursor=null;
//		db.close();
//		return apps;
//	}

	public static List<ExAppsPages> findExAppPageByExAppID(Context context, int exAppId) {
		List<ExAppsPages> rs = new ArrayList<ExAppsPages>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from iphone_ex_app_pages where ex_app_id=?", new String[]{exAppId+""});
		if(cursor!=null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				ExAppsPages exapp = new ExAppsPages();
				rs.add(exapp);
				exapp.setId(cursor.getInt(cursor.getColumnIndex("id")));
				exapp.setExAppID(exAppId);
				exapp.setContent(cursor.getString(cursor.getColumnIndex("content")));
				exapp.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
				exapp.setDate(cursor.getString(cursor.getColumnIndex("date")));
				exapp.setSolving(cursor.getInt(cursor.getColumnIndex("solving")));
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		cursor=null;
		db.close();
		return rs;
	}
	
	public static String findQuestionsDesciptionByID(Context context, int questionId) {
		
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select ex_app_id from iphone_questions where id=?", new String[]{questionId+""});
		if(cursor!=null){
			cursor.moveToFirst();
			String ex_app_id = cursor.getString(cursor.getColumnIndex("ex_app_id"));
			
			cursor = db.rawQuery("select a.name as Title,b.id as Question from iphone_ex_apps a ,iphone_questions b where a.id=? AND b.ex_app_id=a.id", new String[]{ex_app_id});
			if(cursor!=null){
				cursor.moveToFirst();
				int postion = 0;
				while(!cursor.isAfterLast()){
					postion ++;
					if (cursor.getInt(cursor.getColumnIndex("Question")) == questionId) {
						
						String result = cursor.getString(cursor.getColumnIndex("Title")) + ": #" + postion;
						return result;
					}
					cursor.moveToNext();
				}
			}
		}
		return null;
	}

	public static List<Questions> findQuestionsByExAppID(Context context, int exAppId) {

		List<Questions> rs = new ArrayList<Questions>();
		SQLiteDatabase db = DBManager.getInstance(context,ConstantSystem.DATABASE_NAME).openDatabase();
		
		Cursor cursor = db.rawQuery("select * from iphone_questions where ex_app_id=? order by pt_qid", new String[]{exAppId+""});
		if(cursor!=null){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				Questions q = new Questions();
				rs.add(q);
				q.setId(cursor.getInt(cursor.getColumnIndex("id")));
				q.setExAppID(exAppId);
				q.setName(cursor.getString(cursor.getColumnIndex("name")));
				q.setDifficulty(cursor.getInt(cursor.getColumnIndex("difficulty")));
				q.setTextBlock1A(cursor.getString(cursor.getColumnIndex("text_block1a")));
				q.setTextBlock1B(cursor.getString(cursor.getColumnIndex("text_block1b")));
				q.setTextBlock1C(cursor.getString(cursor.getColumnIndex("text_block1c")));
				q.setTextBlock1D(cursor.getString(cursor.getColumnIndex("text_block1d")));
				q.setImage(cursor.getString(cursor.getColumnIndex("image")));
				q.setQuestionStemA(cursor.getString(cursor.getColumnIndex("question_stem_a")));
//				q.setQuestionStemB(cursor.getString(cursor.getColumnIndex("question_stem_b")));
//				q.setQuestionStemC(cursor.getString(cursor.getColumnIndex("question_stem_c")));
//				q.setQuestionStemD(cursor.getString(cursor.getColumnIndex("question_stem_d")));
				q.setAnswer1A(cursor.getString(cursor.getColumnIndex("answer1_a")));
				q.setAnswer2A(cursor.getString(cursor.getColumnIndex("answer2_a")));
				q.setAnswer3A(cursor.getString(cursor.getColumnIndex("answer3_a")));
				q.setAnswer4A(cursor.getString(cursor.getColumnIndex("answer4_a")));
				q.setAnswer5A(cursor.getString(cursor.getColumnIndex("answer5_a")));
				q.setAnswer6A(cursor.getString(cursor.getColumnIndex("answer6_a")));
				q.setSolution(cursor.getString(cursor.getColumnIndex("solution")));
				q.setSolutionText(cursor.getString(cursor.getColumnIndex("solution_text")));
				q.setSolutionText1(cursor.getString(cursor.getColumnIndex("solution_text1")));
				q.setSolutionText2(cursor.getString(cursor.getColumnIndex("solution_text2")));
				q.setSolutionText3(cursor.getString(cursor.getColumnIndex("solution_text3")));
				q.setSolutionText4(cursor.getString(cursor.getColumnIndex("solution_text4")));
				q.setSolutionText5(cursor.getString(cursor.getColumnIndex("solution_text5")));
				
				q.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
				q.setDate(cursor.getString(cursor.getColumnIndex("date")));
				q.setPtId(cursor.getInt(cursor.getColumnIndex("pt_id")));
				q.setPtSection(cursor.getInt(cursor.getColumnIndex("pt_section")));
				q.setPtQid(cursor.getInt(cursor.getColumnIndex("pt_qid")));
				q.setPtGroupFirst(cursor.getInt(cursor.getColumnIndex("pt_group_first")));
				
				q.setVideo1(cursor.getString(cursor.getColumnIndex("video1")));
				q.setVideo2(cursor.getString(cursor.getColumnIndex("video2")));
				
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		cursor=null;
		db.close();
		return rs;
	}


	public static int getScreenWith(WindowManager windowManager) {
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}
	
	public static int getScreenHeight(WindowManager windowManager) {
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}
	
	public static void sendMail(Context context,String receiver,String subject,String content){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");//only use on emulator
		intent.setType("message/rfc822");//use on real environment
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ConstantSystem.application_mail_address});
		intent.putExtra(Intent.EXTRA_SUBJECT,subject);
		intent.putExtra(Intent.EXTRA_TEXT,content);
		context.startActivity(Intent.createChooser(intent, "Choose an app to email us"));
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	/**
	 * Set  screen orientation to Landscape for small size of screen  device
	 * the normal and large screen devices would be  affected.
	 * @param context
	 */
	public static void setOrientation(Activity context){
		String deviceScreenType = context.getString(R.string.device_screen);
		if(context instanceof  ArcadiaprepActivity&&deviceScreenType.equals("hdpi")){
			if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}else if(deviceScreenType.equals("xhdpi")||deviceScreenType.equals("hdpi")){
			if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
	}

}
