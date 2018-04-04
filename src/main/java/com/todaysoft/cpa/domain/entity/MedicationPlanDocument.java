package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_document")
public class MedicationPlanDocument {
    private String planReferenceKey;
    private String planMethodKey;
    private Integer medicinePlanId;
    private String refType;
    private String externalId;

    @Id
    @Column(name = "plan_reference_key")
    public String getPlanReferenceKey() {
        return planReferenceKey;
    }

    public void setPlanReferenceKey(String planReferenceKey) {
        this.planReferenceKey = planReferenceKey;
    }

    @Basic
    @Column(name = "plan_method_key")
    public String getPlanMethodKey() {
        return planMethodKey;
    }

    public void setPlanMethodKey(String planMethodKey) {
        this.planMethodKey = planMethodKey;
    }

    @Basic
    @Column(name = "medicine_plan_id")
    public Integer getMedicinePlanId() {
        return medicinePlanId;
    }

    public void setMedicinePlanId(Integer medicinePlanId) {
        this.medicinePlanId = medicinePlanId;
    }

    @Basic
    @Column(name = "ref_type")
    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    @Basic
    @Column(name = "external_id")
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanDocument that = (MedicationPlanDocument) o;
        return Objects.equals(planReferenceKey, that.planReferenceKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicinePlanId, that.medicinePlanId) &&
                Objects.equals(refType, that.refType) &&
                Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planReferenceKey, planMethodKey, medicinePlanId, refType, externalId);
    }
}
