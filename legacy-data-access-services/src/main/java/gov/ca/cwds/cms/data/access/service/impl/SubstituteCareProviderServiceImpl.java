package gov.ca.cwds.cms.data.access.service.impl;

import static org.apache.commons.lang3.StringUtils.upperCase;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.CWSIdentifier;
import gov.ca.cwds.cms.data.access.Constants;
import gov.ca.cwds.cms.data.access.Constants.PhoneticSearchTables;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.ClientScpEthnicityDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.CountyOwnershipDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.OutOfStateCheckDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.PhoneContactDetailDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.PlacementHomeInformationDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.ScpSsaName3Dao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.SubstituteCareProviderDao;
import gov.ca.cwds.cms.data.access.dao.substitutecareprovider.SubstituteCareProviderUcDao;
import gov.ca.cwds.cms.data.access.mapper.CountyOwnershipMapper;
import gov.ca.cwds.cms.data.access.parameter.SCPParameterObject;
import gov.ca.cwds.cms.data.access.service.SubstituteCareProviderService;
import gov.ca.cwds.cms.data.access.utils.IdGenerator;
import gov.ca.cwds.data.legacy.cms.dao.SsaName3ParameterObject;
import gov.ca.cwds.data.legacy.cms.entity.ClientScpEthnicity;
import gov.ca.cwds.data.legacy.cms.entity.CountyOwnership;
import gov.ca.cwds.data.legacy.cms.entity.OutOfStateCheck;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHomeInformation;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProviderUc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author CWDS CALS API Team
 */

public class SubstituteCareProviderServiceImpl implements SubstituteCareProviderService {

  @Inject
  private SubstituteCareProviderDao substituteCareProviderDao;

  @Inject
  private SubstituteCareProviderUcDao substituteCareProviderUcDao;

  @Inject
  private CountyOwnershipDao countyOwnershipDao;

  @Inject
  private CountyOwnershipMapper countyOwnershipMapper;

  @Inject
  private PlacementHomeInformationDao placementHomeInformationDao;

  @Inject
  private PhoneContactDetailDao phoneContactDetailDao;

  @Inject
  private ScpSsaName3Dao scpSsaName3Dao;

  @Inject
  private ClientScpEthnicityDao clientScpEthnicityDao;

  @Inject
  private OutOfStateCheckDao outOfStateCheckDao;

  @Override
  public SubstituteCareProvider create(SubstituteCareProvider substituteCareProvider,
      SCPParameterObject parameterObject) {
    runBusinessRules(substituteCareProvider);
    SubstituteCareProvider storedSubstituteCareProvider = substituteCareProviderDao
        .create(substituteCareProvider);
    storeSubstituteCareProviderUc(substituteCareProvider, parameterObject);
    storeCountyOwnership(substituteCareProvider.getIdentifier());
    storePlacementHomeInformation(substituteCareProvider, parameterObject);
    storePhoneContactDetails(storedSubstituteCareProvider, parameterObject);
    storeEthnicity(storedSubstituteCareProvider, parameterObject);
    storeOutOfStateChecks(storedSubstituteCareProvider, parameterObject);
    prepareSubstituteCareProviderPhoneticSearchKeywords(substituteCareProvider);
    return storedSubstituteCareProvider;
  }

  private void storeOutOfStateChecks(SubstituteCareProvider storedSubstituteCareProvider,
      SCPParameterObject parameterObject) {
    if (CollectionUtils.isNotEmpty(parameterObject.getOtherStatesOfLiving())) {
      for (CWSIdentifier stateId : parameterObject.getOtherStatesOfLiving()) {
        OutOfStateCheck outOfStateCheck = new OutOfStateCheck();
        outOfStateCheck.setStateC((short)(stateId.getCwsId()));
        outOfStateCheck.setRcpntId(storedSubstituteCareProvider.getIdentifier());
        outOfStateCheck.setRcpntCd("S");
        outOfStateCheck.setIdentifier(IdGenerator.generateId(parameterObject.getStaffPersonId()));
        outOfStateCheck.setLstUpdId(parameterObject.getStaffPersonId());
        outOfStateCheck.setLstUpdTs(LocalDateTime.now());
        outOfStateCheckDao.create(outOfStateCheck);
      }
    }
  }

