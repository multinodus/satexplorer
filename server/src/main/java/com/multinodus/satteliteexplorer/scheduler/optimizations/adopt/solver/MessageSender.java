/**
 * This interface should be implemented by classes wanting to send messages
 */

package com.multinodus.satteliteexplorer.scheduler.optimizations.adopt.solver;

public interface MessageSender {

  public void sendMessage(Message m, String aname, boolean cntFlag);

  public Message getMessage(String aname);
}
