package com.arcadiaprep.app.arca.vo;

import java.util.ArrayList;
import java.util.List;

public class ExamResultInfo {
	private int id;
	private String section;
	private int exAppID;
	private String exAppName;
	private int score;
	private boolean finish = false;
	// the progress of examination shows that how many questions have been done
	private int progress = -1;
	private String startTime;
	private int endTime = 0;
	private int questionCount;

	private List<QuestionExamStatus> questions = new ArrayList<QuestionExamStatus>();

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

	public int getExAppID() {
		return exAppID;
	}

	public void setExAppID(int exAppID) {
		this.exAppID = exAppID;
	}

	public String getExAppName() {
		return exAppName;
	}

	public void setExAppName(String exAppName) {
		this.exAppName = exAppName;
	}

	public int getScore() {
		// int c =0;
		// for(QuestionExamStatus q : questions){
		// if(q.getChoice()==q.getCorrectChoice())c++;
		// }
		//
		// score = c;
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public List<QuestionExamStatus> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionExamStatus> questions) {
		this.questions = questions;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public String getScoreView() {
		if (score > 0 && getQuestionCount() > 0) {
			float f = (float) score / (float) getQuestionCount();
			float b = (float) (Math.round(f * 100)) / 100;
			if (f == 1.0)
				return "100";
			String rs = ((b * 100) + "").substring(0, 2);
			if (rs.endsWith("."))
				rs = rs.substring(0, rs.length() - 1);

			return rs;
		} else
			return "0";

	}

}
