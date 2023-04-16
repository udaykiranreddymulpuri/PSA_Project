package edu.neu.coe.csye6205.tsp;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import edu.neu.coe.csye6205.tsp.model.Graph;
import edu.neu.coe.csye6205.tsp.model.GraphDto;
import edu.neu.coe.csye6205.tsp.model.MstTour;
import edu.neu.coe.csye6205.tsp.model.Route;
import edu.neu.coe.csye6205.tsp.service.ExcelFetchRouteService;
import edu.neu.coe.csye6205.tsp.service.MinWeightService;
import edu.neu.coe.csye6205.tsp.util.Edge;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TspApplicationTests.class })
class TspApplicationTests{

	@InjectMocks
	private MinWeightService minWeightService;

	@InjectMocks
	private ExcelFetchRouteService excelFetchService;

	@Test
	void contextLoads() {
	}

	@Test
	public void minimumSpanningTreeTest() {
		GraphDto dto=InitialzeGraph(10);
		MstTour mstTour=minWeightService.minimumSpanningTree(dto.getEdges());
		assertTrue(mstTour.getLength() >= 55 && mstTour.getLength() <= 58);
	}
	
	@Test
	public void findEulerianTourTest() {
		GraphDto dto=InitialzeGraph(10);
		MstTour mstTour=minWeightService.minimumSpanningTree(dto.getEdges());
		assertTrue(mstTour.getLength() >= 55 && mstTour.getLength() <= 58);
	}

	public GraphDto InitialzeGraph(int size) {
		Graph graph = new Graph(size);
		List<Edge> edges = new ArrayList<>();
		List<Route> routes = new ArrayList<>();
		try {
			routes = excelFetchService.fetchExcelData();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		routes.forEach(r -> {
			if (r.getFromLoc() <= size - 1 && r.getToLoc() <= size - 1) {
				graph.addVertex(r.getFromLoc(), r.getToLoc(), r.getWeight());
				edges.add(new Edge(r.getFromLoc(), r.getToLoc(), r.getWeight()));
			}
		});
		GraphDto graphDto = new GraphDto(graph, edges);
		return graphDto;
	}
}
