package com.todaysoft.cpa.domain.drug.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_product")
public class DrugProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productKey;
    private String drugKey;
    private Integer drugId;
    @JSONField(name = "name")
    private String productName;
    private String tradeName;
    private String drugAttribute;
    @JSONField(name = "dosageForm")
    private String dosageForm;
    @JSONField(name = "route")
    private String productRoute;
    @JSONField(name = "dosageStrength")
    private String dosageStrength;
    private Boolean isMedicalInsurance;
    private String referencePrice;
    @JSONField(name = "labeller")
    private String labeller;
    @JSONField(name = "generic")
    private Boolean generic;
    @JSONField(name = "source")
    private String productSource;
    @JSONField(name = "country")
    private String country;
    @JSONField(name = "unParse")
    private Timestamp marketingStart;
    @JSONField(name = "unParse")
    private Timestamp marketingEnd;
    private String packageImage;
    @JSONField(name = "approved")
    private Boolean approved;
    private Integer checkState;
    private Long createdAt;

    @Id
    @Column(name = "product_key", nullable = false, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "drug_key", nullable = true, length = 64)
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
    @Column(name = "product_name", nullable = true, length = 200)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "trade_name", nullable = true, length = 64)
    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    @Basic
    @Column(name = "drug_attribute", nullable = true, length = 50)
    public String getDrugAttribute() {
        return drugAttribute;
    }

    public void setDrugAttribute(String drugAttribute) {
        this.drugAttribute = drugAttribute;
    }

    @Basic
    @Column(name = "dosage_form", nullable = true, length = 64)
    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    @Basic
    @Column(name = "product_route", nullable = true, length = 200)
    public String getProductRoute() {
        return productRoute;
    }

    public void setProductRoute(String productRoute) {
        this.productRoute = productRoute;
    }

    @Basic
    @Column(name = "dosage_strength", nullable = true, length = 200)
    public String getDosageStrength() {
        return dosageStrength;
    }

    public void setDosageStrength(String dosageStrength) {
        this.dosageStrength = dosageStrength;
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
    @Column(name = "reference_price", nullable = true, length = 16)
    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
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
    @Column(name = "generic", nullable = true)
    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    @Basic
    @Column(name = "product_source", nullable = true, length = 64)
    public String getProductSource() {
        return productSource;
    }

    public void setProductSource(String productSource) {
        this.productSource = productSource;
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
    @Column(name = "package_image", nullable = true, length = 128)
    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    @Basic
    @Column(name = "approved", nullable = true)
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Basic
    @Column(name = "check_state", nullable = false, length = 200)
    public Integer getCheckState() {
        return this.checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Basic
    @Column(name = "created_at", nullable = false, length = 200)
    public Long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugProduct)) return false;

        DrugProduct product = (DrugProduct) o;

        if (!getProductKey().equals(product.getProductKey())) return false;
        if (!getDrugKey().equals(product.getDrugKey())) return false;
        if (getDrugId() != null ? !getDrugId().equals(product.getDrugId()) : product.getDrugId() != null) return false;
        if (getProductName() != null ? !getProductName().equals(product.getProductName()) : product.getProductName() != null)
            return false;
        if (getTradeName() != null ? !getTradeName().equals(product.getTradeName()) : product.getTradeName() != null)
            return false;
        if (getDrugAttribute() != null ? !getDrugAttribute().equals(product.getDrugAttribute()) : product.getDrugAttribute() != null)
            return false;
        if (getDosageForm() != null ? !getDosageForm().equals(product.getDosageForm()) : product.getDosageForm() != null)
            return false;
        if (getProductRoute() != null ? !getProductRoute().equals(product.getProductRoute()) : product.getProductRoute() != null)
            return false;
        if (getDosageStrength() != null ? !getDosageStrength().equals(product.getDosageStrength()) : product.getDosageStrength() != null)
            return false;
        if (getIsMedicalInsurance() != null ? !getIsMedicalInsurance().equals(product.getIsMedicalInsurance()) : product.getIsMedicalInsurance() != null)
            return false;
        if (getReferencePrice() != null ? !getReferencePrice().equals(product.getReferencePrice()) : product.getReferencePrice() != null)
            return false;
        if (getLabeller() != null ? !getLabeller().equals(product.getLabeller()) : product.getLabeller() != null)
            return false;
        if (getGeneric() != null ? !getGeneric().equals(product.getGeneric()) : product.getGeneric() != null)
            return false;
        if (getProductSource() != null ? !getProductSource().equals(product.getProductSource()) : product.getProductSource() != null)
            return false;
        if (getCountry() != null ? !getCountry().equals(product.getCountry()) : product.getCountry() != null)
            return false;
        if (getMarketingStart() != null ? !getMarketingStart().equals(product.getMarketingStart()) : product.getMarketingStart() != null)
            return false;
        if (getMarketingEnd() != null ? !getMarketingEnd().equals(product.getMarketingEnd()) : product.getMarketingEnd() != null)
            return false;
        if (getPackageImage() != null ? !getPackageImage().equals(product.getPackageImage()) : product.getPackageImage() != null)
            return false;
        if (getApproved() != null ? !getApproved().equals(product.getApproved()) : product.getApproved() != null)
            return false;
        if (getCheckState() != null ? !getCheckState().equals(product.getCheckState()) : product.getCheckState() != null)
            return false;
        return getCreatedAt() != null ? getCreatedAt().equals(product.getCreatedAt()) : product.getCreatedAt() == null;
    }

    @Override
    public int hashCode() {
        int result = getProductKey().hashCode();
        result = 31 * result + getDrugKey().hashCode();
        result = 31 * result + (getDrugId() != null ? getDrugId().hashCode() : 0);
        result = 31 * result + (getProductName() != null ? getProductName().hashCode() : 0);
        result = 31 * result + (getTradeName() != null ? getTradeName().hashCode() : 0);
        result = 31 * result + (getDrugAttribute() != null ? getDrugAttribute().hashCode() : 0);
        result = 31 * result + (getDosageForm() != null ? getDosageForm().hashCode() : 0);
        result = 31 * result + (getProductRoute() != null ? getProductRoute().hashCode() : 0);
        result = 31 * result + (getDosageStrength() != null ? getDosageStrength().hashCode() : 0);
        result = 31 * result + (getIsMedicalInsurance() != null ? getIsMedicalInsurance().hashCode() : 0);
        result = 31 * result + (getReferencePrice() != null ? getReferencePrice().hashCode() : 0);
        result = 31 * result + (getLabeller() != null ? getLabeller().hashCode() : 0);
        result = 31 * result + (getGeneric() != null ? getGeneric().hashCode() : 0);
        result = 31 * result + (getProductSource() != null ? getProductSource().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getMarketingStart() != null ? getMarketingStart().hashCode() : 0);
        result = 31 * result + (getMarketingEnd() != null ? getMarketingEnd().hashCode() : 0);
        result = 31 * result + (getPackageImage() != null ? getPackageImage().hashCode() : 0);
        result = 31 * result + (getApproved() != null ? getApproved().hashCode() : 0);
        result = 31 * result + (getCheckState() != null ? getCheckState().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        return result;
    }
}
