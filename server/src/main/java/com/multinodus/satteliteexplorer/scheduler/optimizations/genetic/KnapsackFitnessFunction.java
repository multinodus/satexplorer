package com.multinodus.satteliteexplorer.scheduler.optimizations.genetic;

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 02.05.13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class KnapsackFitnessFunction extends FitnessFunction {
  private IKnapsackData knapsackData;
  private int penalty = 100000;

  public KnapsackFitnessFunction(IKnapsackData knapsackData) {
    super();    //To change body of overridden methods use File | Settings | File Templates.
    this.knapsackData = knapsackData;
  }

  @Override
  protected double evaluate(IChromosome chromosome) {
    double total = 0;
    int[] occupancy = new int[knapsackData.getM()+1];
    for (int taskIndex = 0; taskIndex < chromosome.size(); taskIndex++) {
      total += knapsackData.getProfit()[taskIndex];
      int episodeIndex = (Integer) chromosome.getGene(taskIndex).getAllele();
      occupancy[episodeIndex] += knapsackData.getWeight()[taskIndex];
    }
    for (int m = 0; m < knapsackData.getM() + 1; m++){
      if (occupancy[m] > knapsackData.getCapacity()[m]){
        total -= penalty;
      }
    }
    if (total < 0) {
      total = 0;
    }
    return total;
  }
}
