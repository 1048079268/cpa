package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_plan_reference")
public class PlanReference {
    private String planReferenceKey;
    private String medicationPlanKey;
    private int medicinePlanId;
    @JSONField(name = "id")
    private int referenceId;
    @JSONField(name = "refType")
    private String refType;
    @JSONField(name = "externalId")
    private String externalId;

    @Id
    @Column(name = "plan_reference_key", nullable = false, length = 64)
    public String getPlanReferenceKey() {
        return planReferenceKey;
    }

    public void setPlanReferenceKey(String planReferenceKey) {
        this.planReferenceKey = planReferenceKey;
    }

    @Basic
    @Column(name = "medication_plan_key", nullable = true, length = 64)
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Basic
    @Column(name = "medicine_plan_id", nullable = false)
    public int getMedicinePlanId() {
        return medicinePlanId;
    }

    public void setMedicinePlanId(int medicinePlanId) {
        this.medicinePlanId = medicinePlanId;
    }

    @Basic
    @Column(name = "reference_id", nullable = false)
    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    @Basic
    @Column(name = "ref_type", nullable = true, length = 64)
    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    @Basic
    @Column(name = "external_id", nullable = true, length = 200)
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

        PlanReference that = (PlanReference) o;

        if (medicinePlanId != that.medicinePlanId) return false;
        if (referenceId != that.referenceId) return false;
        if (planReferenceKey != null ? !planReferenceKey.equals(that.planReferenceKey) : that.planReferenceKey != null)
            return false;
        if (medicationPlanKey != null ? !medicationPlanKey.equals(that.medicationPlanKey) : that.medicationPlanKey != null)
            return false;
        if (refType != null ? !refType.equals(that.refType) : that.refType != null) return false;
        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = planReferenceKey != null ? planReferenceKey.hashCode() : 0;
        result = 31 * result + (medicationPlanKey != null ? medicationPlanKey.hashCode() : 0);
        result = 31 * result + medicinePlanId;
        result = 31 * result + referenceId;
        result = 31 * result + (refType != null ? refType.hashCode() : 0);
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        return result;
    }
}
