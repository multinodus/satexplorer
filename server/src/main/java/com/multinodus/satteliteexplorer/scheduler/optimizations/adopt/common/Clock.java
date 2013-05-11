package com.multinodus.satteliteexplorer.scheduler.optimizations.adopt.common;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Clock {

  GregorianCalendar gc = new GregorianCalendar();

  private int time() {
    return (gc.get(Calendar.SECOND) + gc.get(Calendar.MINUTE) * 60 +
        gc.get(Calendar.HOUR) * 60 * 60);
  }

  public void printTime() {
    System.out.println(time());
  }

  public int getTime() {
    return time();
  }

  public static void main(String argv[]) {

    (new Clock()).printTime();

  }

}
