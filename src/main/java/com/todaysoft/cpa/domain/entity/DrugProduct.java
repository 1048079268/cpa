package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author lichy
 * @version 2018/3/6
 * @desc
 */
@Entity
@Table(name = "kt_drug_product")
public class DrugProduct {
    private String productKey;
    private String productName;
    @JSONField(name = "name")
    private String productNameEn;
    @JSONField(name = "labeller")
    private String labeller;
    private String tradeName;
    private String instructionUrl;
    @JSONField(name = "noParse")
    private Timestamp marketingStart;
    @JSONField(name = "noParse")
    private Timestamp marketingEnd;
    @JSONField(name = "generic")
    private Boolean isCopy;
    private Boolean isMedicalInsurance;
    private String drugAttribute;
    @JSONField(name = "dosageForm")
    private String dosageForm;
    @JSONField(name = "dosageStrength")
    private String dosageStrength;
    @JSONField(name = "country")
    private String approvalCountry;
    private String approvalDate;
    private String approvalOrg;
    private String approvalNumber;
    private Long createdAt;
    private Integer createdWay;
    private String createdByName;
    private Integer checkState;

    @Id
    @Column(name = "product_key")
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "product_name_en")
    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    @Basic
    @Column(name = "labeller")
    public String getLabeller() {
        return labeller;
    }

    public void setLabeller(String labeller) {
        this.labeller = labeller;
    }

    @Basic
    @Column(name = "trade_name")
    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    @Basic
    @Column(name = "instruction_url")
    @Type(type = "text")
    public String getInstructionUrl() {
        return instructionUrl;
    }

    public void setInstructionUrl(String instructionUrl) {
        this.instructionUrl = instructionUrl;
    }

    @Basic
    @Column(name = "marketing_start")
    public Timestamp getMarketingStart() {
        return marketingStart;
    }

    public void setMarketingStart(Timestamp marketingStart) {
        this.marketingStart = marketingStart;
    }

    @Basic
    @Column(name = "marketing_end")
    public Timestamp getMarketingEnd() {
        return marketingEnd;
    }

    public void setMarketingEnd(Timestamp marketingEnd) {
        this.marketingEnd = marketingEnd;
    }

    @Basic
    @Column(name = "is_copy")
    public Boolean getCopy() {
        return isCopy;
    }

    public void setCopy(Boolean copy) {
        isCopy = copy;
    }

    @Basic
    @Column(name = "is_medical_insurance")
    public Boolean getMedicalInsurance() {
        return isMedicalInsurance;
    }

    public void setMedicalInsurance(Boolean medicalInsurance) {
        isMedicalInsurance = medicalInsurance;
    }

    @Basic
    @Column(name = "drug_attribute")
    public String getDrugAttribute() {
        return drugAttribute;
    }

    public void setDrugAttribute(String drugAttribute) {
        this.drugAttribute = drugAttribute;
    }

    @Basic
    @Column(name = "dosage_form")
    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    @Basic
    @Column(name = "dosage_strength")
    public String getDosageStrength() {
        return dosageStrength;
    }

    public void setDosageStrength(String dosageStrength) {
        this.dosageStrength = dosageStrength;
    }

    @Basic
    @Column(name = "approval_country")
    public String getApprovalCountry() {
        return approvalCountry;
    }

    public void setApprovalCountry(String approvalCountry) {
        this.approvalCountry = approvalCountry;
    }

    @Basic
    @Column(name = "approval_date")
    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    @Basic
    @Column(name = "approval_org")
    public String getApprovalOrg() {
        return approvalOrg;
    }

    public void setApprovalOrg(String approvalOrg) {
        this.approvalOrg = approvalOrg;
    }

    @Basic
    @Column(name = "approval_number")
    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProduct that = (DrugProduct) o;

        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        if (productNameEn != null ? !productNameEn.equals(that.productNameEn) : that.productNameEn != null)
            return false;
        if (labeller != null ? !labeller.equals(that.labeller) : that.labeller != null) return false;
        if (tradeName != null ? !tradeName.equals(that.tradeName) : that.tradeName != null) return false;
        if (instructionUrl != null ? !instructionUrl.equals(that.instructionUrl) : that.instructionUrl != null)
            return false;
        if (marketingStart != null ? !marketingStart.equals(that.marketingStart) : that.marketingStart != null)
            return false;
        if (marketingEnd != null ? !marketingEnd.equals(that.marketingEnd) : that.marketingEnd != null) return false;
        if (isCopy != null ? !isCopy.equals(that.isCopy) : that.isCopy != null) return false;
        if (isMedicalInsurance != null ? !isMedicalInsurance.equals(that.isMedicalInsurance) : that.isMedicalInsurance != null)
            return false;
        if (drugAttribute != null ? !drugAttribute.equals(that.drugAttribute) : that.drugAttribute != null)
            return false;
        if (dosageForm != null ? !dosageForm.equals(that.dosageForm) : that.dosageForm != null) return false;
        if (dosageStrength != null ? !dosageStrength.equals(that.dosageStrength) : that.dosageStrength != null)
            return false;
        if (approvalCountry != null ? !approvalCountry.equals(that.approvalCountry) : that.approvalCountry != null)
            return false;
        if (approvalDate != null ? !approvalDate.equals(that.approvalDate) : that.approvalDate != null) return false;
        if (approvalOrg != null ? !approvalOrg.equals(that.approvalOrg) : that.approvalOrg != null) return false;
        if (approvalNumber != null ? !approvalNumber.equals(that.approvalNumber) : that.approvalNumber != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(that.createdWay) : that.createdWay != null) return false;
        if (createdByName != null ? !createdByName.equals(that.createdByName) : that.createdByName != null)
            return false;
        if (checkState != null ? !checkState.equals(that.checkState) : that.checkState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productKey != null ? productKey.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productNameEn != null ? productNameEn.hashCode() : 0);
        result = 31 * result + (labeller != null ? labeller.hashCode() : 0);
        result = 31 * result + (tradeName != null ? tradeName.hashCode() : 0);
        result = 31 * result + (instructionUrl != null ? instructionUrl.hashCode() : 0);
        result = 31 * result + (marketingStart != null ? marketingStart.hashCode() : 0);
        result = 31 * result + (marketingEnd != null ? marketingEnd.hashCode() : 0);
        result = 31 * result + (isCopy != null ? isCopy.hashCode() : 0);
        result = 31 * result + (isMedicalInsurance != null ? isMedicalInsurance.hashCode() : 0);
        result = 31 * result + (drugAttribute != null ? drugAttribute.hashCode() : 0);
        result = 31 * result + (dosageForm != null ? dosageForm.hashCode() : 0);
        result = 31 * result + (dosageStrength != null ? dosageStrength.hashCode() : 0);
        result = 31 * result + (approvalCountry != null ? approvalCountry.hashCode() : 0);
        result = 31 * result + (approvalDate != null ? approvalDate.hashCode() : 0);
        result = 31 * result + (approvalOrg != null ? approvalOrg.hashCode() : 0);
        result = 31 * result + (approvalNumber != null ? approvalNumber.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
