package gov.ca.cwds.data.es.transform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import gov.ca.cwds.data.es.ElasticSearchPersonAka;

/**
 * Pseudo-normalized container for CMS legacy "other client names", name aliases, or "also known as"
 * (AKA) by legacy client id.
 * 
 * @author CWDS API Team
 */
public class ReplicatedAkas implements ReadablePerson {

  private static final long serialVersionUID = 1L;

  /**
   * Legacy person id, the client id.
   */
  private String id;

  private List<ElasticSearchPersonAka> akas = new ArrayList<>();

  /**
   * Default constructor.
   */
  public ReplicatedAkas() {
    // Default, no-op.
  }

  /**
   * Preferred constructor.
   * 
   * @param id legacy id
   */
  public ReplicatedAkas(String id) {
    this.id = id;
  }

  @Override
  public Serializable getPrimaryKey() {
    return id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<ElasticSearchPersonAka> getAkas() {
    return akas;
  }

  public void setAkas(List<ElasticSearchPersonAka> akas) {
    this.akas = akas;
  }

  public void addAka(ElasticSearchPersonAka aka) {
    this.akas.add(aka);
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
