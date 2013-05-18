package com.multinodus.satteliteexplorer.scheduler.optimizations.genetic;

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 02.05.13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class KnapsackFitnessFunction extends FitnessFunction {
  private IKnapsackData knapsackData;
  private int penalty = 10;
  private List<Integer> geneIndexes;
  private List<List<Integer>> alleleIndexes;

  public KnapsackFitnessFunction(IKnapsackData knapsackData) {
    super();    //To change body of overridden methods use File | Settings | File Templates.
    this.knapsackData = knapsackData;
  }

  @Override
  protected double evaluate(IChromosome chromosome) {
    double total = 0;
    float[] occupancy = new float[knapsackData.getM()];
    float[] profit = new float[knapsackData.getM()];
    for (int index = 0; index < chromosome.size(); index++) {
      int taskIndex = geneIndexes.get(index);
      int episodeIndex = alleleIndexes.get(index).get((Integer) chromosome.getGene(index).getAllele());
      float cost = knapsackData.getProfit()[taskIndex][episodeIndex];
      total += cost;
      occupancy[episodeIndex] += knapsackData.getWeight()[taskIndex][episodeIndex];
      profit[episodeIndex] += cost;
    }
    for (int m = 0; m < knapsackData.getM(); m++) {
      if (occupancy[m] > knapsackData.getCapacity()[m]) {
        total -= profit[m];
        total -= occupancy[m] - knapsackData.getCapacity()[m];
      }
    }

    total += 20000;

    if (total < 0) {
      total = 0;
    }
    return total;
  }

  public void setGeneIndexes(List<Integer> geneIndexes) {
    this.geneIndexes = geneIndexes;
  }

  public void setAlleleIndexes(List<List<Integer>> alleleIndexes) {
    this.alleleIndexes = alleleIndexes;
  }
}
