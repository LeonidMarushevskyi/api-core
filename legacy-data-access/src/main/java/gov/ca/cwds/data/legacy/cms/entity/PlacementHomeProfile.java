package gov.ca.cwds.data.legacy.cms.entity;

import gov.ca.cwds.data.persistence.PersistentObject;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author CWDS CALS API Team
 *
 * This delineates the specific ethnic groups, religions that a specific PLACEMENT HOME is
 * affiliated with, the specific language spoken within a specific PLACEMENT HOME, the special
 * services that a specific PLACEMENT HOME provide (e.g., drug counseling, etc.).  More than one
 * ethnicity, religion, language, special service may be listed for a PLACEMENT HOME.  This
 * information may be used when searching for a  PLACEMENT HOME for a child.
 */
@Entity
@Table(name = "HM_PRFT")
@IdClass(PlacementHomeProfilePK.class)
@SuppressWarnings({"squid:S3437"}) //LocalDate is serializable
public class PlacementHomeProfile implements PersistentObject {

  private static final long serialVersionUID = -8025606800212058436L;

  /**
   * THIRD_ID - This is a system generated unique number that supplements user supplied data in the
   * primary key. The Third ID attribute will be used as part of the new Primary Key in combination
   * with user supplied data. It will also be used alone as a separate key for direct access. This
   * Third ID is composed of a base 62 Creation Timestamp and the STAFF_PERSON ID (a sequential 3
   * digit base 62 number generated by the system).
   */
  @Id
  @Column(name = "THIRD_ID", nullable = false, length = 10)
  private String thirdId;

  /**
   * CHARACTERISTIC_TYPE - The system generated number which identifies the specific ethnicity
   * group(s) (Characteristic Code = 'E'), the religion group(s) (Characteristic Code = 'R')
   * affiliated with the PLACEMENT_HOME, the type of languages (Characteristic Code = 'L') spoken
   * within the PLACEMENT_HOME, or the particular areas of knowledge, expertise or children group
   * (Characteristic Code = 'P') catered by the PLACEMENT_HOME. For example: for the Ethnicity Type
   * 'ETHNCTYC', valid values are: Black, Asian, Hispanic, etc, for the Religion Type 'RLGN_TPC',
   * valid values are: Catholic, Christian, Hindu, Baptist, etc, for the Language Type 'LANG_TPC',
   * valid values are: English, Spanish, Chinese, etc, for the Population Served Type 'POP_SRVC',
   * valid values are: children under 6, speech pathology, Fire Setter, Bed Wetter, etc.
   */
  @Basic
  @Column(name = "CHRCTR_C", nullable = false)
  private short chrctrC;

  /**
   * CHARACTERISTIC_CODE - This code defines each type of characteristics for which a specific
   * PLACEMENT_HOME_PROFILE is defined (e.g. L = Language Type, R = Religion Type, E = Ethnicity
   * Type, P = Population Served Type).
   */
  @Basic
  @Column(name = "CHRCTR_CD", nullable = false, length = 1)
  private String chrctrCd;

  /**
   * LAST_UPDATE_ID - The ID (a sequential 3 digit base 62 number generated by the system) of the
   * STAFF PERSON or batch program which made the last update to an occurrence of this entity type.
   */
  @Basic
  @Column(name = "LST_UPD_ID", nullable = false, length = 3)
  private String lstUpdId;

  /**
   * LAST_UPDATE_TIMESTAMP - The date and time of the most recent update to an occurrence of this
   * entity type.
   */
  @Basic
  @Column(name = "LST_UPD_TS", nullable = false)
  private LocalDateTime lstUpdTs;

  /**
   * FKPLC_HM_T - Mandatory Foreign key that PROVIDES_HOME_CHARACTERISTIC_FOR a PLACEMENT_HOME.
   */
  @Id
  @Column(name = "FKPLC_HM_T", nullable = false, length = 10)
  private String fkplcHmT;

  public String getThirdId() {
    return thirdId;
  }

  public void setThirdId(String thirdId) {
    this.thirdId = thirdId;
  }

  public short getChrctrC() {
    return chrctrC;
  }

  public void setChrctrC(short chrctrC) {
    this.chrctrC = chrctrC;
  }

  public String getChrctrCd() {
    return chrctrCd;
  }

  public void setChrctrCd(String chrctrCd) {
    this.chrctrCd = chrctrCd;
  }

  public String getLstUpdId() {
    return lstUpdId;
  }

  public void setLstUpdId(String lstUpdId) {
    this.lstUpdId = lstUpdId;
  }

  public LocalDateTime getLstUpdTs() {
    return lstUpdTs;
  }

  public void setLstUpdTs(LocalDateTime lstUpdTs) {
    this.lstUpdTs = lstUpdTs;
  }

  public String getFkplcHmT() {
    return fkplcHmT;
  }

  public void setFkplcHmT(String fkplcHmT) {
    this.fkplcHmT = fkplcHmT;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(thirdId);
  }

  @Override
  public Serializable getPrimaryKey() {
    return new PlacementHomeProfilePK(thirdId, fkplcHmT);
  }
}
