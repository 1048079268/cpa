package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/21 16:51
 */
public class DrugProductIngredientContentPK implements Serializable {
    private String drugKey;
    private String productKey;
    private String approvalKey;

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "product_key", nullable = false, length = 64)
    @Id
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Column(name = "approval_key", nullable = false, length = 64)
    @Id
    public String getApprovalKey() {
        return approvalKey;
    }

    public void setApprovalKey(String approvalKey) {
        this.approvalKey = approvalKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductIngredientContentPK contentPK = (DrugProductIngredientContentPK) o;

        if (drugKey != null ? !drugKey.equals(contentPK.drugKey) : contentPK.drugKey != null) return false;
        if (productKey != null ? !productKey.equals(contentPK.productKey) : contentPK.productKey != null) return false;
        if (approvalKey != null ? !approvalKey.equals(contentPK.approvalKey) : contentPK.approvalKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        result = 31 * result + (approvalKey != null ? approvalKey.hashCode() : 0);
        return result;
    }
}
