package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
public class PlanDrugPK implements Serializable {
    private String medicationPlanKey;
    private String drugKey;

    @Column(name = "medication_plan_key", nullable = false, length = 64)
    @Id
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanDrugPK that = (PlanDrugPK) o;

        if (medicationPlanKey != null ? !medicationPlanKey.equals(that.medicationPlanKey) : that.medicationPlanKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = medicationPlanKey != null ? medicationPlanKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        return result;
    }
}
