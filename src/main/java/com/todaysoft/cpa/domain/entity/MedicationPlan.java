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
@Table(name = "kt_medication_plan")
public class MedicationPlan {
    private String medicationPlanKey;
    private Integer medicinePlanId;
    private String regimenName;
    private String programNameC;
    private String aliasName;
    private String chemotherapyType;
    private String regimenDescription;
    private String cancerDetail;
    private Long createdAt;
    private Integer createdWay;
    private String createdByName;
    private Integer checkState;
    private String approvalAgency;
    private String approvalLink;
    private String chemotherapyName;
    private String diseaseName;

    @Id
    @Column(name = "medication_plan_key")
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
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
    @Column(name = "regimen_name")
    public String getRegimenName() {
        return regimenName;
    }

    public void setRegimenName(String regimenName) {
        this.regimenName = regimenName;
    }

    @Basic
    @Column(name = "program_name_c")
    public String getProgramNameC() {
        return programNameC;
    }

    public void setProgramNameC(String programNameC) {
        this.programNameC = programNameC;
    }

    @Basic
    @Column(name = "alias_name")
    @Type(type = "text")
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Basic
    @Column(name = "chemotherapy_type")
    public String getChemotherapyType() {
        return chemotherapyType;
    }

    public void setChemotherapyType(String chemotherapyType) {
        this.chemotherapyType = chemotherapyType;
    }

    @Basic
    @Column(name = "regimen_description")
    @Type(type = "text")
    public String getRegimenDescription() {
        return regimenDescription;
    }

    public void setRegimenDescription(String regimenDescription) {
        this.regimenDescription = regimenDescription;
    }

    @Basic
    @Column(name = "cancer_detail")
    @Type(type = "text")
    public String getCancerDetail() {
        return cancerDetail;
    }

    public void setCancerDetail(String cancerDetail) {
        this.cancerDetail = cancerDetail;
    }

    @Basic
    @Column(name = "created_at")
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way")
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
    }

    @Basic
    @Column(name = "created_by_name")
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Basic
    @Column(name = "check_state")
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Basic
    @Column(name = "approval_agency")
    public String getApprovalAgency() {
        return approvalAgency;
    }

    public void setApprovalAgency(String approvalAgency) {
        this.approvalAgency = approvalAgency;
    }

    @Basic
    @Column(name = "approval_link")
    public String getApprovalLink() {
        return approvalLink;
    }

    public void setApprovalLink(String approvalLink) {
        this.approvalLink = approvalLink;
    }

    @Basic
    @Column(name = "chemotherapy_name")
    public String getChemotherapyName() {
        return chemotherapyName;
    }

    public void setChemotherapyName(String chemotherapyName) {
        this.chemotherapyName = chemotherapyName;
    }

    @Basic
    @Column(name = "disease_name")
    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlan that = (MedicationPlan) o;
        return Objects.equals(medicationPlanKey, that.medicationPlanKey) &&
                Objects.equals(medicinePlanId, that.medicinePlanId) &&
                Objects.equals(regimenName, that.regimenName) &&
                Objects.equals(programNameC, that.programNameC) &&
                Objects.equals(aliasName, that.aliasName) &&
                Objects.equals(chemotherapyType, that.chemotherapyType) &&
                Objects.equals(regimenDescription, that.regimenDescription) &&
                Objects.equals(cancerDetail, that.cancerDetail) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(createdWay, that.createdWay) &&
                Objects.equals(createdByName, that.createdByName) &&
                Objects.equals(checkState, that.checkState) &&
                Objects.equals(approvalAgency, that.approvalAgency) &&
                Objects.equals(approvalLink, that.approvalLink) &&
                Objects.equals(chemotherapyName, that.chemotherapyName) &&
                Objects.equals(diseaseName, that.diseaseName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(medicationPlanKey, medicinePlanId, regimenName, programNameC, aliasName, chemotherapyType, regimenDescription, cancerDetail, createdAt, createdWay, createdByName, checkState, approvalAgency, approvalLink, chemotherapyName, diseaseName);
    }
}
