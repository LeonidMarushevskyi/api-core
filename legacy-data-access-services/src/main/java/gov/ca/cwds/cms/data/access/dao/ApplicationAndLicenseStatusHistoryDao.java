package gov.ca.cwds.cms.data.access.dao;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.inject.XaDasSessionFactory;
import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.data.legacy.cms.entity.ApplicationAndLicenseStatusHistory;
import org.hibernate.SessionFactory;

public class ApplicationAndLicenseStatusHistoryDao extends
  BaseDaoImpl<ApplicationAndLicenseStatusHistory> {

  @Inject
  public ApplicationAndLicenseStatusHistoryDao(@XaDasSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
