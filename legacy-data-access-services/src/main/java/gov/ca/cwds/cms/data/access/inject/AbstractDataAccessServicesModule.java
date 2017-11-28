package gov.ca.cwds.cms.data.access.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import gov.ca.cwds.cms.data.access.dao.placementhome.BackgroundCheckDao;
import gov.ca.cwds.cms.data.access.dao.placementhome.CountyOwnershipDao;
import gov.ca.cwds.cms.data.access.dao.placementhome.EmergencyContactDetailDao;
import gov.ca.cwds.cms.data.access.dao.placementhome.ExternalInterfaceDao;
import gov.ca.cwds.cms.data.access.dao.placementhome.PlacementHomeDao;
import gov.ca.cwds.cms.data.access.dao.placementhome.PlacementHomeProfileDao;
import gov.ca.cwds.cms.data.access.mapper.BackgroundCheckMapper;
import gov.ca.cwds.cms.data.access.mapper.CountyOwnershipMapper;
import gov.ca.cwds.cms.data.access.mapper.EmergencyContactDetailMapper;
import gov.ca.cwds.cms.data.access.mapper.ExternalInterfaceMapper;
import gov.ca.cwds.cms.data.access.service.PlacementHomeService;
import org.hibernate.SessionFactory;

/**
 * @author CWDS CALS API Team
 */

public abstract class AbstractDataAccessServicesModule extends AbstractModule {

  @Override
  protected final void configure() {
    configureMappers();
    configureDAOs();
    configureDataAccessServices();
  }

  @Provides
  @PlacementHomeSessionFactory
  @Inject
  protected SessionFactory placementHomeSessionFactory(Injector injector) {
    return getPlacementHomeSessionFactory(injector);
  }

  protected abstract SessionFactory getPlacementHomeSessionFactory(Injector injector);

  private void configureDataAccessServices() {
    bind(PlacementHomeService.class);
  }

  private void configureDAOs() {
    bind(CountyOwnershipDao.class);
    bind(PlacementHomeDao.class);
    bind(CountyOwnershipDao.class);
    bind(ExternalInterfaceDao.class);
    bind(BackgroundCheckDao.class);
    bind(EmergencyContactDetailDao.class);
    bind(PlacementHomeProfileDao.class);
  }

  private void configureMappers() {
    bind(CountyOwnershipMapper.class).to(CountyOwnershipMapper.INSTANCE.getClass())
        .asEagerSingleton();
    bind(ExternalInterfaceMapper.class).to(ExternalInterfaceMapper.INSTANCE.getClass())
        .asEagerSingleton();
    bind(EmergencyContactDetailMapper.class).to(EmergencyContactDetailMapper.INSTANCE.getClass())
        .asEagerSingleton();
    bind(BackgroundCheckMapper.class).to(BackgroundCheckMapper.INSTANCE.getClass())
        .asEagerSingleton();
  }

}
