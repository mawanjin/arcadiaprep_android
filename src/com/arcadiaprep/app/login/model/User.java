
package com.arcadiaprep.app.login.model;

public class User {

	private String userName;
	private String password;
	private int profile;

	public User(String userName, String userPassword, int profile) {
		this.userName = userName;
		this.password = userPassword;
		this.profile = profile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String userPassword) {
		this.password = userPassword;
	}

	public int getProfile() {
		return profile;
	}

	public void setProfile(int profile) {
		this.profile = profile;
	}

}
