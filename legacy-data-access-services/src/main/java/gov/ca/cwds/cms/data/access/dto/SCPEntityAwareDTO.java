package gov.ca.cwds.cms.data.access.dto;

import gov.ca.cwds.cms.data.access.CWSIdentifier;
import gov.ca.cwds.data.legacy.cms.entity.PhoneContactDetail;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import gov.ca.cwds.security.realm.PerryAccount;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CWDS CALS API Team
 */

public class SCPEntityAwareDTO extends BaseEntityAwareDTO<SubstituteCareProvider> {

  private String placementHomeId;
  private boolean primaryApplicant;
  private CWSIdentifier ethnicity;
  private List<? extends CWSIdentifier> otherStatesOfLiving = new ArrayList<>();
  private List<PhoneContactDetail> phoneNumbers = new ArrayList<>();

  public SCPEntityAwareDTO(PerryAccount perryAccount) {
    super(perryAccount);
  }

  public String getPlacementHomeId() {
    return placementHomeId;
  }

  public void setPlacementHomeId(String placementHomeId) {
    this.placementHomeId = placementHomeId;
  }

  public boolean isPrimaryApplicant() {
    return primaryApplicant;
  }

  public void setPrimaryApplicant(boolean primaryApplicant) {
    this.primaryApplicant = primaryApplicant;
  }

  public List<PhoneContactDetail> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(
      List<PhoneContactDetail> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public CWSIdentifier getEthnicity() {
    return ethnicity;
  }

  public void setEthnicity(CWSIdentifier ethnicity) {
    this.ethnicity = ethnicity;
  }

  public List<? extends CWSIdentifier> getOtherStatesOfLiving() {
    return otherStatesOfLiving;
  }

  public void setOtherStatesOfLiving(
      List<? extends CWSIdentifier> otherStatesOfLiving) {
    this.otherStatesOfLiving = otherStatesOfLiving;
  }

}
