package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:39
 */
@Entity
@Table(name = "kt_gene")
public class Gene {
    private String geneKey;
    @JSONField(name = "id")
    private Integer geneId;
    @JSONField(name = "geneType")
    private String geneType;
    @JSONField(name = "geneSymbol")
    private String geneSymbol;
    @JSONField(name = "geneFullName")
    private String geneFullName;
    @JSONField(name = "entrezGeneSummary")
    private String entrezGeneSummary;
    @JSONField(name = "cytogeneticBand")
    private String cytogeneticBand;
    @JSONField(name = "hasCosmicMutations")
    private Boolean hasCosmicMutations;
    @JSONField(name = "cancerGene")
    private String cancerGene;
    @JSONField(name = "notParse")
    private String otherNames;
    @JSONField(name = "notParse")
    private String theAlias;
    private Long createAt;
    private Integer createWay;
    private String createdByName="CPA";

    @Basic
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Id
    @Column(name = "gene_key", nullable = false, length = 64)
    public String getGeneKey() {
        return geneKey;
    }

    public void setGeneKey(String geneKey) {
        this.geneKey = geneKey;
    }

    @Basic
    @Column(name = "gene_id", nullable = false)
    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    @Basic
    @Column(name = "gene_type", nullable = true, length = 64)
    public String getGeneType() {
        return geneType;
    }

    public void setGeneType(String geneType) {
        this.geneType = geneType;
    }

    @Basic
    @Column(name = "gene_symbol", nullable = false, length = 200)
    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    @Basic
    @Column(name = "gene_full_name", nullable = false, length = 200)
    public String getGeneFullName() {
        return geneFullName;
    }

    public void setGeneFullName(String geneFullName) {
        this.geneFullName = geneFullName;
    }

    @Basic
    @Column(name = "entrez_gene_summary", nullable = true, length = -1)
    @Type(type = "text")
    public String getEntrezGeneSummary() {
        return entrezGeneSummary;
    }

    public void setEntrezGeneSummary(String entrezGeneSummary) {
        this.entrezGeneSummary = entrezGeneSummary;
    }

    @Basic
    @Column(name = "cytogenetic_band", nullable = true, length = 200)
    public String getCytogeneticBand() {
        return cytogeneticBand;
    }

    public void setCytogeneticBand(String cytogeneticBand) {
        this.cytogeneticBand = cytogeneticBand;
    }

    @Basic
    @Column(name = "has_cosmic_mutations", nullable = true)
    public Boolean getHasCosmicMutations() {
        return hasCosmicMutations;
    }

    public void setHasCosmicMutations(Boolean hasCosmicMutations) {
        this.hasCosmicMutations = hasCosmicMutations;
    }

    @Basic
    @Column(name = "cancer_gene", nullable = true, length = 64)
    public String getCancerGene() {
        return cancerGene;
    }

    public void setCancerGene(String cancerGene) {
        this.cancerGene = cancerGene;
    }

    @Basic
    @Column(name = "created_at", nullable = false, length = 64)
    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }


    @Basic
    @Column(name = "created_way",nullable = false)
    public Integer getCreateWay() {
        return createWay;
    }

    public void setCreateWay(Integer createWay) {
        this.createWay = createWay;
    }

    @Basic
    @Column(name = "other_names",nullable = true,length = -1)
    @Type(type = "text")
    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    @Basic
    @Column(name = "the_alias",nullable = true,length = -1)
    @Type(type = "text")
    public String getTheAlias() {
        return theAlias;
    }

    public void setTheAlias(String theAlias) {
        this.theAlias = theAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gene)) return false;

        Gene gene = (Gene) o;

        if (!getGeneKey().equals(gene.getGeneKey())) return false;
        if (getGeneId() != null ? !getGeneId().equals(gene.getGeneId()) : gene.getGeneId() != null) return false;
        if (getGeneType() != null ? !getGeneType().equals(gene.getGeneType()) : gene.getGeneType() != null)
            return false;
        if (getGeneSymbol() != null ? !getGeneSymbol().equals(gene.getGeneSymbol()) : gene.getGeneSymbol() != null)
            return false;
        if (getGeneFullName() != null ? !getGeneFullName().equals(gene.getGeneFullName()) : gene.getGeneFullName() != null)
            return false;
        if (getEntrezGeneSummary() != null ? !getEntrezGeneSummary().equals(gene.getEntrezGeneSummary()) : gene.getEntrezGeneSummary() != null)
            return false;
        if (getCytogeneticBand() != null ? !getCytogeneticBand().equals(gene.getCytogeneticBand()) : gene.getCytogeneticBand() != null)
            return false;
        if (getHasCosmicMutations() != null ? !getHasCosmicMutations().equals(gene.getHasCosmicMutations()) : gene.getHasCosmicMutations() != null)
            return false;
        if (getCancerGene() != null ? !getCancerGene().equals(gene.getCancerGene()) : gene.getCancerGene() != null)
            return false;
        if (getCreateAt() != null ? !getCreateAt().equals(gene.getCreateAt()) : gene.getCreateAt() != null)
            return false;
        return getCreateWay() != null ? getCreateWay().equals(gene.getCreateWay()) : gene.getCreateWay() == null;
    }

    @Override
    public int hashCode() {
        int result = getGeneKey().hashCode();
        result = 31 * result + (getGeneId() != null ? getGeneId().hashCode() : 0);
        result = 31 * result + (getGeneType() != null ? getGeneType().hashCode() : 0);
        result = 31 * result + (getGeneSymbol() != null ? getGeneSymbol().hashCode() : 0);
        result = 31 * result + (getGeneFullName() != null ? getGeneFullName().hashCode() : 0);
        result = 31 * result + (getEntrezGeneSummary() != null ? getEntrezGeneSummary().hashCode() : 0);
        result = 31 * result + (getCytogeneticBand() != null ? getCytogeneticBand().hashCode() : 0);
        result = 31 * result + (getHasCosmicMutations() != null ? getHasCosmicMutations().hashCode() : 0);
        result = 31 * result + (getCancerGene() != null ? getCancerGene().hashCode() : 0);
        result = 31 * result + (getCreateAt() != null ? getCreateAt().hashCode() : 0);
        result = 31 * result + (getCreateWay() != null ? getCreateWay().hashCode() : 0);
        return result;
    }
}
