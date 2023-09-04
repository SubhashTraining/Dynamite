package TestRunners;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import Test.ExcellDataAccess_Test;

public class TestRunner {
	
	public static void main(String agrs[]) {
		
		Result result = JUnitCore.runClasses(ExcellDataAccess_Test.class);
		System.out.println(result.wasSuccessful());
	}

}
