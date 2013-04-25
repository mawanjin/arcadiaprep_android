package com.arcadiaprep.app.arca.mo;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.arcadiaprep.app.util.EncryptionUtil;

/**
 * Corresponding to table iphone_questions
 * 
 * @author lala
 * 
 */
public class Questions {
	private int id;
	private int exAppID;
	private String name;
	private int difficulty;
	// passage
	private String textBlock1A;
	// hint
	private String textBlock1B;
	private String textBlock1C;
	private String textBlock1D;
	private String image;
	// the question for this problem
	private String questionStemA;
	private String questionStemB;
	private String questionStemC;
	private String questionStemD;

	// a b c d e
	private String answer1A;
	private String answer2A;
	private String answer3A;
	private String answer4A;
	private String answer5A;
	private String answer6A;

	private String solution;
	// the explanation for each choice
	private String solutionText;
	private String solutionText1;
	private String solutionText2;
	private String solutionText3;
	private String solutionText4;
	private String solutionText5;

	private String date;
	private int idx;
	private int ptId;
	private int ptSection;
	private int ptQid;
	private int ptGroupFirst;

	private String video1;
	private String video2;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getTextBlock1A() {
		return textBlock1A;
	}

	public void setTextBlock1A(String textBlock1A) {

		String result;
		String code;
		
		code = "a"+1+"r"+5+"c"+0+"p?";
		try {
			result = EncryptionUtil.dencrypt(textBlock1A, false, code);
		}
		catch (Exception e) {
            e.printStackTrace();	
            this.textBlock1A = "";
            return;
		}
		if (result == null)
			this.textBlock1A = "";
		else
			this.textBlock1A = result.replaceAll("[\\r\\n]", " ");
	}

	public String getTextBlock1B() {
		return textBlock1B;
	}

	public void setTextBlock1B(String textBlock1B) {
		this.textBlock1B = textBlock1B;
	}

	public String getTextBlock1C() {
		return textBlock1C;
	}

	public void setTextBlock1C(String textBlock1C) {
		this.textBlock1C = textBlock1C;
	}

	public String getTextBlock1D() {
		return textBlock1D;
	}

