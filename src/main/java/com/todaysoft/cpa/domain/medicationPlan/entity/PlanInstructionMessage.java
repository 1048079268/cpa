package com.todaysoft.cpa.domain.medicationPlan.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_plan_instruction_message")
public class PlanInstructionMessage {
    private String planInstructionMessageKey;
    private String planInstructionKey;
    private Integer instructionId;
    @JSONField(name = "text")
    private String theText;
    private String theRoute;
    private String theDuration;
    private String theFrequency;
    private String theDosage;
    @JSONField(name = "unParse")
    private String drugIds;

    @Id
    @Column(name = "plan_instruction_message_key", nullable = false, length = 64)
    public String getPlanInstructionMessageKey() {
        return planInstructionMessageKey;
    }

    public void setPlanInstructionMessageKey(String planInstructionMessageKey) {
        this.planInstructionMessageKey = planInstructionMessageKey;
    }

    @Basic
    @Column(name = "plan_instruction_key", nullable = true, length = 64)
    public String getPlanInstructionKey() {
        return planInstructionKey;
    }

    public void setPlanInstructionKey(String planInstructionKey) {
        this.planInstructionKey = planInstructionKey;
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
    @Column(name = "the_text", nullable = true, length = -1)
    @Type(type = "text")
    public String getTheText() {
        return theText;
    }

    public void setTheText(String theText) {
        this.theText = theText;
    }

    @Basic
    @Column(name = "the_route", nullable = true, length = 200)
    public String getTheRoute() {
        return theRoute;
    }

    public void setTheRoute(String theRoute) {
        this.theRoute = theRoute;
    }

    @Basic
    @Column(name = "the_duration", nullable = true, length = 200)
    public String getTheDuration() {
        return theDuration;
    }

    public void setTheDuration(String theDuration) {
        this.theDuration = theDuration;
    }

    @Basic
    @Column(name = "the_frequency", nullable = true, length = 200)
    public String getTheFrequency() {
        return theFrequency;
    }

    public void setTheFrequency(String theFrequency) {
        this.theFrequency = theFrequency;
    }

    @Basic
    @Column(name = "the_dosage", nullable = true, length = 200)
    public String getTheDosage() {
        return theDosage;
    }

    public void setTheDosage(String theDosage) {
        this.theDosage = theDosage;
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

        PlanInstructionMessage that = (PlanInstructionMessage) o;

        if (instructionId != that.instructionId) return false;
        if (planInstructionMessageKey != null ? !planInstructionMessageKey.equals(that.planInstructionMessageKey) : that.planInstructionMessageKey != null)
            return false;
        if (planInstructionKey != null ? !planInstructionKey.equals(that.planInstructionKey) : that.planInstructionKey != null)
            return false;
        if (theText != null ? !theText.equals(that.theText) : that.theText != null) return false;
        if (theRoute != null ? !theRoute.equals(that.theRoute) : that.theRoute != null) return false;
        if (theDuration != null ? !theDuration.equals(that.theDuration) : that.theDuration != null) return false;
        if (theFrequency != null ? !theFrequency.equals(that.theFrequency) : that.theFrequency != null) return false;
        if (theDosage != null ? !theDosage.equals(that.theDosage) : that.theDosage != null) return false;
        if (drugIds != null ? !drugIds.equals(that.drugIds) : that.drugIds != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = planInstructionMessageKey != null ? planInstructionMessageKey.hashCode() : 0;
        result = 31 * result + (planInstructionKey != null ? planInstructionKey.hashCode() : 0);
        result = 31 * result + instructionId;
        result = 31 * result + (theText != null ? theText.hashCode() : 0);
        result = 31 * result + (theRoute != null ? theRoute.hashCode() : 0);
        result = 31 * result + (theDuration != null ? theDuration.hashCode() : 0);
        result = 31 * result + (theFrequency != null ? theFrequency.hashCode() : 0);
        result = 31 * result + (theDosage != null ? theDosage.hashCode() : 0);
        result = 31 * result + (drugIds != null ? drugIds.hashCode() : 0);
        return result;
    }
}
