package gov.ca.cwds.authorizer;

/**
 * Security conditions for client access.
 * 
 * @author CWDS TPT-3 Team
 */
public enum ClientCondition {
  NO_CONDITIONS,

  SAME_COUNTY_SENSITIVE,

  SAME_COUNTY_SEALED,

  DIFFERENT_COUNTY_SENSITIVE,

  DIFFERENT_COUNTY_SEALED,

  NO_COUNTY_SENSITIVE,

  NO_COUNTY_SEALED
}
