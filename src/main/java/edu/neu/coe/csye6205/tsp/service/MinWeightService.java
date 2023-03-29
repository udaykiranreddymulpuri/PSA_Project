package edu.neu.coe.csye6205.tsp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Route;
import edu.neu.coe.csye6205.tsp.repository.RouteWeightRepository;
import edu.neu.coe.csye6205.tsp.util.Edge;
import edu.neu.coe.csye6205.tsp.util.UnionFind;

@Service
public class MinWeightService {

	@Autowired
	RouteWeightRepository routeRepo;
	

	int V = 30;

	public void findPath() {
		// TODO Auto-generated method stub
		Set<Edge> Edges = new HashSet<>();
		List<Route> routes = routeRepo.findAll();
		routes.forEach(r -> {
			Edge e = new Edge(r.getFromLoc(), r.getToLoc(), r.getWeight());
			if (r.getFromLoc() <= 30 && r.getToLoc() <= 30)
				Edges.add(e);
		});
		List<Edge> edges = new ArrayList<>();
		Edges.forEach(e -> {
			edges.add(e);
		});
		List<Edge> mst = minimumSpanningTree(edges);
		for (Edge e : mst) {
			System.out.println(e.from + " - " + e.to + ": " + e.weight);
		}

		Set<Integer> oddver = oddDegreeVertices(mst);
		System.out.println(oddver);
		System.out.println("************************************");
		List<Edge> mst1 = minimumWeightPerfectMatching(oddver, edges);
		for (Edge e : mst1) {
			System.out.println(e.from + " - " + e.to + ": " + e.weight);
		}
		System.out.println("************************************");

		List<Edge> mst2 = combineGraphs(mst, mst1);
		for (Edge e : mst2) {
			System.out.println(e.from + " - " + e.to + ": " + e.weight);
		}
		System.out.println("************************************");

		List<Integer> eulerianTour = eulerianTour(mst2);
		System.out.println(eulerianTour);
		System.out.println("************************************");
		Set<Integer> hs = new HashSet<>();
		List<Edge> outPath = shortcutTour(eulerianTour, mst2);
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

//  Kruskal's Algorithm
	public List<Edge> minimumSpanningTree(List<Edge> edges) {
		List<Edge> sortedEdges = edges;
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
				uf.union(edge.from, edge.to);
			}
		}
		return mst;
	}

	Set<Integer> oddDegreeVertices(List<Edge> mst) {
		Map<Integer, Integer> degreeMap = new HashMap<>();
		for (Edge e : mst) {
			degreeMap.put(e.from, degreeMap.getOrDefault(e.from, 0) + 1);
			degreeMap.put(e.to, degreeMap.getOrDefault(e.to, 0) + 1);
		}
		Set<Integer> oddVertices = new HashSet<>();
		for (int vertex : degreeMap.keySet()) {
			if (degreeMap.get(vertex) % 2 == 1) {
				oddVertices.add(vertex);
			}
		}
		System.out.println(degreeMap);
		System.out.println(degreeMap.size());
		return oddVertices;
	}

	List<Edge> minimumWeightPerfectMatching(Set<Integer> oddVertices, List<Edge> edges) {
		List<Edge> matching = new ArrayList<>();
		Set<Integer> matchedVertices = new HashSet<>();

		for (int v : oddVertices) {
			if (matchedVertices.contains(v)) {
				// Skip this vertex if it has already been matched
				continue;
			}

			Edge minEdge = null;
			for (Edge e : edges) {
				if ((e.from == v || e.to == v) && oddVertices.contains(e.from) && oddVertices.contains(e.to)) {
					if (minEdge == null || e.weight < minEdge.weight) {
						minEdge = e;
					}
				}
			}

			if (minEdge != null) {
				matching.add(minEdge);
				matchedVertices.add(v);
				matchedVertices.add(minEdge.from);
				matchedVertices.add(minEdge.to);
			}
		}

		oddVertices.removeAll(matchedVertices);
		return matching;
	}

	

	List<Edge> combineGraphs(List<Edge> mst, List<Edge> matching) {
		Set<Edge> eulerianGraph = new HashSet<>(mst);
		eulerianGraph.addAll(matching);
		List<Edge> eulerianGraphList = new ArrayList<>(eulerianGraph);
		return eulerianGraphList;
	}

//	public List<Integer> eulerianTour(List<Edge> edges) {
//		Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
//		for (Edge edge : edges) {
//			adjacencyList.computeIfAbsent(edge.from, k -> new ArrayList<>()).add(edge.to);
//			adjacencyList.computeIfAbsent(edge.to, k -> new ArrayList<>()).add(edge.from);
//		}
//		System.out.println(adjacencyList);
//		int startVertex = Integer.MAX_VALUE;
//		System.out.println("*******eulerianTour start*********");
//		for(int i=0;i<adjacencyList.size();i++) {
//			if(adjacencyList.get(i).size()<startVertex)
//				startVertex=i;
//			if(adjacencyList.get(i).size()%2==1)
//				System.out.print(i+",  ");
////			startVertex=Math.min(adjacencyList.get(i).size(), startVertex);
//		}
//		System.out.println("Start Vetex "+ startVertex);
//		List<Integer> tour = new ArrayList<>();
//		Stack<Integer> stack = new Stack<>();
//		stack.push(startVertex);
//		while (!stack.isEmpty()) {
//			int u = stack.peek();
//			List<Integer> neighbors = adjacencyList.get(u);
//			if (neighbors != null && !neighbors.isEmpty()) {
//				int v = neighbors.get(0);
//				stack.push(v);
//				adjacencyList.get(u).remove(Integer.valueOf(v));
//				adjacencyList.get(v).remove(Integer.valueOf(u));
//			} else {
//				stack.pop();
//				tour.add(u);
//			}
//		}
//		return tour;
//	}
	public List<Integer> eulerianTour(List<Edge> edges) {
		Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
		for (Edge edge : edges) {
			adjacencyList.computeIfAbsent(edge.from, k -> new ArrayList<>()).add(edge.to);
			adjacencyList.computeIfAbsent(edge.to, k -> new ArrayList<>()).add(edge.from);
		}
		System.out.println(adjacencyList);
		int startVertex = Integer.MAX_VALUE;
		System.out.println("*******eulerianTour start*********");
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
