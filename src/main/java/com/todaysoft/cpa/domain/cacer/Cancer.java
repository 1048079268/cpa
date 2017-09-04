package com.todaysoft.cpa.domain.cacer;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(name = "id")
    private String doid;
    @JSONField(name = "name")
    private String cancerName;
    private String parentIds;
    private String cancerDefinition;
    private String parentKeys;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

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
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentId) {
        this.parentIds = parentId;
    }

    @Basic
    @Column(name = "parent_keys")
    @Type(type = "text")
    public String getParentKeys() {
        return parentKeys;
    }

    public void setParentKeys(String parentKey) {
        this.parentKeys = parentKey;
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


    @Basic
    @Column(name = "created_at", nullable = true)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true)
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
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
        if (!(o instanceof Cancer)) return false;

        Cancer cancer = (Cancer) o;

        if (getCancerKey() != null ? !getCancerKey().equals(cancer.getCancerKey()) : cancer.getCancerKey() != null)
            return false;
        if (getDoid() != null ? !getDoid().equals(cancer.getDoid()) : cancer.getDoid() != null) return false;
        if (getCancerName() != null ? !getCancerName().equals(cancer.getCancerName()) : cancer.getCancerName() != null)
            return false;
        if (getParentIds() != null ? !getParentIds().equals(cancer.getParentIds()) : cancer.getParentIds() != null)
            return false;
        if (getCancerDefinition() != null ? !getCancerDefinition().equals(cancer.getCancerDefinition()) : cancer.getCancerDefinition() != null)
            return false;
        return getParentKeys() != null ? getParentKeys().equals(cancer.getParentKeys()) : cancer.getParentKeys() == null;
    }

    @Override
    public int hashCode() {
        int result = getCancerKey() != null ? getCancerKey().hashCode() : 0;
        result = 31 * result + (getDoid() != null ? getDoid().hashCode() : 0);
        result = 31 * result + (getCancerName() != null ? getCancerName().hashCode() : 0);
        result = 31 * result + (getParentIds() != null ? getParentIds().hashCode() : 0);
        result = 31 * result + (getCancerDefinition() != null ? getCancerDefinition().hashCode() : 0);
        result = 31 * result + (getParentKeys() != null ? getParentKeys().hashCode() : 0);
        return result;
    }
}
