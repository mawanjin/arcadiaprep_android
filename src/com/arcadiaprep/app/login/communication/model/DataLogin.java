
package com.arcadiaprep.app.login.communication.model;

public class DataLogin {

	private String userName;
	private String userPassword;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public DataLogin(String userName, String userPassword) {
		this.userName = userName;
		this.userPassword = userPassword;
	}

}
