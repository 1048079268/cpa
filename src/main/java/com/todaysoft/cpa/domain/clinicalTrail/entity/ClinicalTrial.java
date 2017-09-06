package com.todaysoft.cpa.domain.clinicalTrail.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
@Entity
@Table(name = "kt_clinical_trial")
public class ClinicalTrial {
    private String clinicalTrialKey;
    @JSONField(name = "id")
    private String clinicalTrialId;
    private String synonyms;
    @JSONField(name = "title")
    private String theTitle;
    @JSONField(name = "status")
    private String theStatus;
    private String theCfda;
    @JSONField(name = "phase")
    private String thePhase;
    @JSONField(name = "type")
    private String theType;
    @JSONField(name = "startDate")
    private String startDate;
    @JSONField(name = "unParse")
    private String countries;
    private String groupCase;
    private String pathologicalState;
    private String existTreatment;
    private String relatedGene;
    private Boolean random;
    private String doubleBlind;
    private String treatmentType;
    private String thePmid;
    private String testCenter;
    private String organization;
    @JSONField(name = "url")
    private String theUrl;
    private String geneDetection;
    private String contract;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

    @Id
    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Basic
    @Column(name = "clinical_trial_id", nullable = true, length = 64)
    public String getClinicalTrialId() {
        return clinicalTrialId;
    }

    public void setClinicalTrialId(String clinicalTrialId) {
        this.clinicalTrialId = clinicalTrialId;
    }

    @Basic
    @Column(name = "synonyms", nullable = true, length = 200)
    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    @Basic
    @Column(name = "the_title", nullable = true, length = -1)
    @Type(type = "text")
    public String getTheTitle() {
        return theTitle;
    }

    public void setTheTitle(String theTitle) {
        this.theTitle = theTitle;
    }

    @Basic
    @Column(name = "the_status", nullable = true, length = 1000)
    public String getTheStatus() {
        return theStatus;
    }

    public void setTheStatus(String theStatus) {
        this.theStatus = theStatus;
    }

    @Basic
    @Column(name = "the_cfda", nullable = true, length = 1000)
    public String getTheCfda() {
        return theCfda;
    }

    public void setTheCfda(String theCfda) {
        this.theCfda = theCfda;
    }

    @Basic
    @Column(name = "the_phase", nullable = true, length = 1000)
    public String getThePhase() {
        return thePhase;
    }

    public void setThePhase(String thePhase) {
        this.thePhase = thePhase;
    }

    @Basic
    @Column(name = "the_type", nullable = true, length = 1000)
    public String getTheType() {
        return theType;
    }

    public void setTheType(String theType) {
        this.theType = theType;
    }

