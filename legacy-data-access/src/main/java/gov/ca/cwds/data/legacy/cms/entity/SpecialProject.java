package gov.ca.cwds.data.legacy.cms.entity;

import static gov.ca.cwds.data.legacy.cms.entity.SpecialProject.FIND_BY_PROJECT_NAME;
import static gov.ca.cwds.data.legacy.cms.entity.SpecialProject.FIND_BY_PROJECT_NAME_QUERY;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import gov.ca.cwds.data.legacy.cms.CmsPersistentObject;

/**
 * Special projects CMS entity.
 *
 * @author CWDS TPT Team
 */
@Entity
@Table(name = "SPC_PRJT")

@NamedQuery(name = SpecialProject.FIND_ACTIVE_SSB_BY_GOVERNMENT_ENTITY,
    query = "FROM SpecialProject WHERE PROJECT_NM = '" + SpecialProject.SSB_SPECIAL_PROJECT_NAME
        + "' AND END_DT IS NULL AND GVR_ENTC = :governmentEntity")

@NamedQuery(name = FIND_BY_PROJECT_NAME,
  query = FIND_BY_PROJECT_NAME_QUERY)


public class SpecialProject extends CmsPersistentObject {

  private static final long serialVersionUID = 241170224860954003L;

  public static final String FIND_ACTIVE_SSB_BY_GOVERNMENT_ENTITY =
      "SpecialProject.findActiveSSBByGovernmentEntity";
  public static final String SSB_SPECIAL_PROJECT_NAME = "S-Safely Surrendered Baby";
  public static final String PARAM_GOVERNMENT_ENTITY = "governmentEntity";
  public static final String PARAM_NAME = "name";

  public static final String FIND_BY_PROJECT_NAME = 
      "gov.ca.cwds.data.persistence.cms.SpecialProject.findByProjectName";
  static final String FIND_BY_PROJECT_NAME_QUERY = 
      "FROM SpecialProject WHERE GOV_ENTC = :governementEntityType AND END_DT IS NULL AND PROJECT_NM = :name";
  
  @Id
  @Column(name = "IDENTIFIER", nullable = false, length = CMS_ID_LEN)
  private String id;

  @Column(name = "PROJECT_NM", nullable = false, length = 30)
  @ColumnTransformer(read = "trim(PROJECT_NM)")
  private String name;

  @Column(name = "PRJCT_DSC", nullable = false, length = 254)
  @ColumnTransformer(read = "trim(PRJCT_DSC)")
  private String projectDescription;

  @Column(name = "GVR_ENTC", nullable = false, length = 5)
  private Short governmentEntityType;

  @Column(name = "START_DT", nullable = false, length = 10)
  private LocalDate startDate;

  @Column(name = "END_DT", nullable = true, length = 10)
  private LocalDate endDate;

  @Column(name = "ARCASS_IND", nullable = false, length = 1)
  @Type(type = "yes_no")
  private Boolean archiveAssociationIndicator;

  /**
   * No-argument constructor.
   */
  public SpecialProject() {
    super();
  }

  /**
   * Constructor
   * 
   * @param archiveAssociationIndicator - archive association indicator
   * @param projectDescription - project description
   * @param endDate - end date
   * @param governmentEntityType - government entity type
   * @param id - primary key
   * @param name - special project name
   * @param startDate - start date
   * 
   */  
  public SpecialProject(Boolean archiveAssociationIndicator, String projectDescription,
      LocalDate endDate, Short governmentEntityType, String id, String name, LocalDate startDate) {
    super();
    this.archiveAssociationIndicator = archiveAssociationIndicator;
    this.projectDescription = projectDescription;
    this.endDate = endDate;
    this.governmentEntityType = governmentEntityType;
    this.id = id;
    this.name = name;
    this.startDate = startDate;
  }
  
  @Override
  public Serializable getPrimaryKey() {
    return getId();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProjectDescription() {
    return projectDescription;
  }

  public void setProjectDescription(String projectDescription) {
    this.projectDescription = projectDescription;
  }

  public Short getGovernmentEntityType() {
    return governmentEntityType;
  }

  public void setGovernmentEntityType(Short governmentEntityType) {
    this.governmentEntityType = governmentEntityType;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Boolean getArrchiveAssociationIndicator() {
    return archiveAssociationIndicator;
  }

  public void setArcassIndicator(Boolean archiveAssociationIndicator) {
    this.archiveAssociationIndicator = archiveAssociationIndicator;
  }
}
