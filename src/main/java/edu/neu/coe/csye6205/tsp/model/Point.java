package edu.neu.coe.csye6205.tsp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Entity
@Data
@Getter
@Setter
public class Point {

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq")
//    @SequenceGenerator(name = "point_seq", sequenceName = "point_seq", initialValue = 0, allocationSize = 1)
	private int pointId;
//	@Column(unique = true)
	private String crimeId;
	private Double longitude;
	private Double latitude;

	

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public String getCrimeId() {
		return crimeId;
	}

	public void setCrimeId(String crimeId) {
		this.crimeId = crimeId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
