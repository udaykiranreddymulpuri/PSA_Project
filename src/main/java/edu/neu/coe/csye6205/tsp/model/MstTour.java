package edu.neu.coe.csye6205.tsp.model;

import java.util.List;

import edu.neu.coe.csye6205.tsp.util.Edge;
import lombok.Data;

@Data
public class MstTour {

	List<Edge> edges;
	double length;
	public MstTour(List<Edge> edges, double length) {
		super();
		this.edges = edges;
		this.length = length;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	
	
}
