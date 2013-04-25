package com.arcadiaprep.app.arca.util;

public class ExamUtil {
	public static String convertChoice(String c){
		if(c.equals("-1"))return "--";
		String rs ="";
		char[] cs =  c.toCharArray();
		for(int i=0;i<cs.length;i++){
			if(cs[i]=='0')rs+="A,";
			else if(cs[i]=='1')rs+="B,";
			else if(cs[i]=='2')rs+="C,";
			else if(cs[i]=='3')rs+="D,";
			else if(cs[i]=='4')rs+="E,";
			else if(cs[i]=='5')rs+="F,";
			else if(cs[i]=='6')rs+="G,";
		}
		if(rs.equals(""))
			return "--";
		else
			return rs.substring(0,rs.lastIndexOf(','));
	}
}	
