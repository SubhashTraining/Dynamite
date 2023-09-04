package com.dynmaite.framework.test;

import static org.junit.Assert.*;
import com.dynamite.framework.ExcelDataAccessForXLS;
import com.dynamite.framework.FrameworkException;
import com.dynamite.framework.FrameworkParameters;
import com.dynamite.framework.Utility;

import org.junit.Test;

public class DataAccessXLS_Test {

	FrameworkParameters parameters = FrameworkParameters.getInstance();
	
	@Test(expected=FrameworkException.class)
	public void doNotSetDataSheet() throws FrameworkException {
		
		String absolutePath = parameters.getRelativePath()+Utility.getFileSeperator()+
							 "src"+Utility.getFileSeperator()+
							 "test"+Utility.getFileSeperator()+
							 "resources"+Utility.getFileSeperator()+
							 "UnitTest_Resources"+Utility.getFileSeperator();
		
		
		String fileName = "DataAccess_UnitTest";
							 
		
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(absolutePath,fileName);
		
		dataAccess.getValue(0, "TestScenario");
		
		
	}
	
	
	@Test(expected = FrameworkException.class)
	public void invalidFileName() throws FrameworkException {
		
		String absolutePath = parameters.getRelativePath()+Utility.getFileSeperator()+
				 "src"+Utility.getFileSeperator()+
				 "test"+Utility.getFileSeperator()+
				 "resources"+Utility.getFileSeperator()+
				 "UnitTest_Resources"+Utility.getFileSeperator();


		String fileName = "DataAccess_UnitTes";
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(absolutePath,fileName);
		
		dataAccess.getValue(0, "TestScenario");
		
	}
	
	@Test
	public void getValuefromExcel() throws FrameworkException {
		
		String absolutePath = System.getProperty("user.dir")+Utility.getFileSeperator()+
				 "src"+Utility.getFileSeperator()+
				 "test"+Utility.getFileSeperator()+
				 "resources"+Utility.getFileSeperator()+
				 "UnitTest_Resources"+Utility.getFileSeperator();


		String fileName = "DataAccess_UnitTest";
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(absolutePath,fileName);
		dataAccess.setDataSheet("Regression");
		
		assertEquals("Metflow_1",dataAccess.getValue(1, "TestScenario"));
		assertEquals(1,dataAccess.getRowNo("Metflow_1", 0,1));
		assertEquals(1,dataAccess.getRowNo("Metflow_1", 0));
		assertEquals(8,dataAccess.getLastRowNum());
		assertEquals(8,dataAccess.getLastRowNum());
		assertEquals(8,dataAccess.getRowCount("RUN_ONE_ITERATION_ONLY",5,0));
		assertEquals(2,dataAccess.getColumnNo("TestInstance", 0));
		assertEquals("Metflow_1",dataAccess.getValue(1, 0));
		dataAccess.setValue(1,9, "Test");
		assertEquals("Test",dataAccess.getValue(1, "SetValue"));
		dataAccess.setValue(1,"SetValue", "Test1");
		assertEquals("Test1",dataAccess.getValue(1, "SetValue"));
		
		
	}
	

}
