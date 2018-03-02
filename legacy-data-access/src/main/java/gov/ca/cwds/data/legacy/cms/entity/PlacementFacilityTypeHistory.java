package gov.ca.cwds.data.legacy.cms.entity;

import gov.ca.cwds.data.persistence.PersistentObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Historic information on when a Placement Home Facility Type is changed and the dates
 * the Placement Home Facility type was of a specific type.
 * Subject Area – PLACEMENT_HOME_INFORMATION
 * Identifier – THIRD_ID, FKPLC_HM_T
 */
@Entity
@Table(name = "PFACHIST")
@IdClass(PlacementFacilityTypeHistoryPK.class)
public class PlacementFacilityTypeHistory implements PersistentObject {

    private static final long serialVersionUID = -5838053429059604299L;

    /**
     * THIRD_ID -  This is a system generated unique number that supplements user supplied data in the primary key.
     * The Third ID attribute will be used as part of the new Primary Key in combination with user supplied data.
     * It will also be used alone as a separate key for direct access.
     * This Third ID is composed of a base 62 Creation Timestamp
     * and the STAFF_PERSON ID (a sequential 3 digit base 62 number generated by the system).
     */
    @Id
    @Column(name = "THIRD_ID", nullable = false, length = 10)
    private String thirdId;

    /**
     * FKPLC_HM_T -  Mandatory Foreign key that RECORDS_PLCMNT_FAC_TYPE_HSTRY_INFO_FOR a PLACEMENT_HOME.
     */
    @Id
    @Column(name = "FKPLC_HM_T", nullable = false, length = 10)
    private String fkplcHmT;

    /**
     * CREATION_TIMESTAMP - The time and date when the PLACEMENT_FACILITY_TYPE_HISTORY was first created.
     */
    @Column(name = "CREATN_TS", nullable = false)
    private LocalDateTime creationTimestamp;

    /**
     * END_TIMESTAMP - The time and date of the most recent update to an occurrence of this entity type.
     */
    @Column(name = "END_TS")
    private LocalDateTime endTimestamp;

    /**
     * LAST_UPDATE_ID - The ID (a sequential 3 digit base 62 number generated by the system)
     * of the STAFF PERSON or batch program which made the last update to an occurrence of this entity type.
     */
    @Column(name = "LST_UPD_ID", nullable = false, length = 3)
    private String lastUpdateId;

    /**
     * LAST_UPDATE_TIMESTAMP - The time and date of the most recent update to an occurrence of this entity type.
     */
    @Column(name = "LST_UPD_TS", nullable = false)
    private LocalDateTime lastUpdateTimestamp;

    /**
     * PLACEMENT_FACILITY_TYPE - The system generated number assigned to each type of placement facility
     * which can be used for PLACEMENT_FACILITY_TYPE_HISTORY
     * (e.g., Foster Family Agency, Licensed Foster Family Home, Relative Home, Small Family Home, Group Home, etc.).
     */
    @Column(name = "PLC_FCLC", nullable = false)
    private Short placementFacilityType;

    /**
     * START_TIMESTAMP - The time and date of the most recent update to an occurrence of this entity type.
     */
    @Column(name = "START_TS", nullable = false)
    private LocalDateTime startTimestamp;

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getFkplcHmT() {
        return fkplcHmT;
    }

    public void setFkplcHmT(String fkplcHmT) {
        this.fkplcHmT = fkplcHmT;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public Short getPlacementFacilityType() {
        return placementFacilityType;
    }

    public void setPlacementFacilityType(Short placementFacilityType) {
        this.placementFacilityType = placementFacilityType;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public Serializable getPrimaryKey() {
        return new PlacementFacilityTypeHistoryPK(this.fkplcHmT, this.thirdId);
    }
}
