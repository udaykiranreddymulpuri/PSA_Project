package edu.neu.coe.csye6205.tsp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Route;

@Service
public class ExcelFetchRouteService {
	
	private String EXCEL_FILE_PATH = System.getProperty("user.dir")+"\\routes.xlsx";

	Workbook workbook;

	public List<Route> fetchExcelData() throws IOException {
		List<Route> list = new ArrayList<Route>();
		try {
			workbook = new XSSFWorkbook(new FileInputStream(EXCEL_FILE_PATH));
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		// Get the first sheet
		Sheet sheet = workbook.getSheetAt(0);
		// Iterate through each row
		for (Row row : sheet) {
			// Set the properties
			Route route = new Route();

			if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null)
				continue;
			route.setRouteId((int) row.getCell(0).getNumericCellValue());
			route.setFromLoc((int) row.getCell(1).getNumericCellValue());
			route.setToLoc((int) row.getCell(2).getNumericCellValue());
			route.setWeight(row.getCell(3).getNumericCellValue());
			list.add(route);
		}
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
