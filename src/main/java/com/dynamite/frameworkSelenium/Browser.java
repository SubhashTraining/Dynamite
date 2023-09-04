package com.dynamite.frameworkSelenium;

public enum Browser {
	
	CHROME("chrome"),
	INTERNET_EXPLORER("internet explorer"),
	FIREFOR("firefox"),
	EDGE("edge"),
	OPERA("opera"),
	SAFARI("safari");	
	
	private String value;
	Browser(String value){
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}

}
