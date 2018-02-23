package gov.ca.cwds.cms.data.access.service.impl.placementHome;

import static org.junit.Assert.fail;

import gov.ca.cwds.cms.data.access.Constants.StaffPersonPrivileges;
import gov.ca.cwds.cms.data.access.dto.ClientEntityAwareDTO;
import gov.ca.cwds.cms.data.access.dto.PlacementHomeEntityAwareDTO;
import gov.ca.cwds.cms.data.access.service.impl.BaseDocToolRulesTest;
import gov.ca.cwds.cms.data.access.service.impl.PlacementHomeServiceImpl;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHome;
import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import gov.ca.cwds.drools.DroolsException;
import gov.ca.cwds.rest.exception.BusinessValidationException;
import org.junit.Before;

/** @author CWDS CALS API Team */
public abstract class BaseDocToolRulesPlacementHomeTest extends BaseDocToolRulesTest {

  protected PlacementHomeServiceImpl placementHomeService;

  protected PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO;

  @Before
  public void setUp() {
    placementHomeService = new PlacementHomeServiceImpl();
    placementHomeService.setDroolsService(droolsService);
    placementHomeEntityAwareDTO = prepareSuccessfulPlacementHomeEntityAwareDTO();
  }

  protected void checkRuleViolatedOnce(PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO, String ruleName) throws DroolsException {
    try {
      runBusinessValidation(placementHomeEntityAwareDTO);
      fail();
    } catch (BusinessValidationException e) {
      assertRuleViolatedOnce(ruleName, e);
    }
  }

  void checkRuleViolated(PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO, String ruleName, int count) throws DroolsException {
    try {
      runBusinessValidation(placementHomeEntityAwareDTO);
      fail();
    } catch (BusinessValidationException e) {
      assertRuleViolated(ruleName, e, count);
    }
  }

  void checkRuleValid(PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO, String ruleName) throws DroolsException {
    try {
      runBusinessValidation(placementHomeEntityAwareDTO);
    } catch (BusinessValidationException e) {
      assertRuleValid(ruleName, e);
    }
  }

  private void runBusinessValidation(PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO) throws DroolsException {
    placementHomeService.runBusinessValidation(placementHomeEntityAwareDTO, principal);
  }

  @Override
  public String getPrivilege() {
    return StaffPersonPrivileges.USR_PRV_RESOURCE_MGMT_PLACEMENT_FACILITY_MAINT;
  }

  protected void check(String ruleCode) {
    try {
      placementHomeService.runDataProcessing(placementHomeEntityAwareDTO, principal);
      placementHomeService.runBusinessValidation(placementHomeEntityAwareDTO, principal);
      fail();
    } catch (BusinessValidationException e) {
      assert e.getValidationDetailsList()
              .stream()
              .filter(issueDetails -> issueDetails.getCode().equals(ruleCode))
              .count()
          == 1;
    } catch (DroolsException e) {
      fail(e.getMessage());
    }
  }

  protected void assertValid(String ruleCode) {
    try {
      placementHomeService.runDataProcessing(placementHomeEntityAwareDTO, principal);
      placementHomeService.runBusinessValidation(placementHomeEntityAwareDTO, principal);
    } catch (BusinessValidationException e) {
      assert e.getValidationDetailsList().stream().noneMatch(issueDetails -> issueDetails.getCode().equals(ruleCode));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  protected void assertValid() {
    try {
      placementHomeService.runBusinessValidation(placementHomeEntityAwareDTO, principal);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  private PlacementHomeEntityAwareDTO prepareSuccessfulPlacementHomeEntityAwareDTO() {
    PlacementHomeEntityAwareDTO placementHomeEntityAwareDTO = new PlacementHomeEntityAwareDTO();
    PlacementHome placementHome = new PlacementHome();
    placementHome.setAgeToNo((short) 23);
    placementHome.setZipNo("11111");
    placementHome.setLaPZipno("11111");

    placementHome.setpZipNo("11111");
    placementHome.setPyeFstnm("PyeFstnm");
    placementHome.setPyeLstnm("PyeLstnm");
    placementHome.setPstreetNm("PstreetNm");
    placementHome.setpCityNm("pCityNm");
    placementHome.setPayeeStateCode((short)1);

    SubstituteCareProvider substituteCareProvider = new SubstituteCareProvider();
    substituteCareProvider.setZipNo("11111");
    placementHome.setPrimarySubstituteCareProvider(substituteCareProvider);
    placementHomeEntityAwareDTO.setEntity(placementHome);
    return placementHomeEntityAwareDTO;
  }
}
