package gov.ca.cwds.cms.data.access.service;

import static gov.ca.cwds.cms.data.access.service.IdGenerator.generateId;
import static gov.ca.cwds.cms.data.access.utils.ParametersValidator.checkNotPersisted;
import static gov.ca.cwds.security.utils.PrincipalUtils.getStaffPersonId;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.CWSIdentifier;
import gov.ca.cwds.cms.data.access.Constants.PhoneticSearchTables;
import gov.ca.cwds.cms.data.access.dao.BackgroundCheckDao;
import gov.ca.cwds.cms.data.access.dao.CountyOwnershipDao;
import gov.ca.cwds.cms.data.access.dao.EmergencyContactDetailDao;
import gov.ca.cwds.cms.data.access.dao.ExternalInterfaceDao;
import gov.ca.cwds.cms.data.access.dao.OtherAdultsInPlacementHomeDao;
import gov.ca.cwds.cms.data.access.dao.OtherChildrenInPlacementHomeDao;
import gov.ca.cwds.cms.data.access.dao.OtherPeopleScpRelationshipDao;
import gov.ca.cwds.cms.data.access.dao.OutOfStateCheckDao;
import gov.ca.cwds.cms.data.access.dao.PlacementFacilityTypeHistoryDao;
import gov.ca.cwds.cms.data.access.dao.PlacementHomeDao;
import gov.ca.cwds.cms.data.access.dao.PlacementHomeProfileDao;
import gov.ca.cwds.cms.data.access.dao.PlacementHomeUcDao;
import gov.ca.cwds.cms.data.access.dao.SsaName3Dao;
import gov.ca.cwds.cms.data.access.dto.OtherAdultInHomeEntityAwareDTO;
import gov.ca.cwds.cms.data.access.dto.OtherChildInHomeEntityAwareDTO;
import gov.ca.cwds.cms.data.access.dto.PlacementHomeEntityAwareDTO;
import gov.ca.cwds.cms.data.access.dto.SCPEntityAwareDTO;
import gov.ca.cwds.cms.data.access.mapper.CountyOwnershipMapper;
import gov.ca.cwds.cms.data.access.mapper.ExternalInterfaceMapper;
import gov.ca.cwds.cms.data.access.service.rules.PlacementHomeDroolsConfiguration;
import gov.ca.cwds.cms.data.access.utils.ParametersValidator;
import gov.ca.cwds.data.legacy.cms.dao.SsaName3ParameterObject;
import gov.ca.cwds.data.legacy.cms.entity.BackgroundCheck;
import gov.ca.cwds.data.legacy.cms.entity.CountyOwnership;
import gov.ca.cwds.data.legacy.cms.entity.EmergencyContactDetail;
import gov.ca.cwds.data.legacy.cms.entity.ExternalInterface;
import gov.ca.cwds.data.legacy.cms.entity.OtherAdultsInPlacementHome;
import gov.ca.cwds.data.legacy.cms.entity.OtherChildrenInPlacementHome;
import gov.ca.cwds.data.legacy.cms.entity.OtherPeopleScpRelationship;
import gov.ca.cwds.data.legacy.cms.entity.OutOfStateCheck;
import gov.ca.cwds.data.legacy.cms.entity.PlacementFacilityTypeHistory;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHome;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHomeProfile;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHomeUc;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import gov.ca.cwds.drools.DroolsException;
import gov.ca.cwds.security.realm.PerryAccount;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/** @author CWDS TPT-3 Team */
public class PlacementHomeCoreService
    extends DataAccessServiceBase<PlacementHomeDao, PlacementHome, PlacementHomeEntityAwareDTO> {

  @Inject private BusinessValidationService businessValidationService;
  @Inject private PlacementHomeUcDao placementHomeUcDao;
  @Inject private CountyOwnershipMapper countyOwnershipMapper;
  @Inject private CountyOwnershipDao countyOwnershipDao;
  @Inject private ExternalInterfaceDao externalInterfaceDao;
  @Inject private ExternalInterfaceMapper externalInterfaceMapper;
  @Inject private EmergencyContactDetailDao emergencyContactDetailDao;
  @Inject private PlacementHomeProfileDao placementHomeProfileDao;
  @Inject private PlacementFacilityTypeHistoryDao placementFacilityTypeHistoryDao;
  @Inject private SubstituteCareProviderCoreService substituteCareProviderService;
  @Inject private OtherChildrenInPlacementHomeDao otherChildrenInPlacementHomeDao;
  @Inject private OtherPeopleScpRelationshipDao otherPeopleScpRelationshipDao;
  @Inject private OtherAdultsInPlacementHomeDao otherAdultsInPlacementHomeDao;
  @Inject private OutOfStateCheckDao outOfStateCheckDao;
  @Inject private BackgroundCheckDao backgroundCheckDao;
  @Inject private SsaName3Dao ssaName3Dao;

  @Inject
  public PlacementHomeCoreService(PlacementHomeDao crudDao) {
    super(crudDao);
  }

  @Override
  public void beforeDataProcessing(DataAccessBundle bundle) {
    validateParameters((PlacementHomeEntityAwareDTO) bundle.getAwareDto());
  }

  @Override
  public void afterBusinessValidation(DataAccessBundle bundle) {
    final PlacementHome placementHome = (PlacementHome) bundle.getAwareDto().getEntity();
    placementHome.setIdentifier(generateId());
    placementHome.setLastUpdateId(getStaffPersonId());
    placementHome.setLastUpdateTime(LocalDateTime.now());
  }

  @Override
  public void dataProcessing(DataAccessBundle bundle, PerryAccount perryAccount)
      throws DroolsException {
    businessValidationService.runDataProcessing(
        bundle.getAwareDto(),
        perryAccount,
        PlacementHomeDroolsConfiguration.DATA_PROCESSING_INSTANCE);
  }

  @Override
  public void businessValidation(DataAccessBundle bundle, PerryAccount perryAccount)
      throws DroolsException {
    businessValidationService.runBusinessValidation(
        bundle.getAwareDto(), perryAccount, PlacementHomeDroolsConfiguration.INSTANCE);
  }

  @Override
  public void afterCreate(DataAccessBundle bundle) throws DataAccessServicesException {
    PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO =
        (PlacementHomeEntityAwareDTO) bundle.getAwareDto();
    createPlacementHomeUc(placementHomeEntityAwareDTO);
    createCountyOwnership(placementHomeEntityAwareDTO);
    createExternalInterface();
    createBackgroundCheck();
    createEmergencyContactDetail(placementHomeEntityAwareDTO);
    createPlacementHomeProfile(placementHomeEntityAwareDTO);
    createPlacementFacilityTypeHistory(placementHomeEntityAwareDTO);
    createSubstituteCareProviders(placementHomeEntityAwareDTO);
    createOtherAdultsInHome(placementHomeEntityAwareDTO);
    createOtherChildrenInHome(placementHomeEntityAwareDTO);
    prepareAddressPhoneticSearchKeywords(placementHomeEntityAwareDTO.getEntity());
  }

  private void validateParameters(PlacementHomeEntityAwareDTO placementHomeParameterObject) {
    checkNotPersisted(placementHomeParameterObject.getEntity());
    ParametersValidator.validateParameterObjects(
        placementHomeParameterObject.getScpParameterObjects());
    ParametersValidator.validateParameterObjects(
        placementHomeParameterObject.getOtherAdultInHomeParameterObjects());
    ParametersValidator.validateParameterObjects(
        placementHomeParameterObject.getOtherChildrenInHomeParameterObjects());
  }

  private void createBackgroundCheck() {
    BackgroundCheck backgroundCheck = new BackgroundCheck();
    backgroundCheck.setIdentifier(IdGenerator.generateId());
    backgroundCheck.setBkgrchkc((short) -1);
    backgroundCheck.setBkgrchkDt(LocalDate.now());
    backgroundCheck.setLstUpdId(getStaffPersonId());
    backgroundCheck.setLstUpdTs(LocalDateTime.now());
    backgroundCheckDao.create(backgroundCheck);
  }

  private void createOtherChildrenInHome(PlacementHomeEntityAwareDTO parameterObject) {
    PlacementHome placementHome = parameterObject.getEntity();
    for (OtherChildInHomeEntityAwareDTO otherChildInHomeParameterObject :
        parameterObject.getOtherChildrenInHomeParameterObjects()) {
      createOtherChildInHome(placementHome, otherChildInHomeParameterObject);
      createChildRelationshipsToScp(otherChildInHomeParameterObject);
    }
  }

  private void createOtherChildInHome(
      PlacementHome placementHome, OtherChildInHomeEntityAwareDTO parameterObject) {
    OtherChildrenInPlacementHome otherChildInPlacementHome = parameterObject.getEntity();
    otherChildInPlacementHome.setLstUpdId(getStaffPersonId());
    otherChildInPlacementHome.setLstUpdTs(LocalDateTime.now());
    otherChildInPlacementHome.setFkplcHmT(placementHome.getIdentifier());
    otherChildInPlacementHome.setIdentifier(generateId());
    otherChildrenInPlacementHomeDao.create(otherChildInPlacementHome);
  }

  private void createChildRelationshipsToScp(OtherChildInHomeEntityAwareDTO parameterObject) {
    OtherChildrenInPlacementHome otherChildInPlacementHome = parameterObject.getEntity();
    for (OtherPeopleScpRelationship relationship : parameterObject.getRelationships()) {
      relationship.setIdentifier(generateId());
      relationship.setFkothKidt(otherChildInPlacementHome.getIdentifier());
      relationship.setLstUpdId(getStaffPersonId());
      relationship.setLstUpdTs(LocalDateTime.now());
      otherPeopleScpRelationshipDao.create(relationship);
    }
  }

  private void createOtherAdultsInHome(PlacementHomeEntityAwareDTO parameterObject) {
    final PlacementHome placementHome = parameterObject.getEntity();
    for (OtherAdultInHomeEntityAwareDTO adultInHomeParameterObject :
        parameterObject.getOtherAdultInHomeParameterObjects()) {
      createOtherAdultInHome(placementHome, adultInHomeParameterObject);
      createAdultRelationshipsToScp(adultInHomeParameterObject);
      createAdultOutOfStateChecks(adultInHomeParameterObject);
    }
  }

  private void createAdultOutOfStateChecks(OtherAdultInHomeEntityAwareDTO parameterObject) {
    OtherAdultsInPlacementHome otherAdultInPlacementHome = parameterObject.getEntity();
    for (OutOfStateCheck outOfStateCheck : parameterObject.getOutOfStateChecks()) {
      outOfStateCheck.setIdentifier(generateId());
      outOfStateCheck.setRcpntCd("O");
      outOfStateCheck.setRcpntId(otherAdultInPlacementHome.getIdentifier());
      outOfStateCheck.setLstUpdId(getStaffPersonId());
      outOfStateCheck.setLstUpdTs(LocalDateTime.now());
      outOfStateCheckDao.create(outOfStateCheck);
    }
  }

  private void createOtherAdultInHome(
      PlacementHome placementHome, OtherAdultInHomeEntityAwareDTO parameterObject) {
    OtherAdultsInPlacementHome otherAdultInPlacementHome = parameterObject.getEntity();
    otherAdultInPlacementHome.setLstUpdId(getStaffPersonId());
    otherAdultInPlacementHome.setLstUpdTs(LocalDateTime.now());
    otherAdultInPlacementHome.setFkplcHmT(placementHome.getIdentifier());
    otherAdultInPlacementHome.setIdentifier(generateId());
    otherAdultsInPlacementHomeDao.create(otherAdultInPlacementHome);
  }

  private void createAdultRelationshipsToScp(OtherAdultInHomeEntityAwareDTO parameterObject) {
    OtherAdultsInPlacementHome otherAdultInPlacementHome = parameterObject.getEntity();
    for (OtherPeopleScpRelationship relationship : parameterObject.getRelationships()) {
      relationship.setIdentifier(generateId());
      relationship.setFkothAdlt(otherAdultInPlacementHome.getIdentifier());
      relationship.setLstUpdId(getStaffPersonId());
      relationship.setLstUpdTs(LocalDateTime.now());
      otherPeopleScpRelationshipDao.create(relationship);
    }
  }

  private void createSubstituteCareProviders(PlacementHomeEntityAwareDTO parameterObject)
      throws DataAccessServicesException {
    final PlacementHome placementHome = parameterObject.getEntity();
    for (SCPEntityAwareDTO scpParameterObject : parameterObject.getScpParameterObjects()) {
      scpParameterObject.setPlacementHomeId(placementHome.getIdentifier());
      SubstituteCareProvider substituteCareProvider =
          substituteCareProviderService.create(scpParameterObject);
      if (scpParameterObject.isPrimaryApplicant()) {
        placementHome.setPrimarySubstituteCareProvider(substituteCareProvider);
      }
    }
  }

  private void createCountyOwnership(PlacementHomeEntityAwareDTO parameterObject) {
    final PlacementHome placementHome = parameterObject.getEntity();
    CountyOwnership countyOwnership =
        countyOwnershipMapper.toCountyOwnership(
            placementHome.getIdentifier(), "P", Collections.emptyList());
    countyOwnershipDao.create(countyOwnership);
  }

  private void createPlacementHomeUc(PlacementHomeEntityAwareDTO parameterObject) {
    PlacementHomeUc placementHomeUc = new PlacementHomeUc();
    final PlacementHome placementHome = parameterObject.getEntity();
    placementHomeUc.setCityNm(StringUtils.upperCase(placementHome.getCityNm()));
    placementHomeUc.setGeoRgntcd(StringUtils.upperCase(placementHome.getGeoRgntcd()));
    placementHomeUc.setLaVndrId(StringUtils.upperCase(placementHome.getLaVndrId()));
    placementHomeUc.setLicenseNo(StringUtils.upperCase(placementHome.getLicenseNo()));
    placementHomeUc.setFacltyNm(StringUtils.upperCase(placementHome.getFacltyNm()));
    placementHomeUc.setStreetNo(StringUtils.upperCase(placementHome.getStreetNo()));
    placementHomeUc.setStreetNm(StringUtils.upperCase(placementHome.getStreetNm()));
    placementHomeUc.setPkplcHmt(placementHome.getIdentifier());
    placementHomeUc.setLstUpdId(getStaffPersonId());
    placementHomeUc.setLstUpdTs(LocalDateTime.now());
    placementHomeUcDao.create(placementHomeUc);
  }

  private void createExternalInterface() {
    ExternalInterface externalInterface = externalInterfaceMapper.toExternalInterface("");
    externalInterfaceDao.create(externalInterface);
  }

  private void createEmergencyContactDetail(PlacementHomeEntityAwareDTO parameterObject) {
    final PlacementHome placementHome = parameterObject.getEntity();
    EmergencyContactDetail emergencyContactDetail = parameterObject.getEmergencyContactDetail();
    if (emergencyContactDetail != null) {
      emergencyContactDetail.setEstblshCd("P"); // P = PLACEMENT HOME
      emergencyContactDetail.setEstblshId(placementHome.getIdentifier());
      emergencyContactDetail.setLstUpdId(getStaffPersonId());
      emergencyContactDetail.setLstUpdTs(LocalDateTime.now());
      emergencyContactDetail.setIdentifier(generateId());
      emergencyContactDetailDao.create(emergencyContactDetail);
    }
  }

  private void createPlacementHomeProfile(PlacementHomeEntityAwareDTO parameterObject) {
    final PlacementHome placementHome = parameterObject.getEntity();
    for (CWSIdentifier homeLanguage : parameterObject.getHomeLanguages()) {
      PlacementHomeProfile placementHomeProfile = new PlacementHomeProfile();
      placementHomeProfile.setThirdId(generateId());
      placementHomeProfile.setChrctrC((short) homeLanguage.getCwsId());
      placementHomeProfile.setChrctrCd("L");
      placementHomeProfile.setLstUpdId(getStaffPersonId());
      placementHomeProfile.setLstUpdTs(LocalDateTime.now());
      placementHomeProfile.setFkplcHmT(placementHome.getIdentifier());
      placementHomeProfileDao.create(placementHomeProfile);
    }
  }

  /**
   * Rule: R - 11179
   *
   * <p>Rule Txt
   *
   * <p>If the placement home is being saved to the database for the first time then create a new
   * Placement Facility Type History row.
   *
   * <p>Logic If (in focus) PLACEMENT_HOME is saved to the database for the first time then create
   * PLACEMENT_HOME > PLACEMENT_FACILITY_TYPE_HISTORY set .Start_Timestamp = System Timestamp AND
   * .Placement_Facility_Type = (in focus) PLACEMENT_HOME.Placement_Facility_Type.
   */
  private void createPlacementFacilityTypeHistory(PlacementHomeEntityAwareDTO parameterObject) {
    final PlacementHome placementHome = parameterObject.getEntity();
    PlacementFacilityTypeHistory placementFacilityTypeHistory = new PlacementFacilityTypeHistory();
    placementFacilityTypeHistory.setThirdId(generateId());
    placementFacilityTypeHistory.setFkplcHmT(placementHome.getIdentifier());
    placementFacilityTypeHistory.setPlacementFacilityType(placementHome.getFacilityType());
    placementFacilityTypeHistory.setStartTimestamp(LocalDateTime.now());
    placementFacilityTypeHistory.setCreationTimestamp(LocalDateTime.now());
    placementFacilityTypeHistory.setLastUpdateTimestamp(LocalDateTime.now());
    placementFacilityTypeHistory.setLastUpdateId(getStaffPersonId());
    placementFacilityTypeHistoryDao.create(placementFacilityTypeHistory);
  }

  private void prepareAddressPhoneticSearchKeywords(PlacementHome placementHome) {
    SsaName3ParameterObject parameterObject = new SsaName3ParameterObject();
    parameterObject.setTableName(PhoneticSearchTables.ADR_PHTT);
    parameterObject.setCrudOper("I");
    parameterObject.setIdentifier(placementHome.getIdentifier());
    parameterObject.setNameCd("P");
    parameterObject.setStreetNumber(placementHome.getStreetNo());
    parameterObject.setStreetName(placementHome.getStreetNm());
    parameterObject.setGvrEntc(placementHome.getGvrEntc());
    parameterObject.setUpdateTimeStamp(new Date());
    parameterObject.setUpdateId(placementHome.getLastUpdateId());
    ssaName3Dao.callStoredProc(parameterObject);
  }
}
