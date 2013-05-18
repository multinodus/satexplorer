package com.multinodus.satteliteexplorer.scheduler.optimizations.branchandbound;

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;
import net.sf.javailp.*;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 18.05.13
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class ILPSolver {
  private final static int scale = 100;

  public void solve(IKnapsackData knapsackData) {
    SolverFactory factory = new SolverFactorySAT4J();
//    factory.setParameter(Solver.VERBOSE, 0);
    factory.setParameter(Solver.TIMEOUT, 60);

    float[][] profit = knapsackData.getProfit();
    float[][] weight = knapsackData.getWeight();
    float[] capacity = knapsackData.getCapacity();
    boolean[][] used = new boolean[knapsackData.getN()][];

    Problem problem = new Problem();

    Linear linear = new Linear();

    for (int i = 0; i < knapsackData.getN(); i++) {
      used[i] = new boolean[knapsackData.getM()];
      for (int j = 0; j < knapsackData.getM(); j++) {
        int k = (int) (profit[i][j] * scale);
        if (k > 0 && weight[i][j] != Float.MAX_VALUE) {
          linear.add(k, String.format("x%d%d", i, j));
          used[i][j] = true;
        } else {
          used[i][j] = false;
        }
      }
    }

    problem.setObjective(linear, OptType.MAX);

    for (int j = 0; j < knapsackData.getM() - 1; j++) {
      linear = new Linear();
      for (int i = 0; i < knapsackData.getN(); i++) {
        int k = (int) (weight[i][j] * scale);
        if (used[i][j] && k > 0 && weight[i][j] != Float.MAX_VALUE) {
          linear.add(k, String.format("x%d%d", i, j));
        }
      }

      problem.add(linear, "<=", (int) (capacity[j] * scale));
    }

    for (int i = 0; i < knapsackData.getN(); i++) {
      for (int j = 0; j < knapsackData.getM(); j++) {
        if (used[i][j]) {
          problem.setVarType(String.format("x%d%d", i, j), Boolean.class);
        }
      }
    }

    Solver solver = factory.get();
    Result result = solver.solve(problem);

    System.out.println(result);
  }
}
