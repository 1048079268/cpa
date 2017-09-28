package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
public class PlanCancerPK implements Serializable {
    private String medicationPlanKey;
    private String cancerKey;

    @Column(name = "medication_plan_key", nullable = false, length = 64)
    @Id
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Column(name = "cancer_key", nullable = false, length = 64)
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

        PlanCancerPK that = (PlanCancerPK) o;

        if (medicationPlanKey != null ? !medicationPlanKey.equals(that.medicationPlanKey) : that.medicationPlanKey != null)
            return false;
        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = medicationPlanKey != null ? medicationPlanKey.hashCode() : 0;
        result = 31 * result + (cancerKey != null ? cancerKey.hashCode() : 0);
        return result;
    }
}
