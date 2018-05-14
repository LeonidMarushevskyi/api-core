package gov.ca.cwds.cms.data.access.service.impl.clientrelationship;

import gov.ca.cwds.cms.data.access.dto.ClientRelationshipAwareDTO;
import gov.ca.cwds.cms.data.access.service.BusinessValidationService;
import gov.ca.cwds.cms.data.access.service.DataAccessServicesException;
import gov.ca.cwds.cms.data.access.service.impl.dbDependentSuite.BaseCwsCmsInMemoryPersistenceTest;
import gov.ca.cwds.data.legacy.cms.dao.ClientDao;
import gov.ca.cwds.data.legacy.cms.dao.ClientRelationshipDao;
import gov.ca.cwds.data.legacy.cms.dao.PaternityDetailDao;
import gov.ca.cwds.data.legacy.cms.dao.TribalMembershipVerificationDao;
import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.data.legacy.cms.entity.ClientRelationship;
import gov.ca.cwds.data.legacy.cms.entity.TribalMembershipVerification;
import gov.ca.cwds.data.legacy.cms.entity.enums.YesNoUnknown;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.ClientRelationshipType;
import gov.ca.cwds.drools.DroolsService;
import gov.ca.cwds.security.realm.PerryAccount;
import gov.ca.cwds.security.utils.PrincipalUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/** @author CWDS TPT-3 Team */
public class R08840DBTest extends BaseCwsCmsInMemoryPersistenceTest {

  private ClientDao clientDao;
  private TribalMembershipVerificationDao tribalMembershipVerificationDao;
  private ClientRelationshipCoreService clientRelationshipCoreService;
  private ClientRelationshipDao clientRelationshipDao;
  private BusinessValidationService businessValidationService;
  private PaternityDetailDao paternityDetailDao;
  private UpdateLifeCycle updateLifeCycle;
  private CreateLifeCycle createLifeCycle;
  private SearchClientRelationshipService searchClientRelationshipService;

  private static final String USER_ID = "0X5";

  @Before
  public void before() {
    businessValidationService = new BusinessValidationService(new DroolsService());
    clientDao = new ClientDao(sessionFactory);
    tribalMembershipVerificationDao = new TribalMembershipVerificationDao(sessionFactory);
    clientRelationshipDao = new ClientRelationshipDao(sessionFactory);
    paternityDetailDao = new PaternityDetailDao(sessionFactory);
    searchClientRelationshipService = new SearchClientRelationshipService(clientRelationshipDao);
    updateLifeCycle =
        new UpdateLifeCycle(
            clientRelationshipDao,
            businessValidationService,
            clientDao,
            tribalMembershipVerificationDao,
            paternityDetailDao,
            searchClientRelationshipService);
    createLifeCycle =
        new CreateLifeCycle(
            clientRelationshipDao,
            businessValidationService,
            clientDao,
            tribalMembershipVerificationDao,
            paternityDetailDao,
            searchClientRelationshipService);
    clientRelationshipCoreService =
        new ClientRelationshipCoreService(
            clientRelationshipDao,
            updateLifeCycle,
            searchClientRelationshipService,
            createLifeCycle);
  }

  @Test
  public void testPrimaryTribalAdded() throws Exception {
    cleanAllAndInsert("/dbunit/R08840_1.xml");

    final List<TribalMembershipVerification> primaryTribals = new ArrayList<>();
    final List<TribalMembershipVerification> secondaryTribals = new ArrayList<>();
    final ClientRelationshipAwareDTO awareDTO = new ClientRelationshipAwareDTO();

    executeInTransaction(
        sessionFactory,
        (sessionFactory) -> {
          primaryTribals.addAll(tribalMembershipVerificationDao.findByClientId("RM1Mq5GABC"));
        });
    executeInTransaction(
        sessionFactory,
        (sessionFactory) -> {
          secondaryTribals.addAll(tribalMembershipVerificationDao.findByClientId("HkKiO2wABC"));
        });

    assertEquals(2, primaryTribals.size());
    assertEquals(2, secondaryTribals.size());

    createRelationship(awareDTO, "RM1Mq5GABC", "HkKiO2wABC");

    List<TribalMembershipVerification> primaryTribalsAfterRUle = new ArrayList<>();
    List<TribalMembershipVerification> secondaryTribalsAfterRUle = new ArrayList<>();

    persistRelationship(
        awareDTO, primaryTribalsAfterRUle, secondaryTribalsAfterRUle, "RM1Mq5GABC", "HkKiO2wABC");

    assertEquals(4, primaryTribalsAfterRUle.size());
    assertEquals(2, secondaryTribals.size());
  }

