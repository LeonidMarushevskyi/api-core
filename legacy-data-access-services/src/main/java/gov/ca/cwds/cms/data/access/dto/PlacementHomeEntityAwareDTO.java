package gov.ca.cwds.cms.data.access.dto;

import gov.ca.cwds.cms.data.access.CWSIdentifier;
import gov.ca.cwds.data.legacy.cms.entity.EmergencyContactDetail;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author CWDS CALS API Team
 */

public class PlacementHomeEntityAwareDTO extends BaseEntityAwareDTO<PlacementHome> {

  private List<SCPEntityAwareDTO> scpParameterObjects = new ArrayList<>();
  private List<OtherChildInHomeEntityAwareDTO> otherChildrenInHomeParameterObjects = new ArrayList<>();
  private List<OtherAdultInHomeEntityAwareDTO> otherAdultInHomeParameterObjects = new ArrayList<>();
  private EmergencyContactDetail emergencyContactDetail;

  private Set<CWSIdentifier> homeLanguages = new HashSet<>();

  public Set<CWSIdentifier> getHomeLanguages() {
    return homeLanguages;
  }

  public void setHomeLanguages(Set<CWSIdentifier> homeLanguages) {
    this.homeLanguages = homeLanguages;
  }

  public List<SCPEntityAwareDTO> getScpParameterObjects() {
    return scpParameterObjects;
  }

  public void setScpParameterObjects(
      List<SCPEntityAwareDTO> parameterObjects) {
    this.scpParameterObjects = parameterObjects;
  }

  public void addSCPParameterObject(SCPEntityAwareDTO parameterObject) {
    scpParameterObjects.add(parameterObject);
  }

  public List<OtherChildInHomeEntityAwareDTO> getOtherChildrenInHomeParameterObjects() {
    return otherChildrenInHomeParameterObjects;
  }

  public void setOtherChildrenInHomeParameterObjects(
      List<OtherChildInHomeEntityAwareDTO> parameterObjects) {
    this.otherChildrenInHomeParameterObjects = parameterObjects;
  }

  public void addOtherChildrenInHomeParameterObject(OtherChildInHomeEntityAwareDTO parameterObject) {
    otherChildrenInHomeParameterObjects.add(parameterObject);
  }

  public List<OtherAdultInHomeEntityAwareDTO> getOtherAdultInHomeParameterObjects() {
    return otherAdultInHomeParameterObjects;
  }

  public void setOtherAdultInHomeParameterObjects(
      List<OtherAdultInHomeEntityAwareDTO> parameterObjects) {
    this.otherAdultInHomeParameterObjects = parameterObjects;
  }

  public void addOtherAdultInHomeParameterObject(OtherAdultInHomeEntityAwareDTO parameterObject) {
    this.otherAdultInHomeParameterObjects.add(parameterObject);
  }

  public void addOtherChildInHomeParameterObject(OtherChildInHomeEntityAwareDTO parameterObject) {
    this.otherChildrenInHomeParameterObjects.add(parameterObject);
  }

  public EmergencyContactDetail getEmergencyContactDetail() {
    return emergencyContactDetail;
  }

  public void setEmergencyContactDetail(
      EmergencyContactDetail emergencyContactDetail) {
    this.emergencyContactDetail = emergencyContactDetail;
  }

}
