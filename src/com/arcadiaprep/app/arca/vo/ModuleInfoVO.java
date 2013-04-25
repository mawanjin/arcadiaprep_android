package com.arcadiaprep.app.arca.vo;

import java.util.ArrayList;
import java.util.List;

public class ModuleInfoVO {
	private String examType;
	private List<ExamSection> examSection = new ArrayList<ExamSection>();
	private String resetPassURL;
	private String serverURL;
	private String secureServerURL;
	private String appCode;
	private String websiteName;
	private String websiteURL;
	private String introPage;
	private String marketGroupId;
	private boolean developerFlag = false;

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public List<ExamSection> getExamSection() {
		return examSection;
	}

	public void setExamSection(List<ExamSection> examSection) {
		this.examSection = examSection;
	}

	public String getResetPassURL() {
		return resetPassURL;
	}

	public void setResetPassURL(String resetPassURL) {
		this.resetPassURL = resetPassURL;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getSecureServerURL() {
		return secureServerURL;
	}

	public void setSecureServerURL(String secureServerURL) {
		this.secureServerURL = secureServerURL;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	public String getIntroPage() {
		return introPage;
	}

	public void setIntroPage(String introPage) {
		this.introPage = introPage;
	}

    public String getMarketGroupId() {
        return marketGroupId;
    }

    public void setMarketGroupId(String groupId) {
        this.marketGroupId = groupId;
    }

    public boolean getDeveloperFlag() {
    	return this.developerFlag;
    }
    public void setDeveloperFlag(String flag) {
    	if (flag != null && flag.equalsIgnoreCase("Yes"))
    		this.developerFlag = true;
    	else 
    		this.developerFlag = false;
    }
}
