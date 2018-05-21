package gov.ca.cwds.cms.data.access.service.impl.clientrelationship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gov.ca.cwds.cms.data.access.service.rules.ClientRelationshipDroolsConfiguration;
import gov.ca.cwds.data.legacy.cms.entity.ClientRelationship;
import gov.ca.cwds.data.legacy.cms.entity.TribalMembershipVerification;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.ClientRelationshipType;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.IndianEnrolmentStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.junit.Test;

/** @author CWDS TPT-3 Team */
public class R08861Test extends BaseDocToolRulesChildClientRelationshipTest {

  private static final String RULE_NAME = "R-08861-2";

  private static final String TRIBAL_THIRD_ID_1 = "1111111111";
  private static final String TRIBAL_THIRD_ID_2 = "2222222222";
  private static final String TRIBAL_THIRD_ID_3 = "3333333333";
  private static final String TRIBAL_THIRD_ID_4 = "4444444444";
  private static final Short RELATION_SHIP_TYPE = 444;
  private static final Short RELATION_SHIP_TYPE_2 = 555;
  private static final Short RELATION_SHIP_TYPE_3 = 190;

  @Test
  public void testAwareDtoHas3TribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_1));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_3));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(3, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testAwareDtoHas1TribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_1));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_3));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(1, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testAwareDtoHas4TribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_1));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_3));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(4, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testAwareDtoHasNoTribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_1));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_3));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(0, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testAwareDtoHasEmptyTribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(0, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testAwareDtoHasDifferentTypeTribalsToDelete() {

    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_1));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(null, TRIBAL_THIRD_ID_3));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE_2));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    assertNotNull(awareDTO);
    assertNotNull(awareDTO.getTribalMembershipVerificationsForDelete());
    assertEquals(2, awareDTO.getTribalMembershipVerificationsForDelete().size());
  }

  @Test
  public void testEnrolmentStatusNotNullAndNoNeedVerificationIsFalse() {
    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE_2));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    checkRuleViolatedOnce(RULE_NAME);
  }

  @Test
  public void testEnrolmentStatusNotNullAndNeedVerificationIsTrue() {
    List<TribalMembershipVerification> tribalMembershipVerifications = new ArrayList<>();
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_2));
    tribalMembershipVerifications.add(
        tribalMembershipVerificationFunction.apply(new IndianEnrolmentStatus(), TRIBAL_THIRD_ID_4));

    awareDTO.getTribalsThatHaveSubTribals().addAll(tribalMembershipVerifications);
    awareDTO.setEntity(clientRelationshipFunction.apply(RELATION_SHIP_TYPE_3));

    businessValidationService.runDataProcessing(
        awareDTO, principal, ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);

    checkRuleSatisfied(RULE_NAME);
  }

  private BiFunction<IndianEnrolmentStatus, String, TribalMembershipVerification>
      tribalMembershipVerificationFunction =
          (status, thirdId) -> {
            TribalMembershipVerification membershipVerification =
                new TribalMembershipVerification();
            membershipVerification.setIndianEnrollmentStatus(status);
            membershipVerification.setThirdId(thirdId);
            return membershipVerification;
          };

  private Function<Short, ClientRelationship> clientRelationshipFunction =
      (relationshipType) -> {
        ClientRelationship clientRelationship = new ClientRelationship();
        ClientRelationshipType type = new ClientRelationshipType();
        type.setSystemId(relationshipType);
        clientRelationship.setType(type);
        return clientRelationship;
      };
}
