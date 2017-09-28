package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:39
 */
@Entity
@Table(name = "kt_gene_alias")
public class GeneAlias {
    private String geneAliasKey;
    private String geneKey;
    private Integer geneId;
    private String geneAlias;

    @Id
    @Column(name = "gene_alias_key", nullable = false, length = 64)
    public String getGeneAliasKey() {
        return geneAliasKey;
    }

    public void setGeneAliasKey(String geneAliasKey) {
        this.geneAliasKey = geneAliasKey;
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
    @Column(name = "gene_id", nullable = false)
    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    @Basic
    @Column(name = "gene_alias", nullable = true, length = 64)
    public String getGeneAlias() {
        return geneAlias;
    }

    public void setGeneAlias(String geneAlias) {
        this.geneAlias = geneAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneAlias geneAlias1 = (GeneAlias) o;

        if (geneAliasKey != null ? !geneAliasKey.equals(geneAlias1.geneAliasKey) : geneAlias1.geneAliasKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(geneAlias1.geneKey) : geneAlias1.geneKey != null) return false;
        if (geneId != null ? !geneId.equals(geneAlias1.geneId) : geneAlias1.geneId != null) return false;
        if (geneAlias != null ? !geneAlias.equals(geneAlias1.geneAlias) : geneAlias1.geneAlias != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = geneAliasKey != null ? geneAliasKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
        result = 31 * result + (geneAlias != null ? geneAlias.hashCode() : 0);
        return result;
    }
}
