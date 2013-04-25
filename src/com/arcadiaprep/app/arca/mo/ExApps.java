package com.arcadiaprep.app.arca.mo;

import com.arcadiaprep.app.arca.vo.ExamResultInfo;


/**
 * Corresponding to table iphone_ex_apps
 * 
 * @author lala
 * 
 */
public class ExApps {
	private int id;
	private String section;
	private String subSection;
	private String name;
	private String date;
	private String duration;
	private String solvingTitle;
	private String evaluationTitle;
	private String price;
	private String totalTime;
	private int difficulty;
	private String videoUrl;
	
	private ExamResultInfo examResultInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubSection() {
		return subSection;
	}

	public void setSubSection(String subSection) {
		this.subSection = subSection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.replace("\\", "");
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSolvingTitle() {
		return solvingTitle;
	}

	public void setSolvingTitle(String solvingTitle) {
		this.solvingTitle = solvingTitle;
	}

	public String getEvaluationTitle() {
		return evaluationTitle;
	}

	public void setEvaluationTitle(String evaluationTitle) {
		this.evaluationTitle = evaluationTitle;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public ExamResultInfo getExamResultInfo() {
		return examResultInfo;
	}

	public void setExamResultInfo(ExamResultInfo examResultInfo) {
		this.examResultInfo = examResultInfo;
	}
	
	

}
