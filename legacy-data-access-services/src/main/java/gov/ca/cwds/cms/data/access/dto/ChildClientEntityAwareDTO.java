package gov.ca.cwds.cms.data.access.dto;

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
import gov.ca.cwds.data.legacy.cms.entity.TribalAncestryNotification;

import java.util.ArrayList;
import java.util.List;

/** @author CWDS TPT-3 Team */
public class ChildClientEntityAwareDTO extends ClientEntityAwareDTO {

  private List<FCEligibility> fcEligibilities = new ArrayList<>();
  private List<HealthInterventionPlan> activeHealthInterventionPlans = new ArrayList<>();
  private List<ParentalRightsTermination> parentalRightsTerminations = new ArrayList<>();
  private List<MedicalEligibilityApplication> medicalEligibilityApplications = new ArrayList<>();
  private List<CsecHistory> csecHistories = new ArrayList<>();
  private List<SchoolOriginHistory> schoolOriginHistories = new ArrayList<>();
  private List<PaternityDetail> paternityDetails = new ArrayList<>();
  private List<CreditReportHistory> creditReportHistories = new ArrayList<>();
  private List<SpecialEducation> specialEducations = new ArrayList<>();
  private List<HealthReferral> healthReferrals = new ArrayList<>();
  private List<TribalAncestryNotification> tribalAncestryNotifications = new ArrayList<>();
  private List<HealthScreening> healthScreenings = new ArrayList<>();

  public List<MedicalEligibilityApplication> getMedicalEligibilityApplications() {
    return medicalEligibilityApplications;
  }

  public List<FCEligibility> getFcEligibilities() {
    return fcEligibilities;
  }

  public List<HealthInterventionPlan> getActiveHealthInterventionPlans() {
    return activeHealthInterventionPlans;
  }

  public List<ParentalRightsTermination> getParentalRightsTerminations() {
    return parentalRightsTerminations;
  }

  public List<CsecHistory> getCsecHistories() {
    return csecHistories;
  }

  public List<SchoolOriginHistory> getSchoolOriginHistories() {
    return schoolOriginHistories;
  }

  public List<PaternityDetail> getPaternityDetails() {
    return paternityDetails;
  }

  public void setPaternityDetails(List<PaternityDetail> paternityDetails) {
    this.paternityDetails = paternityDetails;
  }

  public List<CreditReportHistory> getCreditReportHistories() {
    return creditReportHistories;
  }

  public List<SpecialEducation> getSpecialEducations() {
    return specialEducations;
  }

  public List<HealthReferral> getHealthReferrals() {
    return healthReferrals;
  }

  public List<HealthScreening> getHealthScreenings() {
    return healthScreenings;
  }

  public List<TribalAncestryNotification> getTribalAncestryNotifications() {
    return tribalAncestryNotifications;
  }
}
