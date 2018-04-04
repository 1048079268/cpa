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
@Table(name = "kt_medication_plan_course_group_drug")
public class MedicationPlanCourseGroupDrug {
    private String groupDrugKey;
    private String groupKey;
    private String drugKey;
    private String theMethod;
    private String theFrequency;
    private String theDescription;
    private String theCycle;
    private String theDay;
    private Integer totalCycle;
    private String doseMax;
    private String doseMin;
    private String doseUnit;
    private Integer infusionTimeMax;
    private Integer infusionTimeMin;
    private Integer cycleMax;
    private Integer cycleMin;

    @Id
    @Column(name = "group_drug_key")
    public String getGroupDrugKey() {
        return groupDrugKey;
    }

    public void setGroupDrugKey(String groupDrugKey) {
        this.groupDrugKey = groupDrugKey;
    }

    @Basic
    @Column(name = "group_key")
    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    @Basic
    @Column(name = "drug_key")
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Basic
    @Column(name = "the_method")
    public String getTheMethod() {
        return theMethod;
    }

    public void setTheMethod(String theMethod) {
        this.theMethod = theMethod;
    }

    @Basic
    @Column(name = "the_frequency")
    public String getTheFrequency() {
        return theFrequency;
    }

    public void setTheFrequency(String theFrequency) {
        this.theFrequency = theFrequency;
    }

    @Basic
    @Column(name = "the_description")
    @Type(type = "text")
    public String getTheDescription() {
        return theDescription;
    }

    public void setTheDescription(String theDescription) {
        this.theDescription = theDescription;
    }

    @Basic
    @Column(name = "the_cycle")
    public String getTheCycle() {
        return theCycle;
    }

    public void setTheCycle(String theCycle) {
        this.theCycle = theCycle;
    }

    @Basic
    @Column(name = "the_day")
    public String getTheDay() {
        return theDay;
    }

    public void setTheDay(String theDay) {
        this.theDay = theDay;
    }

    @Basic
    @Column(name = "total_cycle")
    public Integer getTotalCycle() {
        return totalCycle;
    }

    public void setTotalCycle(Integer totalCycle) {
        this.totalCycle = totalCycle;
    }

    @Basic
    @Column(name = "dose_max")
    public String getDoseMax() {
        return doseMax;
    }

    public void setDoseMax(String doseMax) {
        this.doseMax = doseMax;
    }

    @Basic
    @Column(name = "dose_min")
    public String getDoseMin() {
        return doseMin;
    }

    public void setDoseMin(String doseMin) {
        this.doseMin = doseMin;
    }

    @Basic
    @Column(name = "dose_unit")
    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    @Basic
    @Column(name = "infusion_time_max")
    public Integer getInfusionTimeMax() {
        return infusionTimeMax;
    }

    public void setInfusionTimeMax(Integer infusionTimeMax) {
        this.infusionTimeMax = infusionTimeMax;
    }

    @Basic
    @Column(name = "infusion_time_min")
    public Integer getInfusionTimeMin() {
        return infusionTimeMin;
    }

    public void setInfusionTimeMin(Integer infusionTimeMin) {
        this.infusionTimeMin = infusionTimeMin;
    }

    @Basic
    @Column(name = "cycle_max")
    public Integer getCycleMax() {
        return cycleMax;
    }

    public void setCycleMax(Integer cycleMax) {
        this.cycleMax = cycleMax;
    }

    @Basic
    @Column(name = "cycle_min")
    public Integer getCycleMin() {
        return cycleMin;
    }

    public void setCycleMin(Integer cycleMin) {
        this.cycleMin = cycleMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanCourseGroupDrug that = (MedicationPlanCourseGroupDrug) o;
        return Objects.equals(groupDrugKey, that.groupDrugKey) &&
                Objects.equals(groupKey, that.groupKey) &&
                Objects.equals(drugKey, that.drugKey) &&
                Objects.equals(theMethod, that.theMethod) &&
                Objects.equals(theFrequency, that.theFrequency) &&
                Objects.equals(theDescription, that.theDescription) &&
                Objects.equals(theCycle, that.theCycle) &&
                Objects.equals(theDay, that.theDay) &&
                Objects.equals(totalCycle, that.totalCycle) &&
                Objects.equals(doseMax, that.doseMax) &&
                Objects.equals(doseMin, that.doseMin) &&
                Objects.equals(doseUnit, that.doseUnit) &&
                Objects.equals(infusionTimeMax, that.infusionTimeMax) &&
                Objects.equals(infusionTimeMin, that.infusionTimeMin) &&
                Objects.equals(cycleMax, that.cycleMax) &&
                Objects.equals(cycleMin, that.cycleMin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupDrugKey, groupKey, drugKey, theMethod, theFrequency, theDescription, theCycle, theDay, totalCycle, doseMax, doseMin, doseUnit, infusionTimeMax, infusionTimeMin, cycleMax, cycleMin);
    }
}
