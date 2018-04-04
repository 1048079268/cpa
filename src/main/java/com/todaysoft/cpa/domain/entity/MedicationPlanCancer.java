package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_cancer")
@IdClass(MedicationPlanCancerPK.class)
public class MedicationPlanCancer {
    private String medicationPlanKey;
    private String cancerKey;
    private Integer medicinePlanId;
    private Integer doid;
    private String doidName;

    @Id
    @Column(name = "medication_plan_key")
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Id
    @Column(name = "cancer_key")
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
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
    @Column(name = "doid")
    public Integer getDoid() {
        return doid;
    }

    public void setDoid(Integer doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "doid_name")
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
        MedicationPlanCancer that = (MedicationPlanCancer) o;
        return Objects.equals(medicationPlanKey, that.medicationPlanKey) &&
                Objects.equals(cancerKey, that.cancerKey) &&
                Objects.equals(medicinePlanId, that.medicinePlanId) &&
                Objects.equals(doid, that.doid) &&
                Objects.equals(doidName, that.doidName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(medicationPlanKey, cancerKey, medicinePlanId, doid, doidName);
    }
}
