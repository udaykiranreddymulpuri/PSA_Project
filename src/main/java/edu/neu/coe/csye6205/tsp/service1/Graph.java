package edu.neu.coe.csye6205.tsp.service1;

import java.util.ArrayList;
import java.util.List;


public class Graph {

	private int V;
	private double[][] adjMatrix;

	private List<List<Integer>> adjList;

	public Graph(int V) {
		this.V = V;
		adjMatrix = new double[V][V];
		adjList = new ArrayList<>();
		for (int i = 0; i < V; i++) {
			adjList.add(new ArrayList<>());
		}
	}
	
	public void addVertex(int from,int to, double weight) {
		adjMatrix[from][to]=weight;
		adjMatrix[to][from]=weight;
		adjList.get(from).add(to);
		adjList.get(to).add(from);
	}

	public int getV() {
		return V;
	}

	public void setV(int v) {
		V = v;
	}


	public List<List<Integer>> getAdjList() {
		return adjList;
	}

	public void setAdjList(List<List<Integer>> adjList) {
		this.adjList = adjList;
	}

	public double getDistanceBetweenPoints(int sPoint, int ePoint) {
		// TODO Auto-generated method stub
		return adjMatrix[sPoint][ePoint];
	}
	

}