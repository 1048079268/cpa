package com.todaysoft.cpa.domain.gene.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:39
 */
@Entity
@Table(name = "kt_gene_external_id")
public class GeneExternalId {
    private String geneExternalIdKey;
    private String geneKey;
    private Integer geneId;
    @JSONField(name = "source")
    private String source;
    @JSONField(name = "id")
    private String sourceId;

    @Id
    @Column(name = "gene_external_id_key", nullable = false, length = 64)
    public String getGeneExternalIdKey() {
        return geneExternalIdKey;
    }

    public void setGeneExternalIdKey(String geneExternalIdKey) {
        this.geneExternalIdKey = geneExternalIdKey;
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
    @Column(name = "source", nullable = false, length = 32)
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

        GeneExternalId that = (GeneExternalId) o;

        if (geneExternalIdKey != null ? !geneExternalIdKey.equals(that.geneExternalIdKey) : that.geneExternalIdKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(that.geneKey) : that.geneKey != null) return false;
        if (geneId != null ? !geneId.equals(that.geneId) : that.geneId != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = geneExternalIdKey != null ? geneExternalIdKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        return result;
    }
}
