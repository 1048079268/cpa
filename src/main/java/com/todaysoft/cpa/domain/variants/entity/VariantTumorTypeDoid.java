package com.todaysoft.cpa.domain.variants.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:17
 */
@Entity
@Table(name = "kt_variant_tumor_type_doid")
@IdClass(VariantTumorTypeDoidPK.class)
public class VariantTumorTypeDoid {
    private String cancerKey;
    private String typeKey;
    private Integer variantId;
    @JSONField(name = "id")
    private Integer doid;
    private String name;

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Id
    @Column(name = "type_key", nullable = false, length = 64)
    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    @Basic
    @Column(name = "variant_id", nullable = false)
    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    @Basic
    @Column(name = "doid", nullable = true)
    public Integer getDoid() {
        return doid;
    }

    public void setDoid(Integer doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "cancer_name", nullable = true, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantTumorTypeDoid that = (VariantTumorTypeDoid) o;

        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;
        if (typeKey != null ? !typeKey.equals(that.typeKey) : that.typeKey != null) return false;
        if (variantId != null ? !variantId.equals(that.variantId) : that.variantId != null) return false;
        if (doid != null ? !doid.equals(that.doid) : that.doid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cancerKey != null ? cancerKey.hashCode() : 0;
        result = 31 * result + (typeKey != null ? typeKey.hashCode() : 0);
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (doid != null ? doid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
