package com.todaysoft.cpa.domain.medicationPlan.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_plan_drug")
@IdClass(PlanDrugPK.class)
public class PlanDrug {
    private String medicationPlanKey;
    private String drugKey;
    private int medicationPlanId;
    private int drugId;

    @Id
    @Column(name = "medication_plan_key", nullable = false, length = 64)
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Basic
    @Column(name = "medication_plan_id", nullable = false)
    public int getMedicationPlanId() {
        return medicationPlanId;
    }

    public void setMedicationPlanId(int medicationPlanId) {
        this.medicationPlanId = medicationPlanId;
    }

    @Basic
    @Column(name = "drug_id", nullable = false)
    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanDrug planDrug = (PlanDrug) o;

        if (medicationPlanId != planDrug.medicationPlanId) return false;
        if (drugId != planDrug.drugId) return false;
        if (medicationPlanKey != null ? !medicationPlanKey.equals(planDrug.medicationPlanKey) : planDrug.medicationPlanKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(planDrug.drugKey) : planDrug.drugKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = medicationPlanKey != null ? medicationPlanKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + medicationPlanId;
        result = 31 * result + drugId;
        return result;
    }
}
