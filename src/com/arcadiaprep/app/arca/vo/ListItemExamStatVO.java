package com.arcadiaprep.app.arca.vo;

public class ListItemExamStatVO {

	private int exAppID;
	private String name;
	private String score;
	private String totalTime;
	private String per;
	private int numQuestion;
	
	public int getNumQuestion() {
		return numQuestion;
	}
	
	public void setNumQuestion(int n) {
		this.numQuestion = n;
	}
	
	public int getExAppID() {
		return exAppID;
	}

	public void setExAppID(int exAppID) {
		this.exAppID = exAppID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

}
