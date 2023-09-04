package com.dynmaite.framework.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dynamite.frameworkSelenium.Browser;
import com.dynamite.frameworkSelenium.ExecutionMode;
import com.dynamite.frameworkSelenium.SeleniumTestParameters;
import com.dynamite.frameworkSelenium.WebDriverFactory;
import com.dynamite.framework.DriverScript;
import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.FrameworkParameters;
import com.dynamite.frameworkSelenium.DynamiteDriver;

public class DriverScript_Test  {

	
	FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	DynamiteDriver driver ;

	@Test
	public void openBrowser() throws FrameworkException {
	
		frameworkParameters.setRelativePath();
		WebDriver webDriver = WebDriverFactory.getWebDriver(Browser.CHROME);
		 driver = new DynamiteDriver(webDriver);
		driver.get("https://www.google.com");
		Assert.assertEquals("Google", driver.getTitle());
		driver.quit();
		
	}
	
	
	
	@After
	public void quitDriver() {
		driver.quit();
	}
	

}
