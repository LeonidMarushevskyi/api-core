package gov.ca.cwds.cms.data.access.service.impl.client;

import gov.ca.cwds.cms.data.access.service.impl.client.BaseDocToolRulesClientImplementationTest;
import gov.ca.cwds.data.legacy.cms.entity.ChildClient;
import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.drools.DroolsException;
import org.junit.Test;

/** @author CWDS TPT-3 Team */
public class R00743Test extends BaseDocToolRulesClientImplementationTest {

  private static final String RULE_NAME = "R-00743";

  @Test
  public void testPrimaryAnsSecondaryLanguagesNull() throws DroolsException {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 1);
    client.setSecondaryLanguageCode((short) 12);

    clientEntityAwareDTO.setEntity(client);

    checkRuleSatisfied(RULE_NAME);
  }

  @Test
  public void testPrimaryLanguageEmpty() throws DroolsException {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 0);
    client.setSecondaryLanguageCode((short) 12);

    clientEntityAwareDTO.setEntity(client);

    checkRuleViolatedOnce(RULE_NAME);
  }

  @Test
  public void testPrimarySecondaryLanguagesEmpty() throws DroolsException {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 0);
    client.setSecondaryLanguageCode((short) 0);

    clientEntityAwareDTO.setEntity(client);

    checkRuleSatisfied(RULE_NAME);
  }

  @Test
  public void testSecondaryLanguagesEmpty() throws DroolsException {
    ChildClient client = new ChildClient();
    client.setPrimaryLanguageCode((short) 12);
    client.setSecondaryLanguageCode((short) 0);

    checkRuleSatisfied(client, RULE_NAME);
  }
}
