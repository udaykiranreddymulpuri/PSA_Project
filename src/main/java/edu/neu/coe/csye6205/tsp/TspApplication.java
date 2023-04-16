package edu.neu.coe.csye6205.tsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import edu.neu.coe.csye6205.tsp.service.MinWeightService;

@SpringBootApplication
public class TspApplication implements CommandLineRunner {

	@Autowired
	ApplicationContext context;
	@Autowired
	MinWeightService minWeightService;
	
	public static void main(String[] args) {
		SpringApplication.run(TspApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("************ Start of TSP ******************");
		minWeightService.findPath();
		System.out.println("************ End of TSP ******************");
	}

}
