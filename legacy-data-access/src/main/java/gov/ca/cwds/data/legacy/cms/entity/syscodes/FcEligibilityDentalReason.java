package gov.ca.cwds.data.legacy.cms.entity.syscodes;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.annotations.NamedQuery;

/** @author CWDS TPT-3 Team */
@Entity
@Cacheable
@DiscriminatorValue(value = "ELG_DNLC")
@NamedQuery(name = FacilityType.NQ_ALL, query = "FROM FcEligibilityDentalReason")
public class FcEligibilityDentalReason extends SystemCodeTable {

  public static final String NQ_ALL = "FcEligibilityDentalReason.all";

  private static final long serialVersionUID = 1L;
}
