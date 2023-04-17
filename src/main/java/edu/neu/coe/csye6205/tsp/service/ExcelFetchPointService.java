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

import edu.neu.coe.csye6205.tsp.model.Point;

@Service
public class ExcelFetchPointService {

//	System.getProperty("user.dir")+"\\Data\\Student.txt"
	private String EXCEL_FILE_PATH = System.getProperty("user.dir")+"\\points.xlsx";

	Workbook workbook;

	public List<Point> fetchExcelData() throws IOException {
		List<Point> list = new ArrayList<Point>();
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
			Point point = new Point();

			if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null)
				continue;
			point.setPointId((int) row.getCell(0).getNumericCellValue());
			point.setCrimeId(row.getCell(1).getStringCellValue());
			point.setLatitude(row.getCell(2).getNumericCellValue());
			point.setLongitude(row.getCell(3).getNumericCellValue());
			list.add(point);
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
