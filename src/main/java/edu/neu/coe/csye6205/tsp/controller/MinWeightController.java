package edu.neu.coe.csye6205.tsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.coe.csye6205.tsp.service.MinWeightService;
import edu.neu.coe.csye6205.tsp.service1.MinWeightService1;



@RestController
@RequestMapping("v1/tsp")
public class MinWeightController {

	@Autowired
	private MinWeightService minWeightService;
	
	@Autowired
	private MinWeightService1 minWeightService1;
	
	@GetMapping("/findPath")
	public String getMinWeightPath() {
		minWeightService.findPath();
		
		return "Sucess";
	}
	
	@GetMapping("/findPath1")
	public String getMinWeightPath1() {
		minWeightService1.findPath();
		
		return "Sucess";
	}
}