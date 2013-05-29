package com.multinodus.satteliteexplorer.scheduler.optimizations;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 26.05.13
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public interface ISolver {
  public int[] solve(IKnapsackData knapsackData, int timeout)
      throws Exception;
}
