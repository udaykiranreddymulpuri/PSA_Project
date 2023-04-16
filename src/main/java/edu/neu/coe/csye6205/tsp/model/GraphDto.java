package edu.neu.coe.csye6205.tsp.model;

import java.util.List;

import edu.neu.coe.csye6205.tsp.util.Edge;
import lombok.Data;

@Data
public class GraphDto {

	private Graph graph;
	private List<Edge> edges;
	
	public GraphDto(Graph graph, List<Edge> edges) {
		this.graph = graph;
		this.edges = edges;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	
	
	
}
