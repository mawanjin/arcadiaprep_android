package com.arcadiaprep.app.arca.mo;
/**
 * Corresponding to table iphone_ex_app_pages
 * 
 * @author lala
 * 
 */
public class ExAppsPages {
	private int id;
	private int exAppID;
	private String content;
	private int idx;
	private String date;
	private int solving;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content!=null)
			this.content = content.replace("\\", "");
		else
			this.content = null;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSolving() {
		return solving;
	}

	public void setSolving(int solving) {
		this.solving = solving;
	}

}
