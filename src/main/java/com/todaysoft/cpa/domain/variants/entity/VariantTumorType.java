package com.todaysoft.cpa.domain.variants.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:53
 */
@Entity
@Table(name = "kt_variant_tumor_type")
public class VariantTumorType {
    private String typeKey;
    private String variantKey;
    private Integer variantId;
    private String site1;
    private String site2;
    private String site3;
    private String site4;
    private String hist1;
    private String hist2;
    private String hist3;
    private String hist4;

    @Id
    @Column(name = "type_key", nullable = false, length = 64)
    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
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
    @Column(name = "site1", nullable = true, length = 200)
    public String getSite1() {
        return site1;
    }

    public void setSite1(String site1) {
        this.site1 = site1;
    }

    @Basic
    @Column(name = "site2", nullable = true, length = 200)
    public String getSite2() {
        return site2;
    }

    public void setSite2(String site2) {
        this.site2 = site2;
    }

    @Basic
    @Column(name = "site3", nullable = true, length = 200)
    public String getSite3() {
        return site3;
    }

    public void setSite3(String site3) {
        this.site3 = site3;
    }

    @Basic
    @Column(name = "site4", nullable = true, length = 200)
    public String getSite4() {
        return site4;
    }

    public void setSite4(String site4) {
        this.site4 = site4;
    }

    @Basic
    @Column(name = "hist1", nullable = true, length = 200)
    public String getHist1() {
        return hist1;
    }

    public void setHist1(String hist1) {
        this.hist1 = hist1;
    }

    @Basic
    @Column(name = "hist2", nullable = true, length = 200)
    public String getHist2() {
        return hist2;
    }

    public void setHist2(String hist2) {
        this.hist2 = hist2;
    }

    @Basic
    @Column(name = "hist3", nullable = true, length = 200)
    public String getHist3() {
        return hist3;
    }

    public void setHist3(String hist3) {
        this.hist3 = hist3;
    }

    @Basic
    @Column(name = "hist4", nullable = true, length = 200)
    public String getHist4() {
        return hist4;
    }

    public void setHist4(String hist4) {
        this.hist4 = hist4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantTumorType that = (VariantTumorType) o;

        if (variantId != null ? !variantId.equals(that.variantId) : that.variantId != null) return false;
        if (typeKey != null ? !typeKey.equals(that.typeKey) : that.typeKey != null) return false;
        if (variantKey != null ? !variantKey.equals(that.variantKey) : that.variantKey != null) return false;
        if (site1 != null ? !site1.equals(that.site1) : that.site1 != null) return false;
        if (site2 != null ? !site2.equals(that.site2) : that.site2 != null) return false;
        if (site3 != null ? !site3.equals(that.site3) : that.site3 != null) return false;
        if (site4 != null ? !site4.equals(that.site4) : that.site4 != null) return false;
        if (hist1 != null ? !hist1.equals(that.hist1) : that.hist1 != null) return false;
        if (hist2 != null ? !hist2.equals(that.hist2) : that.hist2 != null) return false;
        if (hist3 != null ? !hist3.equals(that.hist3) : that.hist3 != null) return false;
        if (hist4 != null ? !hist4.equals(that.hist4) : that.hist4 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeKey != null ? typeKey.hashCode() : 0;
        result = 31 * result + (variantKey != null ? variantKey.hashCode() : 0);
        result = 31 * result + variantId;
        result = 31 * result + (site1 != null ? site1.hashCode() : 0);
        result = 31 * result + (site2 != null ? site2.hashCode() : 0);
        result = 31 * result + (site3 != null ? site3.hashCode() : 0);
        result = 31 * result + (site4 != null ? site4.hashCode() : 0);
        result = 31 * result + (hist1 != null ? hist1.hashCode() : 0);
        result = 31 * result + (hist2 != null ? hist2.hashCode() : 0);
        result = 31 * result + (hist3 != null ? hist3.hashCode() : 0);
        result = 31 * result + (hist4 != null ? hist4.hashCode() : 0);
        return result;
    }
}
