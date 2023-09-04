package com.dynamite.frameworkSelenium;

import java.util.Properties;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.UserProperties;
import com.dynamite.framework.Utility;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {
	
	
	
	private WebDriverFactory() {
		
	}
	
	public static WebDriver getWebDriver(Browser browser) throws FrameworkException {
		
		WebDriver driver  = null;	
		Properties properties = UserProperties.getGlobalPropeties();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		File file = new File(System.getProperty("user.dir")+Utility.getFileSeperator()
							 + "src" + Utility.getFileSeperator()
							 + "test" + Utility.getFileSeperator()
							 + "resources"+Utility.getFileSeperator()
							 + "Settings.properties");
		
		Properties browserPref = new Properties();
		
		try {
			FileInputStream oFile = new FileInputStream(file);
			try {
				browserPref.load(oFile);
			} catch (IOException e) {
				throw new FrameworkException("Error loading the properties File Settings.properties in at, " +
					file.getAbsolutePath());
			}
		} catch (FileNotFoundException e) {
			throw new FrameworkException("Could Not find the properties file Settings.properties at , " +
					file.getAbsolutePath());
		}
		
		
		
		
		switch(browser)
		{
		case CHROME:
		
			
		File chromeDriverFile = new File(System.getProperty("user.dir")+Utility.getFileSeperator()+
				 "Drivers"+ Utility.getFileSeperator()+
				 "chromedriver.exe");
		
		if (chromeDriverFile.exists())
		{	
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+Utility.getFileSeperator()+
													 "Drivers"+ Utility.getFileSeperator()+
													 "chromedriver.exe");
		}
		else {
			WebDriverManager.chromedriver().setup();
		}
		String downloadPath = browserPref.getProperty("filepath")+Utility.getFileSeperator()+
							  "Downloads";
							  
		Map<String,Object> chromePrefs = new HashMap<String,Object>();
		
		ChromeOptions options = new ChromeOptions();
		
		options.setAcceptInsecureCerts(true);
		if(proxyRequired)
		{
			String proxySettings = properties.getProperty("proxy");
			Proxy proxy = new Proxy();
			proxy.setHttpProxy(proxySettings);
			options.setProxy(proxy);
		}
		
		driver = new ChromeDriver(options);		
		}
		
		
		
		return driver;
	}
	
}
