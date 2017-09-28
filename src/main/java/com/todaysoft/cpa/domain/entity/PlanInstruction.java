package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 10:04
 */
@Entity
@Table(name = "kt_plan_instruction", schema = "project_kb_en", catalog = "")
public class PlanInstruction {
    private String planInstructionKey;
    private String medicationPlanKey;
    private Integer medicationPlanId;
    @JSONField(name = "id")
    private Integer instructionId;
    @JSONField(name = "instructionName")
    private String theName;
    @JSONField(name = "instructionCourse")
    private String theCourse;
    @JSONField(name = "instructionOrder")
    private Integer theOrder;
    @JSONField(name = "reviewRequired")
    private Boolean reviewRequired;
    @JSONField(name = "unParse")
    private String drugIds;

    @Id
    @Column(name = "plan_instruction_key", nullable = false, length = 64)
    public String getPlanInstructionKey() {
        return planInstructionKey;
    }

    public void setPlanInstructionKey(String planInstructionKey) {
        this.planInstructionKey = planInstructionKey;
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
    @Column(name = "instruction_id", nullable = false)
    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    @Basic
    @Column(name = "medication_plan_id", nullable = true)
    public Integer getMedicationPlanId() {
        return medicationPlanId;
    }

    public void setMedicationPlanId(Integer medicationPlanId) {
        this.medicationPlanId = medicationPlanId;
    }

    @Basic
    @Column(name = "the_name", nullable = true, length = 200)
    public String getTheName() {
        return theName;
    }

    public void setTheName(String theName) {
        this.theName = theName;
    }

    @Basic
    @Column(name = "the_course", nullable = true, length = -1)
    @Type(type = "text")
    public String getTheCourse() {
        return theCourse;
    }

    public void setTheCourse(String theCourse) {
        this.theCourse = theCourse;
    }

    @Basic
    @Column(name = "the_order", nullable = true)
    public Integer getTheOrder() {
        return theOrder;
    }

    public void setTheOrder(Integer theOrder) {
        this.theOrder = theOrder;
    }

    @Basic
    @Column(name = "review_required", nullable = true)
    public Boolean getReviewRequired() {
        return reviewRequired;
    }

    public void setReviewRequired(Boolean reviewRequired) {
        this.reviewRequired = reviewRequired;
    }

    @Basic
    @Column(name = "drug_ids", nullable = true, length = 200)
    public String getDrugIds() {
        return drugIds;
    }

    public void setDrugIds(String drugIds) {
        this.drugIds = drugIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanInstruction that = (PlanInstruction) o;

        if (planInstructionKey != null ? !planInstructionKey.equals(that.planInstructionKey) : that.planInstructionKey != null)
            return false;
        if (medicationPlanKey != null ? !medicationPlanKey.equals(that.medicationPlanKey) : that.medicationPlanKey != null)
            return false;
        if (instructionId != null ? !instructionId.equals(that.instructionId) : that.instructionId != null)
            return false;
        if (medicationPlanId != null ? !medicationPlanId.equals(that.medicationPlanId) : that.medicationPlanId != null)
            return false;
        if (theName != null ? !theName.equals(that.theName) : that.theName != null) return false;
        if (theCourse != null ? !theCourse.equals(that.theCourse) : that.theCourse != null) return false;
        if (theOrder != null ? !theOrder.equals(that.theOrder) : that.theOrder != null) return false;
        if (reviewRequired != null ? !reviewRequired.equals(that.reviewRequired) : that.reviewRequired != null)
            return false;
        if (drugIds != null ? !drugIds.equals(that.drugIds) : that.drugIds != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = planInstructionKey != null ? planInstructionKey.hashCode() : 0;
        result = 31 * result + (medicationPlanKey != null ? medicationPlanKey.hashCode() : 0);
        result = 31 * result + (instructionId != null ? instructionId.hashCode() : 0);
        result = 31 * result + (medicationPlanId != null ? medicationPlanId.hashCode() : 0);
        result = 31 * result + (theName != null ? theName.hashCode() : 0);
        result = 31 * result + (theCourse != null ? theCourse.hashCode() : 0);
        result = 31 * result + (theOrder != null ? theOrder.hashCode() : 0);
        result = 31 * result + (reviewRequired != null ? reviewRequired.hashCode() : 0);
        result = 31 * result + (drugIds != null ? drugIds.hashCode() : 0);
        return result;
    }
}
