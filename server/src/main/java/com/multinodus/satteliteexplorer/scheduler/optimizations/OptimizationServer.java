package com.multinodus.satteliteexplorer.scheduler.optimizations;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 10.05.13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public interface OptimizationServer extends Remote {
  public int[] solve(int satSize, int taskSize, double[][] explorationCost, String method) throws RemoteException;
}
