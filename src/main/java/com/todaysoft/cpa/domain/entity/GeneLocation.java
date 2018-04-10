package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:39
 */
@Entity
@Table(name = "kt_gene_location")
public class GeneLocation {
    private String geneLocationKey;
    private String geneKey;
    private Integer geneId;
    @JSONField(name = "refAssembly")
    private String refAssembly;
    @JSONField(name = "chromosome")
    private String chromosome;
    @JSONField(name = "start")
    private String geneStart;
    @JSONField(name = "stop")
    private String geneStop;
    @JSONField(name = "size")
    private Integer geneSize;
    @JSONField(name = "orientation")
    private String orientatio;
    @JSONField(name = "exonCount")
    private Integer exoncount;

    @Id
    @Column(name = "gene_location_key", nullable = false, length = 64)
    public String getGeneLocationKey() {
        return geneLocationKey;
    }

    public void setGeneLocationKey(String geneLocationKey) {
        this.geneLocationKey = geneLocationKey;
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
    @Column(name = "ref_assembly", nullable = true, length = 200)
    public String getRefAssembly() {
        return refAssembly;
    }

    public void setRefAssembly(String refAssembly) {
        this.refAssembly = refAssembly;
    }

    @Basic
    @Column(name = "chromosome", nullable = true, length = 64)
    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    @Basic
    @Column(name = "gene_start", nullable = true, length = 64)
    public String getGeneStart() {
        return geneStart;
    }

    public void setGeneStart(String geneStart) {
        this.geneStart = geneStart;
    }

    @Basic
    @Column(name = "gene_stop", nullable = true, length = 64)
    public String getGeneStop() {
        return geneStop;
    }

    public void setGeneStop(String geneStop) {
        this.geneStop = geneStop;
    }

    @Basic
    @Column(name = "gene_size", nullable = true)
    public Integer getGeneSize() {
        return geneSize;
    }

    public void setGeneSize(Integer geneSize) {
        this.geneSize = geneSize;
    }

    @Basic
    @Column(name = "orientatio", nullable = true, length = 64)
    public String getOrientatio() {
        return orientatio;
    }

    public void setOrientatio(String orientatio) {
        this.orientatio = orientatio;
    }

    @Basic
    @Column(name = "exoncount", nullable = true)
    public Integer getExoncount() {
        return exoncount;
    }

    public void setExoncount(Integer exoncount) {
        this.exoncount = exoncount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneLocation that = (GeneLocation) o;

        if (geneLocationKey != null ? !geneLocationKey.equals(that.geneLocationKey) : that.geneLocationKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(that.geneKey) : that.geneKey != null) return false;
        if (geneId != null ? !geneId.equals(that.geneId) : that.geneId != null) return false;
        if (refAssembly != null ? !refAssembly.equals(that.refAssembly) : that.refAssembly != null) return false;
        if (chromosome != null ? !chromosome.equals(that.chromosome) : that.chromosome != null) return false;
        if (geneStart != null ? !geneStart.equals(that.geneStart) : that.geneStart != null) return false;
        if (geneStop != null ? !geneStop.equals(that.geneStop) : that.geneStop != null) return false;
        if (geneSize != null ? !geneSize.equals(that.geneSize) : that.geneSize != null) return false;
        if (orientatio != null ? !orientatio.equals(that.orientatio) : that.orientatio != null) return false;
        if (exoncount != null ? !exoncount.equals(that.exoncount) : that.exoncount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = geneLocationKey != null ? geneLocationKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
        result = 31 * result + (refAssembly != null ? refAssembly.hashCode() : 0);
        result = 31 * result + (chromosome != null ? chromosome.hashCode() : 0);
        result = 31 * result + (geneStart != null ? geneStart.hashCode() : 0);
        result = 31 * result + (geneStop != null ? geneStop.hashCode() : 0);
        result = 31 * result + (geneSize != null ? geneSize.hashCode() : 0);
        result = 31 * result + (orientatio != null ? orientatio.hashCode() : 0);
        result = 31 * result + (exoncount != null ? exoncount.hashCode() : 0);
        return result;
    }
}
