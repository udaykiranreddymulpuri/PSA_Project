package edu.neu.coe.csye6205.tsp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.neu.coe.csye6205.tsp.model.Point;
import edu.neu.coe.csye6205.tsp.service.ExcelFetchPointService;

@Component
public class VertexUtil {

	@Autowired
	ExcelFetchPointService fetchPointService;

	public Map<Integer, String> getVertexMapping() {
		Map<Integer, String> vertexMap = new HashMap<>();
		List<Point> points = new ArrayList<>();
		;
		try {
			points = fetchPointService.fetchExcelData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		points.forEach(p -> {
			vertexMap.put(p.getPointId(), p.getCrimeId().substring(p.getCrimeId().length()-5, p.getCrimeId().length()));
		});
		return vertexMap;
	}
}
