package com.arcadiaprep.app.arca.mo;

import com.arcadiaprep.app.arca.util.TimeUtil;

public class BookMark {
	private int id;
	private int exappid;
	private int questionId;
	private int position;
	private String title;
	private String date;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExappid() {
		return exappid;
	}

	public void setExappid(int exappid) {
		this.exappid = exappid;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return TimeUtil.getTimeFromMills(date);
	}

	public void setDate(String date) {
		this.date = date;
	}

}
