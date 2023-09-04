package com.dynamite.framework;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Utility {

	private Utility() {
		
	}
	
	
	public  static String getFileSeperator() {
		
		return System.getProperty("file.separator");
	}
	
	
	public static Date getCurrentTime() {		
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();		
	}
	
	
	public static String getCurrentFormattedTimeStamp(String formatPattern) {
		
	 LocalDateTime now = LocalDateTime.now();
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
	 return formatter.format(now);		
	}
	
	
	public static String getAbsolutePath() {
		return new File(System.getProperty("user.dir")).getAbsolutePath();
	}
	
	
	public static String getLastBuildResults() {
		
		File lastBuildResult = new File(getAbsolutePath()+Utility.getFileSeperator()+"LastBuildResults");
		if(!lastBuildResult.isDirectory()) 
			lastBuildResult.mkdirs();
		
		return lastBuildResult.getAbsolutePath();	
	}
	
	public static String getTimeDifferenc(LocalDateTime startTime, LocalDateTime endTime) {
		
		LocalDateTime tempDateTime = LocalDateTime.from(startTime);
		
		long minutes = (tempDateTime.until(endTime, ChronoUnit.MINUTES)%60);
		long hours =   tempDateTime.until(endTime, ChronoUnit.HOURS);
		long seconds = (tempDateTime.until(endTime, ChronoUnit.SECONDS)/1000)%120;
		
		if(hours >0) 
			return Long.toString(hours)+ " hour(s), " + Long.toString(minutes)+" minute(s), " +Long.toString(seconds)+" second(s)"; 
		else
			return   Long.toString(minutes)+" minute(s), " +Long.toString(seconds)+" second(s)";
		
	}
	
	
	
	
}
