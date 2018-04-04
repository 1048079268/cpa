package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_method")
public class MedicationPlanMethod {
    private String planMethodKey;
    private String medicationPlanKey;
    private String methodName;
    private Integer methodInde;
    private String methodDescription;

    @Id
    @Column(name = "plan_method_key")
    public String getPlanMethodKey() {
        return planMethodKey;
    }

    public void setPlanMethodKey(String planMethodKey) {
        this.planMethodKey = planMethodKey;
    }

    @Basic
    @Column(name = "medication_plan_key")
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Basic
    @Column(name = "method_name")
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Basic
    @Column(name = "method_inde")
    public Integer getMethodInde() {
        return methodInde;
    }

    public void setMethodInde(Integer methodInde) {
        this.methodInde = methodInde;
    }

    @Basic
    @Column(name = "method_description")
    @Type(type = "text")
    public String getMethodDescription() {
        return methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanMethod that = (MedicationPlanMethod) o;
        return Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicationPlanKey, that.medicationPlanKey) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(methodInde, that.methodInde) &&
                Objects.equals(methodDescription, that.methodDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planMethodKey, medicationPlanKey, methodName, methodInde, methodDescription);
    }
}