  @Test
  public void testPrimaryTribalNotAdded() throws Exception {
    cleanAllAndInsert("/dbunit/R08840_2.xml");
    initUserAccount(USER_ID);

    final List<TribalMembershipVerification> primaryTribals = new ArrayList<>();
    final List<TribalMembershipVerification> secondaryTribals = new ArrayList<>();
    final ClientRelationshipAwareDTO awareDTO = new ClientRelationshipAwareDTO();

    executeInTransaction(
        sessionFactory,
        (sessionFactory) -> {
          primaryTribals.addAll(tribalMembershipVerificationDao.findByClientId("RM1Mq5GAB1"));
        });
    executeInTransaction(
        sessionFactory,
        (sessionFactory) -> {
          secondaryTribals.addAll(tribalMembershipVerificationDao.findByClientId("HkKiO2wAB1"));
        });

    assertEquals(2, primaryTribals.size());
    assertEquals(2, secondaryTribals.size());

    createRelationship(awareDTO, "RM1Mq5GAB1", "HkKiO2wAB1");

    List<TribalMembershipVerification> primaryTribalsAfterRule = new ArrayList<>();
    List<TribalMembershipVerification> secondaryTribalsAfterRule = new ArrayList<>();

    persistRelationship(
        awareDTO, primaryTribalsAfterRule, secondaryTribalsAfterRule, "RM1Mq5GAB1", "HkKiO2wAB1");

    assertEquals(2, primaryTribalsAfterRule.size());
    assertEquals(2, secondaryTribals.size());
  }

  private void persistRelationship(
      ClientRelationshipAwareDTO awareDTO,
      List<TribalMembershipVerification> primaryTribalsAfterRule,
      List<TribalMembershipVerification> secondaryTribalsAfterRule,
      String primaryClientId,
      String secondaryClientId) {
    executeInTransaction(
        sessionFactory,
        (sessionFactory) -> {
          try {
            clientRelationshipCoreService.update(awareDTO);
            primaryTribalsAfterRule.addAll(
                tribalMembershipVerificationDao.findByClientId(primaryClientId));

            secondaryTribalsAfterRule.addAll(
                tribalMembershipVerificationDao.findByClientId(secondaryClientId));
          } catch (DataAccessServicesException e) {
            e.printStackTrace();
          }
        });
  }

  private void createRelationship(
      ClientRelationshipAwareDTO awareDTO, String primaryClient, String secondaryClient) {
    executeInTransaction(
        sessionFactory,
        (sessionFactory1) -> {
          Client child = clientDao.find(primaryClient);
          Client secondary = clientDao.find(secondaryClient);

          ClientRelationship relationship = new ClientRelationship();
          relationship.setIdentifier("0011001100");
          relationship.setPrimaryClient(child);
          relationship.setSecondaryClient(secondary);
          relationship.setStartDate(LocalDate.of(2000, 1, 1));
          relationship.setEndDate(LocalDate.of(2019, 1, 1));

          ClientRelationshipType type = new ClientRelationshipType();
          type.setSystemId((short) 190);
          relationship.setType(type);
          relationship.setLastUpdateId("0X5");
          relationship.setLastUpdateTime(LocalDateTime.now());
          relationship.setSameHomeStatus(YesNoUnknown.YES);

          awareDTO.setEntity(relationship);
          try {
            clientRelationshipCoreService.create(awareDTO);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void initUserAccount(String userAccount) {
    PerryAccount perryAccount = new PerryAccount();
    mockStatic(PrincipalUtils.class);
    when(PrincipalUtils.getPrincipal()).thenReturn(perryAccount);
    when(PrincipalUtils.getStaffPersonId()).thenReturn(userAccount);
  }
}
