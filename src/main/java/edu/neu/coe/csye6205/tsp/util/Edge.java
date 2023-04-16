package edu.neu.coe.csye6205.tsp.util;


public class Edge  {
	public int from, to;
	public Double weight;

	public Edge(int from, int to, Double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public Edge reverse() {
		return new Edge(to, from, weight);
	}

	public Edge toEdge() {
		return new Edge(from, to, weight);
	}
	

	public Double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
	}

}
