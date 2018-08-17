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
        if (!(o instanceof GeneLocation)) return false;
        GeneLocation that = (GeneLocation) o;
        return Objects.equals(getGeneKey(), that.getGeneKey()) &&
                Objects.equals(getGeneId(), that.getGeneId()) &&
                Objects.equals(getRefAssembly(), that.getRefAssembly()) &&
                Objects.equals(getChromosome(), that.getChromosome()) &&
                Objects.equals(getGeneStart(), that.getGeneStart()) &&
                Objects.equals(getGeneStop(), that.getGeneStop()) &&
                Objects.equals(getGeneSize(), that.getGeneSize()) &&
                Objects.equals(getOrientatio(), that.getOrientatio()) &&
                Objects.equals(getExoncount(), that.getExoncount());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getGeneKey(), getGeneId(), getRefAssembly(), getChromosome(), getGeneStart(), getGeneStop(), getGeneSize(), getOrientatio(), getExoncount());
    }
}
