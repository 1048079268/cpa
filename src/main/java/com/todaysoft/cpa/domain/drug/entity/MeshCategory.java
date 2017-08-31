package com.todaysoft.cpa.domain.drug.entity;

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
    private String meshId;
    private String categoryName;
    private Long createdAt;
    private Integer createdWay;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeshCategory that = (MeshCategory) o;

        if (meshCategoryKey != null ? !meshCategoryKey.equals(that.meshCategoryKey) : that.meshCategoryKey != null)
            return false;
        if (meshId != null ? !meshId.equals(that.meshId) : that.meshId != null) return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(that.createdWay) : that.createdWay != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = meshCategoryKey != null ? meshCategoryKey.hashCode() : 0;
        result = 31 * result + (meshId != null ? meshId.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        return result;
    }
}
