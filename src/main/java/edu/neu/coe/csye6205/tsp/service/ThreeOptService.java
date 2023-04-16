package edu.neu.coe.csye6205.tsp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Graph;
import edu.neu.coe.csye6205.tsp.model.TspTour;

@Service
public class ThreeOptService {
    public TspTour threeOpt(TspTour tspTour, Graph g) {
        TspTour bestTour = tspTour;
        boolean improved = true;
        while (improved) {
            improved = false;
            List<Integer> tour = bestTour.getTour();
            for (int i = 0; i < g.getV() - 2; i++) {
                for (int j = i + 2; j < g.getV() - 1; j++) {
                    for (int k = j + 2; k < g.getV(); k++) {
                        List<Integer> newTour = getNewTour(bestTour.getTour(), i, j, k);
                        double newDistance = tourLength(newTour, g);
                        if (newDistance < bestTour.getLength()) {
                            bestTour = new TspTour(newTour, newDistance);
                            improved = true;
                        }
                    }
                }
            }
        }
        return bestTour;
    }

    private static List<Integer> getNewTour(List<Integer> tour, int i, int j, int k) {
        List<Integer> newTour = new ArrayList<>();
        for (int x = 0; x <= i; x++) {
            newTour.add(tour.get(x));
        }
        for (int x = j; x > i; x--) {
            newTour.add(tour.get(x));
        }
        for (int x = k; x > j; x--) {
            newTour.add(tour.get(x));
        }
        for (int x = k + 1; x < tour.size(); x++) {
            newTour.add(tour.get(x));
        }
        return newTour;
    }

    public static double tourLength(List<Integer> tour, Graph g) {
        double length = 0;
        int n = tour.size();
        for (int i = 0; i < n - 1; i++) {
            length += g.getDistanceBetweenPoints(tour.get(i),tour.get(i+1));
        }
        return length;
    }
}