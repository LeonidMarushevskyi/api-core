package gov.ca.cwds.data;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.ca.cwds.data.persistence.PersistentObject;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * An implementation of {@link CrudsDao}. Class is final and is expected that other {@link Dao} will
 * contain this implementation and delegate.
 * 
 * @author CWDS API Team
 *
 * @param <T> The {@link PersistentObject} to perform CRUDS operations on
 */
public class CrudsDaoImpl<T extends PersistentObject> extends AbstractDAO<T>
    implements CrudsDao<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CrudsDaoImpl.class);

  private SessionFactory sessionFactory;

  /**
   * Default constructor.
   * 
   * @param sessionFactory Hibernate session factory
   */
  public CrudsDaoImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
    this.sessionFactory = sessionFactory;
  }

  /**
   * Provided for <strong>convenience</strong>. Don't abuse this.
   */
  @Override
  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.data.CrudsDao#find(java.io.Serializable)
   */
  @Override
  public T find(Serializable primaryKey) {
    return get(primaryKey);
  }

  /**
   * {@inheritDoc}
   * 
   * @see CrudsDao#delete(java.io.Serializable)
   */
  @Override
  public T delete(Serializable id) {
    T object = find(id);
    if (object != null) {
      currentSession().delete(object);
    }
    return object;
  }

  /**
   * {@inheritDoc}
   * 
   * @see CrudsDao#create(gov.ca.cwds.data.persistence.PersistentObject)
   */
  @Override
  public T create(T object) {
    if (object.getPrimaryKey() != null) {
      T databaseObject = find(object.getPrimaryKey());
      if (databaseObject != null) {
        String msg = MessageFormat.format("entity with id={0} already exists", object);
        LOGGER.error(msg);
        throw new EntityExistsException(msg);
      }
    }
    return persist(object);
  }

  /**
   * {@inheritDoc}
   * 
   * @see CrudsDao#update(gov.ca.cwds.data.persistence.PersistentObject)
   */
  @Override
  public T update(T object) {
    T databaseObject = find(object.getPrimaryKey());
    if (databaseObject == null) {
      String msg =
          MessageFormat.format("Unable to find entity with id={0}", object.getPrimaryKey());
      throw new EntityNotFoundException(msg);
    }
    currentSession().evict(databaseObject);
    return persist(object);
  }

}