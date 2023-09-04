package com.dynamite.framework;

import java.util.Properties;
import java.io.*;

/**
 * 
 * Singleon class that enapsulates user defined propeties for Mobile and Web platform defined 
 * in properties files of the framework
 *  
 * @author Subhash Perumalsamy
 *
 */
public class UserProperties {

	private static  Properties globalProperties;
	private static  Properties MobileProperties;	
	
	private UserProperties(){
		
	}


	 public static Properties getGlobalPropeties() throws FrameworkException {
		if(globalProperties == null)
		{
			globalProperties = loadGlobalProperties();
		}
			
		return 	globalProperties;		
		
	}
	
	
	
	public static Properties getMobilePropeties() throws FrameworkException {
		if(MobileProperties == null)
		{
			MobileProperties = loadMobileProperties();
		}
			
		return 	MobileProperties;		
		
	}
	
	
	
	
	
	private static Properties loadGlobalProperties() throws FrameworkException  {
		
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if(frameworkParameters.getRelativePath()== null) {
			throw new FrameworkException("FrameworkParameters. Relative Path is not Set");
		}
		
		
		Properties properties  = new Properties();
		String userDefPropFolder = frameworkParameters.getRelativePath()+ Utility.getFileSeperator()
									+ "src" + Utility.getFileSeperator()
									+ "main" + Utility.getFileSeperator()
									+ "resources" + Utility.getFileSeperator()
									+ Utility.getFileSeperator();
		
		File webConfigFile = new File(userDefPropFolder + "UserDefinedProperties.properties");
		
		
		try {
			properties.load(new FileInputStream(webConfigFile));
		} catch (FileNotFoundException e) {
			throw new FrameworkException("FileNotFoundException while loading UserDefinedProprties.properties File at the Location: "+userDefPropFolder);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new FrameworkException(
					"IOException while loading UserDefinedProprties file");
			
		}
		
		
									
		return properties;
		
	}
	
	
	private static Properties loadMobileProperties() throws FrameworkException  {
		
		Properties properties = new Properties();
		
		
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if(frameworkParameters.getRelativePath()== null) {
			throw new FrameworkException(
					"FrameworkParameters. Relative Path is not Set");
		}
		
		
		String mobilePropertiesFolder = frameworkParameters.getRelativePath()+
										Utility.getFileSeperator()+"src"+
										Utility.getFileSeperator()+"main"+
										Utility.getFileSeperator()+"resoucres"+
										Utility.getFileSeperator();
										
		
		
		File mobilePropertiesFile = new File(mobilePropertiesFolder+"mobileProperties.properties");
		
		try {
			properties.load(new FileInputStream(mobilePropertiesFile));
		} catch (FileNotFoundException e) {
			throw new FrameworkException(
					"FileNotFoundException when loading mobileProperties file");
		} catch (IOException e) {
			throw new FrameworkException(
					"IOException when loading mobileproperties file");
		}
		return properties;
	}
	
	
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
