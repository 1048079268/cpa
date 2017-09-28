package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:39
 */
@Entity
@Table(name = "kt_gene_other_name")
public class GeneOtherName {
    private String geneOtherNameKey;
    private String geneKey;
    private Integer geneId;
    private String otherName;

    @Id
    @Column(name = "gene_other_name_key", nullable = false, length = 64)
    public String getGeneOtherNameKey() {
        return geneOtherNameKey;
    }

    public void setGeneOtherNameKey(String geneOtherNameKey) {
        this.geneOtherNameKey = geneOtherNameKey;
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
    @Column(name = "other_name", nullable = false, length = 200)
    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneOtherName that = (GeneOtherName) o;

        if (geneOtherNameKey != null ? !geneOtherNameKey.equals(that.geneOtherNameKey) : that.geneOtherNameKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(that.geneKey) : that.geneKey != null) return false;
        if (geneId != null ? !geneId.equals(that.geneId) : that.geneId != null) return false;
        if (otherName != null ? !otherName.equals(that.otherName) : that.otherName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = geneOtherNameKey != null ? geneOtherNameKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
        result = 31 * result + (otherName != null ? otherName.hashCode() : 0);
        return result;
    }
}
