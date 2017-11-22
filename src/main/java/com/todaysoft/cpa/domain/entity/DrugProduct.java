package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:34
 */
@Entity
@Table(name = "kt_drug_product")
public class DrugProduct {
    private String productKey;
    private String productName;
    @JSONField(name = "name")
    private String productNameEn;
    @JSONField(name = "dosageForm")
    private String dosageForm;
    private String drugAttribute;
    private Long createdAt;
    private Integer createdWay;
    private String createdByName;
    private Integer checkState;

    @Id
    @Column(name = "product_key", nullable = false, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "product_name", nullable = true, length = 255)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "product_name_en", nullable = true, length = 255)
    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
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
    @Column(name = "drug_attribute", nullable = true, length = 50)
    public String getDrugAttribute() {
        return drugAttribute;
    }

    public void setDrugAttribute(String drugAttribute) {
        this.drugAttribute = drugAttribute;
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
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

        DrugProduct that = (DrugProduct) o;

        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        if (productNameEn != null ? !productNameEn.equals(that.productNameEn) : that.productNameEn != null)
            return false;
        if (dosageForm != null ? !dosageForm.equals(that.dosageForm) : that.dosageForm != null) return false;
        if (drugAttribute != null ? !drugAttribute.equals(that.drugAttribute) : that.drugAttribute != null)
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
        result = 31 * result + (dosageForm != null ? dosageForm.hashCode() : 0);
        result = 31 * result + (drugAttribute != null ? drugAttribute.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
