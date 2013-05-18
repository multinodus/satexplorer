package com.multinodus.satteliteexplorer.scheduler.util;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 16.12.12
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class Triple<A, B, C> {
  public A f;
  public B s;
  public C t;

  public Triple(A f, B s, C t) {
    this.f = f;
    this.s = s;
    this.t = t;
  }
}
