package com.arcadiaprep.app.arca.vo;

import java.util.ArrayList;
import java.util.List;

public class ProblemPackageVO {

	// Attributes
	private MinItemVO evaluationAndImprovementPage;
	private MinItemVO identifyingAndSolvingPage;
	private MinItemVO aboutPage;
	private String difficulty;
	private String price;
	private String icon;
	// Data
	private String packageSetName;
	private List<String> ProblemSets = new ArrayList<String>();

	// Name
	private String problemSetName;
	private String examSectionName;
	private String examName;

	public MinItemVO getEvaluationAndImprovementPage() {
		return evaluationAndImprovementPage;
	}

	public void setEvaluationAndImprovementPage(
			MinItemVO evaluationAndImprovementPage) {
		this.evaluationAndImprovementPage = evaluationAndImprovementPage;
	}

	public MinItemVO getIdentifyingAndSolvingPage() {
		return identifyingAndSolvingPage;
	}

	public void setIdentifyingAndSolvingPage(MinItemVO identifyingAndSolvingPage) {
		this.identifyingAndSolvingPage = identifyingAndSolvingPage;
	}

	public MinItemVO getAboutPage() {
		return aboutPage;
	}

	public void setAboutPage(MinItemVO aboutPage) {
		this.aboutPage = aboutPage;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPackageSetName() {
		return packageSetName;
	}

	public void setPackageSetName(String packageSetName) {
		this.packageSetName = packageSetName;
	}

	public List<String> getProblemSets() {
		return ProblemSets;
	}

	public void setProblemSets(List<String> problemSets) {
		ProblemSets = problemSets;
	}

	public String getProblemSetName() {
		return problemSetName;
	}

	public void setProblemSetName(String problemSetName) {
		this.problemSetName = problemSetName;
	}

	public String getExamSectionName() {
		return examSectionName;
	}

	public void setExamSectionName(String examSectionName) {
		this.examSectionName = examSectionName;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

}
