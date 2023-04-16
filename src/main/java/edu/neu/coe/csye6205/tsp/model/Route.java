package edu.neu.coe.csye6205.tsp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Entity
@Data
@Getter
@Setter
public class Route {

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private int routeId;
	private int fromLoc;
	private int toLoc;
	private Double weight;

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getFromLoc() {
		return fromLoc;
	}

	public void setFromLoc(int fromLoc) {
		this.fromLoc = fromLoc;
	}

	public int getToLoc() {
		return toLoc;
	}

	public void setToLoc(int toLoc) {
		this.toLoc = toLoc;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Route [routeId=" + routeId + ", fromLoc=" + fromLoc + ", toLoc=" + toLoc + ", weight=" + weight + "]";
	}

}
