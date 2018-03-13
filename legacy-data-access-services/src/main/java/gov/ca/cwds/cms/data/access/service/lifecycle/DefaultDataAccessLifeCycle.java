package gov.ca.cwds.cms.data.access.service.lifecycle;

import gov.ca.cwds.cms.data.access.dto.BaseEntityAwareDTO;
import gov.ca.cwds.cms.data.access.service.DataAccessServicesException;
import gov.ca.cwds.drools.DroolsException;
import gov.ca.cwds.security.realm.PerryAccount;

/** @author CWDS TPT-3 Team */
public class DefaultDataAccessLifeCycle<T extends BaseEntityAwareDTO>
    implements DataAccessServiceLifecycle<T> {

  @Override
  public void beforeDataProcessing(DataAccessBundle bundle) {
    // Do nothing just a stub
  }

  @Override
  public void afterDataProcessing(DataAccessBundle bundle) {
    // Do nothing just a stub
  }

  @Override
  public void beforeBusinessValidation(DataAccessBundle bundle) {
    // Do nothing just a stub
  }

  @Override
  public void afterBusinessValidation(DataAccessBundle bundle) {
    // Do nothing just a stub
  }

  @Override
  public void dataProcessing(DataAccessBundle bundle, PerryAccount perryAccount)
      throws DroolsException {
    // Do nothing just a stub
  }

  @Override
  public void businessValidation(DataAccessBundle bundle, PerryAccount perryAccount)
      throws DroolsException {
    // Do nothing
  }

  @Override
  public void afterStore(DataAccessBundle bundle) throws DataAccessServicesException {
    // Do nothing just a stub
  }
}
