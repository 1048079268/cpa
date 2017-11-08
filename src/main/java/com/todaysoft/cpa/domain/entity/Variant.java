package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:53
 */
@Entity
@Table(name = "kt_variant")
public class Variant {
    private String variantKey;
    private String geneKey;
    @JSONField(name = "id")
    private Integer variantId;
    private Integer geneId;
    private Integer grch;
    private String genomicPosition;
    private String aaMutation;
    private String cdsMutation;
    private String variantType;
    private String cosmicId;
    private String dbsnpId;
    private String mutationEffect;
    private String oncogenicity;
    private String remark;
    private String mutationDetection;
    private Long createdAt;
    private Integer createdWay;
    private String createdByKey;
    private String createdByName="CPA";
    private Integer checkState;
    private String checkReport;
    private String checkedByKey;
    private String checkedByName;
    private String translateByKey;
    private String translateByName;


    @Id
    @Column(name = "variant_key", nullable = false, length = 64)
    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    @Basic
    @Column(name = "gene_key", nullable = true, length = 64)
    public String getGeneKey() {
        return geneKey;
    }

    public void setGeneKey(String geneKey) {
        this.geneKey = geneKey;
    }

    @Basic
    @Column(name = "variant_id", nullable = true)
    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    @Basic
    @Column(name = "gene_id", nullable = true)
    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    @Basic
    @Column(name = "grch", nullable = true)
    public Integer getGrch() {
        return grch;
    }

    public void setGrch(Integer grch) {
        this.grch = grch;
    }

    @Basic
    @Column(name = "genomic_position", nullable = true, length = 200)
    public String getGenomicPosition() {
        return genomicPosition;
    }

    public void setGenomicPosition(String genomicPosition) {
        this.genomicPosition = genomicPosition;
    }

    @Basic
    @Column(name = "aa_mutation", nullable = true, length = 200)
    public String getAaMutation() {
        return aaMutation;
    }

    public void setAaMutation(String aaMutation) {
        this.aaMutation = aaMutation;
    }

    @Basic
    @Column(name = "cds_mutation", nullable = true, length = 200)
    public String getCdsMutation() {
        return cdsMutation;
    }

    public void setCdsMutation(String cdsMutation) {
        this.cdsMutation = cdsMutation;
    }

    @Basic
    @Column(name = "variant_type", nullable = true, length = 200)
    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    @Basic
    @Column(name = "cosmic_id", nullable = true, length = 200)
    public String getCosmicId() {
        return cosmicId;
    }

    public void setCosmicId(String cosmicId) {
        this.cosmicId = cosmicId;
    }

    @Basic
    @Column(name = "dbsnp_id", nullable = true, length = 200)
    public String getDbsnpId() {
        return dbsnpId;
    }

    public void setDbsnpId(String dbsnpId) {
        this.dbsnpId = dbsnpId;
    }

    @Basic
    @Column(name = "mutation_effect", nullable = true, length = 200)
    public String getMutationEffect() {
        return mutationEffect;
    }

    public void setMutationEffect(String mutationEffect) {
        this.mutationEffect = mutationEffect;
    }

    @Basic
    @Column(name = "oncogenicity", nullable = true, length = 200)
    public String getOncogenicity() {
        return oncogenicity;
    }

    public void setOncogenicity(String oncogenicity) {
        this.oncogenicity = oncogenicity;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "mutation_detection", nullable = true, length = -1)
    @Type(type = "text")
    public String getMutationDetection() {
        return mutationDetection;
    }

