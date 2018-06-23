package gov.ca.cwds.data.cms;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
   * @param foreignKeyMetaTable meta group
   * @return all keys by meta table
   */
  @SuppressWarnings("unchecked")
  public SystemCode[] findByForeignKeyMetaTable(String foreignKeyMetaTable) {
    LOGGER.info("SystemCodeDao.findByForeignKeyMetaTable: foreignKeyMetaTable: {}",
        foreignKeyMetaTable);
    final String namedQueryName = SystemCode.class.getName() + ".findByForeignKeyMetaTable";

    // DRS: NO NO NO!! Interferes with transaction management, like XA.
    final Session session = grabSession();
    joinTransaction(session);

    // Transaction txn = session.getTransaction();
    // boolean transactionExists = txn != null && txn.isActive();
    // txn = transactionExists ? txn : session.beginTransaction();

    try {
      final Query<SystemCode> query = session.getNamedQuery(namedQueryName)
          .setString("foreignKeyMetaTable", foreignKeyMetaTable);
      final SystemCode[] systemCodes = query.list().toArray(new SystemCode[0]);

      // DRS: not in managed transactions.
      // if (!transactionExists)
      // txn.commit(); // NO!!

      return systemCodes;
    } catch (HibernateException h) {
      // txn.rollback();
      throw new DaoException(h);
    }
  }

  @SuppressWarnings("unchecked")
  public SystemCode findBySystemCodeId(Number systemCodeId) {
    LOGGER.info("SystemCodeDao.findBySystemCodeId: systemCodeId: {}", systemCodeId);
    final String namedQueryName = SystemCode.class.getName() + ".findBySystemCodeId";
    final Session session = grabSession();
    joinTransaction(session);

    // Transaction txn = session.getTransaction();
    // boolean transactionExists = txn != null && txn.isActive();
    // txn = transactionExists ? txn : session.beginTransaction();

    try {
      final Query<SystemCode> query =
          session.getNamedQuery(namedQueryName).setShort("systemId", systemCodeId.shortValue());
      final SystemCode systemCode = query.getSingleResult();
      // if (!transactionExists)
      // txn.commit(); // NO!!
      return systemCode;
    } catch (HibernateException h) {
      // txn.rollback();
      throw new DaoException(h);
    }
  }

}
