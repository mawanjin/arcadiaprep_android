package com.arcadiaprep.app.arca.vo;

public class QuestionExamStatus {
	private int id;
	private int exAppID;
	private int questionId;
	// 0-A 1-B 2-C 3-D 4-E 5-F -1 no choice
	private String choice="-1";
	// 0-A 1-B 2-C 3-D 4-E 5-F
	private String correctChoice;

	private boolean mark;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public int getExAppID() {
		return exAppID;
	}

	public void setExAppID(int exAppID) {
		this.exAppID = exAppID;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getCorrectChoice() {
		return correctChoice;
	}

	public void setCorrectChoice(String correctChoice) {
		this.correctChoice = correctChoice;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

}
