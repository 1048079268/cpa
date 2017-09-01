package com.todaysoft.cpa.domain.cacer;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:25
 */
@Entity
@Table(name = "kt_cancer")
public class Cancer {
    private String cancerKey;
    private String doid;
    private String cancerName;
    private String parentId;
    private String cancerDefinition;
    private String parentKey;

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Basic
    @Column(name = "doid", nullable = false, length = 11)
    public String getDoid() {
        return doid;
    }

    public void setDoid(String doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "cancer_name", nullable = true, length = 200)
    public String getCancerName() {
        return cancerName;
    }

    public void setCancerName(String cancerName) {
        this.cancerName = cancerName;
    }

    @Basic
    @Column(name = "parent_ids")
    @Type(type = "text")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "parent_keys")
    @Type(type = "text")
    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }


    @Basic
    @Column(name = "cancer_definition", nullable = true, length = -1)
    @Type(type = "text")
    public String getCancerDefinition() {
        return cancerDefinition;
    }

    public void setCancerDefinition(String cancerDefinition) {
        this.cancerDefinition = cancerDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancer)) return false;

        Cancer cancer = (Cancer) o;

        if (getCancerKey() != null ? !getCancerKey().equals(cancer.getCancerKey()) : cancer.getCancerKey() != null)
            return false;
        if (getDoid() != null ? !getDoid().equals(cancer.getDoid()) : cancer.getDoid() != null) return false;
        if (getCancerName() != null ? !getCancerName().equals(cancer.getCancerName()) : cancer.getCancerName() != null)
            return false;
        if (getParentId() != null ? !getParentId().equals(cancer.getParentId()) : cancer.getParentId() != null)
            return false;
        if (getCancerDefinition() != null ? !getCancerDefinition().equals(cancer.getCancerDefinition()) : cancer.getCancerDefinition() != null)
            return false;
        return getParentKey() != null ? getParentKey().equals(cancer.getParentKey()) : cancer.getParentKey() == null;
    }

    @Override
    public int hashCode() {
        int result = getCancerKey() != null ? getCancerKey().hashCode() : 0;
        result = 31 * result + (getDoid() != null ? getDoid().hashCode() : 0);
        result = 31 * result + (getCancerName() != null ? getCancerName().hashCode() : 0);
        result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
        result = 31 * result + (getCancerDefinition() != null ? getCancerDefinition().hashCode() : 0);
        result = 31 * result + (getParentKey() != null ? getParentKey().hashCode() : 0);
        return result;
    }
}
