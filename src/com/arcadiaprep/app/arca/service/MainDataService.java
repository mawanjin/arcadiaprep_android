package com.arcadiaprep.app.arca.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import android.content.Context;
import android.util.Log;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.mo.ExApps;
import com.arcadiaprep.app.arca.mo.ExAppsPages;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.arca.vo.ExamSection;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;
import com.arcadiaprep.app.arca.vo.ListItemRecommendationVO;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;
import com.arcadiaprep.app.arca.vo.ProblemPackageVO;
import com.arcadiaprep.app.util.Item;

public class MainDataService {
	
	final static String lOG_TAG = "MainDataService";
	
	public static List<ListItemMyQuestionVO> questionSets;
	
	/**
	 * 
	 * @param type retrieve from class ConstantMain
	 * @return
	 */ 
	public static List<ListItemMyQuestionVO> findMyQuestionSets(Context context,String section){
		Log.d(lOG_TAG, "Find problemsets for main activity...");
			questionSets = new ArrayList<ListItemMyQuestionVO>();
			List<ExApps> rs = SystemService.findExAppsBySection(context, section);
			for(ExApps a : rs){ 
				String icon_name = convertImg(a.getDifficulty()+""); //a.getDifficulty();

				ListItemMyQuestionVO vo = new ListItemMyQuestionVO(a.getId(),icon_name,a.getName().split(":")[0],(a.getName().split(":").length>1?a.getName().split(":")[1]:""),"",0,(a.getExamResultInfo()==null)?false:a.getExamResultInfo().isFinish(),section);
				vo.setTotalTime(a.getTotalTime());
				questionSets.add(vo);
				if(a.getExamResultInfo()!=null){
					if(a.getExamResultInfo().isFinish()){
						vo.setProgress("Last Score: "+a.getExamResultInfo().getScoreView()+"%");
					}else{
						vo.setProgressMax(a.getExamResultInfo().getQuestionCount());
						vo.setProgressCount(a.getExamResultInfo().getProgress());
						vo.setProgress("In progress: "+a.getExamResultInfo().getProgress()+"/"+a.getExamResultInfo().getQuestionCount());
					}
				}
			}
			
			return questionSets;
	}
	
	public static List<ListItemMyQuestionVO> convertRecommendationsToMyQuestionSets(List<Item> item){
		List<ListItemMyQuestionVO> list = new ArrayList<ListItemMyQuestionVO>();
		int j = item.size();
		for(Item i: item){
			ListItemMyQuestionVO vo = new ListItemMyQuestionVO();
			list.add(vo);
			vo.setRecommendId(i.getId());
			vo.setSection(i.getSectionName());
			vo.setLogoImg(i.getIcon());
			vo.setTitle(i.getTitle());
			vo.setPackageSetName(i.getPackageSetName());
//			vo.setInfo(i.getText());
			vo.setProgress(i.getPrice());
			vo.setRecommends(true);
		}
		
		return list;
	}
	
	
	public static List<ExamResultInfo> findExamResultInfo(Context context,List<ListItemMyQuestionVO> questions){
		
		List<ExamResultInfo> infos = QuestionViewService.getExamResultInfosByProblemSets(context, questions);
		
		
		return infos;
	}
	
	/**
	 * Convert original image to right icon.
	 * @param img
	 * @return
	 */
	private static String convertImg(String img){
		if(img==null||img.equals(""))
			return "difficult0.png";
		else if(img.equals("5")){
			return "difficult0.png";
		}else if(img.equals("1")){
			return "difficult1.png";
		}else if(img.equals("2")){
			return "difficult2.png";
		}else if(img.equals("3")){
			return "difficult3.png";
		}else if(img.equals("4")){
			return "difficult4.png";
		}else
			return "difficult0.png";
	}

	/**
	 * Convert data from moduleinfo.xml
	 * @return
	 */
	public static List<ExamSection> findCategoryBars(Context context){
		ModuleInfoVO module = SystemService.parseModuelInfo(context);
		return module.getExamSection();
	}

	public static String findProblemSetIntroductionByExAppId(Context context,int exAppId) {
		// Log.d(lOG_TAG, "Finding problemSet introduction by exappid["+exAppId+"]");
		List<ExAppsPages> rs = SystemService.findExAppPageByExAppID(context, exAppId);
		for(ExAppsPages p : rs){
			//Log.i(lOG_TAG, "Found introduction and solving is ["+p.getSolving()+"],content is ["+p.getContent()+"]");
			if(p.getSolving()==1)return p.getContent();
		} 
		// return a default content
		ModuleInfoVO module = SystemService.parseModuelInfo(context);
		return module.getIntroPage();
		
/*		
		BufferedReader reader = null; 
		String line, results = "";
		
		try {
			reader = new BufferedReader(new FileReader("file:///android_asset/problemset_intro.html"));
		
			while( ( line = reader.readLine() ) != null)
			{
		        results += line;
			}
			reader.close();

		}
		catch (Exception e) {
	        e.printStackTrace();

		}
	*/

	}

	public static String findProblemSetIntroductionForExamResultByExAppId(Context context,int exAppId){
		Log.i(lOG_TAG, "Finding problemSet introduction by exappid["+exAppId+"]");
		List<ExAppsPages> rs = SystemService.findExAppPageByExAppID(context, exAppId);
		for(ExAppsPages p : rs){
			// Log.i(lOG_TAG, "Found introduction and solving is ["+p.getSolving()+"],content is ["+p.getContent()+"]");
			if(p.getSolving()==0)return p.getContent();
		}
		// return something default
		return "Congratulations for having the discipline to prepare yourself for this difficult test. 'Success is a journey, not a destination. The doing is often more important than the outcome.' - Arthur Ashe ";
	}
	
	public static List<Questions> findQuestionsByExAppIDRs;
	public static int findQuestionsByExAppID_EXAPPID=-1;
	
	public static List<Questions> findQuestionsByExAppID(Context context,int exAppId) {
		if(findQuestionsByExAppID_EXAPPID==exAppId&&findQuestionsByExAppIDRs!=null){
			return findQuestionsByExAppIDRs;
		}else{
			findQuestionsByExAppIDRs =SystemService.findQuestionsByExAppID(context, exAppId);
			findQuestionsByExAppID_EXAPPID = exAppId;
		}
			
		return findQuestionsByExAppIDRs;
		
	}
}
