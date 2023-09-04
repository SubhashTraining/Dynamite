package com.dynamite.frameworkSelenium;



import com.dynamite.framework.TestParameters;

public class SeleniumTestParameters extends TestParameters {
	
	private Browser browser;
	private String platform;
	private String browserVersion;
	private ExecutionMode executionMode;
	
	

	public ExecutionMode getExecutionMode() {
		return executionMode;
	}

	

	public void setExecutionMode(ExecutionMode executionMode) {
		this.executionMode = executionMode;
	}



	public SeleniumTestParameters(String currentScenario, String currentTestCase) {
		super(currentScenario, currentTestCase);
		// TODO Auto-generated constructor stub
	}



	public Browser getBrowser() {
		return browser;
	}



	public void setBrowser(Browser browser) {
		this.browser = browser;
	}



	public String getPlatform() {
		return platform;
	}



	public void setPlatform(String platform) {
		this.platform = platform;
	}



	public String getBrowserVersion() {
		return browserVersion;
	}



	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

}
