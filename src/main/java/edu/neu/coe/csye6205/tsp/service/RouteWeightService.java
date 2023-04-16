package edu.neu.coe.csye6205.tsp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Point;
import edu.neu.coe.csye6205.tsp.model.Route;

@Service
public class RouteWeightService {

//	@Autowired
//	RouteWeightRepository routeRepo;
//
//	@Autowired
//	CsvUtilRepository csvRepo;

	@Autowired
	ExcelFetchPointService  fetchPointService;
	
	public void calculateWeight() {
		// TODO Auto-generated method stub
		List<Point> points = new ArrayList<>();;
		try {
			points = fetchPointService.fetchExcelData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(points);
		int count=1;
		List<Route> routes=new ArrayList<Route>();
		for(int i=0;i<points.size()-1;i++) {
//			List<Route> routes=new ArrayList<Route>();
			for(int j=i+1;j<points.size();j++) {
				Point p1=points.get(i);
				Point p2=points.get(j);
				Route route=new Route();
				route.setFromLoc(p1.getPointId());
				route.setToLoc(p2.getPointId());
				double lon1=p1.getLongitude();double lon2=p2.getLongitude();
				double lat1=p1.getLatitude(); double lat2=p2.getLatitude();
				lon1 = Math.toRadians(lon1);
		        lon2 = Math.toRadians(lon2);
		        lat1 = Math.toRadians(lat1);
		        lat2 = Math.toRadians(lat2);
		 
		        // Haversine formula
		        double dlon = lon2 - lon1;
		        double dlat = lat2 - lat1;
		        double a = Math.pow(Math.sin(dlat / 2), 2)
		                 + Math.cos(lat1) * Math.cos(lat2)
		                 * Math.pow(Math.sin(dlon / 2),2);
		             
		        double c = 2 * Math.asin(Math.sqrt(a));
		 
		        // Radius of earth in kilometers. Use 3956
		        // for miles
		        double r = 6371;
		 
		        // calculate the result
	
//				System.out.println(p1.getCrimeId()+" "+p2.getCrimeId()+" "+weight);
				route.setWeight(c*r);
				route.setRouteId(count++);
				routes.add(route);
				System.out.println(route);
			}
//			routeRepo.saveAll(routes);
			
			System.out.println(routes.size());
		}
		savetoExcel(routes);
		
	}
	
	public void savetoExcel(List<Route> routes) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
			int rownum = 0;
			for (Route route : routes) {
				Row row = sheet.createRow(rownum++);
				createList(route, row);

			}

			FileOutputStream out = new FileOutputStream(new File("routes.xlsx")); // file name with path
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createList(Route route, Row row) // creating cells for each row
	{
		Cell cell = row.createCell(0);
		System.out.println(route.getRouteId());
		cell.setCellValue(route.getRouteId());

		cell = row.createCell(1);
		cell.setCellValue(route.getFromLoc());
		 cell = row.createCell(2);
		cell.setCellValue(route.getToLoc());

		cell = row.createCell(3);
		cell.setCellValue(route.getWeight());

	}
	

}
