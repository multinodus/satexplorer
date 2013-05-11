package com.multinodus.satteliteexplorer.db.entities;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.multinodus.satteliteexplorer.db.SessionFactoryUtil;

import java.util.List;

public class TestExample {
  /**
   * @param args
   */
  public static void main(String[] args) {
    Role role = new Role();
    role.setName("fhgsd");

    listHoney();

    SessionFactoryUtil.close();
  }

  private static void listHoney() {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();

    try {
      tx = session.beginTransaction();
      boolean c = session.isConnected();
      List roles = session.createQuery("select h from Role as h")
          .list();
      System.out.print("Size=" + roles.size());
      tx.commit();
      session.flush();
      session.close();
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
// Second try catch as the rollback could fail as well
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
// throw again the first exception
        throw e;
      }


    }
  }

  private static void createRole(Role role) {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    try {
      tx = session.beginTransaction();
      session.save(role);
      tx.commit();

    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
// Second try catch as the rollback could fail as well
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
// throw again the first exception
        throw e;
      }
    }
  }
}
