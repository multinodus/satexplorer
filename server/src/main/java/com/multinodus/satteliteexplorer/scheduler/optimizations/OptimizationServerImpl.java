package com.multinodus.satteliteexplorer.scheduler.optimizations;

import com.multinodus.satteliteexplorer.scheduler.optimizations.branchandbound.ILPSolver;
import com.multinodus.satteliteexplorer.scheduler.optimizations.genetic.GeneticSolver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 10.05.13
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class OptimizationServerImpl implements OptimizationServer {
  public static final String BINDING_NAME = "optimizeService";
  public static final String GENETIC_SOLVER = "genetic";
  public static final String ILPSolver = "ilp";


  @Override
  public int[] solve(IKnapsackData knapsackData, String method, int timeout) throws RemoteException {
    ISolver solver = null;
    if (method.equals(GENETIC_SOLVER)) {
      solver = new GeneticSolver();
      GeneticSolver geneticSolver = new GeneticSolver();
    }
    if (method.equals(ILPSolver)) {
      solver = new ILPSolver();
    }
    if (solver != null){
      try {
        int[] result = solver.solve(knapsackData, timeout);
        System.out.println("Solved");
        return result;
      } catch (Exception exc) {
        System.out.println(exc.toString());
        return null;
      }
    }
    return null;
  }

  public static void main(String... args) throws Exception {
    System.out.print("Starting registry...");
    final Registry registry = LocateRegistry.createRegistry(2099);
    System.out.println(" OK");

    final OptimizationServer service = new OptimizationServerImpl();
    Remote stub = UnicastRemoteObject.exportObject(service, 0);

    System.out.print("Binding service...");
    registry.bind(BINDING_NAME, stub);
    System.out.println(" OK");

    while (true) {
      Thread.sleep(Integer.MAX_VALUE);
    }
  }
}
