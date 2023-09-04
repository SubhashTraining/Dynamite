package com.dynamite.framework;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
public class ExcelDataAccessForXLS {
	
	
	private final String filePath , fileName;
	public String dataSheet;
	
	

	public ExcelDataAccessForXLS(String filePath, String fileName) {
		
		this.filePath=filePath;
		this.fileName = fileName;
	}

	public String getDataSheet() {
		return dataSheet;
	}

	public void setDataSheet(String dataSheet) {
		this.dataSheet = dataSheet;
	}
	
	private void checkPreRequiste() throws FrameworkException {
		if (this.dataSheet==null)
			throw new FrameworkException(
					"ExcelDataAccess DataSheet Name is not set!");
	}
	
	
	private HSSFWorkbook openFileForReading() throws FrameworkException {
		
		checkPreRequiste();
		FileInputStream fis =null;
		try {
			fis= new FileInputStream(filePath + fileName + ".xls");
		} catch (FileNotFoundException e) {
			throw new FrameworkException(
					"The file "+fileName+ " is not found in the location "+filePath);
			
		}
		HSSFWorkbook workBook ;
		try {
			workBook = new HSSFWorkbook(fis);
			
		} catch (IOException e) {
			throw new FrameworkException(
					"Error Accessing file  "+fileName+ " from file Path: "+filePath);
		}
		
		
		return workBook;
		
		
		
	}
	
	
	private HSSFSheet getWorkSheet(HSSFWorkbook workBook) throws FrameworkException {
		
		HSSFSheet workSheet = workBook.getSheet(dataSheet);
		if(workSheet == null) {
			throw new FrameworkException(
					"The specified worksheet \""
					+dataSheet+ "\" is not available in the workBook "
					+fileName+".xls") ;
		}
		
		return workSheet;
			
	}
	
	private void writeIntoFile(HSSFWorkbook workBook) throws FrameworkException {
		
		
		String absoluteFilePath = filePath+ Utility.getFileSeperator() +
									fileName + ".xls";		
		FileOutputStream fos =null ;	
		try {
			fos = new FileOutputStream(absoluteFilePath);		
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ absoluteFilePath + "\" does not exist!");
		}
		
		try {
			workBook.write(fos);
			fos.close();
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing into the specified Excel workbook \""
							+ absoluteFilePath + "\"");
		}		
	}
	
	
	public int getRowNo(String key, int columnNo, int startRowNo) throws FrameworkException {
		
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet workSheet = getWorkSheet(workBook);
		
		FormulaEvaluator evalator = workBook.getCreationHelper().createFormulaEvaluator();
		String currentValue ;
		for(int currentRow =0; currentRow <= workSheet.getLastRowNum(); currentRow++) {
			
			HSSFRow row = workSheet.getRow(currentRow);
			HSSFCell cell = row.getCell(columnNo);
			currentValue = getCellValueAsString(cell,evalator);
			
			if(currentValue.equals(key))
			return currentRow;
		}	
		
		
		return -1;	
		
	}
	
	
	private String getCellValueAsString(HSSFCell cell, FormulaEvaluator evaluator) throws FrameworkException {
		
		
		if(cell == null || cell.getCellType()== CellType.BLANK)
			return "";
			
		if(evaluator.evaluate(cell).getCellType()== CellType.ERROR)	
			throw new FrameworkException("Error in formula within this cell! " + "Error code: "
								+ cell.getErrorCellValue());
		
		DataFormatter formatter = new DataFormatter();
		
		return formatter.formatCellValue(evaluator.evaluateInCell(cell));		
	}
	
	
	
	
	public int getRowNo(String key, int columnNo) throws FrameworkException {
		return getRowNo(key, columnNo,0);
	}
	
	
	
	public int getLastRowNum() throws FrameworkException {
		checkPreRequiste();
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		return sheet.getLastRowNum();
		
		
	}
	
	
	public int getRowCount(String key, int column, int startRowNo) throws FrameworkException {
		
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		
		FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
		String currentValue;
		int rowCount =0;
		boolean keyFound = false;
		
		for(int currentRowNo =startRowNo; currentRowNo<=sheet.getLastRowNum();currentRowNo++) {
			
			HSSFRow row = sheet.getRow(currentRowNo);
			HSSFCell cell = row.getCell(column);
			currentValue = getCellValueAsString(cell, formulaEvaluator);
			
			if(currentValue.equals(key)) {
				rowCount++;
				keyFound= true;
			}
			else {
				if(keyFound)
					break; // Assumption: Keys always appear contiguously
			}
			
		}
		
		return rowCount;
	}
	
	
	public int getColumnNo (String key, int RowNo) throws FrameworkException {
		

		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		
		FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
		HSSFRow  row =  sheet.getRow(RowNo);
		
		for(int currentColumn =0; currentColumn< row.getLastCellNum(); currentColumn++) {
			
			HSSFCell cell = row.getCell(currentColumn);
			String cellValue = getCellValueAsString(cell,formulaEvaluator );
			
			if(cellValue.equals(key)) {
				return currentColumn;
			}		
		}
		
		return -1;
	}
		
	
	
	public String getValue(int rowNo, int columnNo ) throws FrameworkException {
		
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		
		FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
		
		HSSFRow  row= sheet.getRow(rowNo);
		HSSFCell  cell = row.getCell(columnNo);
		
		return getCellValueAsString(cell, formulaEvaluator);
		
		
		
	}
	
	
	public String getValue(int rowNo,String columnHeader) throws FrameworkException {
		
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		
		FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
		HSSFRow  headerRow= sheet.getRow(0);
		int columnNo=-1;
		for(int currentColumnNo =0; currentColumnNo < headerRow.getLastCellNum(); currentColumnNo++) {
			
			HSSFCell cell = headerRow.getCell(currentColumnNo);
			String headerValue = getCellValueAsString(cell, formulaEvaluator);
			
			if(headerValue.equals(columnHeader))
			{
				 columnNo = currentColumnNo;
				 break;
			}			
		}
		
		if (columnNo == -1) {
			throw new FrameworkException("The specified column header \""
					+ columnHeader + "\"" + "is not found in the sheet \""
					+ dataSheet + "\"!");
		}
		
		HSSFRow row = sheet.getRow(rowNo);
		return getCellValueAsString(row.getCell(columnNo),formulaEvaluator);
			
			
	}
	
	public void setValue(int rowNo, int columnNo, String value) throws FrameworkException {
		
		HSSFWorkbook workBook = openFileForReading();
		HSSFSheet sheet = getWorkSheet(workBook);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(columnNo);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(value);
		
		writeIntoFile(workBook);
		
		
	}

	
	
	public void setValue(int rowNo, String columnHeader, String value) throws FrameworkException {
		
		int columnNo = getColumnNo(columnHeader, 0);
		setValue(rowNo, columnNo, value);
		
	}
	
}
