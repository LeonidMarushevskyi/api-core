package gov.ca.cwds.data.persistence.cms;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gov.ca.cwds.data.CmsSystemCodeDeserializer;
import gov.ca.cwds.data.ReadablePhone;
import gov.ca.cwds.data.SystemCodeSerializer;
import gov.ca.cwds.data.std.ApiAddressAware;
import gov.ca.cwds.data.std.ApiMultiplePhonesAware;
import gov.ca.cwds.data.std.ApiPersonAware;
import gov.ca.cwds.data.std.ApiPhoneAware;

/**
 * @author CWDS API Team
 */
@MappedSuperclass
public abstract class BaseReporter extends CmsPersistentObject
    implements ApiPersonAware, ApiAddressAware, ApiMultiplePhonesAware {

  private static final long serialVersionUID = 1L;

  @Id
  @NotNull
  @Column(name = "FKREFERL_T", length = CMS_ID_LEN)
  protected String referralId;

  @Column(name = "RPTR_BDGNO")
  protected String badgeNumber;

  @Column(name = "RPTR_CTYNM")
  protected String cityName;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "COL_RELC")
  protected Short colltrClientRptrReltnshpType;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "CMM_MTHC")
  protected Short communicationMethodType;

  @Column(name = "CNFWVR_IND")
  protected String confidentialWaiverIndicator;

  @Column(name = "FDBACK_DOC")
  protected String drmsMandatedRprtrFeedback;

  @Column(name = "RPTR_EMPNM")
  protected String employerName;

  @Type(type = "date")
  @Column(name = "FEEDBCK_DT")
  protected Date feedbackDate;

  @Column(name = "FB_RQR_IND")
  protected String feedbackRequiredIndicator;

  @Column(name = "RPTR_FSTNM")
  @ColumnTransformer(read = ("trim(RPTR_FSTNM)"))
  protected String firstName;

  @Column(name = "RPTR_LSTNM")
  @ColumnTransformer(read = ("trim(RPTR_LSTNM)"))
  protected String lastName;

  @NotNull
  @Column(name = "MNRPTR_IND")
  protected String mandatedReporterIndicator;

  @Type(type = "integer")
  @Column(name = "MSG_EXT_NO")
  protected Integer messagePhoneExtensionNumber;

  @Column(name = "MSG_TEL_NO")
  protected Long messagePhoneNumber;

  @Column(name = "MID_INI_NM")
  @ColumnTransformer(read = ("trim(MID_INI_NM)"))
  protected String middleInitialName;

  @Column(name = "NMPRFX_DSC")
  @ColumnTransformer(read = ("trim(NMPRFX_DSC)"))
  protected String namePrefixDescription;

  @Column(name = "PRM_TEL_NO")
  protected Long primaryPhoneNumber;

  @Type(type = "integer")
  @Column(name = "PRM_EXT_NO")
  protected Integer primaryPhoneExtensionNumber;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "STATE_C")
  protected Short stateCodeType;

  @Column(name = "RPTR_ST_NM")
  protected String streetName;

  @Column(name = "RPTR_ST_NO")
  protected String streetNumber;

  @Column(name = "SUFX_TLDSC")
  @ColumnTransformer(read = ("trim(SUFX_TLDSC)"))
  protected String suffixTitleDescription;

  @Type(type = "integer")
  @Column(name = "RPTR_ZIPNO")
  protected Integer zipNumber;

  @Column(name = "FKLAW_ENFT", length = CMS_ID_LEN)
  protected String lawEnforcementId;

  @Type(type = "short")
  @Column(name = "ZIP_SFX_NO")
  protected Short zipSuffixNumber;

  @Column(name = "CNTY_SPFCD")
  protected String countySpecificCode;

  /**
   * Default constructor.
   */
  public BaseReporter() {
    super();
  }

  /**
   * Alternative constructor.
   * 
   * @param lastUpdatedId last updated id
   */
  public BaseReporter(String lastUpdatedId) {
    super(lastUpdatedId);
  }

  /**
   * constructor
   * 
   * @param lastUpdatedId last updated id
   * @param lastUpdatedTime last updated time
   */
  public BaseReporter(String lastUpdatedId, Date lastUpdatedTime) {
    super(lastUpdatedId, lastUpdatedTime);
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.data.persistence.PersistentObject#getPrimaryKey()
   */
  @Override
  public String getPrimaryKey() {
    return getReferralId();
  }

  /**
   * @return the referralId
   */
  public String getReferralId() {
    return referralId;
  }

  /**
   * @return the badgeNumber
   */
  public String getBadgeNumber() {
    return StringUtils.trimToEmpty(badgeNumber);
  }

  /**
   * @return the cityName
   */
  public String getCityName() {
    return StringUtils.trimToEmpty(cityName);
  }

  /**
   * @return the colltrClientRptrReltnshpType
   */
  public Short getColltrClientRptrReltnshpType() {
    return colltrClientRptrReltnshpType;
  }

  /**
   * @return the communicationMethodType
   */
  public Short getCommunicationMethodType() {
    return communicationMethodType;
  }

  /**
   * @return the confidentialWaiverIndicator
   */
  public String getConfidentialWaiverIndicator() {
    return confidentialWaiverIndicator;
  }

  /**
   * @return the drmsMandatedRprtrFeedback
   */
  public String getDrmsMandatedRprtrFeedback() {
    return StringUtils.trimToEmpty(drmsMandatedRprtrFeedback);
  }

  /**
   * @return the employerName
   */
  public String getEmployerName() {
    return StringUtils.trimToEmpty(employerName);
  }

  /**
   * @return the feedbackDate
   */
  public Date getFeedbackDate() {
    return feedbackDate;
  }

  /**
   * @return the feedbackRequiredIndicator
   */
  public String getFeedbackRequiredIndicator() {
    return feedbackRequiredIndicator;
  }

  /**
   * @return the firstName
   */
  @Override
  public String getFirstName() {
    return StringUtils.trimToEmpty(firstName);
  }

  /**
   * @return the lastName
   */
  @Override
  public String getLastName() {
    return StringUtils.trimToEmpty(lastName);
  }

  /**
   * @return the mandatedReporterIndicator
   */
  public String getMandatedReporterIndicator() {
    return mandatedReporterIndicator;
  }

  /**
   * @return the messagePhoneExtensionNumber
   */
  public Integer getMessagePhoneExtensionNumber() {
    return messagePhoneExtensionNumber;
  }

  /**
   * @return the messagePhoneNumber
   */
  public Long getMessagePhoneNumber() {
    return messagePhoneNumber;
  }

  /**
   * @return the middleInitialName
   */
  public String getMiddleInitialName() {
    return StringUtils.trimToEmpty(middleInitialName);
  }

  /**
   * @return the namePrefixDescription
   */
  public String getNamePrefixDescription() {
    return StringUtils.trimToEmpty(namePrefixDescription);
  }

  /**
   * @return the primaryPhoneNumber
   */
  public Long getPrimaryPhoneNumber() {
    return primaryPhoneNumber;
  }

  /**
   * @return the primaryPhoneExtensionNumber
   */
  public Integer getPrimaryPhoneExtensionNumber() {
    return primaryPhoneExtensionNumber;
  }

  /**
   * @return the stateCodeType
   */
  public Short getStateCodeType() {
    return stateCodeType;
  }

  /**
   * @return the streetName
   */
  @Override
  public String getStreetName() {
    return StringUtils.trimToEmpty(streetName);
  }

  /**
   * @return the streetNumber
   */
  @Override
  public String getStreetNumber() {
    return StringUtils.trimToEmpty(streetNumber);
  }

  /**
   * @return the suffixTitleDescription
   */
  public String getSuffixTitleDescription() {
    return StringUtils.trimToEmpty(suffixTitleDescription);
  }

  /**
   * @return the zipNumber
   */
  public Integer getZipNumber() {
    return zipNumber;
  }

  /**
   * @return the lawEnforcementId
   */
  public String getLawEnforcementId() {
    return lawEnforcementId;
  }

  /**
   * @return the zipSuffixNumber
   */
  public Short getZipSuffixNumber() {
    return zipSuffixNumber;
  }

  /**
   * @return the countySpecificCode
   */
  public String getCountySpecificCode() {
    return countySpecificCode;
  }

  @JsonIgnore
  @Override
  @Transient
  public String getMiddleName() {
    return this.getMiddleInitialName();
  }

  @JsonIgnore
  @Override
  @Transient
  public String getGender() {
    // Does not apply.
    return null;
  }

  @JsonIgnore
  @Override
  @Transient
  public Date getBirthDate() {
    // Does not apply.
    return null;
  }

  @JsonIgnore
  @Override
  @Transient
  public String getSsn() {
    // Does not apply.
    return null;
  }

  @JsonIgnore
  @Override
  @Transient
  public String getNameSuffix() {
    return StringUtils.trimToEmpty(this.suffixTitleDescription);
  }

  @JsonIgnore
  @Override
  @Transient
  public String getStreetAddress() {
    return StringUtils.trimToEmpty(this.streetNumber) + " "
        + StringUtils.trimToEmpty(this.streetName);
  }

  @JsonIgnore
  @Override
  @Transient
  public String getCity() {
    return StringUtils.trimToEmpty(this.cityName);
  }

  @JsonIgnore
  @Override
  @Transient
  public String getState() {
    return this.getStateCodeType() != null ? this.getStateCodeType().toString() : null;
  }

  @JsonIgnore
  @Override
  @Transient
  public String getZip() {
    StringBuilder buf = new StringBuilder();

    if (this.zipNumber != null) {
      buf.append(zipNumber);
    }
    if (this.getZipSuffixNumber() != null) {
      buf.append('-').append(getZipSuffixNumber());
    }

    return buf.toString();
  }

  @JsonIgnore
  @Override
  @Transient
  public String getCounty() {
    return this.countySpecificCode;
  }

  @JsonIgnore
  @Override
  @Transient
  public ApiPhoneAware[] getPhones() {
    List<ApiPhoneAware> phones = new ArrayList<>();
    if (this.primaryPhoneNumber != null) {
      phones.add(new ReadablePhone(null, String.valueOf(this.primaryPhoneNumber),
          this.primaryPhoneExtensionNumber != null ? this.primaryPhoneExtensionNumber.toString()
              : null,
          null));
    }

    if (this.messagePhoneNumber != null) {
      phones
          .add(new ReadablePhone(null,
              String.valueOf(this.messagePhoneNumber), this.messagePhoneExtensionNumber != null
                  ? this.messagePhoneExtensionNumber.toString() : null,
              ApiPhoneAware.PhoneType.Cell));
    }

    return phones.toArray(new ApiPhoneAware[0]);
  }

  @Override
  public String getAddressId() {
    return null;
  }

  @Override
  public Short getStateCd() {
    return stateCodeType;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}
