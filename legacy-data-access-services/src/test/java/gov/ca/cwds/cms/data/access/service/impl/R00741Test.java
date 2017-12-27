package gov.ca.cwds.cms.data.access.service.impl;

import gov.ca.cwds.data.legacy.cms.entity.Client;
import org.junit.Test;

public class R00741Test extends BaseDocToolRulesClientTest {

  private static final String RULE_NAME = "R-R00741";

  @Test
  public void testLanguagesAreDifferent() throws Exception {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 1);
    client.setSecondaryLanguageCode((short) 2);

    checkRuleSatisfied(client, RULE_NAME);
  }

  @Test
  public void testOneLanguagesIsUnknown() throws Exception {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 1);
    client.setSecondaryLanguageCode((short) 0);

    checkRuleSatisfied(client, RULE_NAME);
  }

  @Test
  public void testLanguagesAreTheSame() throws Exception {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 1);
    client.setSecondaryLanguageCode((short) 1);

    checkRuleViolatedOnce(client, RULE_NAME);
  }

  @Test
  public void testLanguagesAreUnknown() throws Exception {
    Client client = new Client();
    client.setPrimaryLanguageCode((short) 0);
    client.setSecondaryLanguageCode((short) 0);

    checkRuleSatisfied(client, RULE_NAME);
  }
}
