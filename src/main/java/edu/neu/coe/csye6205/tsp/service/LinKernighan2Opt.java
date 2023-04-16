package edu.neu.coe.csye6205.tsp.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LinKernighan2Opt {

	private double[][] distanceMatrix; // matrix of distances between cities
	private double[][] completeGraphMatrix;
    private int n; // number of cities
    private List<Integer> tour; // current tour
    private boolean[] visited; // keeps track of visited cities

    public LinKernighan2Opt(double[][] distanceMatrix,double[][] completeGraph,List<Integer> tour) {
        this.distanceMatrix = distanceMatrix;
        this.n = distanceMatrix.length;
        this.tour = tour;
        this.visited = new boolean[n];
        this.completeGraphMatrix=completeGraph;
    }
	public List<Integer> solve() {
//        initializeTour();
//	      for(int i=0;i<distanceMatrix.length;i++) {
//	    	  for(int j=0;j<distanceMatrix[i].length;j++) {
//	    		  System.out.println(i+"  "+j+"  "+distanceMatrix[i][j]);
//	    	  }
//	      }
		System.out.println(tour);
        double bestCost = computeTourCost();
        System.out.println("Initial Cost "+bestCost);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < n; i++) {
                for (int j = i+2; j < n; j++) {
                    List<Integer> candidateTour = getTourAfterTwoOptSwap(i, j);
                    double candidateCost = computeTourCost(candidateTour);
                    System.out.println("line 40 "+candidateCost);
                    if (candidateCost!=0&&candidateCost < bestCost) {
                        tour = candidateTour;
                        bestCost = candidateCost;
                        improved = true;
                    }
                }
            }
        }
        System.out.println("Best tsp cost"+bestCost);
        return tour;
    }

//    private void initializeTour() {
//        tour[0] = 0;
//        visited[0] = true;
//        for (int i = 1; i < n; i++) {
//        	System.out.println("  line 57 "+(i-1));
//            int closestCity = getClosestUnvisitedCity(tour[i-1]);
//            tour[i] = closestCity;
//            visited[closestCity] = true;
//        }
//    }
//
//    private int getClosestUnvisitedCity(int city) {
//        int closestCity = -1;
//        double minDistance = Integer.MAX_VALUE;
//        for (int i = 0; i < n&&i!=city; i++) {
//            if (!visited[i] && distanceMatrix[city][i]!=0&&distanceMatrix[city][i] < minDistance) {
//                closestCity = i;
//                minDistance = distanceMatrix[city][i];
//            }
//        }
//        return closestCity;
//    }

    private List<Integer> getTourAfterTwoOptSwap(int i, int j) {
        List<Integer> newTour = tour;
        for (int k = 0; k <= (j-i)/2; k++) {
            int temp = newTour.get(i+k);
            newTour.set(i+k, newTour.get(j-k));
            newTour.set(j-k, temp);
//            newTour[i+k] = newTour[j-k];
//            newTour[j-k] = temp;
        }
        return newTour;
    }

    private double computeTourCost() {
        return computeTourCost(tour);
    }

    private double computeTourCost(List<Integer> tour) {
        double cost = 0;
//        for(int i=0;i<tour.length;i++)
//	    	  System.out.print(tour[i]+" ");
//        System.out.println();
        System.out.println(" 97"+tour);
        for (int i = 0; i < tour.size()-1; i++) {
//        	System.out.println(completeGraphMatrix[tour.get(i)][tour.get(i+1)]);
            cost += completeGraphMatrix[tour.get(i)][tour.get(i+1)];
        }
        cost+=completeGraphMatrix[tour.get(tour.size()-1)][tour.get(0)];
        return cost;
    }
}
