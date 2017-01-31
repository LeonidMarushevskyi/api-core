package gov.ca.cwds.data.ns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * Base class for objects in the NS persistence layer.
 * 
 * <p>
 * Type P stands for the primary key type, which must extend interface {@link Serializable}.
 * </p>
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class NsPersistentObject implements PersistentObject {
  protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSS";

  @Column(name = "create_user_id")
  private String createId;

  @Type(type = "timestamp")
  @Column(name = "create_datetime")
  private Date createTime;


  @Column(name = "update_user_id")
  private String lastUpdatedId;

  @Type(type = "timestamp")
  @Column(name = "update_datetime")
  private Date lastUpdatedTime;



  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  protected NsPersistentObject() {

  }

  /**
   * Constructor
   * 
   * @param lastUpdatedId the id of the last person to update this object
   */
  protected NsPersistentObject(String lastUpdatedId, String createId) {
    this.lastUpdatedId = lastUpdatedId;
    this.lastUpdatedTime = new Date();
    this.createId = createId;
    this.createTime = new Date();
  }


  /**
   * @return the timestampFormat
   */
  public static String getTimestampFormat() {
    return TIMESTAMP_FORMAT;
  }

  /**
   * @return the createId
   */
  public String getCreateId() {
    return createId;
  }

  /**
   * @return the createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * @return the lastUpdatedId
   */
  public String getLastUpdatedId() {
    return lastUpdatedId;
  }

  /**
   * @return the lastUpdatedTime
   */
  public Date getLastUpdatedTime() {
    return lastUpdatedTime;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.data.persistence.PersistentObject#getPrimaryKey()
   */
  @Override
  public abstract Serializable getPrimaryKey();

}
