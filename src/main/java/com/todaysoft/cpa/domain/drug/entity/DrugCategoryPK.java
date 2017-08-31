package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 13:26
 */
public class DrugCategoryPK implements Serializable {
    private String drugKey;
    private String meshCategoryKey;

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "mesh_category_key", nullable = false, length = 64)
    @Id
    public String getMeshCategoryKey() {
        return meshCategoryKey;
    }

    public void setMeshCategoryKey(String meshCategoryKey) {
        this.meshCategoryKey = meshCategoryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugCategoryPK that = (DrugCategoryPK) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (meshCategoryKey != null ? !meshCategoryKey.equals(that.meshCategoryKey) : that.meshCategoryKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (meshCategoryKey != null ? meshCategoryKey.hashCode() : 0);
        return result;
    }
}
