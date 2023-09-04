package com.dynamite.framework;

import com.dynamite.frameworkSelenium.SeleniumTestParameters;
import com.dynamite.frameworkSelenium.WebDriverFactory;
import com.dynamite.frameworkSelenium.DynamiteDriver;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;

import com.dynamite.frameworkSelenium.ExecutionMode;
public class DriverScript {
	
	private SeleniumTestParameters seleniumTestParameters;
	private int currentIteration,currentSubIteration;
	private Date startTime, endTime;
	private String executionTime;
	private Properties properties;
	protected DynamiteDriver driver;
	private final FrameworkParameters parameters = FrameworkParameters.getInstance();
	private boolean linkScreenShotToTestLog = true;
	private String reportPath;
	private ReportSettings reportSetting;
	private DataTable dataTable;
	private List<String> businessFlowData;
	
	
	public DriverScript(SeleniumTestParameters seleniumTestParameters) {
		this.seleniumTestParameters = seleniumTestParameters;
	}
	
	
	public void executeDriverScript() throws FrameworkException
	{
		
		startUp();
		initializeTestIterations();
		initializeWebDriver();
		initializeTestReport();
	}
	
	
	private void startUp() throws FrameworkException {
		
		startTime = Utility.getCurrentTime();
		properties = UserProperties.getGlobalPropeties();
		
	}
	
	private void initializeTestIterations() throws FrameworkException {
		
		IterationMode iteration = seleniumTestParameters.getIterationMode();
		switch(iteration) {
		
		case RUN_ALL_ITERATIONS:
			int nIterations = getNumberOfIterations();
			seleniumTestParameters.setEndIteration(nIterations);
			currentIteration =1;
			break;
		case RUN_ONE_ITERATIONS_ONLY:
			
			currentIteration =1;
			break;
		case RUN_RANGE_OF_ITERATIONS:
			if (seleniumTestParameters.getStartIteration() > seleniumTestParameters
					.getEndIteration()) {
				throw new FrameworkException("Error",
						"StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = seleniumTestParameters.getStartIteration();
			break;
		default:
			throw new FrameworkException("Unhandled Iteration Mode!");
		
		}
		
		
	}
	
	
	private int getNumberOfIterations() throws FrameworkException  {
		
		String dataTablePath = FrameworkParameters.getInstance().getRelativePath()+ Utility.getFileSeperator()+
							   "src" + Utility.getFileSeperator()+
							   "resources" + Utility.getFileSeperator()+
							   "DataTables"+Utility.getFileSeperator();
		
		String fileName = seleniumTestParameters.getCurrentScenario()+".xls";
		
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(dataTablePath, fileName);
		dataAccess.setDataSheet(properties.getProperty("DefaultDataSheet"));
		int startRowNo = dataAccess.getRowNo(seleniumTestParameters.getCurrentTestCase(),0);
		
		int noOfIterations=0;
		int nTestCaseRows= dataAccess.getRowCount(seleniumTestParameters.getCurrentTestCase(), 0, startRowNo);
		for(int currentRow = startRowNo; currentRow<= nTestCaseRows;currentRow++ )
		{
			if(dataAccess.getValue(currentRow,0).equalsIgnoreCase(seleniumTestParameters.getCurrentTestCase())
					&& dataAccess.getValue(startRowNo,1).equals("1")){
				noOfIterations++;
			}
		}		
		return noOfIterations;
			
	}
	
	
	protected void initializeWebDriver() throws FrameworkException {
		
		switch(seleniumTestParameters.getExecutionMode()) {
		
		case LOCAL :
			WebDriver webdriver = WebDriverFactory.getWebDriver(seleniumTestParameters.getBrowser());
			driver = new DynamiteDriver(webdriver);
			driver.setSeleniumTestParamenters(seleniumTestParameters);		
			waitPageToLoad();
		break;	
		
		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		
		}
		implicitWaitForDriver();
	}
		
	
	private void implicitWaitForDriver() {
		
		Long ObjectSyncTimeout = Long.parseLong(properties.getProperty("ObjectSyncTimeout").toString());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ObjectSyncTimeout));
		
	}
	
