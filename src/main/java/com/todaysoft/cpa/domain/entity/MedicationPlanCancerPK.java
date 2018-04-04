package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
public class MedicationPlanCancerPK implements Serializable {
    private String medicationPlanKey;
    private String cancerKey;

    @Column(name = "medication_plan_key")
    @Id
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Column(name = "cancer_key")
    @Id
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanCancerPK that = (MedicationPlanCancerPK) o;
        return Objects.equals(medicationPlanKey, that.medicationPlanKey) &&
                Objects.equals(cancerKey, that.cancerKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(medicationPlanKey, cancerKey);
    }
}
