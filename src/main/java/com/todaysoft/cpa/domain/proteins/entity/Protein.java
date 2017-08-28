package com.todaysoft.cpa.domain.proteins.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:28
 */
@Entity
@Table(name = "kt_protein")
public class Protein {
    private String proteinKey;
    private String geneKey;
    @JSONField(name = "id")
    private String proteinId;
    @JSONField(name = "geneId")
    private Integer geneId;
    @JSONField(name = "entryName")
    private String entryName;
    @JSONField(name = "proteinName")
    private String proteinName;
    @JSONField(name = "mass")
    private Integer mass;
    @JSONField(name = "length")
    private Integer length;
    @JSONField(name = "functionDescription")
    private String functionDescription;
    @JSONField(name = "tissueSpecificity")
    private String tissueSpecificity;
    @JSONField(name = "tissueSpecificityEvidenceId")
    private Integer tissueSpecificityEvidenceId;
    private Long createdAt;
    private Integer createWay;

    @Id
    @Column(name = "protein_key", nullable = false, length = 64)
    public String getProteinKey() {
        return proteinKey;
    }

    public void setProteinKey(String proteinKey) {
        this.proteinKey = proteinKey;
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
    @Column(name = "protein_id", nullable = false, length = 11)
    public String getProteinId() {
        return proteinId;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
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
    @Column(name = "entry_name", nullable = true, length = 200)
    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    @Basic
    @Column(name = "protein_name", nullable = false, length = 200)
    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    @Basic
    @Column(name = "mass", nullable = true)
    public Integer getMass() {
        return mass;
    }

    public void setMass(Integer mass) {
        this.mass = mass;
    }

    @Basic
    @Column(name = "length", nullable = true)
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Basic
    @Column(name = "function_description", nullable = true, length = -1)
    @Type(type = "text")
    public String getFunctionDescription() {
        return functionDescription;
    }

    public void setFunctionDescription(String functionDescription) {
        this.functionDescription = functionDescription;
    }

    @Basic
    @Column(name = "tissue_specificity", nullable = true, length = -1)
    @Type(type = "text")
    public String getTissueSpecificity() {
        return tissueSpecificity;
    }

    public void setTissueSpecificity(String tissueSpecificity) {
        this.tissueSpecificity = tissueSpecificity;
    }

    @Basic
    @Column(name = "tissue_specificity_evidence_id", nullable = true)
    public Integer getTissueSpecificityEvidenceId() {
        return tissueSpecificityEvidenceId;
    }

    public void setTissueSpecificityEvidenceId(Integer tissueSpecificityEvidenceId) {
        this.tissueSpecificityEvidenceId = tissueSpecificityEvidenceId;
    }

    @Basic
    @Column(name = "created_at", nullable = false)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way",nullable = false)
    public Integer getCreateWay() {
        return createWay;
    }

    public void setCreateWay(Integer createWay) {
        this.createWay = createWay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Protein)) return false;

        Protein protein = (Protein) o;

        if (!getProteinKey().equals(protein.getProteinKey())) return false;
        if (!getGeneKey().equals(protein.getGeneKey())) return false;
        if (getProteinId() != null ? !getProteinId().equals(protein.getProteinId()) : protein.getProteinId() != null)
            return false;
        if (getGeneId() != null ? !getGeneId().equals(protein.getGeneId()) : protein.getGeneId() != null) return false;
        if (getEntryName() != null ? !getEntryName().equals(protein.getEntryName()) : protein.getEntryName() != null)
            return false;
        if (getProteinName() != null ? !getProteinName().equals(protein.getProteinName()) : protein.getProteinName() != null)
            return false;
        if (getMass() != null ? !getMass().equals(protein.getMass()) : protein.getMass() != null) return false;
        if (getLength() != null ? !getLength().equals(protein.getLength()) : protein.getLength() != null) return false;
        if (getFunctionDescription() != null ? !getFunctionDescription().equals(protein.getFunctionDescription()) : protein.getFunctionDescription() != null)
            return false;
        if (getTissueSpecificity() != null ? !getTissueSpecificity().equals(protein.getTissueSpecificity()) : protein.getTissueSpecificity() != null)
            return false;
        if (getTissueSpecificityEvidenceId() != null ? !getTissueSpecificityEvidenceId().equals(protein.getTissueSpecificityEvidenceId()) : protein.getTissueSpecificityEvidenceId() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(protein.getCreatedAt()) : protein.getCreatedAt() != null)
            return false;
        return getCreateWay() != null ? getCreateWay().equals(protein.getCreateWay()) : protein.getCreateWay() == null;
    }

    @Override
    public int hashCode() {
        int result = getProteinKey().hashCode();
        result = 31 * result + getGeneKey().hashCode();
        result = 31 * result + (getProteinId() != null ? getProteinId().hashCode() : 0);
        result = 31 * result + (getGeneId() != null ? getGeneId().hashCode() : 0);
        result = 31 * result + (getEntryName() != null ? getEntryName().hashCode() : 0);
        result = 31 * result + (getProteinName() != null ? getProteinName().hashCode() : 0);
        result = 31 * result + (getMass() != null ? getMass().hashCode() : 0);
        result = 31 * result + (getLength() != null ? getLength().hashCode() : 0);
        result = 31 * result + (getFunctionDescription() != null ? getFunctionDescription().hashCode() : 0);
        result = 31 * result + (getTissueSpecificity() != null ? getTissueSpecificity().hashCode() : 0);
        result = 31 * result + (getTissueSpecificityEvidenceId() != null ? getTissueSpecificityEvidenceId().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getCreateWay() != null ? getCreateWay().hashCode() : 0);
        return result;
    }
}
