package edu.neu.coe.csye6205.tsp.model;

import java.util.ArrayList;
import java.util.Random;

public class Ant {
	
	// Number of cities
    private int numCities;

    // Distance matrix
    private double[][] distanceMatrix;
	// Tour
	private ArrayList<Integer> tour;

	// Cost
	private double cost;

	// Constructor
	public Ant(int numCities, int start) {
		tour = new ArrayList<Integer>();
		tour.add(start);
	}

	// Select the next city
	public void selectNextCity(double[][] pheromoneMatrix, double[][] distance) {
		int currentCity = tour.get(tour.size() - 1);

//Calculate the probabilities
		double[] probabilities = new double[numCities];
		double sum = 0.0;
		for (int i = 0; i < numCities; i++) {
			if (!tour.contains(i) && i != currentCity && distanceMatrix[currentCity][i] > 0) {
				probabilities[i] = Math.pow(pheromoneMatrix[currentCity][i], 10) / distanceMatrix[currentCity][i];
				sum += probabilities[i];
			}
		}

//Normalize the probabilities
		for (int i = 0; i < numCities; i++) {
			probabilities[i] /= sum;
		}

//Select the next city
		Random random = new Random();
		double p = random.nextDouble();

		sum = 0.0;
		for (int i = 0; i < numCities; i++) {
			sum += probabilities[i];
			if (p <= sum) {
				tour.add(i);
				break;
			}
		}

	}

	// Calculate the cost of the tour
	public void calculateCost(double[][] distanceMatrix) {
		cost = 0;
		for (int i = 0; i < tour.size() - 1; i++) {
			cost += distanceMatrix[tour.get(i)][tour.get(i + 1)];
		}
	}

	// Getters
	public ArrayList<Integer> getTour() {
		return tour;
	}

	public double getCost() {
		return cost;
	}
}