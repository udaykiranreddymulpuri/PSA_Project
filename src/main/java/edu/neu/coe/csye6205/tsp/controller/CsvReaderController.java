package edu.neu.coe.csye6205.tsp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.coe.csye6205.tsp.model.Point;
import edu.neu.coe.csye6205.tsp.service.CsvUtilService;

@RestController
@RequestMapping("v1/csv")
public class CsvReaderController {

	@Autowired
	CsvUtilService utilService;
	
	@GetMapping("/saveData")
    public String saveExcelData() {
    	System.out.println("In save Excel Data");
    	try {
			List<Point> excelDataAsList = utilService.uploadExcelData();
			System.out.println(excelDataAsList);
			int noOfRecords = utilService.saveExcelData(excelDataAsList);
			System.out.println(noOfRecords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "success";
    }
}
