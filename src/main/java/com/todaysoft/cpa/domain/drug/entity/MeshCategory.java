package com.todaysoft.cpa.domain.drug.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:54
 */
@Entity
@Table(name = "kt_mesh_category")
public class MeshCategory {
    private String meshCategoryKey;
    @JSONField(name = "meshId")
    private String meshId;
    @JSONField(name = "categoryName")
    private String categoryName;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

    @Id
    @Column(name = "mesh_category_key", nullable = false, length = 64)
    public String getMeshCategoryKey() {
        return meshCategoryKey;
    }

    public void setMeshCategoryKey(String meshCategoryKey) {
        this.meshCategoryKey = meshCategoryKey;
    }

    @Basic
    @Column(name = "mesh_id", nullable = false, length = 64)
    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    @Basic
    @Column(name = "category_name", nullable = true, length = 200)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        if (!(o instanceof MeshCategory)) return false;

        MeshCategory that = (MeshCategory) o;

        if (getMeshCategoryKey() != null ? !getMeshCategoryKey().equals(that.getMeshCategoryKey()) : that.getMeshCategoryKey() != null)
            return false;
        if (getMeshId() != null ? !getMeshId().equals(that.getMeshId()) : that.getMeshId() != null) return false;
        if (getCategoryName() != null ? !getCategoryName().equals(that.getCategoryName()) : that.getCategoryName() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
            return false;
        if (getCreatedWay() != null ? !getCreatedWay().equals(that.getCreatedWay()) : that.getCreatedWay() != null)
            return false;
        return getCheckState() != null ? getCheckState().equals(that.getCheckState()) : that.getCheckState() == null;
    }

    @Override
    public int hashCode() {
        int result = getMeshCategoryKey() != null ? getMeshCategoryKey().hashCode() : 0;
        result = 31 * result + (getMeshId() != null ? getMeshId().hashCode() : 0);
        result = 31 * result + (getCategoryName() != null ? getCategoryName().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getCreatedWay() != null ? getCreatedWay().hashCode() : 0);
        result = 31 * result + (getCheckState() != null ? getCheckState().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MeshCategory{" +
                "meshCategoryKey='" + meshCategoryKey + '\'' +
                ", meshId='" + meshId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
