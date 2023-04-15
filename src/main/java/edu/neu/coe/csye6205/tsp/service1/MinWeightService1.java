package edu.neu.coe.csye6205.tsp.service1;

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

import edu.neu.coe.csye6205.tsp.model.Route;
import edu.neu.coe.csye6205.tsp.repository.RouteWeightRepository;
import edu.neu.coe.csye6205.tsp.util.Edge;
import edu.neu.coe.csye6205.tsp.util.UnionFind;

@Service
public class MinWeightService1 {

	@Autowired
	RouteWeightRepository routeRepo;

	private int v = 10;

	public void findPath() {
		// TODO Auto-generated method stub
		Graph graph = new Graph(this.v);
		List<Edge> edges = new ArrayList<>();
		List<Route> routes = routeRepo.findAll();
		routes.forEach(r -> {
			if (r.getFromLoc() <= this.v - 1 && r.getToLoc() <= this.v - 1) {
				graph.addVertex(r.getFromLoc(), r.getToLoc(), r.getWeight());
				edges.add(new Edge(r.getFromLoc(), r.getToLoc(), r.getWeight()));
			}
		});

		List<Edge> mst = minimumSpanningTree(edges);

		List<Integer> oddver = oddDegreeVertices(mst);
//		System.out.println(oddver);
//		System.out.println("************");
		List<Edge> minWeightMatch = findMinimumWeightMatching(oddver, graph);
//		System.out.println(minWeightMatch);
//		for (Edge e : mst1) {
//			System.out.println(e.from + " - " + e.to + ": " + e.weight);
//		}
//		System.out.println("************");
//
		List<Edge> combineGraph = combineGraphs(mst, minWeightMatch);
//		for (Edge e : combineGraph) {
//			System.out.println(e.from + " - " + e.to + ": " + e.weight);
//		}
//		System.out.println("************");
//
		List<Integer> eulerianTour = eulerianTour(combineGraph);
//		System.out.println(eulerianTour);
//		System.out.println("************");
		Set<Integer> hs = new HashSet<>();
		List<Edge> outPath = shortcutTour(eulerianTour, combineGraph);
		outPath.forEach(o -> {
			if (!hs.contains(o.from))
				hs.add(o.from);

			if (!hs.contains(o.to))
				hs.add(o.to);

		});
		System.out.println("vertex count " + hs.size());
		System.out.println(outPath);
		double totalWeight = 0;
		for (int i = 0; i < outPath.size(); i++) {
			if (outPath.get(i).weight != null)
				totalWeight += outPath.get(i).weight;
		}
		System.out.println(totalWeight);

	}

	public List<Edge> minimumSpanningTree(List<Edge> edges) {
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
		System.out.println(sortedEdges);
		List<Edge> mst = new ArrayList<>();
		UnionFind uf = new UnionFind(edges.size());
		for (Edge edge : sortedEdges) {
			if (!uf.connected(edge.from, edge.to)) {
				mst.add(edge);
				weight += edge.getWeight();
				uf.union(edge.from, edge.to);
			}
		}
		System.out.println(weight);
		return mst;
	}

	List<Integer> oddDegreeVertices(List<Edge> mst) {
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
		System.out.println(degreeMap);
		System.out.println(degreeMap.size());
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

	List<Edge> combineGraphs(List<Edge> mst, List<Edge> matching) {
		Set<Edge> eulerianGraph = new HashSet<>(mst);
		eulerianGraph.addAll(matching);
		List<Edge> eulerianGraphList = new ArrayList<>(eulerianGraph);
		return eulerianGraphList;
	}
	
	public List<Integer> eulerianTour(List<Edge> edges) {
		Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
		for (Edge edge : edges) {
			adjacencyList.computeIfAbsent(edge.from, k -> new ArrayList<>()).add(edge.to);
			adjacencyList.computeIfAbsent(edge.to, k -> new ArrayList<>()).add(edge.from);
		}
		System.out.println(adjacencyList);
		int startVertex = Integer.MAX_VALUE;
		System.out.println("**eulerianTour start****");
		for (int i = 0; i < adjacencyList.size(); i++) {
			if (adjacencyList.get(i).size() < startVertex)
				startVertex = i;
			if (adjacencyList.get(i).size() % 2 == 1)
				System.out.print(i + ",  ");
//			startVertex=Math.min(adjacencyList.get(i).size(), startVertex);
		}
		System.out.println("Start Vetex " + startVertex);
		List<Integer> tour = new ArrayList<>();
		Stack<Integer> stack = new Stack<>();
		stack.push(startVertex);
		while (!stack.isEmpty()) {
			int u = stack.peek();
			List<Integer> neighbors = adjacencyList.get(u);
			if (neighbors != null && !neighbors.isEmpty()) {
				int v = neighbors.get(0);
				stack.push(v);
				adjacencyList.get(u).remove(Integer.valueOf(v));
				adjacencyList.get(v).remove(Integer.valueOf(u));
			} else {
				stack.pop();
				tour.add(u);
			}
		}
		return tour;
	}
	
	public List<Edge> shortcutTour(List<Integer> eulerianTour, List<Edge> edges) {
		List<Edge> hamiltonianTour = new ArrayList<>();
		Set<Integer> visited = new HashSet<>();
		Map<Integer, Map<Integer, Double>> map = util(edges);
		System.out.println(map);
		for (int i = 0; i < eulerianTour.size() - 1; i++) {
			int u = eulerianTour.get(i);
			int v = eulerianTour.get(i + 1);
			System.out.println(u + " " + v);
			if (!visited.contains(u)) {
				hamiltonianTour.add(new Edge(u, v, map.get(u).get(v)));
				visited.add(u);
			}
		}

		return hamiltonianTour;
	}

	Map<Integer, Map<Integer, Double>> util(List<Edge> edges) {
		Map<Integer, Map<Integer, Double>> map = new HashMap<>();
		for (Edge edge : edges) {
			Map<Integer, Double> hm;
			if (map.get(edge.from) == null)
				hm = new HashMap<>();
			else
				hm = map.get(edge.from);
			hm.put(edge.to, hm.getOrDefault(edge.to, edge.weight));
			map.put(edge.from, hm);
		}
		for (Edge edge : edges) {
			Map<Integer, Double> hm;
			if (map.get(edge.to) == null)
				hm = new HashMap<>();
			else
				hm = map.get(edge.to);
			hm.put(edge.from, hm.getOrDefault(edge.from, edge.weight));
			map.put(edge.to, hm);
		}
		System.out.println(map);
		return map;
	}
}