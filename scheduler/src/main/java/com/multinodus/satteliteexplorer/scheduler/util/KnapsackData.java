package com.multinodus.satteliteexplorer.scheduler.util;

import com.multinodus.satteliteexplorer.scheduler.optimizations.IKnapsackData;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 15.05.13
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class KnapsackData implements IKnapsackData {
  public int n;
  public int m;
  public float profit[][];
  public float weight[][];
  public float capacity[];

  public KnapsackData(int n, int m, float profit[][], float weight[][], float capacity[]){
    this.n = n;
    this.m = m;
    this.profit = profit;
    this.weight = weight;
    this.capacity = capacity;
  }

  public int getN() {
    return n;
  }

  public void setN(int n) {
    this.n = n;
  }

  public int getM() {
    return m;
  }

  public void setM(int m) {
    this.m = m;
  }

  public float[][] getProfit() {
    return profit;
  }

  public void setProfit(float[][] profit) {
    this.profit = profit;
  }

  public float[][] getWeight() {
    return weight;
  }

  public void setWeight(float[][] weight) {
    this.weight = weight;
  }

  public float[] getCapacity() {
    return capacity;
  }

  public void setCapacity(float[] capacity) {
    this.capacity = capacity;
  }
}
