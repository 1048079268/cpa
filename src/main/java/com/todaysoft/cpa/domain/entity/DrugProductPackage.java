package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author lichy
 * @version 2018/3/6
 * @desc
 */
@Entity
@Table(name = "kt_drug_product_package", schema = "project_kb_en", catalog = "")
public class DrugProductPackage {
    private String packageKey;
    private String productKey;
    private String packageValue;

    @Id
    @Column(name = "package_key")
    public String getPackageKey() {
        return packageKey;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
    }

    @Basic
    @Column(name = "product_key")
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "package_value")
    @Type(type = "text")
    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductPackage that = (DrugProductPackage) o;

        if (packageKey != null ? !packageKey.equals(that.packageKey) : that.packageKey != null) return false;
        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (packageValue != null ? !packageValue.equals(that.packageValue) : that.packageValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = packageKey != null ? packageKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        result = 31 * result + (packageValue != null ? packageValue.hashCode() : 0);
        return result;
    }
}
