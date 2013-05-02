package satteliteExplorer.scheduler.optimizations;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 02.05.13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class ExplorationTimeFitnessFunction extends FitnessFunction{
  private double[][] explorationCost;

  public ExplorationTimeFitnessFunction(double[][] explorationCost) {
    super();    //To change body of overridden methods use File | Settings | File Templates.
    this.explorationCost = explorationCost;
  }

  @Override
  protected double evaluate(IChromosome chromosome) {
    double total = 0;
    for (int taskIndex = 0; taskIndex < chromosome.size(); taskIndex++){
      int satIndex = (Integer)chromosome.getGene(taskIndex).getAllele();
      total += explorationCost[taskIndex][satIndex];
    }
    return total;
  }
}
