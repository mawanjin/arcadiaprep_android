package com.arcadiaprep.app.arca.vo;

public class CategoryBarVO {
	private String secName;
	private String secNameShort;
	
	public CategoryBarVO(){}

	public CategoryBarVO(String secName, String secNameShort) {
		super();
		this.secName = secName;
		this.secNameShort = secNameShort;
	}

	public String getSecName() {
		return secName;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}

	public String getSecNameShort() {
		return secNameShort;
	}

	public void setSecNameShort(String secNameShort) {
		this.secNameShort = secNameShort;
	}

}
