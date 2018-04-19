package gov.ca.cwds.cms.data.access.service.impl;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.dto.ChildClientEntityAwareDTO;
import gov.ca.cwds.cms.data.access.service.lifecycle.DataAccessBundle;
import gov.ca.cwds.cms.data.access.service.lifecycle.DataAccessServiceLifecycle;
import gov.ca.cwds.data.legacy.cms.dao.ChildClientDao;
import gov.ca.cwds.data.legacy.cms.dao.CreditReportHistoryDao;
import gov.ca.cwds.data.legacy.cms.dao.FCEligibilityDao;
import gov.ca.cwds.data.legacy.cms.dao.HealthInterventionPlanDao;
import gov.ca.cwds.data.legacy.cms.dao.HealthReferralDao;
import gov.ca.cwds.data.legacy.cms.dao.HealthScreeningDao;
import gov.ca.cwds.data.legacy.cms.dao.MedicalEligibilityApplicationDao;
import gov.ca.cwds.data.legacy.cms.dao.ParentalRightsTerminationDao;
import gov.ca.cwds.data.legacy.cms.dao.PaternityDetailDao;
import gov.ca.cwds.data.legacy.cms.dao.SchoolOriginHistoryDao;
import gov.ca.cwds.data.legacy.cms.dao.SpecialEducationDao;
import gov.ca.cwds.data.legacy.cms.entity.ChildClient;
import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.data.legacy.cms.entity.CreditReportHistory;
import gov.ca.cwds.data.legacy.cms.entity.CsecHistory;
import gov.ca.cwds.data.legacy.cms.entity.FCEligibility;
import gov.ca.cwds.data.legacy.cms.entity.HealthInterventionPlan;
import gov.ca.cwds.data.legacy.cms.entity.HealthReferral;
import gov.ca.cwds.data.legacy.cms.entity.HealthScreening;
import gov.ca.cwds.data.legacy.cms.entity.MedicalEligibilityApplication;
import gov.ca.cwds.data.legacy.cms.entity.ParentalRightsTermination;
import gov.ca.cwds.data.legacy.cms.entity.PaternityDetail;
import gov.ca.cwds.data.legacy.cms.entity.SchoolOriginHistory;
import gov.ca.cwds.data.legacy.cms.entity.SpecialEducation;
import gov.ca.cwds.security.annotations.Authorize;
import gov.ca.cwds.security.utils.PrincipalUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static gov.ca.cwds.cms.data.access.Constants.Authorize.CLIENT_READ_CLIENT;

/** @author CWDS TPT-3 Team */
public class ChildClientCoreService extends ClientCoreService {

  @Inject private HealthInterventionPlanDao healthInterventionPlanDao;
  @Inject private ParentalRightsTerminationDao parentalRightsTerminationDao;
  @Inject private MedicalEligibilityApplicationDao medicalEligibilityApplicationDao;
  @Inject private FCEligibilityDao fcEligibilityDao;
  @Inject private CsecHistoryService csecHistoryService;
  @Inject private PaternityDetailDao paternityDetailDao;
  @Inject private CreditReportHistoryDao creditReportHistoryDao;
  @Inject private SpecialEducationDao specialEducationDao;
  @Inject private HealthReferralDao healthReferralDao;
  @Inject private SchoolOriginHistoryDao schoolOriginHistoryDao;
  @Inject private HealthScreeningDao healthScreeningDao;

  @Inject
  public ChildClientCoreService(ChildClientDao crudDao) {
    super(crudDao);
  }

  @Override
  @Authorize(CLIENT_READ_CLIENT)
  public ChildClient find(Serializable primaryKey) {
    Client childClient = super.find(primaryKey);
    if (childClient instanceof ChildClient) {
      return (ChildClient) childClient;
    }
    return null;
  }

  @Override
  protected DataAccessServiceLifecycle getUpdateLifeCycle() {
    return new ChildClientUpdateLifeCycle();
  }

  private class ChildClientUpdateLifeCycle extends UpdateLifecycle {

