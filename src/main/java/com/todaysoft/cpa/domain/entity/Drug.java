package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug")
public class Drug implements Serializable{
    private static final long serialVersionUID = 1L;
    private String drugKey;
    @JSONField(name = "id")
    private Integer drugId;
    private String genericName;
    @JSONField(name = "name")
    private String nameEn;
    private String nameChinese;
    @JSONField(name = "oncodrug")
    private Boolean oncoDrug;
    @JSONField(name = "description")
    private String description;
    private String drugType;
    @JSONField(name = "unParse")
    private String chemicalFormula;
    @JSONField(name = "unParse")
    private String otherNames;
    @JSONField(name = "unParse")
    private String molecularWeight;
    @JSONField(name = "mechanismOfAction")
    private String mechanismOfAction;
    @JSONField(name = "toxicity")
    private String toxicity;
    @JSONField(name = "indication")
    private String structuredIndicationDesc;
    @JSONField(name = "absorption")
    private String absorption;
    private String attention;
    @JSONField(name = "volumeOfDistribution")
    private String volumeOfDistribution;
    @JSONField(name = "proteinBinding")
    private String proteinBinding;
    private String majorMetabolicSites;
    @JSONField(name = "halfLife")
    private String halfLife;
    @JSONField(name = "clearance")
    private String clearance;
    @JSONField(name = "pharmacodynamics")
    private String pharmacodynamics;
    @JSONField(name = "fdaLabel")
    private String fdaLabel;
    @JSONField(name = "routeOfElimination")
    private String routeOfElimination;
    private String cpicLevel;
    private String pharmGkbEvidenceLevel;
    private String publishedGuidance;
    private String summary;
    private Integer researchPhase;
    private Integer secondPhase;
    private Integer checkState;
    @JSONField(name = "unParse")
    private Long createdAt;
    private Integer createWay;
    private String createdByName;

