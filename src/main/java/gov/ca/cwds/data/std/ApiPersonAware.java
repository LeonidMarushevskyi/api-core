package gov.ca.cwds.data.std;

import java.util.Date;

import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * Interface defines naming standard methods for persistence classes that represent persons. Allows
 * DAO and service classes to operate on person-aware objects efficiently, without possessing
 * knowledge of internal implementation details.
 * 
 * @author CWDS API Team
 */
public interface ApiPersonAware extends PersistentObject {

  /**
   * Getter for first name.
   * 
   * @return first name
   */
  String getFirstName();

  /**
   * Getter for middle name.
   * 
   * @return middle name
   */
  String getMiddleName();

  /**
   * Getter for last name.
   * 
   * @return last name
   */
  String getLastName();

  /**
   * Getter for gender.
   * 
   * @return gender
   */
  String getGender();

  /**
   * Getter for birth date.
   * 
   * @return birth date
   */
  Date getBirthDate();

  /**
   * Getter for SSN.
   * 
   * @return SSN
   */
  String getSsn();

  /**
   * Getter for name suffix.
   * 
   * @return name suffix
   */
  String getNameSuffix();

  /**
   * @return the sensitivityIndicator
   */
  String getSensitivityIndicator();

  /**
   * @return the soc158SealedClientIndicator
   */
  String getSoc158SealedClientIndicator();
}
