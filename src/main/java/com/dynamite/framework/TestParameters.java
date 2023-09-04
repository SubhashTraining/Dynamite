package com.dynamite.framework;

public class TestParameters {
	
	
	private final String currentScenario;
	private final String currentTestCase;
	private String currentTestDesciption;
	private String currentTestInstance;
	private String additionDetails;
	private IterationMode iterationMode;
	private int startIteration;
	private int endIteration;
	
	
	public TestParameters(String currentScenario, String currentTestCase) {
		this.currentScenario = currentScenario;
		this.currentTestCase = currentTestCase;
		
		//Set default values for all test param
		this.currentTestDesciption="";
		this.currentTestInstance="Instance1";
		this.iterationMode=IterationMode.RUN_ALL_ITERATIONS;
		this.startIteration=1;
		this.endIteration=1;
		
	}


	public String getCurrentScenario() {
		return currentScenario;
	}


	public String getCurrentTestCase() {
		return currentTestCase;
	}


	public String getCurrentTestDesciption() {
		return currentTestDesciption;
	}


	public void setCurrentTestDesciption(String currentTestDesciption) {
		this.currentTestDesciption = currentTestDesciption;
	}


	public String getCurrentTestInstance() {
		return currentTestInstance;
	}


	public void setCurrentTestInstance(String currentTestInstance) {
		this.currentTestInstance = currentTestInstance;
	}


	public String getAdditionDetails() {
		return additionDetails;
	}


	public void setAdditionDetails(String additionDetails) {
		this.additionDetails = additionDetails;
	}


	public IterationMode getIterationMode() {
		return iterationMode;
	}


	public void setIterationMode(IterationMode iterationMode) {
		this.iterationMode = iterationMode;
	}


	public int getStartIteration() {
		return startIteration;
	}


	public void setStartIteration(int startIteration) {
		if(startIteration>0)
		this.startIteration = startIteration;
	}


	public int getEndIteration() {
		return endIteration;
	}


	public void setEndIteration(int endIteration) throws FrameworkException {
		if(endIteration>0)
		this.endIteration = endIteration;
		if(this.endIteration>this.startIteration)
			throw new FrameworkException(""
					+ "Start Iteration is greater than end iteration for test case: "+this.currentTestCase);
		
	}

}
