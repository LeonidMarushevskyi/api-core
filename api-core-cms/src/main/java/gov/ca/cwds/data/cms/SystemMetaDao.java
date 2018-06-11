package gov.ca.cwds.data.cms;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.DaoException;
import gov.ca.cwds.data.persistence.cms.SystemMeta;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * Hibernate DAO for DB2 {@link SystemMeta}.
 * 
 * @author CWDS API Team
 */
public class SystemMetaDao extends CrudsDaoImpl<SystemMeta> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public SystemMetaDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * @return all meta data records
   */
  @SuppressWarnings("unchecked")
  public SystemMeta[] findAll() {

    final String namedQueryName = SystemMeta.class.getName() + ".findAll";
    final Session session = grabSession();
    final Transaction txn = joinTransaction(session);
    boolean transactionExists = txn != null && txn.isActive();

    try {
      Query query = session.getNamedQuery(namedQueryName);
      SystemMeta[] systemMetas = (SystemMeta[]) query.list().toArray(new SystemMeta[0]);

      if (!transactionExists)
        txn.commit();
      return systemMetas;
    } catch (HibernateException h) {
      if (txn != null && txn.getStatus().canRollback()) {
        txn.rollback();
      }
      throw new DaoException(h);
    }
  }
}
