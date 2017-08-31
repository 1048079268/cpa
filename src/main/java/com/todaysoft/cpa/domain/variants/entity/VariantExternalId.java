package com.todaysoft.cpa.domain.variants.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:53
 */
@Entity
@Table(name = "kt_variant_external_id")
public class VariantExternalId {
    private String id;
    private String variantKey;
    private Integer variantId;
    private String source;
    private String sourceId;

    @Id
    @Column(name = "id", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "variant_key", nullable = true, length = 64)
    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
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
    @Column(name = "source", nullable = false, length = 64)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Basic
    @Column(name = "source_id", nullable = false, length = 64)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantExternalId that = (VariantExternalId) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (variantKey != null ? !variantKey.equals(that.variantKey) : that.variantKey != null) return false;
        if (variantId != null ? !variantId.equals(that.variantId) : that.variantId != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (variantKey != null ? variantKey.hashCode() : 0);
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        return result;
    }
}
