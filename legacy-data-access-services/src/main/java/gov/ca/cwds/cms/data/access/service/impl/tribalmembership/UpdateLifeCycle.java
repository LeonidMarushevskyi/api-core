package gov.ca.cwds.cms.data.access.service.impl.tribalmembership;

import com.google.inject.Inject;
import gov.ca.cwds.cms.data.access.dto.TribalMembershipVerificationAwareDto;
import gov.ca.cwds.cms.data.access.service.BusinessValidationService;
import gov.ca.cwds.cms.data.access.service.DataAccessServicesException;
import gov.ca.cwds.cms.data.access.service.impl.ClientCoreService;
import gov.ca.cwds.cms.data.access.service.impl.IdGenerator;
import gov.ca.cwds.cms.data.access.service.lifecycle.DataAccessBundle;
import gov.ca.cwds.data.legacy.cms.dao.TribalMembershipVerificationDao;
import gov.ca.cwds.data.legacy.cms.entity.TribalMembershipVerification;
import gov.ca.cwds.security.utils.PrincipalUtils;
import java.time.LocalDateTime;

/**
 * @author CWDS TPT-3 Team
 * */
public class UpdateLifeCycle extends CreateUpdateLifeCycle {

  @Inject
  public UpdateLifeCycle(
      TribalMembershipVerificationDao tribalMembershipVerificationDao,
      ClientCoreService clientCoreService,
      BusinessValidationService<TribalMembershipVerification, TribalMembershipVerificationAwareDto>
          businessValidationService) {
    super(tribalMembershipVerificationDao, clientCoreService, businessValidationService);
  }

  @Override
  public void beforeDataProcessing(DataAccessBundle bundle) throws DataAccessServicesException {
    super.beforeDataProcessing(bundle);
    createChildTribalForDuplicate(bundle);
  }

  private void createChildTribalForDuplicate(DataAccessBundle bundle) {
    TribalMembershipVerificationAwareDto awareDto =
      (TribalMembershipVerificationAwareDto) bundle.getAwareDto();

    TribalMembershipVerification childTribal = new TribalMembershipVerification();
    childTribal.setFkFromTribalMembershipVerification(awareDto.getEntity().getThirdId());
    childTribal.setFkSentToTribalOrganization(awareDto.getEntity().getFkSentToTribalOrganization());
    childTribal.setIndianEnrollmentStatus(awareDto.getEntity().getIndianEnrollmentStatus());
    childTribal.setIndianTribeType(awareDto.getEntity().getIndianTribeType());
    childTribal.setStatusDate(awareDto.getEntity().getStatusDate());
    childTribal.setThirdId(IdGenerator.generateId());
    childTribal.setClientId(awareDto.getChildId());
    childTribal.setLastUpdateTime(LocalDateTime.now());
    childTribal.setLastUpdateId(PrincipalUtils.getStaffPersonId());

    awareDto.setChildTribalForDuplicate(childTribal);
  }
}
