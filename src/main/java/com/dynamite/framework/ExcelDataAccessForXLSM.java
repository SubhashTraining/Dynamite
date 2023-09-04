package com.dynamite.framework;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;


public class ExcelDataAccessForXLSM
{
	
	private String fileName;
	private String filePath;
	private String dataSheetName;
	
	
	public ExcelDataAccessForXLSM(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
				
	}
	
	public void setDataSheet(String dataSheetName) {
		
		this.dataSheetName= dataSheetName;
		
	}
	
	public int getLastRowNo() throws FrameworkException {
		
		checkPreRequiste();
		XSSFWorkbook workbook =openWorkBookForReading();
		XSSFSheet sheet = workbook.getSheet(dataSheetName);
		return sheet.getLastRowNum();
		
	}
	
	public void checkPreRequiste() throws FrameworkException {
				
		if(dataSheetName== null) {
			throw new FrameworkException(
					"ExcelDataAccess.DataSheetName is set to null");
		}
		
		
	}

	
	public XSSFWorkbook openWorkBookForReading() throws FrameworkException{
		
		String absoluteFilePath = filePath+Utility.getFileSeperator()+fileName+".xlsm";
		
		FileInputStream fis ;
		
		try {
			fis = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FrameworkException(
					"The Specified File "+absoluteFilePath+" does not exists");
		}
		
		XSSFWorkbook workBook=null;
		
		try {
			workBook = new XSSFWorkbook(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new FrameworkException(
					"Error while accessing the file "+ absoluteFilePath);
		}
		
		
		
		return workBook;
	}
	
	
	public XSSFSheet getWorksheet(XSSFWorkbook workbook) throws FrameworkException {
		
		XSSFSheet  workSheet = workbook.getSheet(dataSheetName);
		if(workSheet == null)
			throw new FrameworkException(
					"The dataSheet "+this.dataSheetName+ 
					" does not exist within the work book "+ fileName+".xlsm");
		return workSheet;
		
	}
	
	
	
	public int getTotalRows() throws FrameworkException {
		
		checkPreRequiste();
		XSSFWorkbook workBook = openWorkBookForReading();
		XSSFSheet workSheet = getWorksheet(workBook);
		return workSheet.getLastRowNum();
		
	}
	
	
	public String getValue(int rowNo,String columnHeader) throws FrameworkException {
		
		
		XSSFWorkbook workBook = openWorkBookForReading();
		XSSFSheet workSheet = getWorksheet(workBook);
		
		String cellValueAsString= null;
		
		XSSFFormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		XSSFRow hrow = workSheet.getRow(0);
		boolean columnFound =false;
		
		
		
		for(int currentColumnNo =0; currentColumnNo < hrow.getLastCellNum();currentColumnNo++) {
			
			XSSFCell currentCell = hrow.getCell(currentColumnNo);
			String currentColHeader = getCellValueAsString(currentCell,evaluator);
			
			
			
			if(currentColHeader.equals(columnHeader)) {
				XSSFRow row = workSheet.getRow(rowNo);
				XSSFCell cell = row.getCell(currentColumnNo);
				cellValueAsString = getCellValueAsString(cell,evaluator);
				columnFound =true;
				break;
				
			}		
		}
		
		if(!columnFound)
			throw new FrameworkException(
					"The column "+columnHeader+" is not found in the sheet "+ this.dataSheetName 
					+" in the workBook "+this.fileName+".xlsm");
		
		
		return cellValueAsString;
	}
	
	

	
	
	public String getCellValueAsString(XSSFCell cell, XSSFFormulaEvaluator evaluator) throws FrameworkException {
		
		
		if(cell == null || cell.getCellType() == CellType.BLANK){
			return "";
		}else {
			if(cell.getCellType()== CellType.ERROR)
				throw new FrameworkException(
						"Error in formula within the cell "+cell.getErrorCellValue());
		}
	
		DataFormatter dataFomatter = new DataFormatter();
		
		return dataFomatter.formatCellValue(evaluator.evaluateInCell(cell));		
	}
	
	
	public List<Map<String,String>> getValues (String[] keys) throws FrameworkException{
		
		checkPreRequiste();
		XSSFWorkbook workbook = openWorkBookForReading();
		XSSFSheet worksheet = getWorksheet(workbook);
		XSSFFormulaEvaluator evaluator= workbook.getCreationHelper().createFormulaEvaluator();
		XSSFRow hrow = worksheet.getRow(0);
		
		List<Map<String,String>> valueList = new ArrayList<Map<String,String>>();
			
		
			for(int i=1; i < worksheet.getLastRowNum();i++) {
				XSSFRow row = worksheet.getRow(i);
				Map<String,String> keyMap = new HashMap<String,String>();
				for(String key : keys) {
					String keyValue = getCellValueAsString(key,hrow, row, evaluator);
					keyMap.put(key, keyValue);					
				}
				valueList.add(keyMap);
			}
				
		return valueList;
	}
	
	
	private String getCellValueAsString(String key, XSSFRow hrow, XSSFRow row, XSSFFormulaEvaluator evaluator) throws FrameworkException {
		
		int headerCellNo = -1;
		for(int col=0;col<hrow.getLastCellNum();col++)
		{
			XSSFCell cell = hrow.getCell(col);
			if(getCellValueAsString(cell,evaluator).equals(key)) {
				headerCellNo =col;
				break;
			}
		}
		
		if(headerCellNo==-1)
			throw new FrameworkException(
					"The Header "+key+" is not found in the sheet "+this.dataSheetName+" and file "+ this.fileName+".xlsm");
		
		XSSFCell cell = row.getCell(headerCellNo);
		return getCellValueAsString(cell, evaluator);
	}
	
	
	public int getRowNo(String key, int columNum, int startRow) throws FrameworkException {
		
		checkPreRequiste();
		XSSFWorkbook  workBook = openWorkBookForReading();
		XSSFSheet workSheet = workBook.getSheet(dataSheetName);
		int lastRow = workSheet.getLastRowNum();
		XSSFFormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		for(int currentRow =startRow; currentRow <lastRow; currentRow++) {
			
			XSSFRow row = workSheet.getRow(currentRow);
			XSSFCell cell = row.getCell(columNum);
			String cellValue = getCellValueAsString(cell,evaluator);
			if(cellValue.equals(key)) {
				return currentRow;
				
			}
			
		}		
		return -1;
		
	}
	
	
}
