package edu.neu.coe.csye6205.tsp.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import com.graphbuilder.struc.LinkedList;

public class WeightedGraph {
    
    private Map<Integer, List<Edge1>> adjList;
    
    public WeightedGraph() {
        adjList = new HashMap<>();
    }
    
    public void addVertex(int vertex) {
        adjList.put(vertex, new LinkedList<>());
    }
    
    public void addEdge(int src, int dest, int weight) {
        Edge1 edge = new Edge1(dest, weight);
        adjList.get(src).add(edge);
    }
    
    public List<Edge1> getNeighbors(int vertex) {
        return adjList.get(vertex);
    }

	public Map<Integer, List<Edge1>> getAdjList() {
		return adjList;
	}

	public void setAdjList(Map<Integer, List<Edge1>> adjList) {
		this.adjList = adjList;
	}
    
}