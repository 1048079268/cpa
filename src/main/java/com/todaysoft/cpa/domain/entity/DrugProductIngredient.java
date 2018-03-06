package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @author lichy
 * @version 2018/3/6
 * @desc
 */
@Entity
@Table(name = "kt_drug_product_ingredient")
@IdClass(DrugProductIngredientPK.class)
public class DrugProductIngredient {
    private String drugKey;
    private String productKey;

    @Id
    @Column(name = "drug_key")
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "product_key")
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductIngredient that = (DrugProductIngredient) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        return result;
    }
}
