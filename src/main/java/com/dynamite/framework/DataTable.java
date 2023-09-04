package com.dynamite.framework;

import java.util.Properties;

public class DataTable {
	
	private final String dataTablePath ;
	private final String dataTableName;
	private String dataReferenceIdentifier = "#";

	private int currentIteration =0, currentSubIteration =0;
	private String currentTestCase;
	private Properties properties;
	
	
	public  DataTable(String dataTablePath, String dataTableName) throws FrameworkException {
		
		this.dataTableName= dataTableName;
		this.dataTablePath= dataTablePath;		
		properties= UserProperties.getGlobalPropeties();
		
	}
	
	public void setDataReferenceIdentifier(String dataReferenceIdentifier) throws FrameworkException {
		if (dataReferenceIdentifier.length() != 1) {
			throw new FrameworkException(
					"The data reference identifier must be a single character!");
		}

		this.dataReferenceIdentifier = dataReferenceIdentifier;
	}
	

	public void setCurrentRow (String currentTestCase, int currentIteration, int currentSubIteration ) {
		this.currentTestCase = currentTestCase;
		this.currentIteration = currentIteration;
		this.currentSubIteration = currentSubIteration;
	}
	
	

	private void checkPreRequisites() throws FrameworkException {
		if (currentTestCase == null) {
			throw new FrameworkException(
					"CraftDataTable.currentTestCase is not set!");
		}
		if (currentIteration == 0) {
			throw new FrameworkException(
					"CraftDataTable.currentIteration is not set!");
		}
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			if (currentSubIteration == 0) {
				throw new FrameworkException(
						"CraftDataTable.currentSubIteration is not set!");
			}
		}
	}
	
	public String getData(String dataSheetName, String columnHeader) throws FrameworkException {
		
		
		checkPreRequisites();
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(dataTablePath,dataTableName);
		dataAccess.setDataSheet(dataSheetName);
		
		int rowNo = dataAccess.getRowNo(currentTestCase, 0, 1);
		
		if(rowNo ==-1) 
			throw new FrameworkException("The iteration number \""
					+ currentIteration + "\"" + "of the test case \""
					+ currentTestCase + "\""
					+ "is not found in the test data sheet \"" + dataSheetName
					+ "\"!");
		
		
		if(properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			
			rowNo = dataAccess.getRowNo(Integer.toString(currentSubIteration), 2, rowNo);
			if (rowNo == -1) {
				throw new FrameworkException("The sub iteration number \""
						+ currentSubIteration + "\""
						+ "under iteration number \"" + currentIteration + "\""
						+ "of the test case \"" + currentTestCase + "\""
						+ "is not found in the test data sheet \""
						+ dataSheetName + "\"!");
			}
			
			if(!dataAccess.getValue(rowNo, 0).equals(currentTestCase)) {
				throw new FrameworkException("The sub iteration number \""
						+ currentSubIteration + "\""
						+ "under iteration number \"" + currentIteration + "\""
						+ "of the test case \"" + currentTestCase + "\""
						+ "is not found in the test data sheet \""
						+ dataSheetName + "\"!");		
				
			}
		}
		
		String dataValue = dataAccess.getValue(rowNo, columnHeader);

		if (dataValue.startsWith(dataReferenceIdentifier)) {
			dataValue = getCommonData(columnHeader, dataValue);
		}
		return dataValue;
		
	}
	
	private String getCommonData(String columnHeader,String dataValue) throws FrameworkException {
		
		ExcelDataAccessForXLS  commonDataTable = new ExcelDataAccessForXLS(dataTablePath, "Common Testdata");
		commonDataTable.setDataSheet("Common_Testdata");
		
		String dataReferenceId = dataValue.split(dataReferenceIdentifier)[1];
		int rowNo =commonDataTable.getRowNo(dataReferenceId, 0, 1);
		if(rowNo ==-1)	
			throw new FrameworkException(
				"The common test data row identified by \""
						+ dataReferenceId + "\""
						+ "is not found in the common test data sheet!");
		
		
		return commonDataTable.getValue(rowNo, columnHeader);
	}
	
	
	public void putData(String dataSheetName, String columnHeader, String dataValue) throws FrameworkException {
		ExcelDataAccessForXLS dataAccess = new ExcelDataAccessForXLS(dataTablePath,dataTableName);
		dataAccess.setDataSheet(dataSheetName);
		
		
		int rowNo = dataAccess.getRowNo(currentTestCase, 0);
		
		if(rowNo ==-1)
			throw new FrameworkException("The sub iteration number \""
					+ currentSubIteration + "\""
					+ "under iteration number \"" + currentIteration + "\""
					+ "of the test case \"" + currentTestCase + "\""
					+ "is not found in the test data sheet \""
					+ dataSheetName + "\"!");
		
		
		rowNo = dataAccess.getRowNo(Integer.toString(currentIteration), 1);
		
		if (rowNo == -1) {
			throw new FrameworkException("The iteration number \""
					+ currentIteration + "\"" + "of the test case \""
					+ currentTestCase + "\""
					+ "is not found in the test data sheet \"" + dataSheetName
					+ "\"!");
		}
		
		
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			
			rowNo = dataAccess.getRowNo(Integer.toString(currentSubIteration), 2,rowNo );
			if (rowNo == -1) {
				throw new FrameworkException("The sub iteration number \""
						+ currentSubIteration + "\""
						+ "under iteration number \"" + currentIteration + "\""
						+ "of the test case \"" + currentTestCase + "\""
						+ "is not found in the test data sheet \""
						+ dataSheetName + "\"!");
			}
			if(!dataAccess.getValue(rowNo, 0).equals(currentTestCase)) {
				throw new FrameworkException("The sub iteration number \""
						+ currentSubIteration + "\""
						+ "under iteration number \"" + currentIteration + "\""
						+ "of the test case \"" + currentTestCase + "\""
						+ "is not found in the test data sheet \""
						+ dataSheetName + "\"!");		
					}		
		}
		synchronized(DataTable.class) {
			dataAccess.setValue(rowNo, columnHeader, dataValue);
		}
		
	}
}