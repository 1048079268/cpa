package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Objects;

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
        if (!(o instanceof GeneExternalId)) return false;
        GeneExternalId that = (GeneExternalId) o;
        return Objects.equals(getGeneKey(), that.getGeneKey()) &&
                Objects.equals(getGeneId(), that.getGeneId()) &&
                Objects.equals(getSource(), that.getSource()) &&
                Objects.equals(getSourceId(), that.getSourceId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getGeneKey(), getGeneId(), getSource(), getSourceId());
    }
}
