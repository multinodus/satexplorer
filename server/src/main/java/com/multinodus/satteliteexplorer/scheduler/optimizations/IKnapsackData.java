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

  public float[][] getProfit();
  public void setProfit(float[][] profit);

  public float[][] getWeight();
  public void setWeight(float[][] weight);

  public float[] getCapacity();
  public void setCapacity(float[] capacity);
}
