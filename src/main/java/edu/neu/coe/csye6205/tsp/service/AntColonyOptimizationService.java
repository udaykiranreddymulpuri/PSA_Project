package edu.neu.coe.csye6205.tsp.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import edu.neu.coe.csye6205.tsp.model.Ant;
import edu.neu.coe.csye6205.tsp.model.Graph;


public class AntColonyOptimizationService {

    // Number of ants
    private int numAnts;

    // Number of iterations
    private int numIterations;

    // Number of cities
    private int numCities;

    // Distance matrix
    private double[][] distanceMatrix;

    // Pheromone matrix
    private double[][] pheromoneMatrix;

    private double[][] delta;

    // Constructor
    public AntColonyOptimizationService(int numAnts, int numIterations, Graph g,List<Integer> tour) {
        this.numAnts = numAnts;
        this.numIterations = numIterations;
        this.numCities = g.getV();
        this.distanceMatrix = g.getAdjMatrix();
        this.pheromoneMatrix = new double[numCities][numCities];
        this.delta = new double[numCities][numCities];

        for(int i=0; i<numCities-1;i++){
            pheromoneMatrix[tour.get(i)][tour.get(i+1)]  = 2;
            pheromoneMatrix[tour.get(i+1)][tour.get(i)]  = 2;
        }
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if(pheromoneMatrix[i][j] != 2){
                    pheromoneMatrix[i][j] = 0.01;
                    pheromoneMatrix[j][i] = 0.01;
                }
            }
        }
    }

    // Run the ant colony optimization
    public Ant runAnColony(Graph g) {
        long timeStart = System.currentTimeMillis();
        System.out.println();

        System.out.printf("Random swapping optimization started at %d", timeStart);
        Ant bestAnt = null;
        int i =0;
        while (i<numIterations){
            i++;
            Ant[] ants = new Ant[numAnts];
            for (int j = 0; j < numAnts; j++) {
                ants[j] = new Ant(numCities, new Random().nextInt(numCities));
            }
            for (int j = 0; j < numAnts; j++) {
                for (int k = 0; k < numCities - 1; k++) {
                    ants[j].selectNextCity(pheromoneMatrix,distanceMatrix);
                }
                ants[j].getTour().add(ants[j].getTour().get(0));
            }
            for (int j = 0; j < numAnts; j++) {
                ants[j].calculateCost(distanceMatrix);
            }
            for (int j = 0; j < numAnts; j++) {
                if(bestAnt == null){
                    bestAnt = ants[j];
                }else if (ants[j].getCost() < bestAnt.getCost()) {
                    bestAnt = ants[j];
                }
            }
            for (int  j= 0; j < numCities; j++) {
                for (int k = 0; k < numCities; k++) {
                    delta[j][k] = 0;
                }
            }
            for (Ant ant : ants) {
                double contribution = 1 / ant.getCost();
                for (int c = 0; c < numCities - 1; c++) {
                    delta[ant.getTour().get(c)][ant.getTour().get(c + 1)] += contribution;
                    delta[ant.getTour().get(c+1)][ant.getTour().get(c)] =delta[ant.getTour().get(c)][ant.getTour().get(c + 1)];
                }
            }

            for (int j = 0; j < numCities; j++) {
                for (int k = 0; k < numCities; k++) {
                    pheromoneMatrix[j][k] = 0.5*pheromoneMatrix[j][k];
                    pheromoneMatrix[j][k] += delta[j][k];
                }
            }
            if (isConverged()) {
                break;
            }
        }
  
        
        return bestAnt;
         
        
    }

    private boolean isConverged() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (delta[i][j] > 0.0001) {
                    return false;
                }
            }
        }
        return true;
    }

    
    

}