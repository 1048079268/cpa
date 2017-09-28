package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_medication_plan")
public class MedicationPlan {
    private String medicationPlanKey;
    @JSONField(name = "id")
    private Integer medicinePlanId;
    @JSONField(name = "regimenName")
    private String regimenName;
    private String programNameC;
    @JSONField(name = "diseaseName")
    private String diseaseName;
    @JSONField(name = "chemotherapyType")
    private String chemotherapyType;
    @JSONField(name="chemotherapyName")
    private String chemotherapyName;
    @JSONField(name = "chemotherapyDescription")
    private String regimenDescription;
    private String approvalAgency;
    private String approvalLink;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

    @Id
    @Column(name = "medication_plan_key", nullable = false, length = 64)
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Basic
    @Column(name = "medicine_plan_id", nullable = false)
    public Integer getMedicinePlanId() {
        return medicinePlanId;
    }

    public void setMedicinePlanId(Integer medicinePlanId) {
        this.medicinePlanId = medicinePlanId;
    }

    @Basic
    @Column(name = "regimen_name", nullable = false, length = 200)
    public String getRegimenName() {
        return regimenName;
    }

    public void setRegimenName(String regimenName) {
        this.regimenName = regimenName;
    }

    @Basic
    @Column(name = "program_name_c", nullable = true, length = 200)
    public String getProgramNameC() {
        return programNameC;
    }

    public void setProgramNameC(String programNameC) {
        this.programNameC = programNameC;
    }

    @Basic
    @Column(name = "disease_name", nullable = true, length = 200)
    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    @Basic
    @Column(name = "chemotherapy_type", nullable = true, length = 200)
    public String getChemotherapyType() {
        return chemotherapyType;
    }

    public void setChemotherapyType(String chemotherapyType) {
        this.chemotherapyType = chemotherapyType;
    }

    @Basic
    @Column(name = "chemotherapy_name", nullable = true, length = 200)
    public String getChemotherapyName() {
        return chemotherapyName;
    }

    public void setChemotherapyName(String chemotherapyName) {
        this.chemotherapyName = chemotherapyName;
    }

    @Basic
    @Column(name = "regimen_description", nullable = true, length = -1)
    @Type(type = "text")
    public String getRegimenDescription() {
        return regimenDescription;
    }

    public void setRegimenDescription(String regimenDescription) {
        this.regimenDescription = regimenDescription;
    }

    @Basic
    @Column(name = "approval_agency", nullable = true, length = 64)
    public String getApprovalAgency() {
        return approvalAgency;
    }

    public void setApprovalAgency(String approvalAgency) {
        this.approvalAgency = approvalAgency;
    }

    @Basic
    @Column(name = "approval_link", nullable = true, length = 64)
    public String getApprovalLink() {
        return approvalLink;
    }

    public void setApprovalLink(String approvalLink) {
        this.approvalLink = approvalLink;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true)
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
    }

    @Basic
    @Column(name = "check_state", nullable = true)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicationPlan)) return false;

        MedicationPlan that = (MedicationPlan) o;

        if (getMedicationPlanKey() != null ? !getMedicationPlanKey().equals(that.getMedicationPlanKey()) : that.getMedicationPlanKey() != null)
            return false;
        if (getMedicinePlanId() != null ? !getMedicinePlanId().equals(that.getMedicinePlanId()) : that.getMedicinePlanId() != null)
            return false;
        if (getRegimenName() != null ? !getRegimenName().equals(that.getRegimenName()) : that.getRegimenName() != null)
            return false;
        if (getProgramNameC() != null ? !getProgramNameC().equals(that.getProgramNameC()) : that.getProgramNameC() != null)
            return false;
        if (getDiseaseName() != null ? !getDiseaseName().equals(that.getDiseaseName()) : that.getDiseaseName() != null)
            return false;
        if (getChemotherapyType() != null ? !getChemotherapyType().equals(that.getChemotherapyType()) : that.getChemotherapyType() != null)
            return false;
        if (getChemotherapyName() != null ? !getChemotherapyName().equals(that.getChemotherapyName()) : that.getChemotherapyName() != null)
            return false;
        if (getRegimenDescription() != null ? !getRegimenDescription().equals(that.getRegimenDescription()) : that.getRegimenDescription() != null)
            return false;
        if (getApprovalAgency() != null ? !getApprovalAgency().equals(that.getApprovalAgency()) : that.getApprovalAgency() != null)
            return false;
        if (getApprovalLink() != null ? !getApprovalLink().equals(that.getApprovalLink()) : that.getApprovalLink() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
            return false;
        if (getCreatedWay() != null ? !getCreatedWay().equals(that.getCreatedWay()) : that.getCreatedWay() != null)
            return false;
        return getCheckState() != null ? getCheckState().equals(that.getCheckState()) : that.getCheckState() == null;
    }

    @Override
    public int hashCode() {
        int result = getMedicationPlanKey() != null ? getMedicationPlanKey().hashCode() : 0;
        result = 31 * result + (getMedicinePlanId() != null ? getMedicinePlanId().hashCode() : 0);
        result = 31 * result + (getRegimenName() != null ? getRegimenName().hashCode() : 0);
        result = 31 * result + (getProgramNameC() != null ? getProgramNameC().hashCode() : 0);
        result = 31 * result + (getDiseaseName() != null ? getDiseaseName().hashCode() : 0);
        result = 31 * result + (getChemotherapyType() != null ? getChemotherapyType().hashCode() : 0);
        result = 31 * result + (getChemotherapyName() != null ? getChemotherapyName().hashCode() : 0);
        result = 31 * result + (getRegimenDescription() != null ? getRegimenDescription().hashCode() : 0);
        result = 31 * result + (getApprovalAgency() != null ? getApprovalAgency().hashCode() : 0);
        result = 31 * result + (getApprovalLink() != null ? getApprovalLink().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getCreatedWay() != null ? getCreatedWay().hashCode() : 0);
        result = 31 * result + (getCheckState() != null ? getCheckState().hashCode() : 0);
        return result;
    }
}
