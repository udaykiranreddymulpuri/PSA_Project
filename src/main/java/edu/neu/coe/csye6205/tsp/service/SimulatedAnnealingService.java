package edu.neu.coe.csye6205.tsp.service;

import static java.lang.Math.exp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Graph;
import edu.neu.coe.csye6205.tsp.model.TspTour;

@Service
public class SimulatedAnnealingService {

	public static TspTour simulatedAnnealingTour(TspTour tspTour, double temp, double r, Graph g){
        List<Integer> tour = new ArrayList<>(tspTour.getTour());
        List<Integer> bestTour = new ArrayList<>(tspTour.getTour());
        double bestLength = tspTour.getLength();
        double currentLength = tspTour.getLength();
        int n = tour.size();
        boolean improved = true;
        double delta;
        Random random = new Random();

        while(temp >= 1 && improved){
            improved = false;
            for (int i = 0; i < g.getV() - 2; i++) {
                for (int j = i + 2; j < g.getV() - 1; j++) {
                    for (int k = j + 2; k < g.getV(); k++) {
                        List<Integer> newTour = getNewTour(tour, i, j, k);
                        double newLength = tourLength(newTour, g);
                        delta = newLength - currentLength;
                        if (newLength < currentLength && acceptanceProbability(delta,temp) > random.nextDouble()) {
                            currentLength = newLength;
                            tour = newTour;
                            if(newLength < bestLength){
                                improved = true;
                                bestLength = newLength;
                                bestTour = newTour;
                            }
                        }
                    }
                }
            }
            temp = temp*r;
        }

        return new TspTour(bestTour, bestLength);
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

    public static double acceptanceProbability(double delta, double T){
        return exp(-delta/T);
    }
}
