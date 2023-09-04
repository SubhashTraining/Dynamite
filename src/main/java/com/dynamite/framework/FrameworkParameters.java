package com.dynamite.framework;

/**
 * Singleton class that encapsulates framework level global parameters 
 * 
 */

import java.io.*;

public class FrameworkParameters {
	
	private String relativePath;
	private String runConfiguration;
	private boolean stopExecution;
	
	public static final FrameworkParameters FRAMEWORK_PARAMETERS = new FrameworkParameters();
	
	private FrameworkParameters() {
		//To prevent external initialization of the class
	}
	
	
	/**
	 * Function to return singleton Instance of {@FrameworkParameters} object 
	 * return Instance of {@link FrameworkParameters } object
	 */	
	public static FrameworkParameters getInstance() {
		return FRAMEWORK_PARAMETERS;		
	}

	/**
	 * Function to get the Relative Path of the framework (to be used as relative path)
	 * @return
	 */
	public String getRelativePath() {
		return relativePath;
	}


	public void setRelativePath() {
		
		this.relativePath= new File(System.getProperty("user.dir")).getAbsolutePath();		
	}


	public String getRunConfiguration() {
		return runConfiguration;
	}


	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}


	public boolean getStopExecution() {
		return stopExecution;
	}


	public void setStopExecution(boolean stopExecution) {
		this.stopExecution = stopExecution;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	

}
