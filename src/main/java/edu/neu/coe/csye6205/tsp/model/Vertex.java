package edu.neu.coe.csye6205.tsp.model;

public class Vertex {

	private int vertex;
	private String crimeId;

	public Vertex(int vertex, String crimeId) {
		super();
		this.vertex = vertex;
		this.crimeId = crimeId;
	}

	public int getVertex() {
		return vertex;
	}

	public void setVertex(int vertex) {
		this.vertex = vertex;
	}

	public String getCrimeId() {
		return crimeId;
	}

	public void setCrimeId(String crimeId) {
		this.crimeId = crimeId;
	}

	@Override
	public String toString() {
		return String.format("vertexId = %-3s CrimeId = %3s", vertex, crimeId);
	}
	
	

}
