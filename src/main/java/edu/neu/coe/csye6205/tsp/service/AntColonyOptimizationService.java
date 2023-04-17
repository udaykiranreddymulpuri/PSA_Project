package edu.neu.coe.csye6205.tsp.service;

import java.util.Random;

import edu.neu.coe.csye6205.tsp.model.Graph;


public class AntColonyOptimizationService {

	private int numAnts;
    private int maxIterations;
    private double alpha;
    private double beta;
    private double evaporationRate;
    private double initialPheromone;
    private Graph graph;
    private Random random;
    private double[][] pheromoneMatrix;
    private double[][] distanceMatrix;
    private int numCities;

    public AntColonyOptimizationService(Graph graph, int numIterations,double alpha, double beta,double evaporationRate, double initialPheromone) {
        this.numAnts = numAnts;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.distanceMatrix = graph.getAdjMatrix();
        this.numCities = distanceMatrix.length;
        this.maxIterations = numIterations;
        this.pheromoneMatrix = new double[numAnts][numAnts];
        for (int i = 0; i < graph.getV(); i++) {
            for (int j = 0; j < graph.getV(); j++) {
                this.pheromoneMatrix[i][j] = initialPheromone;
            }
        }
    }
}
