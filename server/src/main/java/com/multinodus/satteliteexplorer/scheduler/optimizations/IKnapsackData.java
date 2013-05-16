package com.multinodus.satteliteexplorer.scheduler.optimizations;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 16.05.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public interface IKnapsackData {
  public int getN();
  public void setN(int n);

  public int getM();
  public void setM(int m);

  public int[] getProfit();
  public void setProfit(int[] profit);

  public int[] getWeight();
  public void setWeight(int[] weight);

  public int[] getCapacity();
  public void setCapacity(int[] capacity);
}
