package com.todaysoft.cpa.domain.drug.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_classification")
public class DrugClassification implements Serializable {
    private static final long serialVersionUID = 1L;

    private String drugClassificationKey;
    private String drugKey;
    private int drugId;
    private String class1;
    private String class2;
    private String class3;
    private String class4;
    private String description;

    @Id
    @Column(name = "drug_classification_key", nullable = false, length = 64)
    public String getDrugClassificationKey() {
        return drugClassificationKey;
    }

    public void setDrugClassificationKey(String drugClassificationKey) {
        this.drugClassificationKey = drugClassificationKey;
    }

    @Basic
    @Column(name = "drug_key", nullable = true, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Basic
    @Column(name = "drug_id", nullable = false)
    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "class1", nullable = true, length = 64)
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @Basic
    @Column(name = "class2", nullable = true, length = 64)
    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    @Basic
    @Column(name = "class3", nullable = true, length = 64)
    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    @Basic
    @Column(name = "class4", nullable = true, length = 64)
    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    @Type(type = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugClassification that = (DrugClassification) o;

        if (drugId != that.drugId) return false;
        if (drugClassificationKey != null ? !drugClassificationKey.equals(that.drugClassificationKey) : that.drugClassificationKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (class1 != null ? !class1.equals(that.class1) : that.class1 != null) return false;
        if (class2 != null ? !class2.equals(that.class2) : that.class2 != null) return false;
        if (class3 != null ? !class3.equals(that.class3) : that.class3 != null) return false;
        if (class4 != null ? !class4.equals(that.class4) : that.class4 != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugClassificationKey != null ? drugClassificationKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (class1 != null ? class1.hashCode() : 0);
        result = 31 * result + (class2 != null ? class2.hashCode() : 0);
        result = 31 * result + (class3 != null ? class3.hashCode() : 0);
        result = 31 * result + (class4 != null ? class4.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
