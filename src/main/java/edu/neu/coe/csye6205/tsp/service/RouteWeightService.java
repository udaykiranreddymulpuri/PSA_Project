package edu.neu.coe.csye6205.tsp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Point;
import edu.neu.coe.csye6205.tsp.model.Route;
import edu.neu.coe.csye6205.tsp.repository.CsvUtilRepository;
import edu.neu.coe.csye6205.tsp.repository.RouteWeightRepository;

@Service
public class RouteWeightService {

	@Autowired
	RouteWeightRepository routeRepo;

	@Autowired
	CsvUtilRepository csvRepo;

	public void calculateWeight() {
		// TODO Auto-generated method stub
		List<Point> points=csvRepo.findAll();
//		List<Route> routes=new ArrayList<Route>();
		for(int i=0;i<points.size()-1;i++) {
			List<Route> routes=new ArrayList<Route>();
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
				routes.add(route);
				System.out.println(route);
			}
			routeRepo.saveAll(routes);
			System.out.println(routes.size());
		}
		
		
	}

}
