package gov.ca.cwds.data.legacy.cms.dao;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.ActiveServiceComponentType;
import gov.ca.cwds.inject.CmsSessionFactory;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * @author CWDS CASE API Team
 */
public class ActiveServiceComponentTypeDao extends BaseDaoImpl<ActiveServiceComponentType> {

  @Inject
  public ActiveServiceComponentTypeDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public List<ActiveServiceComponentType> findAll() {
    Session session = this.getSessionFactory().getCurrentSession();
    Query<ActiveServiceComponentType> query = session
        .createNamedQuery(ActiveServiceComponentType.NQ_ALL, ActiveServiceComponentType.class);
    ImmutableList.Builder<ActiveServiceComponentType> entities = new ImmutableList.Builder<>();
    entities.addAll(query.list());
    return entities.build();
  }

}
