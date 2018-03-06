package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author lichy
 * @version 2018/3/6
 * @desc
 */
public class DrugProductIngredientPK implements Serializable {
    private String drugKey;
    private String productKey;

    @Column(name = "drug_key")
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "product_key")
    @Id
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

        DrugProductIngredientPK pk = (DrugProductIngredientPK) o;

        if (drugKey != null ? !drugKey.equals(pk.drugKey) : pk.drugKey != null) return false;
        if (productKey != null ? !productKey.equals(pk.productKey) : pk.productKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        return result;
    }
}
