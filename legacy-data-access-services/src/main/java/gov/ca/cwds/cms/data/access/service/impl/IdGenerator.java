package gov.ca.cwds.cms.data.access.service.impl;

import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.security.utils.PrincipalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS CALS API Team
 */
class IdGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);

  private static final Object monitor = new Object();
  private static String lastId;

  private IdGenerator() {
  }

  public static String generateId() {
    synchronized (monitor) {
      String generated = null;
      do {
        generated = CmsKeyIdGenerator.generate(PrincipalUtils.getStaffPersonId());
        try {
          monitor.wait(10L);
        } catch (InterruptedException e) {
          LOG.warn("Interrupted: " + e.getMessage(), e);
          Thread.currentThread().interrupt();
        }
      } while (lastId != null && lastId.equals(generated));

      lastId = generated;
    }
    return lastId;
  }

}

