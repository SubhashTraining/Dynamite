package Runners;

import com.dynamite.framework.FrameworkParameters;
import com.dynamite.frameworkSelenium.SeleniumTestParameters;
import com.dynamite.framework.DriverScript;
import com.dynamite.framework.FrameworkException;

public class ParallelRunner implements Runnable {

	private final SeleniumTestParameters seleniumTestParameters;
	private int testBatchStatus;
	
	ParallelRunner(SeleniumTestParameters seleniumTestParameters){
		super();
		this.seleniumTestParameters=seleniumTestParameters;
	}
	
	public int getStatus() {
		
		return testBatchStatus;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		FrameworkParameters parameters = FrameworkParameters.getInstance();
		String testReportName , executionTime, testStatus; 
		
		if(parameters.getStopExecution()) {
			testReportName = "N/A";
			executionTime = "N/A";
			testStatus = "Aborted";
			testBatchStatus =1;//Non Zero indicates Failure
		}
		else {
			DriverScript driverScript = new DriverScript(seleniumTestParameters);
			try {
				driverScript.executeDriverScript();
			} catch (FrameworkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	
	

}
