package Test;


import org.junit.Test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;

import com.dynamite.framework.*;

public class ExcellDataAccess_Test {
	
	ExcelDataAccessForXLSM dataAccess = new ExcelDataAccessForXLSM("", "");
	private static FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	@Before
	public void startScripts() {
		
		frameworkParameters.setRelativePath();
	}
	
	
	@Test
	public void test1_getCellValue() throws FrameworkException {
		
	String fileAbsolutePath = frameworkParameters.getRelativePath()+Utility.getFileSeperator()
								+ "src"+Utility.getFileSeperator()
								+  "test" + Utility.getFileSeperator()
								+ "resources";
	String fileName = "Run Manager";
	System.out.println(System.getProperty("file.separator"));
	ExcelDataAccessForXLSM access = new ExcelDataAccessForXLSM(fileName,fileAbsolutePath);
	access.setDataSheet("Regression");
	
	String CellValue = access.getValue(1, "TestScenario");
	
	Assert.assertEquals("Metflow_1",CellValue) ;					
	
	List<Map<String,String>> testScenario = access.getValues(new String[] {"TestScenario","Description","Execute"});
		
	for(Map<String,String> map : testScenario) {
		System.out.println();
		for(Map.Entry<String,String> entry:map.entrySet()) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
			
		}
		
	}
	
	}

}
