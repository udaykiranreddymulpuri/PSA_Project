package edu.neu.coe.csye6205.tsp.service;

import java.util.ArrayList;
import java.util.Arrays;
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
public class MinWeightService {

	@Autowired
	RouteWeightRepository routeRepo;
	

//	int V = 31;

	public void findPath() {
		// TODO Auto-generated method stub
		Set<Edge> Edges = new HashSet<>();
		List<Route> routes = routeRepo.findAll();
		routes.forEach(r -> {
			Edge e = new Edge(r.getFromLoc(), r.getToLoc(), r.getWeight());
			if (r.getFromLoc() <= 20 && r.getToLoc() <= 20)
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
		System.out.println("************");
//		List<Edge> updateEdge=new ArrayList<>();
//		for(int i=0;i<edges.size();i++) {
//			boolean b1=true;
//			for(int j=0;j<mst.size();j++) {
//				if(edges.get(i).from==mst.get(j).from && edges.get(i).to==mst.get(j).to)
//					b1=false;
//			}
//			if(b1)
//				updateEdge.add(edges.get(i));
//		}
//		System.out.println("updated edge");
//		System.out.println(updateEdge);
		List<Edge> mst1 = minimumWeightPerfectMatching1(oddver, edges);
//		System.out.println("test method start********");
//		minimumWeightPerfectMatching1(oddver, edges);
//		System.out.println("test method end********");
		for (Edge e : mst1) {
			System.out.println(e.from + " - " + e.to + ": " + e.weight);
		}
		System.out.println("************");

		List<Edge> mst2 = combineGraphs(mst, mst1);
		for (Edge e : mst2) {
			System.out.println(e.from + " - " + e.to + ": " + e.weight);
		}
		System.out.println("************");

		List<Integer> eulerianTour = eulerianTour(mst2);
		System.out.println(eulerianTour);
		System.out.println("************");
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
		PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
		for(int i=0;i<edges.size();i++) {
			edgeQueue.add(edges.get(i));
		}
//		Map<Integer, Map<Integer, Double>> adjMap = util(edges);
		boolean[] matched = new boolean[11];
//		while (!edgeQueue.isEmpty()) {
//            Edge edge = edgeQueue.poll();
//            int index1 = edge.from;
//            int index2 = edge.to;
//
//            if (!matched[index1] && !matched[index2]) {
//                matched[index1] = true;
//                matched[index2] = true;
//                matching.add(edge);
//                adjMap.get(edge.getU()).add(edge.getV());
//                adjList.get(edge.getV()).add(edge.getU());
//                Node start = g.getNode(edge.getU());
//                Node end = g.getNode(edge.getV());
//                double startXPoint = start.getX();
//                double startYPoint = start.getY();
//                double endXPoint = end.getX();
//                double endYPoint = end.getY();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
////                Platform.runLater(() -> {
////                    gc.setStroke(Color.RED);
////                    gc.strokeLine(startXPoint,startYPoint,endXPoint,endYPoint);
////                });
//            }
//            if(matching.size() == points.size()/2)
//                break;
//        }
//        g.setAdjList(adjList);
        
		for (int v : oddVertices) {
//			if (matchedVertices.contains(v)) {
//				// Skip this vertex if it has already been matched
//				continue;
//			}

			Edge minEdge = null;
			for (Edge e : edgeQueue) {
				if ((e.from == v || e.to == v)) {
//					if ((e.from == v || e.to == v) && oddVertices.contains(e.from) && oddVertices.contains(e.to))
					if (minEdge == null || e.weight < minEdge.weight) {
						minEdge = e;
					}
				}
			}

			if (minEdge != null&&!matched[minEdge.from]&&!matched[minEdge.to]) {
				matched[minEdge.from]=true;
				matched[minEdge.to]=true;
				matching.add(minEdge);
				matchedVertices.add(v);
				matchedVertices.add(minEdge.from);
				matchedVertices.add(minEdge.to);
			}
		}

		oddVertices.removeAll(matchedVertices);
		return matching;
	}
	
	List<Edge>  minimumWeightPerfectMatching1(Set<Integer> oddVertices, List<Edge> edges) {
		int n=21;
		List<Edge> edge=new ArrayList<>();
		double[][] graph=new double[n][n];
		for(int i=0;i<edges.size();i++) {
			graph[edges.get(i).from][edges.get(i).to]=edges.get(i).weight;
			graph[edges.get(i).to][edges.get(i).from]=edges.get(i).weight;
		}
		int[] matching = new int[n];
        Arrays.fill(matching, -1);

        for (int i = 0; i < n; i++) {
            if (matching[i] == -1) {
                augmentPath(i, graph, matching, new boolean[n]);
            }
        }

        // print the minimum weight matching
        for (int i = 0; i < n; i++) {
            if (matching[i] != -1) {
                System.out.println(i + " - " + matching[i]+" "+graph[i][matching[i]]);
                edge.add(new Edge(i,matching[i],graph[i][matching[i]]));
            }
        }
        return edge;
	}
	public static boolean augmentPath(int u, double[][] graph, int[] matching, boolean[] visited) {
        visited[u] = true;

        for (int v = 0; v < graph.length; v++) {
            if (graph[u][v] != 0 && !visited[v]) {
                visited[v] = true;

                if (matching[v] == -1 || augmentPath(matching[v], graph, matching, visited)) {
                    matching[v] = u;
                    matching[u] = v;
                    return true;
                }
            }
        }

        return false;
    }
//	public static List<Edge> findMinimumWeightMatching(List<Node> points, Graph g, GraphicsContext gc) {
//        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
//        boolean[] matched = new boolean[g.getSize()];
//        int sPoint;
//        int ePoint;
//        double weight;
//        for (int i = 0; i < points.size(); i++) {
//            for (int j = i + 1; j < points.size(); j++) {
//                sPoint = points.get(i).getPos();
//                ePoint = points.get(j).getPos();
//                weight = g.getDistanceBetweenPoints(sPoint,ePoint);
//                edgeQueue.offer(new Edge(points.get(i).getPos(), points.get(j).getPos(),weight));
//            }
//        }
//        List<List<Integer>> adjList = g.getAdjList();
//        List<Edge> matching = new ArrayList<>();
//        
//        while (!edgeQueue.isEmpty()) {
//            Edge edge = edgeQueue.poll();
//            int index1 = edge.getU();
//            int index2 = edge.getV();
//
//            if (!matched[index1] && !matched[index2]) {
//                matched[index1] = true;
//                matched[index2] = true;
//                matching.add(edge);
//                adjList.get(edge.getU()).add(edge.getV());
//                adjList.get(edge.getV()).add(edge.getU());
//                Node start = g.getNode(edge.getU());
//                Node end = g.getNode(edge.getV());
//                double startXPoint = start.getX();
//                double startYPoint = start.getY();
//                double endXPoint = end.getX();
//                double endYPoint = end.getY();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
////                Platform.runLater(() -> {
////                    gc.setStroke(Color.RED);
////                    gc.strokeLine(startXPoint,startYPoint,endXPoint,endYPoint);
////                });
//            }
//            if(matching.size() == points.size()/2)
//                break;
//        }
//        g.setAdjList(adjList);
//        return matching;
//    }
	

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
//		System.out.println("**eulerianTour start****");
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