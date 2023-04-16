package edu.neu.coe.csye6205.tsp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Point;
import edu.neu.coe.csye6205.tsp.repository.CsvUtilRepository;

@Service
public class CsvUtilService {
	

	private String EXCEL_FILE_PATH = "/PSA_Project/src/main/java/testdata.csv";
	@Autowired
	CsvUtilRepository repo;

	Workbook workbook;

	public List<Point> uploadExcelData() throws IOException {

		// Read the Excel file
//         workbook = new XSSFWorkbook(file.getInputStream());
		System.out.println("uploadExcel data");
		List<Point> list = new ArrayList<Point>();
		Set<String> set=new HashSet<String>();
		try {
			workbook = new XSSFWorkbook(new FileInputStream(EXCEL_FILE_PATH));
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		// Get the first sheet
		Sheet sheet = workbook.getSheetAt(0);
		// Iterate through each row
		for (Row row : sheet) {

			System.out.println(row.getRowNum());
			// Skip the header row
			if (row.getRowNum() == 0) {
				continue;
			}

			// Set the properties
			Point point = new Point();

			if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null)
				continue;
			if(set.contains(row.getCell(0).getStringCellValue()))
				continue;
			set.add(row.getCell(0).getStringCellValue());

			point.setCrimeId(row.getCell(0).getStringCellValue());
			point.setLongitude(Double.valueOf(row.getCell(1).getNumericCellValue()));
//			System.out.println(excelData.get(i+4));
			point.setLatitude(Double.valueOf(row.getCell(2).getNumericCellValue()));

			// Save the Employee object to the database
//            repo.save(employee)
			list.add(point);

		}

		// Close the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public int saveExcelData(List<Point> points) {
		points = repo.saveAll(points);
		return points.size();
	}

}
