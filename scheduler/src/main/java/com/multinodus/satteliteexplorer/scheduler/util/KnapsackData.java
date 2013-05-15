package com.multinodus.satteliteexplorer.scheduler.util;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 15.05.13
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class KnapsackData {
  public int n;
  public int m;
  public int profit[];
  public int weight[];
  public int capacity[];

  public KnapsackData(int n, int m, int profit[], int weight[], int capacity[]){
    this.n = n;
    this.m = m;
    this.profit = profit;
    this.weight = weight;
    this.capacity = capacity;
  }
}
