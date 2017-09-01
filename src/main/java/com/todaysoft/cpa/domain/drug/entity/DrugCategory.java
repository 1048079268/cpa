package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 13:26
 */
@Entity
@Table(name = "kt_drug_category")
@IdClass(DrugCategoryPK.class)
public class DrugCategory {
    private String drugKey;
    private String meshCategoryKey;
    private Integer drugId;
    private String meshId;
    private String categoryName;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "mesh_category_key", nullable = false, length = 64)
    public String getMeshCategoryKey() {
        return meshCategoryKey;
    }

    public void setMeshCategoryKey(String meshCategoryKey) {
        this.meshCategoryKey = meshCategoryKey;
    }

    @Basic
    @Column(name = "drug_id", nullable = true)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "mesh_id", nullable = true, length = 64)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugCategory category = (DrugCategory) o;

        if (drugKey != null ? !drugKey.equals(category.drugKey) : category.drugKey != null) return false;
        if (meshCategoryKey != null ? !meshCategoryKey.equals(category.meshCategoryKey) : category.meshCategoryKey != null)
            return false;
        if (drugId != null ? !drugId.equals(category.drugId) : category.drugId != null) return false;
        if (meshId != null ? !meshId.equals(category.meshId) : category.meshId != null) return false;
        if (categoryName != null ? !categoryName.equals(category.categoryName) : category.categoryName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (meshCategoryKey != null ? meshCategoryKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (meshId != null ? meshId.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrugCategory{" +
                "drugKey='" + drugKey + '\'' +
                ", meshCategoryKey='" + meshCategoryKey + '\'' +
                ", drugId=" + drugId +
                ", meshId='" + meshId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
