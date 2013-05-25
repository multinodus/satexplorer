package com.multinodus.satteliteexplorer.scheduler.optimizations.genetic;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 02.05.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import org.jgap.*;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.List;

public class GeneticSolver {
  private static final int MAX_ALLOWED_EVOLUTIONS = 150;

  public EvolutionMonitor m_monitor;

  public int[] solve(IKnapsackData knapsackData, boolean a_doMonitor)
      throws Exception {
    Configuration conf = new DefaultConfiguration();

    conf.setPreservFittestIndividual(true);
    conf.setKeepPopulationSizeConstant(false);

    KnapsackFitnessFunction myFunc =
        new KnapsackFitnessFunction(knapsackData);
    conf.setFitnessFunction(myFunc);
    if (a_doMonitor) {
      m_monitor = new EvolutionMonitor();
      conf.setMonitor(m_monitor);
    }

    List<Integer> geneIndexes = new ArrayList<Integer>();
    List<List<Integer>> alleleIndexes = new ArrayList<List<Integer>>();

    for (int i = 0; i < knapsackData.getN(); i++) {
      if (knapsackData.getWeight()[i][0] != Float.MAX_VALUE) {
        ArrayList<Integer> alleleIndex = new ArrayList<>();
        for (int j = 0; j < knapsackData.getM(); j++) {
          if (knapsackData.getProfit()[i][j] > 0) {
            alleleIndex.add(j);
          }
        }
        alleleIndex.add(knapsackData.getM() - 1);

        alleleIndexes.add(alleleIndex);
        geneIndexes.add(i);
      }
    }

    Gene[] genes = new Gene[geneIndexes.size()];

    int k = 0;
    for (List<Integer> alleleIndex : alleleIndexes) {
      genes[k] = new IntegerGene(conf, 0, alleleIndex.size() - 1);
      k++;
    }

    myFunc.setAlleleIndexes(alleleIndexes);
    myFunc.setGeneIndexes(geneIndexes);

    IChromosome sampleChromosome = new Chromosome(conf, genes);
    conf.setSampleChromosome(sampleChromosome);

    conf.setPopulationSize(20);

    Genotype population = Genotype.randomInitialGenotype(conf);
//    Genotype population = createInitialGenotype(conf, genes, taskSize, satSize, explorationCost);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
      if (!uniqueChromosomes(population.getPopulation())) {
        throw new RuntimeException("Invalid state in generation " + i);
      }
      if (m_monitor != null) {
        population.evolve(m_monitor);
      } else {
        population.evolve();
      }
      double v1 = population.getFittestChromosome().getFitnessValue();
      System.out.println("The best solution has a fitness value of " +
          v1);
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total evolution time: " + (endTime - startTime)
        + " ms");

    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    double v1 = bestSolutionSoFar.getFitnessValue();
    System.out.println("The best solution has a fitness value of " + v1);
    bestSolutionSoFar.setFitnessValueDirectly(-1);

    return getResult(bestSolutionSoFar);
  }

  private Genotype createInitialGenotype(Configuration conf, Gene[] genes, int taskSize, int satSize, double[][] explorationCost)
      throws Exception {
    Gene[] initial_genes = genes.clone();
    for (int i = 0; i < taskSize; i++) {
      double max = 0;
      int index = 0;
      for (int j = 0; j < satSize + 1; j++) {
        if (explorationCost[i][j] > max) {
          max = explorationCost[i][j];
          index = j;
        }
      }
      initial_genes[i].setAllele(index);
    }
    Chromosome initial_chromosome = new Chromosome(conf, initial_genes);
    Population initial_population = new Population(conf, initial_chromosome);
    Genotype population = new Genotype(conf, initial_population);
    return population;
  }

  private int[] getResult(IChromosome bestSolutionSoFar) {
    int[] result = new int[bestSolutionSoFar.size()];

    for (int i = 0; i < bestSolutionSoFar.size(); i++) {
      result[i] = (Integer) bestSolutionSoFar.getGene(i).getAllele();
    }

    return result;
  }

  private boolean uniqueChromosomes(Population a_pop) {
    for (int i = 0; i < a_pop.size() - 1; i++) {
      IChromosome c = a_pop.getChromosome(i);
      for (int j = i + 1; j < a_pop.size(); j++) {
        IChromosome c2 = a_pop.getChromosome(j);
        if (c == c2) {
          return false;
        }
      }
    }
    return true;
  }
}
