package satteliteExplorer.scheduler.optimizations;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 02.05.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */

import org.jgap.*;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class GeneticSolver {
  private static final int MAX_ALLOWED_EVOLUTIONS = 1;

  public EvolutionMonitor m_monitor;

  public int[] solve(int satSize, int taskSize, double[][] explorationCost, boolean a_doMonitor)
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    // Care that the fittest individual of the current population is
    // always taken to the next generation.
    // Consider: With that, the pop. size may exceed its original
    // size by one sometimes!
    // -------------------------------------------------------------
    conf.setPreservFittestIndividual(true);
    conf.setKeepPopulationSizeConstant(false);

    FitnessFunction myFunc =
        new ExplorationTimeFitnessFunction(explorationCost);
    conf.setFitnessFunction(myFunc);
    if (a_doMonitor) {
      // Turn on monitoring/auditing of evolution progress.
      // --------------------------------------------------
      m_monitor = new EvolutionMonitor();
      conf.setMonitor(m_monitor);
    }

    Gene[] genes = new Gene[taskSize];
    for (int i = 0; i < taskSize; i++) {
      genes[i] = new IntegerGene(conf, 0, satSize - 1);
    }

    IChromosome sampleChromosome = new Chromosome(conf, genes);
    conf.setSampleChromosome(sampleChromosome);

    conf.setPopulationSize(20);

    Genotype population = Genotype.randomInitialGenotype(conf);

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
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total evolution time: " + (endTime - startTime)
        + " ms");

    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    double v1 = bestSolutionSoFar.getFitnessValue();
    System.out.println("The best solution has a fitness value of " +
        bestSolutionSoFar.getFitnessValue());
    bestSolutionSoFar.setFitnessValueDirectly(-1);

    return getResult(bestSolutionSoFar);
  }

  private int[] getResult(IChromosome bestSolutionSoFar){
    int[] result = new int[bestSolutionSoFar.size()];

    for (int i = 0; i < bestSolutionSoFar.size(); i++) {
      result[i] = (Integer)bestSolutionSoFar.getGene(i).getAllele();
    }

    return result;
  }

  /**
   * @param a_pop the population to verify
   * @return true if all chromosomes in the populationa are unique
   * @author Klaus Meffert
   * @since 3.3.1
   */
  private boolean uniqueChromosomes(Population a_pop) {
    // Check that all chromosomes are unique
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
