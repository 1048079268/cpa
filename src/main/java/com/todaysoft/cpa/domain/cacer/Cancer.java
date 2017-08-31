package com.todaysoft.cpa.domain.cacer;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:25
 */
@Entity
@Table(name = "kt_cancer", schema = "project_kb_en", catalog = "")
public class Cancer {
    private String cancerKey;
    private String doid;
    private String cancerName;
    private String parentId;
    private String cancerDefinition;

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
    @Column(name = "parent_id", nullable = true, length = 11)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
        if (o == null || getClass() != o.getClass()) return false;

        Cancer cancer = (Cancer) o;

        if (cancerKey != null ? !cancerKey.equals(cancer.cancerKey) : cancer.cancerKey != null) return false;
        if (doid != null ? !doid.equals(cancer.doid) : cancer.doid != null) return false;
        if (cancerName != null ? !cancerName.equals(cancer.cancerName) : cancer.cancerName != null) return false;
        if (parentId != null ? !parentId.equals(cancer.parentId) : cancer.parentId != null) return false;
        if (cancerDefinition != null ? !cancerDefinition.equals(cancer.cancerDefinition) : cancer.cancerDefinition != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cancerKey != null ? cancerKey.hashCode() : 0;
        result = 31 * result + (doid != null ? doid.hashCode() : 0);
        result = 31 * result + (cancerName != null ? cancerName.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (cancerDefinition != null ? cancerDefinition.hashCode() : 0);
        return result;
    }
}
