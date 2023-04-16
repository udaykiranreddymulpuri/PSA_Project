package edu.neu.coe.csye6205.tsp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Graph;
import edu.neu.coe.csye6205.tsp.model.TspTour;


@Service
public class TwoOptService {

    public  TspTour twoOpt(TspTour tspTour, Graph g) {
        boolean improved = true;
        List<Integer> tour = new ArrayList<>(tspTour.getTour());
        int n = tour.size();
        double bestLength = tspTour.getLength();
        while (improved) {
            improved = false;
            for (int i = 1; i < n - 3; i++) {
                for (int j = i + 1; j < n-2; j++) {
                    if (i == j - 1 || i == j) {
                        continue;
                    }
                    List<Integer> newTour = twoOptSwap(tour, i, j);
                    double newLength = tourLength(newTour, g);
                    if (newLength < bestLength) {
                        bestLength = newLength;
                        improved = true;
                        tour = newTour;
                    }
                }
            }
        }

        return new TspTour(tour, bestLength);
    }

    public static List<Integer> twoOptSwap(List<Integer> tour, int i, int j) {
        int n = tour.size();
        List<Integer> newTour = new ArrayList<>();
        for (int k = 0; k <= i - 1; k++) {
            newTour.add(tour.get(k));
        }
        for (int k = j; k >= i; k--) {
            newTour.add(tour.get(k));
        }
        for (int k = j + 1; k < n; k++) {
            newTour.add(tour.get(k));
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
