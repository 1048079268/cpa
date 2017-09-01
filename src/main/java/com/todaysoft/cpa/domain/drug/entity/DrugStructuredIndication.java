package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:35
 */
@Entity
@Table(name = "kt_drug_structured_indication", schema = "project_kb_en", catalog = "")
@IdClass(DrugStructuredIndicationPK.class)
public class DrugStructuredIndication {
    private String drugKey;
    private String indicationKey;
    private int drugId;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "indication_key", nullable = false, length = 64)
    public String getIndicationKey() {
        return indicationKey;
    }

    public void setIndicationKey(String indicationKey) {
        this.indicationKey = indicationKey;
    }

    @Basic
    @Column(name = "drug_id", nullable = false)
    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

//    @Basic
//    @Column(name = "structured_indication", nullable = false, length = 200)
//    public String getStructuredIndication() {
//        return structuredIndication;
//    }
//
//    public void setStructuredIndication(String structuredIndication) {
//        this.structuredIndication = structuredIndication;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugStructuredIndication that = (DrugStructuredIndication) o;

        if (drugId != that.drugId) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (indicationKey != null ? !indicationKey.equals(that.indicationKey) : that.indicationKey != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (indicationKey != null ? indicationKey.hashCode() : 0);
        result = 31 * result + drugId;
        return result;
    }
}
