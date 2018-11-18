package gov.ca.cwds.data.es.transform;

import java.io.Serializable;
import java.util.List;

import gov.ca.cwds.data.es.ElasticSearchSystemCode;

/**
 * @author CWDS API Team
 */
@FunctionalInterface
public interface ApiClientCountyAware extends Serializable {

  /**
   * Get client counties
   * 
   * @return The client counties
   */
  List<ElasticSearchSystemCode> getClientCounties();
}
