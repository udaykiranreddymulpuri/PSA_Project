package edu.neu.coe.csye6205.tsp.model;

import java.util.List;

public class TspTour {


    List<Integer> tour;
    Graph graph;

    double length;

    public TspTour(){

    }

    public TspTour(List<Integer> tour,double length ){
        this.tour = tour;
        this.length = length;
    }


    public List<Integer> getTour() {
        return tour;
    }

    public void setTour(List<Integer> tour) {
        this.tour = tour;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
    
}
