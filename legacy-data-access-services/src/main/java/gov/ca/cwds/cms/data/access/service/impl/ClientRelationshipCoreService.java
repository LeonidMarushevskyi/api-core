package gov.ca.cwds.cms.data.access.service.impl;

import static gov.ca.cwds.cms.data.access.Constants.Authorize.CLIENT_READ_CLIENT_ID;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

import gov.ca.cwds.cms.data.access.dto.ClientRelationshipAwareDTO;
import gov.ca.cwds.cms.data.access.service.BusinessValidationService;
import gov.ca.cwds.cms.data.access.service.DataAccessServiceBase;
import gov.ca.cwds.cms.data.access.service.DataAccessServicesException;
import gov.ca.cwds.cms.data.access.service.lifecycle.DataAccessBundle;
import gov.ca.cwds.cms.data.access.service.lifecycle.DataAccessServiceLifecycle;
import gov.ca.cwds.cms.data.access.service.lifecycle.DefaultDataAccessLifeCycle;
import gov.ca.cwds.cms.data.access.service.rules.ClientRelationshipDroolsConfiguration;
import gov.ca.cwds.cms.data.access.utils.ParametersValidator;
import gov.ca.cwds.data.legacy.cms.dao.ClientDao;
import gov.ca.cwds.data.legacy.cms.dao.ClientRelationshipDao;
import gov.ca.cwds.data.legacy.cms.dao.TribalMembershipVerificationDao;
import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.data.legacy.cms.entity.ClientRelationship;
import gov.ca.cwds.data.legacy.cms.entity.TribalMembershipVerification;
import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.drools.DroolsException;
import gov.ca.cwds.security.annotations.Authorize;
import gov.ca.cwds.security.realm.PerryAccount;
import gov.ca.cwds.security.utils.PrincipalUtils;
import java.time.Month;
import org.apache.commons.lang3.StringUtils;

/**
 * Service for create/update/find ClientRelationship with business validation and data processing.
 *
 * @author CWDS TPT-3 Team
 */
