package com.dynamite.frameworkSelenium;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DynamiteDriver implements WebDriver{
	
	private WebDriver driver;
	private SeleniumTestParameters seleniumTestParamenters;
	
	
	public DynamiteDriver(WebDriver driver) {
		this.driver= driver;
	}
	
	
	public WebDriver getDriver() {
		return driver;
	}

	

	public SeleniumTestParameters getSeleniumTestParamenters() {
		return seleniumTestParamenters;
	}

	public void setSeleniumTestParamenters(SeleniumTestParameters seleniumTestParamenters) {
		this.seleniumTestParamenters = seleniumTestParamenters;
	}

	@Override
	public void get(String url) {
		// TODO Auto-generated method stub
		driver.get(url);
		
	}

	@Override
	public String getCurrentUrl() {
		// TODO Auto-generated method stub
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		// TODO Auto-generated method stub
		return driver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		// TODO Auto-generated method stub
		return driver.findElement(by);
	}

	@Override
	public String getPageSource() {
		// TODO Auto-generated method stub
		return driver.getPageSource();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		driver.close();
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		driver.quit();
		
	}

	@Override
	public Set<String> getWindowHandles() {
		// TODO Auto-generated method stub
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		// TODO Auto-generated method stub
		return driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		// TODO Auto-generated method stub
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		// TODO Auto-generated method stub
		return driver.navigate();
	}

	@Override
	public Options manage() {
		// TODO Auto-generated method stub
		return driver.manage();
	}

}