    @Basic
    @Column(name = "start_date", nullable = true, length = 64)
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "countries", nullable = true, length = 64)
    @Type(type = "text")
    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    @Basic
    @Column(name = "group_case", nullable = true, length = 200)
    public String getGroupCase() {
        return groupCase;
    }

    public void setGroupCase(String groupCase) {
        this.groupCase = groupCase;
    }

    @Basic
    @Column(name = "pathological_state", nullable = true, length = 200)
    public String getPathologicalState() {
        return pathologicalState;
    }

    public void setPathologicalState(String pathologicalState) {
        this.pathologicalState = pathologicalState;
    }

    @Basic
    @Column(name = "exist_treatment", nullable = true, length = 200)
    public String getExistTreatment() {
        return existTreatment;
    }

    public void setExistTreatment(String existTreatment) {
        this.existTreatment = existTreatment;
    }

    @Basic
    @Column(name = "related_gene", nullable = true, length = 200)
    public String getRelatedGene() {
        return relatedGene;
    }

    public void setRelatedGene(String relatedGene) {
        this.relatedGene = relatedGene;
    }

    @Basic
    @Column(name = "random", nullable = true)
    public Boolean getRandom() {
        return random;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    @Basic
    @Column(name = "double_blind", nullable = true, length = 16)
    public String getDoubleBlind() {
        return doubleBlind;
    }

    public void setDoubleBlind(String doubleBlind) {
        this.doubleBlind = doubleBlind;
    }

    @Basic
    @Column(name = "treatment_type", nullable = true, length = 16)
    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    @Basic
    @Column(name = "the_pmid", nullable = true, length = 64)
    public String getThePmid() {
        return thePmid;
    }

    public void setThePmid(String thePmid) {
        this.thePmid = thePmid;
    }

    @Basic
    @Column(name = "test_center", nullable = true, length = 64)
    public String getTestCenter() {
        return testCenter;
    }

    public void setTestCenter(String testCenter) {
        this.testCenter = testCenter;
    }

    @Basic
    @Column(name = "organization", nullable = true, length = 64)
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Basic
    @Column(name = "the_url", nullable = true, length = 200)
    public String getTheUrl() {
        return theUrl;
    }

    public void setTheUrl(String theUrl) {
        this.theUrl = theUrl;
    }

    @Basic
    @Column(name = "gene_detection", nullable = true, length = 200)
    public String getGeneDetection() {
        return geneDetection;
    }

    public void setGeneDetection(String geneDetection) {
        this.geneDetection = geneDetection;
    }

    @Basic
    @Column(name = "contract", nullable = true, length = 255)
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
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
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalTrial that = (ClinicalTrial) o;

        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (clinicalTrialId != null ? !clinicalTrialId.equals(that.clinicalTrialId) : that.clinicalTrialId != null)
            return false;
        if (synonyms != null ? !synonyms.equals(that.synonyms) : that.synonyms != null) return false;
        if (theTitle != null ? !theTitle.equals(that.theTitle) : that.theTitle != null) return false;
        if (theStatus != null ? !theStatus.equals(that.theStatus) : that.theStatus != null) return false;
        if (theCfda != null ? !theCfda.equals(that.theCfda) : that.theCfda != null) return false;
        if (thePhase != null ? !thePhase.equals(that.thePhase) : that.thePhase != null) return false;
        if (theType != null ? !theType.equals(that.theType) : that.theType != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (countries != null ? !countries.equals(that.countries) : that.countries != null) return false;
        if (groupCase != null ? !groupCase.equals(that.groupCase) : that.groupCase != null) return false;
        if (pathologicalState != null ? !pathologicalState.equals(that.pathologicalState) : that.pathologicalState != null)
            return false;
        if (existTreatment != null ? !existTreatment.equals(that.existTreatment) : that.existTreatment != null)
            return false;
        if (relatedGene != null ? !relatedGene.equals(that.relatedGene) : that.relatedGene != null) return false;
        if (random != null ? !random.equals(that.random) : that.random != null) return false;
        if (doubleBlind != null ? !doubleBlind.equals(that.doubleBlind) : that.doubleBlind != null) return false;
        if (treatmentType != null ? !treatmentType.equals(that.treatmentType) : that.treatmentType != null)
            return false;
        if (thePmid != null ? !thePmid.equals(that.thePmid) : that.thePmid != null) return false;
        if (testCenter != null ? !testCenter.equals(that.testCenter) : that.testCenter != null) return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;
        if (theUrl != null ? !theUrl.equals(that.theUrl) : that.theUrl != null) return false;
        if (geneDetection != null ? !geneDetection.equals(that.geneDetection) : that.geneDetection != null)
            return false;
        if (contract != null ? !contract.equals(that.contract) : that.contract != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(that.createdWay) : that.createdWay != null) return false;
        if (checkState != null ? !checkState.equals(that.checkState) : that.checkState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0;
        result = 31 * result + (clinicalTrialId != null ? clinicalTrialId.hashCode() : 0);
        result = 31 * result + (synonyms != null ? synonyms.hashCode() : 0);
        result = 31 * result + (theTitle != null ? theTitle.hashCode() : 0);
        result = 31 * result + (theStatus != null ? theStatus.hashCode() : 0);
        result = 31 * result + (theCfda != null ? theCfda.hashCode() : 0);
        result = 31 * result + (thePhase != null ? thePhase.hashCode() : 0);
        result = 31 * result + (theType != null ? theType.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
        result = 31 * result + (groupCase != null ? groupCase.hashCode() : 0);
        result = 31 * result + (pathologicalState != null ? pathologicalState.hashCode() : 0);
        result = 31 * result + (existTreatment != null ? existTreatment.hashCode() : 0);
        result = 31 * result + (relatedGene != null ? relatedGene.hashCode() : 0);
        result = 31 * result + (random != null ? random.hashCode() : 0);
        result = 31 * result + (doubleBlind != null ? doubleBlind.hashCode() : 0);
        result = 31 * result + (treatmentType != null ? treatmentType.hashCode() : 0);
        result = 31 * result + (thePmid != null ? thePmid.hashCode() : 0);
        result = 31 * result + (testCenter != null ? testCenter.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + (theUrl != null ? theUrl.hashCode() : 0);
        result = 31 * result + (geneDetection != null ? geneDetection.hashCode() : 0);
        result = 31 * result + (contract != null ? contract.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
