package edu.neu.coe.csye6205.tsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.coe.csye6205.tsp.service.MinWeightService;



@RestController
@RequestMapping("v1/tsp")
public class MinWeightController {

	@Autowired
	private MinWeightService minWeightService;
	
	@GetMapping("/findPath")
	public String getMinWeightPath() {
		minWeightService.findPath();
		
		return "Sucess";
	}
}
