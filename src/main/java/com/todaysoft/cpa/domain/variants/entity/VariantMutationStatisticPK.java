package com.todaysoft.cpa.domain.variants.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:23
 */
public class VariantMutationStatisticPK implements Serializable {
    private String cancerKey;
    private String variantKey;

    @Column(name = "cancer_key", nullable = false, length = 64)
    @Id
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Column(name = "variant_key", nullable = false, length = 64)
    @Id
    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantMutationStatisticPK that = (VariantMutationStatisticPK) o;

        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;
        if (variantKey != null ? !variantKey.equals(that.variantKey) : that.variantKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cancerKey != null ? cancerKey.hashCode() : 0;
        result = 31 * result + (variantKey != null ? variantKey.hashCode() : 0);
        return result;
    }
}
