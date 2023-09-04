package com.dynamite.framework;

import java.io.File;
import java.util.*;

import com.dynamite.frameworkSelenium.SeleniumTestParameters;

public class Report {
	
	private static final String EXCEL_RESULTS = "Excel Results";
	private static final String HTML_RESULTS = "HTML Results";
	private static final String SCREENSHOTS = "Screenshots";
	private static final String PERFECTO_RESULTS = "Perfecto Results";
	private static final String DOWNLOADS = "Downloads";
	
	
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	
	private int stepNumber;
	private int nStepsPassed, nStepsFailed;
	private int nTestsPassed, nTestsFailed;
	
	private String testStatus;
	private String failureDescription;
	private List<ReportType> reportTypes = new ArrayList<ReportType> ();
	
	
	private final SeleniumTestParameters testParameters; 
	public Report(ReportSettings reportSettings, ReportTheme reportTheme,SeleniumTestParameters testParameters) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		this.testParameters=testParameters;

		nStepsPassed = 0;
		nStepsFailed = 0;
		testStatus = "Passed";
	}
	
	public String getTestStatus() {
		return testStatus;
	}
	
	public String getFailureDescription() {
		return failureDescription;
	}
	
	
	public void initialize() {
	
		if(reportSettings.shouldGenerateHtmlReports()) {
			
			new File(reportSettings.getReportPath()+Utility.getFileSeperator()+"HTML_RESULTS").mkdir();
			HtmlReport htmlReport = new HtmlReport(reportSettings,reportTheme);
			reportTypes.add(htmlReport);	
			
		}
		
		new File(reportSettings.getReportPath()+Utility.getFileSeperator()+"HTML_RESULTS").mkdir();
		
		new File(reportSettings.getReportPath()+Utility.getFileSeperator()+"Downloads").mkdir();
		
		
		
	}

	
	
}
