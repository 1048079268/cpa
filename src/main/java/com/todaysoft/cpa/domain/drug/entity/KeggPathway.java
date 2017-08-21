package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_kegg_pathway", schema = "project_kb")
public class KeggPathway implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pathwayKey;
    private String keggId;
    private String pathwayName;
    private String keggLink;
    private String selleckLink;

    @Id
    @Column(name = "pathway_key", nullable = false, length = 64)
    public String getPathwayKey() {
        return pathwayKey;
    }

    public void setPathwayKey(String pathwayKey) {
        this.pathwayKey = pathwayKey;
    }

    @Basic
    @Column(name = "kegg_id", nullable = true, length = 64)
    public String getKeggId() {
        return keggId;
    }

    public void setKeggId(String keggId) {
        this.keggId = keggId;
    }

    @Basic
    @Column(name = "pathway_name", nullable = true, length = 64)
    public String getPathwayName() {
        return pathwayName;
    }

    public void setPathwayName(String pathwayName) {
        this.pathwayName = pathwayName;
    }

    @Basic
    @Column(name = "kegg_link", nullable = true, length = 64)
    public String getKeggLink() {
        return keggLink;
    }

    public void setKeggLink(String keggLink) {
        this.keggLink = keggLink;
    }

    @Basic
    @Column(name = "selleck_link", nullable = true, length = 64)
    public String getSelleckLink() {
        return selleckLink;
    }

    public void setSelleckLink(String selleckLink) {
        this.selleckLink = selleckLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeggPathway that = (KeggPathway) o;

        if (pathwayKey != null ? !pathwayKey.equals(that.pathwayKey) : that.pathwayKey != null) return false;
        if (keggId != null ? !keggId.equals(that.keggId) : that.keggId != null) return false;
        if (pathwayName != null ? !pathwayName.equals(that.pathwayName) : that.pathwayName != null) return false;
        if (keggLink != null ? !keggLink.equals(that.keggLink) : that.keggLink != null) return false;
        if (selleckLink != null ? !selleckLink.equals(that.selleckLink) : that.selleckLink != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pathwayKey != null ? pathwayKey.hashCode() : 0;
        result = 31 * result + (keggId != null ? keggId.hashCode() : 0);
        result = 31 * result + (pathwayName != null ? pathwayName.hashCode() : 0);
        result = 31 * result + (keggLink != null ? keggLink.hashCode() : 0);
        result = 31 * result + (selleckLink != null ? selleckLink.hashCode() : 0);
        return result;
    }
}