	public void setTextBlock1D(String textBlock1D) {
		this.textBlock1D = textBlock1D;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getQuestionStemA() {
		return questionStemA;
	}

	public void setQuestionStemA(String questionStemA) {
//		this.questionStemA = questionStemA;
		
		String result;
		try {
			String code;
			
			code = "a"+1+"r"+5+"c"+0+"p?";
			result = EncryptionUtil.dencrypt(questionStemA, false, code);
		}
		catch (Exception e) {
            e.printStackTrace();	
            this.questionStemA = "";
            return;
		}
		//this.questionStemA = result.substring(0,53);
		this.questionStemA = result.replaceAll("[\\r\\n]", " ");
		//Log.d("My Test","Length:" + (this.questionStemA.length()));
		//Log.d("My Test","StemA: " + this.questionStemA);

		
	}

	public String getQuestionStemB() {
		return questionStemB;
	}

	public void setQuestionStemB(String questionStemB) {
		this.questionStemB = questionStemB;
	}

	public String getQuestionStemC() {
		return questionStemC;
	}

	public void setQuestionStemC(String questionStemC) {
		this.questionStemC = questionStemC;
	}

	public String getQuestionStemD() {
		return questionStemD;
	}

	public void setQuestionStemD(String questionStemD) {
		this.questionStemD = questionStemD;
	}

	public String getAnswer1A() {
		return answer1A;
	}

	public void setAnswer1A(String answer1a) {
		answer1A = answer1a;
	}

	public String getAnswer2A() {
		return answer2A;
	}

	public void setAnswer2A(String answer2a) {
		answer2A = answer2a;
	}

	public String getAnswer3A() {
		return answer3A;
	}

	public void setAnswer3A(String answer3a) {
		answer3A = answer3a;
	}

	public String getAnswer4A() {
		return answer4A;
	}

	public void setAnswer4A(String answer4a) {
		answer4A = answer4a;
	}

	public String getAnswer5A() {
		return answer5A;
	}

	public void setAnswer5A(String answer5a) {
		answer5A = answer5a;
	}

	public String getAnswer6A() {
		return answer6A;
	}

	public void setAnswer6A(String answer6a) {
		answer6A = answer6a;
	}

	public String getSolution() {
		String rs="";
		if(solution.length()>0){
			for(int i=0;i<solution.length();i++){
				if(Character.toLowerCase(solution.charAt(i))=='a'){
					rs+="0";
				}else if(Character.toLowerCase(solution.charAt(i))=='b'){
					rs+="1";
				}else if(Character.toLowerCase(solution.charAt(i))=='c'){
					rs+="2";
				}else if(Character.toLowerCase(solution.charAt(i))=='d'){
					rs+="3";
				}else if(Character.toLowerCase(solution.charAt(i))=='e'){
					rs+="4";
				}else if(Character.toLowerCase(solution.charAt(i))=='f'){
					rs+="5";
				}else if(Character.toLowerCase(solution.charAt(i))=='g'){
					rs+="6";
				}else if(Character.toLowerCase(solution.charAt(i))=='h'){
					rs+="7";
				}
			}
		}
		
		return rs;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getSolutionText() {
		return solutionText;
	}

	public void setSolutionText(String solutionText) {
		if (solutionText != null)
			this.solutionText = solutionText;
		else
			this.solutionText = "";
	}

	public String getSolutionText1() {
		return solutionText1;
	}

	public void setSolutionText1(String solutionText1) {
		this.solutionText1 = solutionText1;
	}

	public String getSolutionText2() {
		return solutionText2;
	}

	public void setSolutionText2(String solutionText2) {
		this.solutionText2 = solutionText2;
	}

	public String getSolutionText3() {
		return solutionText3;
	}

	public void setSolutionText3(String solutionText3) {
		this.solutionText3 = solutionText3;
	}

	public String getSolutionText4() {
		return solutionText4;
	}

	public void setSolutionText4(String solutionText4) {
		this.solutionText4 = solutionText4;
	}

	public String getSolutionText5() {
		return solutionText5;
	}

	public void setSolutionText5(String solutionText5) {
		this.solutionText5 = solutionText5;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getPtId() {
		return ptId;
	}

	public void setPtId(int ptId) {
		this.ptId = ptId;
	}

	public int getPtSection() {
		return ptSection;
	}

	public void setPtSection(int ptSection) {
		this.ptSection = ptSection;
	}

	public int getPtQid() {
		return ptQid;
	}

	public void setPtQid(int ptQid) {
		this.ptQid = ptQid;
	}

	public int getPtGroupFirst() {
		return ptGroupFirst;
	}

	public void setPtGroupFirst(int ptGroupFirst) {
		this.ptGroupFirst = ptGroupFirst;
	}

	public String getVideo1() {
		return video1;
	}

	public void setVideo1(String video1) {
		this.video1 = video1;
	}

	public String getVideo2() {
		return video2;
	}

	public void setVideo2(String video2) {
		this.video2 = video2;
	}

	public String[] getAnswerLetters() {
		List<String> a = new ArrayList<String>();
		if (getAnswer1A() != null && !getAnswer1A().equals("")) {
			a.add("A");
		}
		if (getAnswer2A() != null && !getAnswer2A().equals("")) {
			a.add("B");
		}
		if (getAnswer3A() != null && !getAnswer3A().equals("")) {
			a.add("C");
		}
		if (getAnswer4A() != null && !getAnswer4A().equals("")) {
			a.add("D");
		}
		if (getAnswer5A() != null && !getAnswer5A().equals("")) {
			a.add("E");
		}
		if (getAnswer6A() != null && !getAnswer6A().equals("")) {
			a.add("F");
		}
		return a.toArray(new String[a.size()]);
		
	}

	public String getAnswers() {
		String a, b, c, d, e, f;
		String a2,b2,c2,d2,e2,f2;
		String rs = "[";

		if (getAnswer1A() != null && !getAnswer1A().equals("")) {
			a = getAnswer1A().replaceAll("\'", "\\\\'");
		} else {
			return rs += "]";
		}
		
		if (getSolutionText1() != null && !getSolutionText1().equals("")) {
			a2 = getSolutionText1().replaceAll("\'", "\\\\'");
		} else {
			a2 = "";
		}
		rs += "{text:'" + a + "',tip:'" + a2 + "'},";

		if (getAnswer2A() != null && !getAnswer2A().equals("")) {
			b = getAnswer2A().replaceAll("\'", "\\\\'");
		} else {
			b = "";
			rs = rs.substring(0, rs.length() - 1);
			return rs += "]";
		}
		
		if (getSolutionText2() != null && !getSolutionText2().equals("")) {
			b2 = getSolutionText2().replaceAll("\'", "\\\\'");
		} else {
			b2 = "";
		}

		rs += "{text:'" + b + "',tip:'" + b2 + "'},";

		if (getAnswer3A() != null && !getAnswer3A().equals("")) {
			c = getAnswer3A().replaceAll("\'", "\\\\'");
		} else {
			rs = rs.substring(0, rs.length() - 1);
			return rs += "]";
		}
		
		if (getSolutionText3() != null && !getSolutionText3().equals("")) {
			c2 = getSolutionText3().replaceAll("\'", "\\\\'");
		} else {
			c2 = "";
		}
		rs += "{text:'" + c + "',tip:'" + c2 + "'},";

		if (getAnswer4A() != null && !getAnswer4A().equals("")) {
			d = getAnswer4A().replaceAll("\'", "\\\\'");
		} else {
			rs = rs.substring(0, rs.length() - 1);
			return rs += "]";
		}
		if (getSolutionText4() != null && !getSolutionText4().equals("")) {
			d2 = getSolutionText4().replaceAll("\'", "\\\\'");
		} else {
			d2 = "";
		}
		rs += "{text:'" + d + "',tip:'" + d2 + "'},";

		if (getAnswer5A() != null && !getAnswer5A().equals("")) {
			e = getAnswer5A().replaceAll("\'", "\\\\'");
		} else {
			rs = rs.substring(0, rs.length() - 1);
			return rs += "]";
		}
		if (getSolutionText5() != null && !getSolutionText5().equals("")) {
			e2 = getSolutionText5().replaceAll("\'", "\\\\'");
		} else {
			e2 = "";
		}
		rs += "{text:'" + e + "',tip:'" + e2 + "'},";

		if (getAnswer6A() != null && !getAnswer6A().equals("")) {
			f = getAnswer6A().replaceAll("\'", "\\\\'");
		} else {
			rs = rs.substring(0, rs.length() - 1);
			return rs += "]";
		}
		

		f2 = "";
		rs += "{text:'" + f + "',tip:'" + f2 + "'}";

		return rs += "]";

	}

	public int getHintCount() {
		int c = 0;
		if (getTextBlock1B() != null && !getTextBlock1B().equals(""))
			c++;
		if (getTextBlock1C() != null && !getTextBlock1C().equals(""))
			c++;
		if (getTextBlock1D() != null && !getTextBlock1D().equals(""))
			c++;
		return c;
	}

}
