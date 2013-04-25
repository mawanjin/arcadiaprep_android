package com.arcadiaprep.app.arca.vo;

import android.graphics.Bitmap;

public class ImageVO {
	private Bitmap bitMap;
	private String path;
	
	

	public ImageVO(Bitmap bitMap, String path) {
		super();
		this.bitMap = bitMap;
		this.path = path;
	}

	public Bitmap getBitMap() {
		return bitMap;
	}

	public void setBitMap(Bitmap bitMap) {
		this.bitMap = bitMap;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
