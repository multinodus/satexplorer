package satteliteExplorer.db;

import com.google.common.collect.Lists;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import satteliteExplorer.db.entities.Sat;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 12.12.12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class EntityContext {
  private Map<Class, List<Object>> storage;

  private static EntityContext context;

  private EntityContext() {
    storage = new HashMap<Class, List<Object>>();
  }

  public static EntityContext get() {
    if (context == null) {
      context = new EntityContext();
    }
    return context;
  }

  public List<Object> getAllEntities(Class className) {
    if (storage.containsKey(className)) {
      return storage.get(className);
    } else {
      Transaction tx = null;
      Session session = SessionFactoryUtil.getInstance().getCurrentSession();

      try {
        tx = session.beginTransaction();
        List entities = session.createQuery("select h from " + className.getSimpleName() + " as h")
            .list();
        tx.commit();
        if (className == Sat.class){
          entities = Lists.newArrayList(entities.get(0));
        }
        storage.put(className, entities);
        return entities;
      } catch (RuntimeException e) {
        if (tx != null && tx.isActive()) {
          try {
            tx.rollback();
          } catch (HibernateException e1) {
            throw e;
          }
          throw e;
        }
      }
    }
    return null;
  }

  public void createEntity(Object entity, Class className) {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    try {
      tx = session.beginTransaction();
      session.save(entity);
      tx.commit();
      if (storage.containsKey(className)) {
        storage.get(className).add(entity);
      }
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
        throw e;
      }
    }
  }

  public void createEntities(Collection<Object> entities) {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    try {
      tx = session.beginTransaction();
      for (Object entity : entities) {
        session.save(entity);
      }
      tx.commit();
      for (Object entity : entities) {
        Class className = entity.getClass();
        if (storage.containsKey(className)) {
          storage.get(className).add(entity);
        }
      }
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
        throw e;
      }
    }
  }

  public void deleteEntity(Object entity, Class className) {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    try {
      tx = session.beginTransaction();
      session.delete(entity);
      tx.commit();
      if (storage.containsKey(className)) {
        storage.get(className).remove(entity);
      }
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
        throw e;
      }
    }
  }

  public void updateEntity(Object entity, Class className) {
    Transaction tx = null;
    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    try {
      tx = session.beginTransaction();
      session.update(entity);
      tx.commit();
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        try {
          tx.rollback();
        } catch (HibernateException e1) {
          throw e;
        }
        throw e;
      }
    }
  }
}
