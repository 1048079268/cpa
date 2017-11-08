package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_kegg_pathway")
public class KeggPathway implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pathwayKey;
    private String keggId;
    private String pathwayName;
    private String keggLink;
    private String selleckLink;
    private Long createAt;
    private Integer createWay;
    private Integer checkState;
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

    @Basic
    @Column(name = "created_at", nullable = true, length = 64)
    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true, length = 64)
    public Integer getCreateWay() {
        return createWay;
    }

    public void setCreateWay(Integer createWay) {
        this.createWay = createWay;
    }

    @Basic
    @Column(name = "check_state", nullable = true)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeggPathway)) return false;

        KeggPathway that = (KeggPathway) o;

        if (getPathwayKey() != null ? !getPathwayKey().equals(that.getPathwayKey()) : that.getPathwayKey() != null)
            return false;
        if (getKeggId() != null ? !getKeggId().equals(that.getKeggId()) : that.getKeggId() != null) return false;
        if (getPathwayName() != null ? !getPathwayName().equals(that.getPathwayName()) : that.getPathwayName() != null)
            return false;
        if (getKeggLink() != null ? !getKeggLink().equals(that.getKeggLink()) : that.getKeggLink() != null)
            return false;
        if (getSelleckLink() != null ? !getSelleckLink().equals(that.getSelleckLink()) : that.getSelleckLink() != null)
            return false;
        if (getCreateAt() != null ? !getCreateAt().equals(that.getCreateAt()) : that.getCreateAt() != null)
            return false;
        if (getCreateWay() != null ? !getCreateWay().equals(that.getCreateWay()) : that.getCreateWay() != null)
            return false;
        return getCheckState() != null ? getCheckState().equals(that.getCheckState()) : that.getCheckState() == null;
    }

    @Override
    public int hashCode() {
        int result = getPathwayKey() != null ? getPathwayKey().hashCode() : 0;
        result = 31 * result + (getKeggId() != null ? getKeggId().hashCode() : 0);
        result = 31 * result + (getPathwayName() != null ? getPathwayName().hashCode() : 0);
        result = 31 * result + (getKeggLink() != null ? getKeggLink().hashCode() : 0);
        result = 31 * result + (getSelleckLink() != null ? getSelleckLink().hashCode() : 0);
        result = 31 * result + (getCreateAt() != null ? getCreateAt().hashCode() : 0);
        result = 31 * result + (getCreateWay() != null ? getCreateWay().hashCode() : 0);
        result = 31 * result + (getCheckState() != null ? getCheckState().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KeggPathway{" +
                "pathwayKey='" + pathwayKey + '\'' +
                ", keggId='" + keggId + '\'' +
                ", pathwayName='" + pathwayName + '\'' +
                '}';
    }
}
