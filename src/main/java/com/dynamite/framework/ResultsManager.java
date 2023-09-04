package com.dynamite.framework;

/**
 * 
 */
public class ResultsManager {
	
	public static final ResultsManager RESULT_MANAGER = new ResultsManager();
	
	
	
	
	private ResultsManager() {
		
	}
	
	/**
	 * Function to return singleton instance of (@link ResultsManger) Object
	 * @return  Instance of {@link ResultsManager} object
	 */
	public static ResultsManager getInstance() {
		return RESULT_MANAGER;
			
	}

}
