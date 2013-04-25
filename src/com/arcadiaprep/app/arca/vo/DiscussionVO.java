package com.arcadiaprep.app.arca.vo;


public class DiscussionVO{

	private int id;
	private int discusstionId;
	private int userId;
	private String subject;
	private String message;
	private String date;
	private int repliedTo;
	private int pseudonym;
	private String cid;
	private int profile;
	private String profilePic;
	private String uname;
	private String member;
	private String attachIamge;
	private int questionId;
	
	
	

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiscusstionId() {
		return discusstionId;
	}

	public void setDiscusstionId(int discusstionId) {
		this.discusstionId = discusstionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRepliedTo() {
		return repliedTo;
	}

	public void setRepliedTo(int repliedTo) {
		this.repliedTo = repliedTo;
	}

	public int getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(int pseudonym) {
		this.pseudonym = pseudonym;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public int getProfile() {
		return profile;
	}

	public void setProfile(int profile) {
		this.profile = profile;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getAttachIamge() {
		return attachIamge;
	}

	public void setAttachIamge(String attachIamge) {
		this.attachIamge = attachIamge;
	}
	
	

}