    @Override
    public void beforeBusinessValidation(DataAccessBundle bundle) {
      super.beforeBusinessValidation(bundle);

      ChildClientEntityAwareDTO clientEntityAwareDTO =
          (ChildClientEntityAwareDTO) bundle.getAwareDto();

      ChildClient childClient = (ChildClient) clientEntityAwareDTO.getEntity();
      final String childClientId = childClient.getIdentifier();

      if (childClient.getAfdcFcEligibilityIndicatorVar()) {
        List<FCEligibility> fcEligibilities = fcEligibilityDao.findByChildClientId(childClientId);
        clientEntityAwareDTO.getFcEligibilities().addAll(fcEligibilities);
      }

      List<HealthInterventionPlan> activeHealthInterventionPlans =
          healthInterventionPlanDao.findByChildClientId(childClientId);
      clientEntityAwareDTO.getActiveHealthInterventionPlans().addAll(activeHealthInterventionPlans);

      List<ParentalRightsTermination> parentalRightsTerminations =
          parentalRightsTerminationDao.findByChildClientId(childClientId);
      clientEntityAwareDTO.getParentalRightsTerminations().addAll(parentalRightsTerminations);

      List<MedicalEligibilityApplication> medicalEligibilityApplications =
          medicalEligibilityApplicationDao.findByChildClientId(childClientId);
      clientEntityAwareDTO
          .getMedicalEligibilityApplications()
          .addAll(medicalEligibilityApplications);

      List<PaternityDetail> paternityDetails =
          paternityDetailDao.findByChildClientId(childClientId);
      clientEntityAwareDTO.setPaternityDetails(paternityDetails);

      List<CreditReportHistory> creditReportHistories =
          creditReportHistoryDao.findByClientId(childClientId);
      clientEntityAwareDTO.getCreditReportHistories().addAll(creditReportHistories);

      List<SpecialEducation> specialEducations = specialEducationDao.findByClientId(childClientId);
      clientEntityAwareDTO.getSpecialEducations().addAll(specialEducations);

      List<HealthReferral> healthReferrals = healthReferralDao.findByChildClientId(childClientId);
      clientEntityAwareDTO.getHealthReferrals().addAll(healthReferrals);

      final Collection<SchoolOriginHistory> schoolOriginHistories =
          schoolOriginHistoryDao.findByClientId(childClientId);
      clientEntityAwareDTO.getSchoolOriginHistories().addAll(schoolOriginHistories);

      List<HealthScreening> healthScreenings =
          healthScreeningDao.findByChildClientId(childClientId);
      clientEntityAwareDTO.getHealthScreenings().addAll(healthScreenings);

      if (!clientEntityAwareDTO.isEnriched()) {
        List<CsecHistory> csecHistories = csecHistoryService.findByClientId(childClientId);
        clientEntityAwareDTO.getCsecHistories().addAll(csecHistories);
      }
    }

    @Override
    public void afterBusinessValidation(DataAccessBundle bundle) {
      super.afterBusinessValidation(bundle);
      ChildClientEntityAwareDTO childClientEntityAwareDTO =
          (ChildClientEntityAwareDTO) bundle.getAwareDto();
      ChildClient childClient = (ChildClient) childClientEntityAwareDTO.getEntity();
      childClient.setChildClientLastUpdateId(PrincipalUtils.getStaffPersonId());
      childClient.setChildClientLastUpdateTime(LocalDateTime.now());
    }

    @Override
    public void afterStore(DataAccessBundle bundle) {
      super.afterStore(bundle);
      ChildClientEntityAwareDTO childClientEntityAwareDTO =
          (ChildClientEntityAwareDTO) bundle.getAwareDto();
      if (childClientEntityAwareDTO.isEnriched()) {
        csecHistoryService.updateCsecHistoriesByClientId(
            childClientEntityAwareDTO.getEntity().getIdentifier(),
            childClientEntityAwareDTO.getCsecHistories());
      }
    }
  }
}
