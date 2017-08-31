package com.todaysoft.cpa.domain.variants.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:17
 */
public class VariantTumorTypeDoidPK implements Serializable {
    private String cancerKey;
    private String typeKey;

    @Column(name = "cancer_key", nullable = false, length = 64)
    @Id
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Column(name = "type_key", nullable = false, length = 64)
    @Id
    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantTumorTypeDoidPK that = (VariantTumorTypeDoidPK) o;

        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;
        if (typeKey != null ? !typeKey.equals(that.typeKey) : that.typeKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cancerKey != null ? cancerKey.hashCode() : 0;
        result = 31 * result + (typeKey != null ? typeKey.hashCode() : 0);
        return result;
    }
}