    @Basic
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    @Column(name = "drug_id", nullable = false)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "generic_name", nullable = true, length = 500)
    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    @Basic
    @Column(name = "name_en", nullable = true, length = 500)
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Basic
    @Column(name = "name_chinese", nullable = true, length = 500)
    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    @Basic
    @Column(name = "onco_drug", nullable = true)
    public Boolean getOncoDrug() {
        return oncoDrug;
    }

    public void setOncoDrug(Boolean oncoDrug) {
        this.oncoDrug = oncoDrug;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    @Type(type = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "drug_type", nullable = true, length = 50)
    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    @Basic
    @Column(name = "chemical_formula", nullable = true, length = 200)
    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public void setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
    }

    @Basic
    @Column(name = "molecular_weight", nullable = true, length = 32)
    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    @Basic
    @Column(name = "mechanism_of_action", nullable = true, length = -1)
    @Type(type = "text")
    public String getMechanismOfAction() {
        return mechanismOfAction;
    }

    public void setMechanismOfAction(String mechanismOfAction) {
        this.mechanismOfAction = mechanismOfAction;
    }

    @Basic
    @Column(name = "toxicity", nullable = true, length = -1)
    @Type(type = "text")
    public String getToxicity() {
        return toxicity;
    }

    public void setToxicity(String toxicity) {
        this.toxicity = toxicity;
    }

    @Basic
    @Column(name = "structured_indication_desc", nullable = true, length = -1)
    @Type(type = "text")
    public String getStructuredIndicationDesc() {
        return structuredIndicationDesc;
    }

    public void setStructuredIndicationDesc(String structuredIndicationDesc) {
        this.structuredIndicationDesc = structuredIndicationDesc;
    }

    @Basic
    @Column(name = "absorption", nullable = true, length = -1)
    @Type(type = "text")
    public String getAbsorption() {
        return absorption;
    }

    public void setAbsorption(String absorption) {
        this.absorption = absorption;
    }

    @Basic
    @Column(name = "attention", nullable = true, length = -1)
    @Type(type = "text")
    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    @Basic
    @Column(name = "volume_of_distribution", nullable = true, length = -1)
    @Type(type = "text")
    public String getVolumeOfDistribution() {
        return volumeOfDistribution;
    }

    public void setVolumeOfDistribution(String volumeOfDistribution) {
        this.volumeOfDistribution = volumeOfDistribution;
    }

    @Basic
    @Column(name = "protein_binding", nullable = true, length = -1)
    @Type(type = "text")
    public String getProteinBinding() {
        return proteinBinding;
    }

    public void setProteinBinding(String proteinBinding) {
        this.proteinBinding = proteinBinding;
    }

    @Basic
    @Column(name = "major_metabolic_sites", nullable = true, length = 64)
    public String getMajorMetabolicSites() {
        return majorMetabolicSites;
    }

    public void setMajorMetabolicSites(String majorMetabolicSites) {
        this.majorMetabolicSites = majorMetabolicSites;
    }

    @Basic
    @Column(name = "half_life", nullable = true, length = -1)
    @Type(type = "text")
    public String getHalfLife() {
        return halfLife;
    }

    public void setHalfLife(String halfLife) {
        this.halfLife = halfLife;
    }

    @Basic
    @Column(name = "clearance", nullable = true, length = -1)
    @Type(type = "text")
    public String getClearance() {
        return clearance;
    }

    public void setClearance(String clearance) {
        this.clearance = clearance;
    }

    @Basic
    @Column(name = "pharmacodynamics", nullable = true, length = -1)
    @Type(type = "text")
    public String getPharmacodynamics() {
        return pharmacodynamics;
    }

    public void setPharmacodynamics(String pharmacodynamics) {
        this.pharmacodynamics = pharmacodynamics;
    }

    @Basic
    @Column(name = "fda_label", nullable = true, length = 200)
    public String getFdaLabel() {
        return fdaLabel;
    }

    public void setFdaLabel(String fdaLabel) {
        this.fdaLabel = fdaLabel;
    }

    @Basic
    @Column(name = "route_of_elimination", nullable = true, length = -1)
    @Type(type = "text")
    public String getRouteOfElimination() {
        return routeOfElimination;
    }

    public void setRouteOfElimination(String routeOfElimination) {
        this.routeOfElimination = routeOfElimination;
    }

    @Basic
    @Column(name = "cpic_level", nullable = true, length = 64)
    public String getCpicLevel() {
        return cpicLevel;
    }

    public void setCpicLevel(String cpicLevel) {
        this.cpicLevel = cpicLevel;
    }

    @Basic
    @Column(name = "pharm_gkb_evidence_level", nullable = true, length = 64)
    public String getPharmGkbEvidenceLevel() {
        return pharmGkbEvidenceLevel;
    }

    public void setPharmGkbEvidenceLevel(String pharmGkbEvidenceLevel) {
        this.pharmGkbEvidenceLevel = pharmGkbEvidenceLevel;
    }

    @Basic
    @Column(name = "published_guidance", nullable = true, length = -1)
    @Type(type = "text")
    public String getPublishedGuidance() {
        return publishedGuidance;
    }

    public void setPublishedGuidance(String publishedGuidance) {
        this.publishedGuidance = publishedGuidance;
    }

    @Basic
    @Column(name = "summary", nullable = true, length = -1)
    @Type(type = "text")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "research_phase", nullable = true)
    public Integer getResearchPhase() {
        return researchPhase;
    }

    public void setResearchPhase(Integer researchPhase) {
        this.researchPhase = researchPhase;
    }

    @Basic
    @Column(name = "other_names", nullable = true ,length = -1)
    @Type(type = "text")
    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    @Basic
    @Column(name = "second_phase", nullable = true)
    public Integer getSecondPhase() {
        return secondPhase;
    }

    public void setSecondPhase(Integer secondPhase) {
        this.secondPhase = secondPhase;
    }


    @Basic
    @Column(name = "check_state", nullable = false, length = 200)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Basic
    @Column(name = "created_at", nullable = false, length = 200)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way",nullable = false)
    public Integer getCreateWay() {
        return createWay;
    }

    public void setCreateWay(Integer createWay) {
        this.createWay = createWay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drug)) return false;

        Drug drug = (Drug) o;

        if (!getDrugKey().equals(drug.getDrugKey())) return false;
        if (getDrugId() != null ? !getDrugId().equals(drug.getDrugId()) : drug.getDrugId() != null) return false;
        if (getGenericName() != null ? !getGenericName().equals(drug.getGenericName()) : drug.getGenericName() != null)
            return false;
        if (getNameEn() != null ? !getNameEn().equals(drug.getNameEn()) : drug.getNameEn() != null) return false;
        if (getNameChinese() != null ? !getNameChinese().equals(drug.getNameChinese()) : drug.getNameChinese() != null)
            return false;
        if (getOncoDrug() != null ? !getOncoDrug().equals(drug.getOncoDrug()) : drug.getOncoDrug() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(drug.getDescription()) : drug.getDescription() != null)
            return false;
        if (getDrugType() != null ? !getDrugType().equals(drug.getDrugType()) : drug.getDrugType() != null)
            return false;
        if (getChemicalFormula() != null ? !getChemicalFormula().equals(drug.getChemicalFormula()) : drug.getChemicalFormula() != null)
            return false;
        if (getMolecularWeight() != null ? !getMolecularWeight().equals(drug.getMolecularWeight()) : drug.getMolecularWeight() != null)
            return false;
        if (getMechanismOfAction() != null ? !getMechanismOfAction().equals(drug.getMechanismOfAction()) : drug.getMechanismOfAction() != null)
            return false;
        if (getToxicity() != null ? !getToxicity().equals(drug.getToxicity()) : drug.getToxicity() != null)
            return false;
        if (getStructuredIndicationDesc() != null ? !getStructuredIndicationDesc().equals(drug.getStructuredIndicationDesc()) : drug.getStructuredIndicationDesc() != null)
            return false;
        if (getAbsorption() != null ? !getAbsorption().equals(drug.getAbsorption()) : drug.getAbsorption() != null)
            return false;
        if (getAttention() != null ? !getAttention().equals(drug.getAttention()) : drug.getAttention() != null)
            return false;
        if (getVolumeOfDistribution() != null ? !getVolumeOfDistribution().equals(drug.getVolumeOfDistribution()) : drug.getVolumeOfDistribution() != null)
            return false;
        if (getProteinBinding() != null ? !getProteinBinding().equals(drug.getProteinBinding()) : drug.getProteinBinding() != null)
            return false;
        if (getMajorMetabolicSites() != null ? !getMajorMetabolicSites().equals(drug.getMajorMetabolicSites()) : drug.getMajorMetabolicSites() != null)
            return false;
        if (getHalfLife() != null ? !getHalfLife().equals(drug.getHalfLife()) : drug.getHalfLife() != null)
            return false;
        if (getClearance() != null ? !getClearance().equals(drug.getClearance()) : drug.getClearance() != null)
            return false;
        if (getPharmacodynamics() != null ? !getPharmacodynamics().equals(drug.getPharmacodynamics()) : drug.getPharmacodynamics() != null)
            return false;
        if (getFdaLabel() != null ? !getFdaLabel().equals(drug.getFdaLabel()) : drug.getFdaLabel() != null)
            return false;
        if (getRouteOfElimination() != null ? !getRouteOfElimination().equals(drug.getRouteOfElimination()) : drug.getRouteOfElimination() != null)
            return false;
        if (getCpicLevel() != null ? !getCpicLevel().equals(drug.getCpicLevel()) : drug.getCpicLevel() != null)
            return false;
        if (getPharmGkbEvidenceLevel() != null ? !getPharmGkbEvidenceLevel().equals(drug.getPharmGkbEvidenceLevel()) : drug.getPharmGkbEvidenceLevel() != null)
            return false;
        if (getPublishedGuidance() != null ? !getPublishedGuidance().equals(drug.getPublishedGuidance()) : drug.getPublishedGuidance() != null)
            return false;
        if (getSummary() != null ? !getSummary().equals(drug.getSummary()) : drug.getSummary() != null) return false;
        if (getResearchPhase() != null ? !getResearchPhase().equals(drug.getResearchPhase()) : drug.getResearchPhase() != null)
            return false;
        if (getSecondPhase() != null ? !getSecondPhase().equals(drug.getSecondPhase()) : drug.getSecondPhase() != null)
            return false;
        if (getCheckState() != null ? !getCheckState().equals(drug.getCheckState()) : drug.getCheckState() != null)
            return false;
        return getCreatedAt() != null ? getCreatedAt().equals(drug.getCreatedAt()) : drug.getCreatedAt() == null;
    }

    @Override
    public int hashCode() {
        int result = getDrugKey().hashCode();
        result = 31 * result + (getDrugId() != null ? getDrugId().hashCode() : 0);
        result = 31 * result + (getGenericName() != null ? getGenericName().hashCode() : 0);
        result = 31 * result + (getNameEn() != null ? getNameEn().hashCode() : 0);
        result = 31 * result + (getNameChinese() != null ? getNameChinese().hashCode() : 0);
        result = 31 * result + (getOncoDrug() != null ? getOncoDrug().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getDrugType() != null ? getDrugType().hashCode() : 0);
        result = 31 * result + (getChemicalFormula() != null ? getChemicalFormula().hashCode() : 0);
        result = 31 * result + (getMolecularWeight() != null ? getMolecularWeight().hashCode() : 0);
        result = 31 * result + (getMechanismOfAction() != null ? getMechanismOfAction().hashCode() : 0);
        result = 31 * result + (getToxicity() != null ? getToxicity().hashCode() : 0);
        result = 31 * result + (getStructuredIndicationDesc() != null ? getStructuredIndicationDesc().hashCode() : 0);
        result = 31 * result + (getAbsorption() != null ? getAbsorption().hashCode() : 0);
        result = 31 * result + (getAttention() != null ? getAttention().hashCode() : 0);
        result = 31 * result + (getVolumeOfDistribution() != null ? getVolumeOfDistribution().hashCode() : 0);
        result = 31 * result + (getProteinBinding() != null ? getProteinBinding().hashCode() : 0);
        result = 31 * result + (getMajorMetabolicSites() != null ? getMajorMetabolicSites().hashCode() : 0);
        result = 31 * result + (getHalfLife() != null ? getHalfLife().hashCode() : 0);
        result = 31 * result + (getClearance() != null ? getClearance().hashCode() : 0);
        result = 31 * result + (getPharmacodynamics() != null ? getPharmacodynamics().hashCode() : 0);
        result = 31 * result + (getFdaLabel() != null ? getFdaLabel().hashCode() : 0);
        result = 31 * result + (getRouteOfElimination() != null ? getRouteOfElimination().hashCode() : 0);
        result = 31 * result + (getCpicLevel() != null ? getCpicLevel().hashCode() : 0);
        result = 31 * result + (getPharmGkbEvidenceLevel() != null ? getPharmGkbEvidenceLevel().hashCode() : 0);
        result = 31 * result + (getPublishedGuidance() != null ? getPublishedGuidance().hashCode() : 0);
        result = 31 * result + (getSummary() != null ? getSummary().hashCode() : 0);
        result = 31 * result + (getResearchPhase() != null ? getResearchPhase().hashCode() : 0);
        result = 31 * result + (getSecondPhase() != null ? getSecondPhase().hashCode() : 0);
        result = 31 * result + (getCheckState() != null ? getCheckState().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        return result;
    }
}
