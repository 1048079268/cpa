package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:34
 */
@Entity
@Table(name = "kt_drug_product_approval")
public class DrugProductApproval {
    private String approvalKey;
    private String productKey;
    @JSONField(name = "country")
    private String country;
    private String approvalDate;
    private String approvalOrg;
    private String approvalNumber;
    @JSONField(name = "no")
    private Timestamp marketingStart;
    @JSONField(name = "no")
    private Timestamp marketingEnd;
    @JSONField(name = "labeller")
    private String labeller;
    private Boolean isMedicalInsurance;
    private String tradeName;
    private String drugAttribute;
    private Double dosageStrengthValue;
    private String dosageStrengthUnit;
    private String packageImage;
    private Integer packageValue;
    private String packageUnit;
    private String instructionUrl;
    @JSONField(name = "generic")
    private Boolean isCopy;

    @Id
    @Column(name = "approval_key", nullable = false, length = 64)
    public String getApprovalKey() {
        return approvalKey;
    }

    public void setApprovalKey(String approvalKey) {
        this.approvalKey = approvalKey;
    }

    @Basic
    @Column(name = "product_key", nullable = true, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "country", nullable = true, length = 32)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "approval_date", nullable = true, length = 50)
    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    @Basic
    @Column(name = "approval_org", nullable = true, length = 50)
    public String getApprovalOrg() {
        return approvalOrg;
    }

    public void setApprovalOrg(String approvalOrg) {
        this.approvalOrg = approvalOrg;
    }

    @Basic
    @Column(name = "approval_number", nullable = false, length = 100)
    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    @Basic
    @Column(name = "marketing_start", nullable = true)
    public Timestamp getMarketingStart() {
        return marketingStart;
    }

    public void setMarketingStart(Timestamp marketingStart) {
        this.marketingStart = marketingStart;
    }

    @Basic
    @Column(name = "marketing_end", nullable = true)
    public Timestamp getMarketingEnd() {
        return marketingEnd;
    }

    public void setMarketingEnd(Timestamp marketingEnd) {
        this.marketingEnd = marketingEnd;
    }

    @Basic
    @Column(name = "labeller", nullable = true, length = 200)
    public String getLabeller() {
        return labeller;
    }

    public void setLabeller(String labeller) {
        this.labeller = labeller;
    }

    @Basic
    @Column(name = "is_medical_insurance", nullable = true)
    public Boolean getIsMedicalInsurance() {
        return isMedicalInsurance;
    }

    public void setIsMedicalInsurance(Boolean isMedicalInsurance) {
        this.isMedicalInsurance = isMedicalInsurance;
    }

    @Basic
    @Column(name = "is_copy", nullable = true)
    public Boolean getIsCopy() {
        return this.isCopy;
    }

    public void setIsCopy(Boolean isCopy) {
        this.isCopy = isCopy;
    }


    @Basic
    @Column(name = "trade_name", nullable = true, length = 255)
    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    @Basic
    @Column(name = "drug_attribute", nullable = true, length = 255)
    public String getDrugAttribute() {
        return drugAttribute;
    }

    public void setDrugAttribute(String drugAttribute) {
        this.drugAttribute = drugAttribute;
    }

    @Basic
    @Column(name = "dosage_strength_value", nullable = true, precision = 3)
    public Double getDosageStrengthValue() {
        return dosageStrengthValue;
    }

    public void setDosageStrengthValue(Double dosageStrengthValue) {
        this.dosageStrengthValue = dosageStrengthValue;
    }

    @Basic
    @Column(name = "dosage_strength_unit", nullable = true, length = 10)
    public String getDosageStrengthUnit() {
        return dosageStrengthUnit;
    }

    public void setDosageStrengthUnit(String dosageStrengthUnit) {
        this.dosageStrengthUnit = dosageStrengthUnit;
    }

    @Basic
    @Column(name = "package_image", nullable = true, length = 255)
    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    @Basic
    @Column(name = "package_value", nullable = true)
    public Integer getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(Integer packageValue) {
        this.packageValue = packageValue;
    }

    @Basic
    @Column(name = "package_unit", nullable = true, length = 255)
    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }

    @Basic
    @Column(name = "instruction_url", nullable = true, length = -1)
    @Type(type = "text")
    public String getInstructionUrl() {
        return instructionUrl;
    }

    public void setInstructionUrl(String instructionUrl) {
        this.instructionUrl = instructionUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductApproval that = (DrugProductApproval) o;

        if (approvalKey != null ? !approvalKey.equals(that.approvalKey) : that.approvalKey != null) return false;
        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (approvalDate != null ? !approvalDate.equals(that.approvalDate) : that.approvalDate != null) return false;
        if (approvalOrg != null ? !approvalOrg.equals(that.approvalOrg) : that.approvalOrg != null) return false;
        if (approvalNumber != null ? !approvalNumber.equals(that.approvalNumber) : that.approvalNumber != null)
            return false;
        if (marketingStart != null ? !marketingStart.equals(that.marketingStart) : that.marketingStart != null)
            return false;
        if (marketingEnd != null ? !marketingEnd.equals(that.marketingEnd) : that.marketingEnd != null) return false;
        if (labeller != null ? !labeller.equals(that.labeller) : that.labeller != null) return false;
        if (isMedicalInsurance != null ? !isMedicalInsurance.equals(that.isMedicalInsurance) : that.isMedicalInsurance != null)
            return false;
        if (tradeName != null ? !tradeName.equals(that.tradeName) : that.tradeName != null) return false;
        if (drugAttribute != null ? !drugAttribute.equals(that.drugAttribute) : that.drugAttribute != null)
            return false;
        if (dosageStrengthValue != null ? !dosageStrengthValue.equals(that.dosageStrengthValue) : that.dosageStrengthValue != null)
            return false;
        if (dosageStrengthUnit != null ? !dosageStrengthUnit.equals(that.dosageStrengthUnit) : that.dosageStrengthUnit != null)
            return false;
        if (packageImage != null ? !packageImage.equals(that.packageImage) : that.packageImage != null) return false;
        if (packageValue != null ? !packageValue.equals(that.packageValue) : that.packageValue != null) return false;
        if (packageUnit != null ? !packageUnit.equals(that.packageUnit) : that.packageUnit != null) return false;
        if (instructionUrl != null ? !instructionUrl.equals(that.instructionUrl) : that.instructionUrl != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = approvalKey != null ? approvalKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (approvalDate != null ? approvalDate.hashCode() : 0);
        result = 31 * result + (approvalOrg != null ? approvalOrg.hashCode() : 0);
        result = 31 * result + (approvalNumber != null ? approvalNumber.hashCode() : 0);
        result = 31 * result + (marketingStart != null ? marketingStart.hashCode() : 0);
        result = 31 * result + (marketingEnd != null ? marketingEnd.hashCode() : 0);
        result = 31 * result + (labeller != null ? labeller.hashCode() : 0);
        result = 31 * result + (isMedicalInsurance != null ? isMedicalInsurance.hashCode() : 0);
        result = 31 * result + (tradeName != null ? tradeName.hashCode() : 0);
        result = 31 * result + (drugAttribute != null ? drugAttribute.hashCode() : 0);
        result = 31 * result + (dosageStrengthValue != null ? dosageStrengthValue.hashCode() : 0);
        result = 31 * result + (dosageStrengthUnit != null ? dosageStrengthUnit.hashCode() : 0);
        result = 31 * result + (packageImage != null ? packageImage.hashCode() : 0);
        result = 31 * result + (packageValue != null ? packageValue.hashCode() : 0);
        result = 31 * result + (packageUnit != null ? packageUnit.hashCode() : 0);
        result = 31 * result + (instructionUrl != null ? instructionUrl.hashCode() : 0);
        return result;
    }
}
