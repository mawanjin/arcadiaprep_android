package com.arcadiaprep.app.arca.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeUtil {

	/**
	 * convert second to "HH:mm:ss"
	 * 
	 * @param millis
	 * @return
	 */
	public static String getTime(int second) {
		
		int min = (second / 60);
		int sec = (second % 60);
		int hour = (min / 60);
		return String.format("%1$02d:%2$02d:%3$02d", hour,min, sec);
		
	}
	
	public static String getTimeFromMills(String mills){
		
		Calendar c = Calendar.getInstance();
		try{
			c.setTimeInMillis(Long.parseLong(mills));	
		}catch(Exception e){
			return mills;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}
}
