package gov.ca.cwds.cms.data.access.service.impl;

import static org.apache.commons.lang3.StringUtils.upperCase;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.SubstituteCareProviderDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.SubstituteCareProviderUcDao;
import gov.ca.cwds.cms.data.access.parameter.SCPParameterObject;
import gov.ca.cwds.cms.data.access.service.SubstituteCareProviderService;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProviderUc;
import java.time.LocalDateTime;

/**
 * @author CWDS CALS API Team
 */

public class SubstituteCareProviderServiceImpl implements SubstituteCareProviderService {

  @Inject
  private SubstituteCareProviderDao substituteCareProviderDao;

  @Inject
  private SubstituteCareProviderUcDao substituteCareProviderUcDao;

  @Override
  public SubstituteCareProvider create(SubstituteCareProvider substituteCareProvider,
      SCPParameterObject parameterObject) {
    runBusinessRules(substituteCareProvider);
    SubstituteCareProvider storedSubstituteCareProvider = substituteCareProviderDao
        .create(substituteCareProvider);
    storeSubstituteCareProviderUc(substituteCareProvider, parameterObject);
    return storedSubstituteCareProvider;
  }

  private void storeSubstituteCareProviderUc(SubstituteCareProvider substituteCareProvider,
      SCPParameterObject parameterObject) {
    SubstituteCareProviderUc substituteCareProviderUc = new SubstituteCareProviderUc();
    substituteCareProviderUc.setPksbPvdrt(substituteCareProvider.getIdentifier());
    substituteCareProviderUc.setCaDlicNo(upperCase(substituteCareProvider.getCaDlicNo()));
    substituteCareProviderUc.setFirstNm(upperCase(substituteCareProvider.getFirstNm()));
    substituteCareProviderUc.setLastNm(upperCase(substituteCareProvider.getLastNm()));
    substituteCareProviderUc.setLstUpdId(parameterObject.getStaffPersonId());
    substituteCareProviderUc.setLstUpdTs(LocalDateTime.now());
    substituteCareProviderUcDao.create(substituteCareProviderUc);
  }

}
