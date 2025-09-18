package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	String [] [] ingredient = new String [100] [100];
	String retreiveditem;
	int i = 0,j=0;

public String readExcelSheet(int rowvalue, int colvalue, String sheetname) throws IOException {
	
		String path = System.getProperty("user.dir")+"/src/test/resources/TestData/IngredientsAndComorbidities-ScrapperHackathon.xlsx";
		File Excelfile = new File(path);
		
		FileInputStream Fis = new FileInputStream(Excelfile);
		XSSFWorkbook workbook = new XSSFWorkbook(Fis);
		XSSFSheet sheet = workbook.getSheet(sheetname);
		
		Iterator<Row> row = sheet.rowIterator();
		
		while(row.hasNext()) {
			
			Row currRow = row.next();
			Iterator<Cell> cell = currRow.cellIterator();
			
			while(cell.hasNext()) {
				Cell currCell = cell.next();
				i=currCell.getRowIndex();
				j=currCell.getColumnIndex();				
				ingredient[i][j] = currCell.getStringCellValue();
			}
		}
		workbook.close();
		retreiveditem = ingredient[rowvalue][colvalue];
		return retreiveditem;
	}
public int readlastrowindex(int rowvalue, int colvalue, String sheetname) throws IOException {
	
	String path = System.getProperty("user.dir")+"/src/test/resources/TestData/IngredientsAndComorbidities-ScrapperHackathon.xlsx";
	File Excelfile = new File(path);
	
	FileInputStream Fis = new FileInputStream(Excelfile);
	XSSFWorkbook workbook = new XSSFWorkbook(Fis);
	XSSFSheet sheet = workbook.getSheet(sheetname);
	
	Iterator<Row> row = sheet.rowIterator();
	
	while(row.hasNext()) {
		
		Row currRow = row.next();
		Iterator<Cell> cell = currRow.cellIterator();
		
		while(cell.hasNext()) {
			Cell currCell = cell.next();
			i=currCell.getRowIndex();
			j=currCell.getColumnIndex();				
			ingredient[i][j] = currCell.getStringCellValue();
		}
	}
	workbook.close();
	while(ingredient[i][colvalue].length() ==0) {
		i=i-1;
	}
	
	return i;
}

public String readexcelvalue(int rownumber,int columnnumber, String sheetname) throws IOException {	
	String checkitem;
	checkitem = readExcelSheet(rownumber, columnnumber, sheetname);
	return checkitem;	
}
}



