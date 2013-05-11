/**********************************************************************
 * File Name: Value.java
 * Author: Jay Modi (modi@isi.edu)
 * Date: Nov 11 2000
 *
 *
 **********************************************************************/
package com.multinodus.satteliteexplorer.scheduler.optimizations.adopt.problem;

/**
 * An abstract class for representing values of variables.
 */
public abstract class Value {

  public abstract boolean equal(Value v);

  public abstract String toString();
}
