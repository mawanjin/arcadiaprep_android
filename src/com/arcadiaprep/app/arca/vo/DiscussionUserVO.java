package com.arcadiaprep.app.arca.vo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DiscussionUserVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1201203560002463994L;
	
	private int userID;
	
	private String uname;
	private String extra;
	private String location;
	private String aboutme;
	private String post;
	private String imgAttach;
	private String head;
//	private Bitmap imgAttach;
//	private Bitmap head;
	

	public int getUserID() {
		return userID;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

//	public Bitmap getHead() {
//		return head;
//	}
//
//	public void setHead(Bitmap head) {
//		this.head = head;
//	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAboutme() {
		return aboutme;
	}

	public void setAboutme(String aboutme) {
		this.aboutme = aboutme;
	}

//	public Bitmap getImgAttach() {
//		return imgAttach;
//	}
//
//	public void setImgAttach(Bitmap imgAttach) {
//		this.imgAttach = imgAttach;
//	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getImgAttach() {
		return imgAttach;
	}

	public void setImgAttach(String imgAttach) {
		this.imgAttach = imgAttach;
	}
	
	

}