    public void setMutationDetection(String mutationDetection) {
        this.mutationDetection = mutationDetection;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true)
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
    }

    @Basic
    @Column(name = "created_by_key", nullable = true, length = 32)
    @Type(type = "char")
    public String getCreatedByKey() {
        return createdByKey;
    }

    public void setCreatedByKey(String createdByKey) {
        this.createdByKey = createdByKey;
    }

    @Basic
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Basic
    @Column(name = "check_state", nullable = true)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Basic
    @Column(name = "check_report")
    @Type(type = "text")
    public String getCheckReport() {
        return checkReport;
    }

    public void setCheckReport(String checkReport) {
        this.checkReport = checkReport;
    }

    @Basic
    @Column(name = "checked_by_key", nullable = true, length = 32)
    @Type(type = "char")
    public String getCheckedByKey() {
        return checkedByKey;
    }

    public void setCheckedByKey(String checkedByKey) {
        this.checkedByKey = checkedByKey;
    }

    @Basic
    @Column(name = "checked_by_name", nullable = true, length = 20)
    public String getCheckedByName() {
        return checkedByName;
    }

    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }

    @Basic
    @Column(name = "translate_by_key", nullable = true, length = 32)
    @Type(type = "char")
    public String getTranslateByKey() {
        return translateByKey;
    }

    public void setTranslateByKey(String translateByKey) {
        this.translateByKey = translateByKey;
    }

    @Basic
    @Column(name = "translate_by_name", nullable = true, length = 20)
    public String getTranslateByName() {
        return translateByName;
    }

    public void setTranslateByName(String translateByName) {
        this.translateByName = translateByName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variant variant = (Variant) o;

        if (variantKey != null ? !variantKey.equals(variant.variantKey) : variant.variantKey != null) return false;
        if (geneKey != null ? !geneKey.equals(variant.geneKey) : variant.geneKey != null) return false;
        if (variantId != null ? !variantId.equals(variant.variantId) : variant.variantId != null) return false;
        if (geneId != null ? !geneId.equals(variant.geneId) : variant.geneId != null) return false;
        if (grch != null ? !grch.equals(variant.grch) : variant.grch != null) return false;
        if (genomicPosition != null ? !genomicPosition.equals(variant.genomicPosition) : variant.genomicPosition != null)
            return false;
        if (aaMutation != null ? !aaMutation.equals(variant.aaMutation) : variant.aaMutation != null) return false;
        if (cdsMutation != null ? !cdsMutation.equals(variant.cdsMutation) : variant.cdsMutation != null) return false;
        if (variantType != null ? !variantType.equals(variant.variantType) : variant.variantType != null) return false;
        if (cosmicId != null ? !cosmicId.equals(variant.cosmicId) : variant.cosmicId != null) return false;
        if (dbsnpId != null ? !dbsnpId.equals(variant.dbsnpId) : variant.dbsnpId != null) return false;
        if (mutationEffect != null ? !mutationEffect.equals(variant.mutationEffect) : variant.mutationEffect != null)
            return false;
        if (oncogenicity != null ? !oncogenicity.equals(variant.oncogenicity) : variant.oncogenicity != null)
            return false;
        if (remark != null ? !remark.equals(variant.remark) : variant.remark != null) return false;
        if (mutationDetection != null ? !mutationDetection.equals(variant.mutationDetection) : variant.mutationDetection != null)
            return false;
        if (createdAt != null ? !createdAt.equals(variant.createdAt) : variant.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(variant.createdWay) : variant.createdWay != null) return false;
        if (createdByKey != null ? !createdByKey.equals(variant.createdByKey) : variant.createdByKey != null)
            return false;
        if (createdByName != null ? !createdByName.equals(variant.createdByName) : variant.createdByName != null)
            return false;
        if (checkState != null ? !checkState.equals(variant.checkState) : variant.checkState != null) return false;
        if (checkReport != null ? !checkReport.equals(variant.checkReport) : variant.checkReport != null) return false;
        if (checkedByKey != null ? !checkedByKey.equals(variant.checkedByKey) : variant.checkedByKey != null)
            return false;
        if (checkedByName != null ? !checkedByName.equals(variant.checkedByName) : variant.checkedByName != null)
            return false;
        if (translateByKey != null ? !translateByKey.equals(variant.translateByKey) : variant.translateByKey != null)
            return false;
        if (translateByName != null ? !translateByName.equals(variant.translateByName) : variant.translateByName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = variantKey != null ? variantKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
        result = 31 * result + (grch != null ? grch.hashCode() : 0);
        result = 31 * result + (genomicPosition != null ? genomicPosition.hashCode() : 0);
        result = 31 * result + (aaMutation != null ? aaMutation.hashCode() : 0);
        result = 31 * result + (cdsMutation != null ? cdsMutation.hashCode() : 0);
        result = 31 * result + (variantType != null ? variantType.hashCode() : 0);
        result = 31 * result + (cosmicId != null ? cosmicId.hashCode() : 0);
        result = 31 * result + (dbsnpId != null ? dbsnpId.hashCode() : 0);
        result = 31 * result + (mutationEffect != null ? mutationEffect.hashCode() : 0);
        result = 31 * result + (oncogenicity != null ? oncogenicity.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (mutationDetection != null ? mutationDetection.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (createdByKey != null ? createdByKey.hashCode() : 0);
        result = 31 * result + (createdByName != null ? createdByName.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        result = 31 * result + (checkReport != null ? checkReport.hashCode() : 0);
        result = 31 * result + (checkedByKey != null ? checkedByKey.hashCode() : 0);
        result = 31 * result + (checkedByName != null ? checkedByName.hashCode() : 0);
        result = 31 * result + (translateByKey != null ? translateByKey.hashCode() : 0);
        result = 31 * result + (translateByName != null ? translateByName.hashCode() : 0);
        return result;
    }
}