public class ClientRelationshipCoreService
    extends DataAccessServiceBase<
        ClientRelationshipDao, ClientRelationship, ClientRelationshipAwareDTO> {

  private final BusinessValidationService businessValidationService;
  private final ClientDao clientDao;
  private final TribalMembershipVerificationDao tribalMembershipVerificationDao;

  /**
   * Constructor with injected services.
   *
   * @param clientRelationshipDao client relationship DAO
   * @param businessValidationService business validator
   * @param clientDao client DAO
   */
  @Inject
  public ClientRelationshipCoreService(
      final ClientRelationshipDao clientRelationshipDao,
      BusinessValidationService businessValidationService,
      ClientDao clientDao,
      TribalMembershipVerificationDao tribalMembershipVerificationDao) {
    super(clientRelationshipDao);
    this.businessValidationService = businessValidationService;
    this.clientDao = clientDao;
    this.tribalMembershipVerificationDao = tribalMembershipVerificationDao;
  }

  @Override
  public ClientRelationship create(ClientRelationshipAwareDTO entityAwareDto)
      throws DataAccessServicesException {
    entityAwareDto.getEntity().setLastUpdateTime(LocalDateTime.now());
    entityAwareDto.getEntity().setLastUpdateId(PrincipalUtils.getStaffPersonId());
    entityAwareDto
        .getEntity()
        .setIdentifier(CmsKeyIdGenerator.getNextValue(PrincipalUtils.getStaffPersonId()));
    return super.create(entityAwareDto);
  }

  @Override
  public ClientRelationship update(ClientRelationshipAwareDTO entityAwareDto)
      throws DataAccessServicesException, DroolsException {
    entityAwareDto.getEntity().setLastUpdateTime(LocalDateTime.now());
    entityAwareDto.getEntity().setLastUpdateId(PrincipalUtils.getStaffPersonId());
    return super.update(entityAwareDto);
  }

  @Override
  protected DataAccessServiceLifecycle<ClientRelationshipAwareDTO> getUpdateLifeCycle() {
    return new UpdateLificycle();
  }

  @Override
  protected DataAccessServiceLifecycle<ClientRelationshipAwareDTO> getCreateLifeCycle() {
    return new DefaultDataAccessLifeCycle<>();
  }

  @Override
  protected DataAccessServiceLifecycle<ClientRelationshipAwareDTO> getDeleteLifeCycle() {
    return new DefaultDataAccessLifeCycle<>();
  }

  public List<ClientRelationship> findRelationshipsByLeftSide(
      @Authorize(CLIENT_READ_CLIENT_ID) final String clientId) {
    return crudDao.findRelationshipsBySecondaryClientId(clientId, LocalDate.now());
  }

  public List<ClientRelationship> findRelationshipsByRightSide(
      @Authorize(CLIENT_READ_CLIENT_ID) final String clientId) {
    return crudDao.findRelationshipsByPrimaryClientId(clientId, LocalDate.now());
  }

  private class UpdateLificycle extends DefaultDataAccessLifeCycle {

    @Override
    public void beforeDataProcessing(DataAccessBundle bundle) {
      super.beforeDataProcessing(bundle);
      enrichWithPrimaryAndSecondaryClients(bundle);
      validateAndAddIfNeededTribalMembershipVerification(bundle);
    }

    @Override
    public void dataProcessing(DataAccessBundle bundle, PerryAccount perryAccount)
        throws DroolsException {
      super.dataProcessing(bundle, perryAccount);
      businessValidationService.runBusinessValidation(
          bundle.getAwareDto(),
          PrincipalUtils.getPrincipal(),
          ClientRelationshipDroolsConfiguration.DATA_PROCESSING_INSTANCE);
    }

    @Override
    public void beforeBusinessValidation(DataAccessBundle bundle) {

      ClientRelationshipAwareDTO awareDTO = (ClientRelationshipAwareDTO) bundle.getAwareDto();
      Client client = awareDTO.getEntity().getPrimaryClient();
      String clientId = client.getIdentifier();

      List<ClientRelationship> otherRelationshipsForThisClient =
          new ArrayList<>(findRelationshipsByRightSide(clientId));
      otherRelationshipsForThisClient.addAll(findRelationshipsByRightSide(clientId));

      otherRelationshipsForThisClient.removeIf(
          e -> e.getIdentifier().equals(awareDTO.getEntity().getIdentifier()));

      awareDTO.getClientRelationshipList().addAll(otherRelationshipsForThisClient);
    }

    @Override
    public void businessValidation(DataAccessBundle bundle, PerryAccount perryAccount)
        throws DroolsException {
      businessValidationService.runBusinessValidation(
          bundle.getAwareDto(),
          PrincipalUtils.getPrincipal(),
          ClientRelationshipDroolsConfiguration.INSTANCE);
    }

    private void enrichWithPrimaryAndSecondaryClients(DataAccessBundle bundle) {
      ClientRelationshipAwareDTO awareDTO = (ClientRelationshipAwareDTO) bundle.getAwareDto();
      ParametersValidator.checkEntityId(
          awareDTO.getEntity().getPrimaryClient(), awareDTO.getEntity().getClass().getName());
      Client primaryClient =
          clientDao.find(awareDTO.getEntity().getPrimaryClient().getPrimaryKey());
      ParametersValidator.checkEntityId(
          awareDTO.getEntity().getSecondaryClient(), awareDTO.getEntity().getClass().getName());
      Client secondaryClient =
          clientDao.find(awareDTO.getEntity().getSecondaryClient().getPrimaryKey());
      awareDTO.getEntity().setPrimaryClient(primaryClient);
      awareDTO.getEntity().setSecondaryClient(secondaryClient);
    }

    /**
     * Rule-08840 When a relationship of Birth Mother, Alleged Mother, Presumed Mother, Birth
     * Father, Alleged Father or Presumed Father is selected and the parent has Tribal Membership
     * Verification data, create a duplicate Tribal Membership Verification row for all of their
     * children.If the child already has a Tribal Membership row that was created prior to R5.5 with
     * the same Tribal Affiliation/Tribe combination as the new row being added from the parent,
     * then overwrite the child's existing Tribal Membership row with the new Tribal Membership row
     * created by the parent and display error message. Keep the Membership Status, Status Date, and
     * Enrollment Number from the child's existing row if they exist.
     */
    private void validateAndAddIfNeededTribalMembershipVerification(DataAccessBundle bundle) {
      ClientRelationshipAwareDTO awareDTO = (ClientRelationshipAwareDTO) bundle.getAwareDto();
      if (!awareDTO.isNeedMembershipVerification()) {
        return;
      }

      ParametersValidator.checkEntityId(
          awareDTO.getEntity().getPrimaryClient(),
          awareDTO.getEntity().getPrimaryClient().getClass().getName());
      Client primaryClient =
          clientDao.find(awareDTO.getEntity().getPrimaryClient().getIdentifier());
      ParametersValidator.checkEntityId(
          awareDTO.getEntity().getSecondaryClient(),
          awareDTO.getEntity().getSecondaryClient().getClass().getName());
      Client secondaryClient =
          clientDao.find(awareDTO.getEntity().getSecondaryClient().getIdentifier());

      List<TribalMembershipVerification> parentTribals =
          tribalMembershipVerificationDao.findParensByClientId(secondaryClient.getIdentifier());

      List<TribalMembershipVerification> parentExtraTribals =
          getExtraRowsForPrimaryClient(parentTribals);

      List<TribalMembershipVerification> childTribals =
          tribalMembershipVerificationDao.findParensByClientId(primaryClient.getIdentifier());

      parentExtraTribals.forEach(
          e -> {
            TribalMembershipVerification newlyAdded = tribalMembershipVerificationDao.create(e);
            changedRowsForChild(newlyAdded, childTribals);
          });
    }

    private void changedRowsForChild(
        TribalMembershipVerification newlyAdded, List<TribalMembershipVerification> childTribals) {
      if (newlyAdded == null || childTribals == null || childTribals.isEmpty()) {
        return;
      }
      childTribals.forEach(
          e -> {
            if (needToUpdateRow(e, newlyAdded)) {
              e.setStatusDate(newlyAdded.getStatusDate());
              e.setEnrollmentNumber(newlyAdded.getEnrollmentNumber());
              tribalMembershipVerificationDao.update(e);
            }
          });
    }

    private boolean needToUpdateRow(
        TribalMembershipVerification tribalForUpdate,
        TribalMembershipVerification newlyAddedTribal) {
      Date dateFromThirdId = CmsKeyIdGenerator.getDateFromKey(tribalForUpdate.getThirdId());
      if (dateFromThirdId == null) {
        return false;
      }

      LocalDate date = dateFromThirdId.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      if (date.isBefore(LocalDate.of(2005, Month.NOVEMBER, 19))) {
        if (tribalForUpdate.getIndianTribeType() == newlyAddedTribal.getIndianTribeType()
            && tribalForUpdate.getFkFromTribalMembershipVerification()
                == newlyAddedTribal.getFkFromTribalMembershipVerification()) return true;
      }

      return false;
    }

    private List<TribalMembershipVerification> getExtraRowsForPrimaryClient(
        List<TribalMembershipVerification> primaryTribals) {
      if (primaryTribals == null || primaryTribals.isEmpty()) {
        return new ArrayList<>();
      }

      final List<TribalMembershipVerification> extraRows = new ArrayList<>();
      primaryTribals.forEach(
          a -> {
            if (StringUtils.isEmpty(a.getFkFromTribalMembershipVerification())) {
              TribalMembershipVerification extra = new TribalMembershipVerification();
              extra.setFkFromTribalMembershipVerification(a.getThirdId());
              extra.setFkSentTotribalOrganization(a.getFkSentTotribalOrganization());
              extra.setIndianEnrollmentStatus(a.getIndianEnrollmentStatus());
              extra.setIndianTribeType(a.getIndianTribeType());
              extra.setThirdId(IdGenerator.generateId());
              extra.setClientId(a.getClientId());
              extra.setLastUpdateId(PrincipalUtils.getStaffPersonId());
              extra.setLastUpdateTime(LocalDateTime.now());
              extraRows.add(extra);
            }
          });

      return extraRows;
    }
  }
}
