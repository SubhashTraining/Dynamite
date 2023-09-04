package Runners;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.FrameworkParameters;
import com.dynamite.framework.IterationMode;
import com.dynamite.framework.UserProperties;
import com.dynamite.framework.Utility;
import com.dynamite.frameworkSelenium.Browser;
import com.dynamite.frameworkSelenium.ExecutionMode;
import com.dynamite.frameworkSelenium.SeleniumTestParameters;
import com.dynamite.framework.ExcelDataAccessForXLSM;
public class Executor {
	
	private Properties properties;
	private Properties mobileProperties;
	private FrameworkParameters frameworkParamenter = FrameworkParameters.getInstance();
	
	
	public static void main(String args[]) throws FrameworkException {
		
		Executor executor = new Executor();
		executor.startExecution();
		
	}
	
	private void startExecution() throws FrameworkException {
		
		properties = UserProperties.getGlobalPropeties();
		frameworkParamenter.setRelativePath();
		int threadCount = Integer.parseInt(properties.getProperty("threads"));
		int testBatchStatus = executeTests(threadCount);
		
	}	
	
	private int executeTests(int threadCount) throws FrameworkException {
		
		List<SeleniumTestParameters> testsToRun = loadTestsToRun(frameworkParamenter.getRunConfiguration());
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(threadCount);
		
		ParallelRunner testRunner = null;
		for(int currentTestInstance =0; currentTestInstance<testsToRun.size();currentTestInstance ++) {
			testRunner = new ParallelRunner(testsToRun.get(currentTestInstance));
			parallelExecutor.execute(testRunner);
			if(frameworkParamenter.getStopExecution()) {
				break;
			}
		}
		parallelExecutor.shutdown();
		while(!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(testRunner.equals(null)) {
			return 0;	//All tests are flagged as No in the Run Manager
		}
		else {
			return testRunner.getStatus();
					
		}		
	}
	
	
	private List<SeleniumTestParameters> loadTestsToRun(String runSheet) throws FrameworkException{
		
		ExcelDataAccessForXLSM dataAccess = 
				new ExcelDataAccessForXLSM("RunManager.xlsm",frameworkParamenter.getRelativePath()+
																			Utility.getFileSeperator()+"src"+
																			Utility.getFileSeperator()+"main"+
																			Utility.getFileSeperator()+"resouces"+
																			Utility.getFileSeperator());
		
		dataAccess.setDataSheet(runSheet);
		int noOfRows = dataAccess.getLastRowNo();
		LinkedList<SeleniumTestParameters> testsToRun = new LinkedList<SeleniumTestParameters>();
		
		for(int currentTestInstance =1;currentTestInstance<=noOfRows;currentTestInstance++) {
			
			String executeFlag = dataAccess.getValue(currentTestInstance, "Execute");
			if("yes".equalsIgnoreCase(executeFlag)) {
				
				String currentScenario = dataAccess.getValue(currentTestInstance, "TestScenario");
				String currentTestCase = dataAccess.getValue(currentTestInstance, "TestCase");
				SeleniumTestParameters seleniumTestParameter = new SeleniumTestParameters(currentScenario, currentTestCase);
				seleniumTestParameter.setCurrentTestInstance("Instance"+dataAccess.getValue(currentTestInstance, "TestInstance"));
				seleniumTestParameter.setCurrentTestDesciption(dataAccess.getValue(currentTestInstance,"Description"));
				String iterationMode = dataAccess.getValue(currentTestInstance, "IterationMode");
				if(!iterationMode.equals("")) {
					seleniumTestParameter.setIterationMode(IterationMode.valueOf(iterationMode));
				}
				else {
					seleniumTestParameter.setIterationMode(IterationMode.RUN_ALL_ITERATIONS);
				}
				
				String startIteration = dataAccess.getValue(currentTestInstance, "StartIteration");
				if(!"".equalsIgnoreCase(startIteration)) {
					seleniumTestParameter.setStartIteration(Integer.parseInt(startIteration));
				}
				
				String endIteration = dataAccess.getValue(currentTestInstance, "EndIteration");
				if(!"".equalsIgnoreCase(endIteration)) {
					seleniumTestParameter.setEndIteration(Integer.parseInt(endIteration));
				}
						
				
				String testConfig = dataAccess.getValue(currentTestInstance, "TestConfigurationID");
				if(!testConfig.equals("")) {
					getTestConfigurations(dataAccess,"TestConfigurations",testConfig,seleniumTestParameter);
				}
				dataAccess.setDataSheet(runSheet);
				testsToRun.add(seleniumTestParameter);			
			}		
			
		}
			
		return testsToRun;	
		
	}


	private void getTestConfigurations(ExcelDataAccessForXLSM dataAccess, String sheetName,String testConfig, SeleniumTestParameters testParameters  ) throws FrameworkException {
		
		dataAccess.setDataSheet(sheetName);
		
		int row = dataAccess.getRowNo(testConfig, 0, 1);
		String executionEnvironment = dataAccess.getValue(row, "ExecutionMode") ;
		if(! executionEnvironment.equals(""))
			testParameters.setExecutionMode(ExecutionMode.valueOf(executionEnvironment));
		else {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties.getProperty("DefaultExecutionMode")));
		}
		
		String browser = dataAccess.getValue(row, "Browser");
		if(!"".equals(browser)) {
			testParameters.setBrowser(Browser.valueOf(browser));
		}
		else {
			testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
		}
		
	}
}
