package edu.neu.coe.csye6205.tsp.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Graph;
import edu.neu.coe.csye6205.tsp.model.GraphDto;
import edu.neu.coe.csye6205.tsp.model.MstTour;
import edu.neu.coe.csye6205.tsp.model.Route;
import edu.neu.coe.csye6205.tsp.model.TspTour;
import edu.neu.coe.csye6205.tsp.model.Vertex;
import edu.neu.coe.csye6205.tsp.util.Edge;
import edu.neu.coe.csye6205.tsp.util.UnionFind;
import edu.neu.coe.csye6205.tsp.util.VertexUtil;

@Service
public class MinWeightService {

//	@Autowired
//	RouteWeightRepository routeRepo;

	@Autowired
	TwoOptService opt2Service;

	@Autowired
	ThreeOptService opt3Service;

	@Autowired
	ExcelFetchRouteService excelFetchService;

	@Autowired
	VertexUtil vertexUtil;

	@Autowired
	SimulatedAnnealingService simulatedAnnealingService;

	private int v = 585;

	public GraphDto InitialzeGraph(int size) {
		Graph graph = new Graph(size);
		List<Edge> edges = new ArrayList<>();
		// uncomment if you wanna execute using DB
//		List<Route> routes = routeRepo.findAll();
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

	public void findPath() {
		// TODO Auto-generated method stub
//		Graph graph = new Graph(this.v);
//		List<Edge> edges = new ArrayList<>();
		Timestamp start = new Timestamp(System.currentTimeMillis());
		Map<Integer, String> vertexMap = vertexUtil.getVertexMapping();
		List<Vertex> vertexList = new ArrayList<>();
		GraphDto graphDto = InitialzeGraph(this.v);
		Graph graph = graphDto.getGraph();
		List<Edge> edges = graphDto.getEdges();
		// uncomment if you wanna execute using DB
//		List<Route> routes = routeRepo.findAll();
		System.out.println("***************** Start of Execution ********************");
		System.out.println("No of Vertexs " + this.v);
		System.out.println("***************************************************************");
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		MstTour mstTour = minimumSpanningTree(edges);
		System.out.println("MST Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		System.out.println();
		List<Integer> oddver = oddDegreeVertices(mstTour.getEdges());
		oddver.forEach(oddVer -> {
			Vertex v = new Vertex(oddVer, vertexMap.get(oddVer));
			vertexList.add(v);
		});
		System.out.println("*********************");

		System.out.println("List of Odd vertices ");
		System.out.println(vertexList);
		System.out.println("***************************************************************");
		startTime = new Timestamp(System.currentTimeMillis());
		List<Edge> minWeightMatch = findMinimumWeightMatching(oddver, graph);
		System.out.println("findMinimumWeightMatching Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
//		System.out.println("List of min weight matching pairs "+minWeightMatch);
//		System.out.println(minWeightMatch);
//		System.out.println("*********************");
		List<Edge> combineGraph = combineGraphs(mstTour.getEdges(), minWeightMatch);
		Graph mstAndminWeightCombinedGraph = new Graph(this.v);
		combineGraph.forEach(e -> {
			mstAndminWeightCombinedGraph.addVertex(e.from, e.to, e.weight);
		});
		startTime = new Timestamp(System.currentTimeMillis());
		List<Integer> eulerianTour = findEulerianTour(mstAndminWeightCombinedGraph, 0);
		System.out.println("findEulerianTour Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		System.out.println("  Eulerian Tour ");
		List<Vertex> EulerianVertexTour = new ArrayList<>();
		eulerianTour.stream().forEach(e -> {
			Vertex v = new Vertex(e, vertexMap.get(e));
			EulerianVertexTour.add(v);
		});
		System.out.println(EulerianVertexTour);
		System.out.println("***************************************************************");
		startTime = new Timestamp(System.currentTimeMillis());
		TspTour tsp = getTspPath(0, graph, eulerianTour);
		System.out.println("getTspPath Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		List<Vertex> tspVertexTour = new ArrayList<>();
		tsp.getTour().stream().forEach(e -> {
			Vertex v = new Vertex(e, vertexMap.get(e));
			tspVertexTour.add(v);
		});
		System.out.println(" Tsp Weight in meters " + tsp.getLength() * 1000);
		System.out.println("********************   Tsp Tour *******************************");
		System.out.println(printUtil(tspVertexTour));
		System.out.println("***************************************************************");
		startTime = new Timestamp(System.currentTimeMillis());
		TspTour tsp2optTour = opt2Service.twoOptTour(tsp, graph);
		System.out.println("2Opt Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		List<Vertex> opt2VertexTour = new ArrayList<>();
		tsp2optTour.getTour().stream().forEach(e -> {
			Vertex v = new Vertex(e, vertexMap.get(e));
			opt2VertexTour.add(v);
		});
		System.out.println(" 2 Opt Tsp Weight in meters " + tsp2optTour.getLength() * 1000);
		System.out.println("******************** 2 Opt Tsp Tour ***************************");
		System.out.println(printUtil(opt2VertexTour));
		System.out.println("***************************************************************");
		startTime = new Timestamp(System.currentTimeMillis());
		TspTour tsp3optTour = opt3Service.threeOptTour(tsp, graph);
		System.out.println("3Opt Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		List<Vertex> opt3VertexTour = new ArrayList<>();
		tsp3optTour.getTour().stream().forEach(e -> {
			Vertex v = new Vertex(e, vertexMap.get(e));
			opt3VertexTour.add(v);
		});
		System.out.println(" 3 Opt Tsp Weight in meters " + tsp3optTour.getLength() * 1000);
		System.out.println("******************** 3 Opt Tsp Tour ***************************");
		System.out.println(printUtil(opt3VertexTour));
		System.out.println("***************************************************************");
		startTime = new Timestamp(System.currentTimeMillis());
		TspTour tspSimulated = simulatedAnnealingService.simulatedAnnealingTour(tsp, 2, 0.95, graph);
		System.out.println("Simulated Annealing Opt Execution Time in Millisecs: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - startTime.getTime()));
		List<Vertex> simulatedAnnealingVertexTour = new ArrayList<>();
		tspSimulated.getTour().stream().forEach(e -> {
			Vertex v = new Vertex(e, vertexMap.get(e));
			simulatedAnnealingVertexTour.add(v);
		});
		System.out.println(" Simulated Annealing Tsp Weight in meters " + (tspSimulated.getLength()) * 1000);
		System.out.println(" Simulated Annealing Tsp Tour " + simulatedAnnealingVertexTour);
		System.out.println("****************** Simulated Annealing Tsp Tour *************************");
		System.out.println(printUtil(simulatedAnnealingVertexTour));
		System.out.println("Execution Time in Minutes: "
				+ (new Timestamp(System.currentTimeMillis()).getTime() - start.getTime()) / 60000);
		System.out.println("****** End of Execution *******");

	}

	public String printUtil(List<Vertex> tspVertexTour) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tspVertexTour.size() - 4; i = i + 4) {

		}
		for (int i = 0; i < tspVertexTour.size(); i = i + 4) {
			sb.append(tspVertexTour.get(i));
			if (i + 1 < tspVertexTour.size())
				sb.append(tspVertexTour.get(i + 1));
			if (i + 2 < tspVertexTour.size())
				sb.append(tspVertexTour.get(i + 2));
			if (i + 3 < tspVertexTour.size())
				sb.append(tspVertexTour.get(i + 3));
			sb.append("\n");
		}
		return sb.toString();
	}

	public MstTour minimumSpanningTree(List<Edge> edges) {
		List<Edge> sortedEdges = edges;
		double weight = 0;
		Collections.sort(sortedEdges, new Comparator<Edge>() {
			@Override
			public int compare(Edge edge1, Edge edge2) {
				if (edge1.weight < edge2.weight) {
					return -1;
				} else if (edge1.weight > edge2.weight) {
					return 1;
				} else {
					if (edge1.from < edge2.from)
						return -1;
					else if (edge1.from > edge2.from)
						return 1;
					else
						return 0;
				}

			}
		});
		List<Edge> mst = new ArrayList<>();
		UnionFind uf = new UnionFind(edges.size());
		for (Edge edge : sortedEdges) {
			if (!uf.connected(edge.from, edge.to)) {
				mst.add(edge);
				weight += edge.getWeight();
				uf.union(edge.from, edge.to);
			}
		}
		System.out.println("Minimum Spaninng Tree weight in meters " + weight * 1000);
		MstTour mstTour = new MstTour(mst, weight);
		return mstTour;
	}

	public List<Integer> oddDegreeVertices(List<Edge> mst) {
		Map<Integer, Integer> degreeMap = new HashMap<>();
		for (Edge e : mst) {
			degreeMap.put(e.from, degreeMap.getOrDefault(e.from, 0) + 1);
			degreeMap.put(e.to, degreeMap.getOrDefault(e.to, 0) + 1);
		}
		List<Integer> oddVertices = new ArrayList<>();
		for (int vertex : degreeMap.keySet()) {
			if (degreeMap.get(vertex) % 2 == 1 && !oddVertices.contains(vertex)) {
				oddVertices.add(vertex);
			}
		}
		return oddVertices;
	}

	public List<Edge> findMinimumWeightMatching(List<Integer> points, Graph g) {
		PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
		boolean[] matched = new boolean[this.v];
		int sPoint;
		int ePoint;
		double weight;
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				sPoint = points.get(i);
				ePoint = points.get(j);
				weight = g.getDistanceBetweenPoints(sPoint, ePoint);
				edgeQueue.offer(new Edge(points.get(i), points.get(j), weight));
			}
		}
		List<List<Integer>> adjList = g.getAdjList();
		List<Edge> matching = new ArrayList<>();
		while (!edgeQueue.isEmpty()) {
			Edge edge = edgeQueue.poll();
			int index1 = edge.from;
			int index2 = edge.to;

			if (!matched[index1] && !matched[index2]) {
				matched[index1] = true;
				matched[index2] = true;
				matching.add(edge);
				adjList.get(edge.from).add(edge.to);
				adjList.get(edge.from).add(edge.to);
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			if (matching.size() == points.size() / 2)
				break;
		}
		g.setAdjList(adjList);
		return matching;
	}

	public List<Edge> combineGraphs(List<Edge> mst, List<Edge> matching) {
		Set<Edge> eulerianGraph = new HashSet<>(mst);
		eulerianGraph.addAll(matching);
		List<Edge> eulerianGraphList = new ArrayList<>(eulerianGraph);
		return eulerianGraphList;
	}

	static void dfs(Map<Integer, List<Edge>> adjacencyList, int startVertex, List<Integer> cycle) {
		for (Edge x : adjacencyList.get(startVertex)) {
			if (!cycle.contains(x.to)) {
				cycle.add(x.to);
				dfs(adjacencyList, x.to, cycle);
			}
		}
	}

//	public List<Edge> shortcutTour(List<Integer> eulerianTour, List<Edge> edges, Graph g) {
//		List<Edge> hamiltonianTour = new ArrayList<>();
//		Set<Integer> visited = new HashSet<>();
//		Map<Integer, Map<Integer, Double>> map = util(edges);
//		System.out.println(map);
//		for (int i = 0; i < eulerianTour.size() - 1; i++) {
//			int u = eulerianTour.get(i);
//			int v = eulerianTour.get(i + 1);
//			if (!visited.contains(u)) {
//				System.out.println(u + "  -  " + v + " " + g.getDistanceBetweenPoints(u, v));
//				hamiltonianTour.add(new Edge(u, v, g.getDistanceBetweenPoints(u, v)));
//				visited.add(u);
//			}
//		}
//
//		return hamiltonianTour;
//	}

	public static List<Integer> findEulerianTour(Graph g, int start) {
		List<List<Integer>> adjList = g.getAdjList();
		int n = adjList.size();
		List<Integer> tour = new ArrayList<>();
		Stack<Integer> stack = new Stack<>();
		int[] degree = new int[n];
		for (int i = 0; i < adjList.size(); i++) {
			degree[i] = adjList.get(i).size();
		}
		stack.push(start);
		while (!stack.isEmpty()) {
			int v = stack.peek();
			if (degree[v] == 0) {
				tour.add(v);
				stack.pop();
			} else {
				int t = adjList.get(v).remove(--degree[v]);
				adjList.get(t).remove((Integer) (v));
				degree[t]--;
				stack.push(t);
			}
		}
		return tour;
	}

	public TspTour getTspPath(int start, Graph g, List<Integer> tour) {
		TspTour tspTour = new TspTour();
		Graph tspGraph = new Graph(g.getV());
		double tspWeight = 0.0;
		boolean[] visited = new boolean[tour.size()];
		visited[0] = true;
		List<Integer> tspTourList = new ArrayList<>();
		tspTourList.add(start);
		int u = start;
		int v;
		for (int i = 1; i < tour.size(); i++) {
			v = tour.get(i);
			if (!visited[v] || i == tour.size() - 1) {
				visited[tour.get(i)] = true;
//	              System.out.println(u+" - "+v+" "+g.getDistanceBetweenPoints(u,v));
				tspGraph.addVertex(u, v, g.getDistanceBetweenPoints(u, v));
				tspWeight += g.getDistanceBetweenPoints(u, v);
				tspTourList.add(v);
				u = v;
			}
		}
		tspTour.setTour(tspTourList);
		tspTour.setLength(tspWeight);
		tspTour.setGraph(tspGraph);
		return tspTour;
	}

}