  private void storeEthnicity(SubstituteCareProvider storedSubstituteCareProvider,
      SCPParameterObject parameterObject) {
    ClientScpEthnicity clientScpEthnicity = new ClientScpEthnicity();
    clientScpEthnicity.setEthnctyc((short)parameterObject.getEthnicity().getCwsId());
    clientScpEthnicity.setEstblshId(storedSubstituteCareProvider.getIdentifier());
    clientScpEthnicity.setEstblshCd("S");
    clientScpEthnicity.setIdentifier(IdGenerator.generateId(parameterObject.getStaffPersonId()));
    clientScpEthnicity.setLstUpdId(parameterObject.getStaffPersonId());
    clientScpEthnicity.setLstUpdTs(LocalDateTime.now());
    clientScpEthnicityDao.create(clientScpEthnicity);
  }

  private void prepareSubstituteCareProviderPhoneticSearchKeywords(
      SubstituteCareProvider substituteCareProvider) {
    SsaName3ParameterObject parameterObject = new SsaName3ParameterObject();
    parameterObject.setTableName(PhoneticSearchTables.SCP_PHTT);
    parameterObject.setCrudOper("I");
    parameterObject.setIdentifier(substituteCareProvider.getIdentifier());
    parameterObject.setFirstName(substituteCareProvider.getFirstNm());
    parameterObject.setMiddleName(substituteCareProvider.getMidIniNm());
    parameterObject.setLastName(substituteCareProvider.getLastNm());
    parameterObject.setStreetNumber("");
    parameterObject.setStreetName("");
    parameterObject.setGvrEntc((short) 0);
    parameterObject.setUpdateTimeStamp(new Date());
    parameterObject.setUpdateId(substituteCareProvider.getLstUpdId());
    scpSsaName3Dao.callStoredProc(parameterObject);
  }

  private void storePhoneContactDetails(SubstituteCareProvider substituteCareProvider,
      SCPParameterObject parameterObject) {
    if (CollectionUtils.isNotEmpty(parameterObject.getPhoneNumbers())) {
      parameterObject.getPhoneNumbers()
          .forEach(phoneNumber -> {
            phoneNumber.setEstblshId(substituteCareProvider.getIdentifier());
            phoneContactDetailDao.create(phoneNumber);
          });
    }
  }

  private void storePlacementHomeInformation(SubstituteCareProvider substituteCareProvider,
      SCPParameterObject parameterObject) {
    PlacementHomeInformation placementHomeInformation = new PlacementHomeInformation();
    placementHomeInformation.setThirdId(IdGenerator.generateId(parameterObject.getStaffPersonId()));
    placementHomeInformation.setStartDt(LocalDate.now());
    placementHomeInformation.setLicnseeCd("U");
    placementHomeInformation.setCrprvdrCd("Y");
    placementHomeInformation.setLstUpdId(parameterObject.getStaffPersonId());
    placementHomeInformation.setLstUpdTs(LocalDateTime.now());
    placementHomeInformation.setFksbPvdrt(substituteCareProvider.getIdentifier());
    placementHomeInformation.setFkplcHmT(parameterObject.getPlacementHomeId());
    placementHomeInformation
        .setPrprvdrCd(parameterObject.isPrimaryApplicant() ? Constants.Y : Constants.N);
    placementHomeInformation.setCdsPrsn(Constants.SPACE);
    placementHomeInformation
        .setScprvdInd(parameterObject.isPrimaryApplicant() ? Constants.N : Constants.Y);
    placementHomeInformationDao.create(placementHomeInformation);
  }

  private void storeCountyOwnership(String scpIdentifier) {
    CountyOwnership countyOwnership =
        countyOwnershipMapper.toCountyOwnership(scpIdentifier,
            "S", Collections.emptyList());
    countyOwnershipDao.create(countyOwnership);
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
