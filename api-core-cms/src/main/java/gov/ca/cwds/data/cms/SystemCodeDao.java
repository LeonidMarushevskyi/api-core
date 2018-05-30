package gov.ca.cwds.data.cms;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.DaoException;
import gov.ca.cwds.data.persistence.cms.SystemCode;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * Hibernate DAO for DB2 {@link SystemCode}.
 * 
 * @author CWDS API Team
 */
public class SystemCodeDao extends CrudsDaoImpl<SystemCode> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemCodeDao.class);

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public SystemCodeDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Get the current session, if available, or open a new one.
   * 
   * @return Hibernate session
   */
  protected Session getCurrentSession() {
    Session session;
    try {
      session = getSessionFactory().getCurrentSession();
    } catch (HibernateException e) { // NOSONAR
      LOGGER.warn("NO SESSION!");
      session = getSessionFactory().openSession();
    }

    return session;
  }

  /**
   * @param foreignKeyMetaTable meta group
   * @return all keys by meta table
   */
  @SuppressWarnings("unchecked")
  public SystemCode[] findByForeignKeyMetaTable(String foreignKeyMetaTable) {
    final String namedQueryName = SystemCode.class.getName() + ".findByForeignKeyMetaTable";

    final Session session = getCurrentSession();
    Transaction txn = session.getTransaction();
    boolean transactionExists = txn != null && txn.isActive();
    txn = transactionExists ? txn : session.beginTransaction();

    try {
      final Query query = session.getNamedQuery(namedQueryName).setString("foreignKeyMetaTable",
          foreignKeyMetaTable);
      final SystemCode[] systemCodes = (SystemCode[]) query.list().toArray(new SystemCode[0]);
      if (!transactionExists)
        txn.commit();
      return systemCodes;
    } catch (HibernateException h) {
      txn.rollback();
      throw new DaoException(h);
    }
  }

  @SuppressWarnings("unchecked")
  public SystemCode findBySystemCodeId(Number systemCodeId) {
    final String namedQueryName = SystemCode.class.getName() + ".findBySystemCodeId";
    Session session = getCurrentSession();

    Transaction txn = session.getTransaction();
    boolean transactionExists = txn != null && txn.isActive();
    txn = transactionExists ? txn : session.beginTransaction();

    try {
      final Query query =
          session.getNamedQuery(namedQueryName).setShort("systemId", systemCodeId.shortValue());
      final SystemCode systemCode = (SystemCode) query.getSingleResult();
      if (!transactionExists)
        txn.commit();
      return systemCode;
    } catch (HibernateException h) {
      txn.rollback();
      throw new DaoException(h);
    }
  }
}
