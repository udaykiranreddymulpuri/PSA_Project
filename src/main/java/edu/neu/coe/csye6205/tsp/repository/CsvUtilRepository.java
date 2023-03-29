package edu.neu.coe.csye6205.tsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.neu.coe.csye6205.tsp.model.Point;

@Repository
public interface  CsvUtilRepository extends JpaRepository<Point, String>{

}
