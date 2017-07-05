package gov.ca.cwds.rest.validation;

import java.util.Set;

import gov.ca.cwds.rest.api.domain.cms.SystemCode;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeCache;
import gov.ca.cwds.rest.api.domain.cms.SystemMeta;

@SuppressWarnings("serial")
class TestSystemCodeCache implements SystemCodeCache {

  TestSystemCodeCache() {
    register();
  }

  @Override
  public Set<SystemMeta> getAllSystemMetas() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<SystemCode> getAllSystemCodes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SystemCode getSystemCode(Number systemCodeId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<SystemCode> getSystemCodesForMeta(String metaId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSystemCodeShortDescription(Number systemCodeId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean verifyActiveSystemCodeIdForMeta(Number systemCodeId, String metaId) {
    if (456 == systemCodeId.intValue()) {
      return false;
    } else if (6404 == systemCodeId.intValue()) {
      return false;
    } else if (19 == systemCodeId.intValue()) {
      return true;
    }

    return true;
  }

  @Override
  public boolean verifyActiveSystemCodeDescriptionForMeta(String shortDesc, String metaId) {
    System.out
        .println("TestSystemCodeCache.verifyActiveSystemCodeDescriptionForMeta -> shortDesc: ["
            + shortDesc + "]");
    if ("djdjskshahfdsa".equals(shortDesc)) {
      return false;
    } else if ("".equals(shortDesc)) {
      return false;
    } else if ("Breasts".equals(shortDesc)) {
      return true;
    }
    return true;
  }
}
