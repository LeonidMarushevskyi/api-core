package gov.ca.cwds.data.legacy.cms.entity;

import gov.ca.cwds.data.legacy.cms.CmsPersistentObject;
import gov.ca.cwds.data.legacy.cms.entity.enums.ReferralOutcome;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.HealthConsentType;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.HealthReferralType;
import gov.ca.cwds.data.legacy.cms.entity.syscodes.HealthReferredToType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

@SuppressWarnings({"squid:S3437", "squid:S2160"})
@Entity
@Table(name = "HLTH_RFT")
public class HealthReferral extends CmsPersistentObject {

  private static final long serialVersionUID = -7710959950918747285L;

  @Id
  @Size(min = CMS_ID_LEN, max = CMS_ID_LEN)
  @Column(name = "THIRD_ID")
  private String id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(
      name = "FKCHLD_CLT",
      referencedColumnName = "FKCLIENT_T",
      insertable = false,
      updatable = false
  )
  private ChildClient childClient;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(
      name = "HL_REFC",
      referencedColumnName = "SYS_ID",
      insertable = false,
      updatable = false)
  private HealthReferralType healthReferralType;

  @NotNull
  @Column(name = "HL_REF_DT")
  private LocalDate referralDate;

  @NotFound(action = NotFoundAction.IGNORE)
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(
      name = "HL_CNSTC",
      referencedColumnName = "SYS_ID",
      insertable = false,
      updatable = false)
  private HealthConsentType healthConsentType;

  @Column(name = "HL_CNST_DT")
  private LocalDate consentOnFileDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(
      name = "HL_REF2C",
      referencedColumnName = "SYS_ID",
      insertable = false,
      updatable = false)
  private HealthReferredToType healthReferredToType;

  @Column(name = "COMNT_DSC")
  @ColumnTransformer(read = "trim(COMNT_DSC)")
  private String comment;

  @Column(name = "HL_REFO_CD")
  @Convert(converter = ReferralOutcome.ReferralOutcomeConverter.class)
  private ReferralOutcome referralOutcome;

  @Column(name = "HL_REFO_DT")
  private LocalDate referralOutcomeDate;

  @Type(type = "yes_no")
  @Column(name = "HL_OOC_IND")
  private boolean outOfCountyIndicator;

  @Override
  public Serializable getPrimaryKey() {
    return null;
  }

  public ChildClient getChildClient() {
    return childClient;
  }

  public void setChildClient(ChildClient childClient) {
    this.childClient = childClient;
  }

  public String getId() {
    return id;
  }

  public void setId(String thirdId) {
    this.id = thirdId;
  }

  public HealthReferralType getHealthReferralType() {
    return healthReferralType;
  }

  public void setHealthReferralType(HealthReferralType healthReferralType) {
    this.healthReferralType = healthReferralType;
  }

  public LocalDate getReferralDate() {
    return referralDate;
  }

  public void setReferralDate(LocalDate referralDate) {
    this.referralDate = referralDate;
  }

  public HealthConsentType getHealthConsentType() {
    return healthConsentType;
  }

  public void setHealthConsentType(HealthConsentType healthConsentType) {
    this.healthConsentType = healthConsentType;
  }

  public LocalDate getConsentOnFileDate() {
    return consentOnFileDate;
  }

  public void setConsentOnFileDate(LocalDate consentOnFileDate) {
    this.consentOnFileDate = consentOnFileDate;
  }

  public HealthReferredToType getHealthReferredToType() {
    return healthReferredToType;
  }

  public void setHealthReferredToType(HealthReferredToType healthReferredToType) {
    this.healthReferredToType = healthReferredToType;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public ReferralOutcome getHlRefoCd() {
    return referralOutcome;
  }

  public void setHlRefoCd(ReferralOutcome referralOutcome) {
    this.referralOutcome = referralOutcome;
  }

  public LocalDate getReferralOutcomeDate() {
    return referralOutcomeDate;
  }

  public void setReferralOutcomeDate(LocalDate referralOutcomeDate) {
    this.referralOutcomeDate = referralOutcomeDate;
  }

  public boolean getOutOfCountyIndicator() {
    return outOfCountyIndicator;
  }

  public void setOutOfCountyIndicator(boolean outOfCountyIndicator) {
    this.outOfCountyIndicator = outOfCountyIndicator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HealthReferral)) {
      return false;
    }
    HealthReferral that = (HealthReferral) o;
    return Objects.equals(getChildClient(), that.getChildClient()) &&
        Objects.equals(getHealthReferralType(), that.getHealthReferralType()) &&
        Objects.equals(getReferralDate(), that.getReferralDate()) &&
        Objects.equals(getHealthConsentType(), that.getHealthConsentType()) &&
        Objects.equals(getConsentOnFileDate(), that.getConsentOnFileDate()) &&
        Objects.equals(getHealthReferredToType(), that.getHealthReferredToType()) &&
        referralOutcome == that.referralOutcome &&
        Objects.equals(getReferralOutcomeDate(), that.getReferralOutcomeDate());
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(getChildClient(), getHealthReferralType(), getReferralDate(),
            getHealthConsentType(), getConsentOnFileDate(), getHealthReferredToType(),
            referralOutcome, getReferralOutcomeDate());
  }
}
