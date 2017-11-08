package gov.ca.cwds.data.legacy.cms.dao;

import com.google.inject.Inject;
import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHomeInformation;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.SessionFactory;

/**
 * @author CWDS CALS API Team
 */
public class PlacementHomeInformationDao extends BaseDaoImpl<PlacementHomeInformation> {

  @Inject
  public PlacementHomeInformationDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
