package com.multinodus.satteliteexplorer.scheduler.optimizations.branchandbound;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 15.05.13
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class TestSolver {
  public static void main(String args[]) {
    int n = 100;
    int m = 20;
    int depth = 300000;
    int profit[] = {0, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61, 46, 50, 45, 58, 74, 52, 36, 30, 79, 61};
    int weight[] = {0, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28, 81, 83, 43, 34, 68, 58, 60, 72, 42, 28};
    int capacity[] = {0, 125, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 146};
    int sol[] = new int[n + 1];
    MultipleKnapsackSolver solver = new MultipleKnapsackSolver();
    solver.multipleKnapsack(n, m, profit, weight, capacity, depth, sol);
    if (sol[0] > 0) {
      System.out.println("Optimal solution found:");
      for (int i = 1; i <= n; i++)
        System.out.print(" " + sol[i]);
      System.out.println("\n\nTotal profit = " + sol[0]);
    } else
      System.out.println("Error returned = " + sol[0]);
  }
}
