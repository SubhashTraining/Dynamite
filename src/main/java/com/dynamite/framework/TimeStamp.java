package com.dynamite.framework;

import java.io.File;
import java.util.Properties;

public class TimeStamp {
	
	
	private static volatile String reportPathWithTimeStamp;
	private static volatile String oldReportPathWithTimeStamp;
	
	private TimeStamp() {
		
	}
	
	
	public static String getInstance() throws FrameworkException {
		
		if(reportPathWithTimeStamp== null)
		{
			synchronized(TimeStamp.class)
			{
				if(reportPathWithTimeStamp ==null) {//double check
					
					FrameworkParameters parameters = FrameworkParameters.getInstance();
					if(parameters.getRelativePath()==null)
						throw new FrameworkException(
								"FrameworkParameters.relativePath() is not set");
					
					if(parameters.getRunConfiguration()==null)
						throw new FrameworkException(
								"FrameworkParameters.runConfiguration() is not set");
					
					Properties properties = UserProperties.getGlobalPropeties();
					String timeFormat = properties.getProperty("DateFormatString").replace(" ","_").replace(":","-");
					String timeStamp = "Run_"+Utility.getCurrentFormattedTimeStamp(timeFormat);
					
					reportPathWithTimeStamp = parameters.getRelativePath()+
											  Utility.getFileSeperator()+"Results"+
											  Utility.getFileSeperator()+parameters.getRunConfiguration()+
											  Utility.getFileSeperator()+timeStamp;			
					
					new File(reportPathWithTimeStamp).mkdirs();
				}				
			}		
		}	
		
		return reportPathWithTimeStamp;
	}
	
	
	public static String getOldReportInstance() throws FrameworkException {
		
		if(oldReportPathWithTimeStamp== null) {
			synchronized(TimeStamp.class) {
				
				if(oldReportPathWithTimeStamp== null) {
					String timeFormat = UserProperties.getGlobalPropeties().getProperty("DateFormatString").replace(" ","_").replace(":","-");
					String timeStamp = "Run_"+ Utility.getCurrentFormattedTimeStamp(timeFormat);
					oldReportPathWithTimeStamp = Utility.getLastBuildResults()+Utility.getFileSeperator()
												 +timeStamp;
					new File(oldReportPathWithTimeStamp).mkdirs();
					
				}
			}
			
		}
		return oldReportPathWithTimeStamp;		
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	

	

}
