package gov.ca.cwds.cms.data.access.service.impl;

import gov.ca.cwds.data.legacy.cms.entity.SubstituteCareProvider;
import org.junit.Test;

/**
 * Created by TPT2 on 12/21/2017.
 */
public class R03233Test extends BaseDocToolRulesTest {
  
  //zip no
  @Test
  public void testZipNoSuffixShort() {
    placementHomeEntityAwareDTO.getEntity().setZipSfxNo("1");
    check("R-03233");
  }

  @Test
  public void testZipNoSuffixLong() {
    placementHomeEntityAwareDTO.getEntity().setZipSfxNo("11111111");
    check("R-03233");
  }

  @Test
  public void testZipNoSuffixFormat() {
    placementHomeEntityAwareDTO.getEntity().setZipSfxNo("aa");
    check("R-03233");
  }

  
  //P zip

  @Test
  public void testPyZipSfxShort() {
    placementHomeEntityAwareDTO.getEntity().setPyZipSfx("1");
    check("R-03233");
  }

  @Test
  public void testPyZipSfxLong() {
    placementHomeEntityAwareDTO.getEntity().setPyZipSfx("11111111");
    check("R-03233");
  }

  @Test
  public void testPyZipSfxFormat() {
    placementHomeEntityAwareDTO.getEntity().setPyZipSfx("aa");
    check("R-03233");
  }

  

  //La P zip

  @Test
  public void testLaPZpsfxShort() {
    placementHomeEntityAwareDTO.getEntity().setLaPZpsfx("1");
    check("R-03233");
  }

  @Test
  public void testLaPZpsfxLong() {
    placementHomeEntityAwareDTO.getEntity().setLaPZpsfx("11111111");
    check("R-03233");
  }

  @Test
  public void testLaPZpsfxFormat() {
    placementHomeEntityAwareDTO.getEntity().setLaPZpsfx("aa");
    check("R-03233");
  }

  
  //SCP zip no

  @Test
  public void testZipSfxNoShort() {
    placementHomeEntityAwareDTO.getEntity().getPrimarySubstituteCareProvider().setZipSfxNo("1");
    check("R-03233");
  }

  @Test
  public void testZipSfxNoLong() {
    placementHomeEntityAwareDTO.getEntity().getPrimarySubstituteCareProvider().setZipSfxNo("11111111");
    check("R-03233");
  }

  @Test
  public void testZipSfxNoFormat() {
    placementHomeEntityAwareDTO.getEntity().getPrimarySubstituteCareProvider().setZipSfxNo("aaa");
    check("R-03233");
  }
  
}
