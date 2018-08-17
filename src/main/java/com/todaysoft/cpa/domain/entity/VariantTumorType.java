package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Objects;

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
    @JSONField(name = "site1")
    private String site1;
    @JSONField(name = "site2")
    private String site2;
    @JSONField(name = "site3")
    private String site3;
    @JSONField(name = "site4")
    private String site4;
    @JSONField(name = "hist1")
    private String hist1;
    @JSONField(name = "hist2")
    private String hist2;
    @JSONField(name = "hist3")
    private String hist3;
    @JSONField(name = "hist4")
    private String hist4;
    private Integer numOfSamples;

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

    @Basic
    @Column(name = "num_of_samples")
    public Integer getNumOfSamples() {
        return numOfSamples;
    }

    public void setNumOfSamples(Integer numOfSamples) {
        this.numOfSamples = numOfSamples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantTumorType)) return false;
        VariantTumorType that = (VariantTumorType) o;
        return Objects.equals(getVariantKey(), that.getVariantKey()) &&
                Objects.equals(getVariantId(), that.getVariantId()) &&
                Objects.equals(getSite1(), that.getSite1()) &&
                Objects.equals(getSite2(), that.getSite2()) &&
                Objects.equals(getSite3(), that.getSite3()) &&
                Objects.equals(getSite4(), that.getSite4()) &&
                Objects.equals(getHist1(), that.getHist1()) &&
                Objects.equals(getHist2(), that.getHist2()) &&
                Objects.equals(getHist3(), that.getHist3()) &&
                Objects.equals(getHist4(), that.getHist4());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getVariantKey(), getVariantId(), getSite1(), getSite2(), getSite3(), getSite4(), getHist1(), getHist2(), getHist3(), getHist4());
    }
}
