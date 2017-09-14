package com.todaysoft.cpa.domain.medicationPlan.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_plan_cancer")
@IdClass(PlanCancerPK.class)
public class PlanCancer {
    private String medicationPlanKey;
    private String cancerKey;
    private int medicinePlanId;
    @JSONField(name = "doid")
    private int doid;
    @JSONField(name = "name")
    private String doidName;

    @Id
    @Column(name = "medication_plan_key", nullable = false, length = 64)
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
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
    @Column(name = "doid", nullable = false)
    public int getDoid() {
        return doid;
    }

    public void setDoid(int doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "doid_name", nullable = true, length = 200)
    public String getDoidName() {
        return doidName;
    }

    public void setDoidName(String doidName) {
        this.doidName = doidName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanCancer that = (PlanCancer) o;

        if (medicinePlanId != that.medicinePlanId) return false;
        if (doid != that.doid) return false;
        if (medicationPlanKey != null ? !medicationPlanKey.equals(that.medicationPlanKey) : that.medicationPlanKey != null)
            return false;
        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;
        if (doidName != null ? !doidName.equals(that.doidName) : that.doidName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = medicationPlanKey != null ? medicationPlanKey.hashCode() : 0;
        result = 31 * result + (cancerKey != null ? cancerKey.hashCode() : 0);
        result = 31 * result + medicinePlanId;
        result = 31 * result + doid;
        result = 31 * result + (doidName != null ? doidName.hashCode() : 0);
        return result;
    }
}
