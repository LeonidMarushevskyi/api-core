package gov.ca.cwds.authorizer;

import static gov.ca.cwds.util.PerryAccountUtils.initPerryAccountWithPrivileges;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import gov.ca.cwds.authorizer.drools.DroolsAuthorizationService;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHome;
import gov.ca.cwds.drools.DroolsService;
import gov.ca.cwds.security.realm.PerryAccount;
import gov.ca.cwds.security.realm.PerrySubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author CWDS TPT-2 Team
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("org.drools.core.util.Drools")
@PrepareForTest(PerrySubject.class)
public class PlacementHomeCreateAuthorizerTest {

  private PlacementHomeCreateAuthorizer testSubject;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    final DroolsService droolsService = new DroolsService();
    final DroolsAuthorizationService droolsAuthorizationService = new DroolsAuthorizationService(droolsService);
    testSubject = new PlacementHomeCreateAuthorizer(droolsAuthorizationService);
  }

  @Test
  public void checkInstance_returnFalse_whenPerryPrivilegesAreNull() {
    final PerryAccount perryAccount = new PerryAccount();
    final boolean expectedResult = false;
    checkInstance_returnExpected_withPreparedPerryAccount(perryAccount, expectedResult);
  }

  @Test
  public void checkInstance_returnFalse_whenPerryPrivilegesAreEmpty() {
    final PerryAccount perryAccount = initPerryAccountWithPrivileges();
    final boolean expectedResult = false;
    checkInstance_returnExpected_withPreparedPerryAccount(perryAccount, expectedResult);
  }

  private void checkInstance_returnExpected_withPreparedPerryAccount(PerryAccount perryAccount,
      boolean expectedResult) {
    // given
    mockStatic(PerrySubject.class);
    when(PerrySubject.getPerryAccount()).thenReturn(perryAccount);

    // when
    final boolean actual = testSubject.checkInstance(new PlacementHome());

    // then
    assertThat(actual, is(expectedResult));
    verifyStatic(PerrySubject.class, times(1));
    PerrySubject.getPerryAccount();
  }

  @Test
  public void checkCWSCaseManagementUnauthorized() {
    // given
    final PerryAccount perryAccount = initPerryAccountWithPrivileges("CWS Case Management System");
    perryAccount.setCountyCwsCode("1068");
    perryAccount.setCountyCode("01");
    perryAccount.setCountyName("Alameda");
    mockStatic(PerrySubject.class);
    when(PerrySubject.getPerryAccount()).thenReturn(perryAccount);
    final PlacementHome placementHome= initPlacementHome();

    // when
    final boolean actual = testSubject.checkInstance(placementHome);

    // then
    assertThat(actual, is(false));
  }

  @Test
  public void checkResourceMgmtAuthorized() {
    // given
    final PerryAccount perryAccount = initPerryAccountWithPrivileges("Resource Mgmt Placement Facility Maint");
    perryAccount.setCountyCwsCode("1126");
    perryAccount.setCountyCode("99");
    perryAccount.setCountyName("State of California");
    mockStatic(PerrySubject.class);
    when(PerrySubject.getPerryAccount()).thenReturn(perryAccount);
    final PlacementHome placementHome= initPlacementHome();

    // when
    final boolean actual = testSubject.checkInstance(placementHome);

    // then
    assertThat(actual, is(true));
  }

  private PlacementHome initPlacementHome() {
    final PlacementHome placementHome = new PlacementHome();
    placementHome.setIdentifier("id");
    placementHome.setLicensrCd("CT");
    placementHome.setFacilityType(Short.valueOf("1416"));
    return placementHome;
  }

}