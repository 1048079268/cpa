package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/21 16:51
 */
@Entity
@Table(name = "kt_drug_product_ingredient_content")
@IdClass(DrugProductIngredientContentPK.class)
public class DrugProductIngredientContent {
    private String drugKey;
    private String productKey;
    private String approvalKey;
    private Double contentValue;
    private String contentExplain;
    private String contentUnit;
    private Double contentConcentration;
    private Double releaseRateValue;
    private String releaseRateUnit;
    private Double releaseCycleValue;
    private String releaseCycleUnit;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "product_key", nullable = false, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Id
    @Column(name = "approval_key", nullable = false, length = 64)
    public String getApprovalKey() {
        return approvalKey;
    }

    public void setApprovalKey(String approvalKey) {
        this.approvalKey = approvalKey;
    }

    @Basic
    @Column(name = "content_value", nullable = true, precision = 3)
    public Double getContentValue() {
        return contentValue;
    }

    public void setContentValue(Double contentValue) {
        this.contentValue = contentValue;
    }

    @Basic
    @Column(name = "content_unit", nullable = true, length = 255)
    public String getContentUnit() {
        return contentUnit;
    }

    public void setContentUnit(String contentUnit) {
        this.contentUnit = contentUnit;
    }

    @Basic
    @Column(name = "content_concentration", nullable = true, precision = 3)
    public Double getContentConcentration() {
        return contentConcentration;
    }

    public void setContentConcentration(Double contentConcentration) {
        this.contentConcentration = contentConcentration;
    }

    @Basic
    @Column(name = "release_rate_value", nullable = true, precision = 3)
    public Double getReleaseRateValue() {
        return releaseRateValue;
    }

    public void setReleaseRateValue(Double releaseRateValue) {
        this.releaseRateValue = releaseRateValue;
    }

    @Basic
    @Column(name = "release_rate_unit", nullable = true, length = 10)
    public String getReleaseRateUnit() {
        return releaseRateUnit;
    }

    public void setReleaseRateUnit(String releaseRateUnit) {
        this.releaseRateUnit = releaseRateUnit;
    }

    @Basic
    @Column(name = "release_cycle_value", nullable = true, precision = 3)
    public Double getReleaseCycleValue() {
        return releaseCycleValue;
    }

    public void setReleaseCycleValue(Double releaseCycleValue) {
        this.releaseCycleValue = releaseCycleValue;
    }

    @Basic
    @Column(name = "release_cycle_unit", nullable = true, length = 10)
    public String getReleaseCycleUnit() {
        return releaseCycleUnit;
    }

    public void setReleaseCycleUnit(String releaseCycleUnit) {
        this.releaseCycleUnit = releaseCycleUnit;
    }

    @Basic
    @Column(name = "content_explain", nullable = true, length = 255)
    public String getContentExplain() {
        return contentExplain;
    }

    public void setContentExplain(String contentExplain) {
        this.contentExplain = contentExplain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugProductIngredientContent)) return false;

        DrugProductIngredientContent content = (DrugProductIngredientContent) o;

        if (getDrugKey() != null ? !getDrugKey().equals(content.getDrugKey()) : content.getDrugKey() != null)
            return false;
        if (getProductKey() != null ? !getProductKey().equals(content.getProductKey()) : content.getProductKey() != null)
            return false;
        if (getApprovalKey() != null ? !getApprovalKey().equals(content.getApprovalKey()) : content.getApprovalKey() != null)
            return false;
        if (getContentValue() != null ? !getContentValue().equals(content.getContentValue()) : content.getContentValue() != null)
            return false;
        if (getContentExplain() != null ? !getContentExplain().equals(content.getContentExplain()) : content.getContentExplain() != null)
            return false;
        if (getContentUnit() != null ? !getContentUnit().equals(content.getContentUnit()) : content.getContentUnit() != null)
            return false;
        if (getContentConcentration() != null ? !getContentConcentration().equals(content.getContentConcentration()) : content.getContentConcentration() != null)
            return false;
        if (getReleaseRateValue() != null ? !getReleaseRateValue().equals(content.getReleaseRateValue()) : content.getReleaseRateValue() != null)
            return false;
        if (getReleaseRateUnit() != null ? !getReleaseRateUnit().equals(content.getReleaseRateUnit()) : content.getReleaseRateUnit() != null)
            return false;
        if (getReleaseCycleValue() != null ? !getReleaseCycleValue().equals(content.getReleaseCycleValue()) : content.getReleaseCycleValue() != null)
            return false;
        return getReleaseCycleUnit() != null ? getReleaseCycleUnit().equals(content.getReleaseCycleUnit()) : content.getReleaseCycleUnit() == null;
    }

    @Override
    public int hashCode() {
        int result = getDrugKey() != null ? getDrugKey().hashCode() : 0;
        result = 31 * result + (getProductKey() != null ? getProductKey().hashCode() : 0);
        result = 31 * result + (getApprovalKey() != null ? getApprovalKey().hashCode() : 0);
        result = 31 * result + (getContentValue() != null ? getContentValue().hashCode() : 0);
        result = 31 * result + (getContentExplain() != null ? getContentExplain().hashCode() : 0);
        result = 31 * result + (getContentUnit() != null ? getContentUnit().hashCode() : 0);
        result = 31 * result + (getContentConcentration() != null ? getContentConcentration().hashCode() : 0);
        result = 31 * result + (getReleaseRateValue() != null ? getReleaseRateValue().hashCode() : 0);
        result = 31 * result + (getReleaseRateUnit() != null ? getReleaseRateUnit().hashCode() : 0);
        result = 31 * result + (getReleaseCycleValue() != null ? getReleaseCycleValue().hashCode() : 0);
        result = 31 * result + (getReleaseCycleUnit() != null ? getReleaseCycleUnit().hashCode() : 0);
        return result;
    }
}
