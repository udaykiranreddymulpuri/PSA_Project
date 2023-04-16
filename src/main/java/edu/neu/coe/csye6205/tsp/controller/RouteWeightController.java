package edu.neu.coe.csye6205.tsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.coe.csye6205.tsp.service.RouteWeightService;

@RestController
@RequestMapping("v1/route")
public class RouteWeightController {

	@Autowired
	RouteWeightService routeWeightService;
	
	@GetMapping("/calculateWeight")
	public String calculateWeight() {
		routeWeightService.calculateWeight();
		return "Sucess";
	}

}
