package edu.neu.coe.csye6205.tsp;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import edu.neu.coe.csye6205.tsp.model.TspTour;
import edu.neu.coe.csye6205.tsp.service.ExcelFetchRouteService;
import edu.neu.coe.csye6205.tsp.service.MinWeightService;
import edu.neu.coe.csye6205.tsp.service.ThreeOptService;
import edu.neu.coe.csye6205.tsp.service.TwoOptService;
import edu.neu.coe.csye6205.tsp.util.Edge;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TspApplicationTests.class })
class TspApplicationTests {

	@InjectMocks
	private MinWeightService minWeightService;

	@InjectMocks
	private ExcelFetchRouteService excelFetchService;
	
	@InjectMocks
	TwoOptService opt2Service;

	@InjectMocks
	ThreeOptService opt3Service;

	@Test
	public void minimumSpanningTreeTest1() {
		GraphDto dto = InitialzeGraph();
		MstTour mstTour = minWeightService.minimumSpanningTree(dto.getEdges());
		assertTrue(mstTour.getLength() == 4);
	}

	@Test
	public void OddDegreeVertexTest() {
		List<Integer> res = new ArrayList<>(Arrays.asList(0, 2, 3, 4));
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge(1, 0, 1.0));
		edges.add(new Edge(2, 1, 1.0));
		edges.add(new Edge(3, 0, 1.0));
		edges.add(new Edge(4, 0, 1.0));
		List<Integer> oddVertex = minWeightService.oddDegreeVertices(edges);
		assertTrue(res.equals(oddVertex));
	}

	@Test
	public void findMinimumWeightMatchingTest() {
		List<Integer> res = new ArrayList<>(Arrays.asList(0, 2, 3, 4));
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge(1, 0, 1.0));
		edges.add(new Edge(2, 1, 1.0));
		edges.add(new Edge(3, 0, 1.0));
		edges.add(new Edge(4, 0, 1.0));
		GraphDto dto = InitialzeGraph();
		List<Edge> minWeightEdges = minWeightService.findMinimumWeightMatching(res, dto.getGraph());
		List<Edge> combinedGraphEdges = minWeightService.combineGraphs(edges, minWeightEdges);
		assertTrue(minWeightService.oddDegreeVertices(combinedGraphEdges).size() == 0);

	}

	@Test
	public void findEulerianTourTest() {
		List<Edge> combinedGraphEdges = new ArrayList<>();
		combinedGraphEdges.add(new Edge(2, 1, 1.0));
		combinedGraphEdges.add(new Edge(1, 0, 1.0));
		combinedGraphEdges.add(new Edge(0, 3, 1.0));
		combinedGraphEdges.add(new Edge(3, 0, 1.0));
		combinedGraphEdges.add(new Edge(2, 4, 1.0));
		combinedGraphEdges.add(new Edge(4, 0, 1.0));
		Graph mstAndminWeightCombinedGraph = new Graph(5);
		combinedGraphEdges.forEach(e -> {
			mstAndminWeightCombinedGraph.addVertex(e.from, e.to, e.weight);
		});
		List<Integer> eulerianTour = minWeightService.findEulerianTour(mstAndminWeightCombinedGraph, 0);
		assertTrue(new HashSet<>(eulerianTour).size() == 5);
	}

	@Test
	public void getTstPathAndWeightTest() {
		GraphDto dto = InitialzeGraph();
		List<Edge> combinedGraphEdges = new ArrayList<>();
		combinedGraphEdges.add(new Edge(2, 1, 1.0));
		combinedGraphEdges.add(new Edge(1, 0, 1.0));
		combinedGraphEdges.add(new Edge(0, 3, 1.0));
		combinedGraphEdges.add(new Edge(3, 0, 1.0));
		combinedGraphEdges.add(new Edge(2, 4, 1.0));
		combinedGraphEdges.add(new Edge(4, 0, 1.0));
		Graph mstAndminWeightCombinedGraph = new Graph(5);
		combinedGraphEdges.forEach(e -> {
			mstAndminWeightCombinedGraph.addVertex(e.from, e.to, e.weight);
		});
		List<Integer> eulerianTour = minWeightService.findEulerianTour(mstAndminWeightCombinedGraph, 0);
		TspTour tspTour = minWeightService.getTspPath(0, dto.getGraph(), eulerianTour);
		System.out.println(tspTour.getTour());
		assertTrue(tspTour.getLength() == 6 && new HashSet<>(tspTour.getTour()).size() == 5
				&& tspTour.getTour().get(0) == tspTour.getTour().get(tspTour.getTour().size() - 1));
	}
	
	@Test
	public void Opt2Test() {
		GraphDto dto = InitialzeGraph();
		List<Integer> tspTour=new ArrayList<>(Arrays.asList(0,3,1,2,4,0));
		TspTour tsp=new TspTour();
		tsp.setTour(tspTour);
		tsp.setLength(6);
		TspTour tsp2optTour=opt2Service.twoOpt(tsp, dto.getGraph());
		assertTrue(tsp2optTour.getLength() <= 6 && new HashSet<>(tsp2optTour.getTour()).size() == 5
				&& tsp2optTour.getTour().get(0) == tsp2optTour.getTour().get(tsp2optTour.getTour().size() - 1));
	}
	
	@Test
	public void Opt3Test() {
		GraphDto dto = InitialzeGraph();
		List<Integer> tspTour=new ArrayList<>(Arrays.asList(0,3,1,2,4,0));
		TspTour tsp=new TspTour();
		tsp.setTour(tspTour);
		tsp.setLength(6);
		TspTour tsp3optTour=opt2Service.twoOpt(tsp, dto.getGraph());
		assertTrue(tsp3optTour.getLength() <= 6 && new HashSet<>(tsp3optTour.getTour()).size() == 5
				&& tsp3optTour.getTour().get(0) == tsp3optTour.getTour().get(tsp3optTour.getTour().size() - 1));
	}
	

	public GraphDto InitialzeGraph() {
		Graph graph = new Graph(5);
		graph.addVertex(0, 1, 1);
		graph.addVertex(0, 2, 2);
		graph.addVertex(0, 3, 1);
		graph.addVertex(0, 4, 1);
		graph.addVertex(1, 2, 1);
		graph.addVertex(1, 3, 2);
		graph.addVertex(1, 4, 1);
		graph.addVertex(2, 3, 1);
		graph.addVertex(2, 4, 1);
		graph.addVertex(3, 4, 1);
		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < graph.getAdjMatrix().length; i++) {
			for (int j = 0; j < graph.getAdjMatrix()[i].length && i != j; j++) {
				edges.add(new Edge(i, j, graph.getAdjMatrix()[i][j]));
			}
		}
		GraphDto graphDto = new GraphDto(graph, edges);
		return graphDto;
	}
}
