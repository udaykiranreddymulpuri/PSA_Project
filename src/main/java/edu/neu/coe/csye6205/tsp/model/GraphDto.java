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
	
	
	
}