	private void waitPageToLoad() {
		
		Long PageLoadTimeout = Long.parseLong(properties.getProperty("PageLoadTimeout").toString());		
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PageLoadTimeout));
		driver.manage().window().maximize();
		
	}
	
	
	private void initializeTestReport() throws FrameworkException {
		initializeReportSettings();
		ReportTheme theme=  ReportThemeFactory.getReportTheme(ReportThemeFactory.Theme.valueOf(properties.getProperty("ReportsTheme")));
		
	}
	
	
	private void initializeReportSettings() throws FrameworkException {
		reportPath = TimeStamp.getInstance();
		reportSetting = new ReportSettings(reportPath,seleniumTestParameters.getCurrentScenario()
				+"_"+seleniumTestParameters.getCurrentTestCase()+"_"+seleniumTestParameters.getCurrentTestInstance());
	}
	
	
	private synchronized void initializeDataTable() throws FrameworkException {
		
		String dataTablePath = parameters.getRelativePath()+Utility.getFileSeperator()
								+	"src"  + Utility.getFileSeperator()
								+   "main" + Utility.getFileSeperator()
								+	"resources" + Utility.getFileSeperator()
								+   "DataTables";
		
		String runTimeDataTablePath ;
		Boolean includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));
		if(includeTestDataInReport)
		{
			runTimeDataTablePath = reportPath + Utility.getFileSeperator()
											  + "DataTables";
			
			File runTimeDataTableFile = new File(runTimeDataTablePath +Utility.getFileSeperator()
														+ seleniumTestParameters.getCurrentTestCase()+".xls");
											
			if(! runTimeDataTableFile.exists()) {
				File dataTable = new File(dataTablePath+Utility.getFileSeperator()
				+ seleniumTestParameters.getCurrentTestCase()+".xls");
				
				try {
					FileUtils.copyFile(dataTable, runTimeDataTableFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the datatable failed...");
				}
			}
			
			File runTimeCommonDatatableFile = new File(runTimeDataTablePath+ Utility.getFileSeperator()+
					"Common Testdata.xls");
			
			if(!runTimeCommonDatatableFile.exists()) {
				
				File commonDataTable = new File(dataTablePath + Utility.getFileSeperator()+"Common Testdata.xls");
				try {
					FileUtils.copyFile(commonDataTable, runTimeCommonDatatableFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new FrameworkException(
							"Error in creating run-time datatable: Copying the common datatable failed...");
				}
				
			}
			
		}
		else 
			runTimeDataTablePath = dataTablePath;
 	
		
		dataTable = new DataTable(runTimeDataTablePath, seleniumTestParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier(properties
				.getProperty("DataReferenceIdentifier"));
	}
	
	private void initializeTestScript() throws FrameworkException {
	
		
		initializeBusinessFlow();
	}
	
	
	private void initializeBusinessFlow() throws FrameworkException {
		
		
		String businessFlowDataPath = parameters.getRelativePath()+Utility.getFileSeperator()
									  + "src" + Utility.getFileSeperator()
									  + "main"+Utility.getFileSeperator() 
									  + "resources"+Utility.getFileSeperator()
									  +"DataTables";
		
		
		ExcelDataAccessForXLS businessFlowAccess = new ExcelDataAccessForXLS(businessFlowDataPath,seleniumTestParameters.getCurrentScenario());
		businessFlowAccess.setDataSheet("Business_Flow");
		
		int rowNo = businessFlowAccess.getRowNo(seleniumTestParameters.getCurrentTestCase(), 0);
		if(rowNo==-1)
			throw new FrameworkException("The test case \""
					+ seleniumTestParameters.getCurrentTestCase()
					+ "\" is not found in the Business Flow sheet!");
		
		int currentColumn =1;
		businessFlowData = new ArrayList<>();
		while(true) {
			String keyword = businessFlowAccess.getValue(rowNo, currentColumn);
			if("".equals(keyword))
				break;
			else {
				
				businessFlowData.add(keyword);
				currentColumn++;
			}	
		}
		
		if(businessFlowData.isEmpty())
			throw new FrameworkException(
					"No business flow found against the test case \""
							+ seleniumTestParameters.getCurrentTestCase() + "\"");
		
	}
	
	
	private void executeTestIterations() {
		
		while(currentIteration<seleniumTestParameters.getEndIteration()) {
			
			executeTestCase();
			
		}
		
		
	}
	
	private void executeTestCase() throws ClassNotFoundException, FrameworkException {
		
		Map<String,Integer> keywordDirectory = new HashMap<>();
		for(int currentKeyword=0;currentKeyword<=businessFlowData.size();currentKeyword++) {
			
			String[] keywordData = businessFlowData.get(currentKeyword).split(",");
			
			String keyword= keywordData[0];
			int nKeywordIterations=1;
			if(keywordData.length>1) 
				nKeywordIterations=Integer.parseInt(keywordData[1]);			
			for(int currentKeywordIteration=0; currentKeywordIteration<nKeywordIterations;currentKeywordIteration++) {
				
				if(keywordDirectory.containsKey(keyword)) {
					keywordDirectory.put(keyword, keywordDirectory.get(keyword)+1);
				}
				else {
					keywordDirectory.put(keyword, 1);
					}
				
				currentSubIteration = keywordDirectory.get(keyword);
				dataTable.setCurrentRow(seleniumTestParameters.getCurrentTestCase(), currentIteration, currentSubIteration);
				
				invokeBusinessComponent(keyword);				
			}		
		}		
	}
	
	
	
	private void invokeBusinessComponent(String keyword) throws ClassNotFoundException, FrameworkException {
		
		boolean methodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		
		String businessComponentFilePath = parameters.getRelativePath()+Utility.getFileSeperator()
											+ "target" +Utility.getFileSeperator()
											+ "target-classes" + Utility.getFileSeperator()
											+ "main-classes" + Utility.getFileSeperator()
											+ "businessComponents";
											
 		String componentGroups = 	parameters.getRelativePath()+Utility.getFileSeperator()
 											+ "target" +Utility.getFileSeperator()
 											+ "target-classes" + Utility.getFileSeperator()
 											+ "main-classes" + Utility.getFileSeperator()
 											+ "componentGroups";							
		
 		File[] packageDirectories= {new File(businessComponentFilePath), new File(componentGroups)};
 		
 		for(File packageDirectory : packageDirectories) {
 			
 			File[] packageFiles = packageDirectory.listFiles();
 			String packageName = packageDirectory.getName();
 			
 				for(int fileNo =0; fileNo<packageFiles.length; fileNo++) {
 					File packageFile = packageFiles[fileNo];
 					String fileName = packageFile.getName();
 					if(fileName.endsWith(CLASS_FILE_EXTENSION)) {
 						String className = fileName.substring(0, fileName.length()-CLASS_FILE_EXTENSION.length());
 						
 						Class<?> reusableComponents = Class.forName(packageName+"."+className);
 						Method executeComponent;
 						
 						
 						 keyword = keyword.substring(0,1).toLowerCase()+keyword.substring(1);
 						 try {
							executeComponent = reusableComponents.getMethod(keyword,(Class<?>)null);
						} catch (NoSuchMethodException e) {
							// If the method is not found in this class, search the
							// next class
							continue;
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new FrameworkException(e.getMessage().toString());
						}
 						 
 						
 						methodFound=true;
 						Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
 						Object object = ctor.newInstance(scriptHelper);
 						executeComponent.invoke(object, (Object[])null);
 						break;
 						
 					}
 					
 				}
			
		}
 		if(!methodFound) {
 			throw new FrameworkException("Keyword " + keyword
					+ " not found within any class "
					+ "inside the businesscomponents package");
 		}
	}
	
	
}
