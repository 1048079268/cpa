package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_structured_indication")
public class DrugStructuredIndication implements Serializable {
    private static final long serialVersionUID = 1L;
    private String structuredIndicationKey;
    private String drugKey;
    private int drugId;
    private String structuredIndication;

    @Id
    @Column(name = "structured_indication_key", nullable = false, length = 64)
    public String getStructuredIndicationKey() {
        return structuredIndicationKey;
    }

    public void setStructuredIndicationKey(String structuredIndicationKey) {
        this.structuredIndicationKey = structuredIndicationKey;
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
    @Column(name = "structured_indication", nullable = false, length = 200)
    public String getStructuredIndication() {
        return structuredIndication;
    }

    public void setStructuredIndication(String structuredIndication) {
        this.structuredIndication = structuredIndication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugStructuredIndication that = (DrugStructuredIndication) o;

        if (drugId != that.drugId) return false;
        if (structuredIndicationKey != null ? !structuredIndicationKey.equals(that.structuredIndicationKey) : that.structuredIndicationKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (structuredIndication != null ? !structuredIndication.equals(that.structuredIndication) : that.structuredIndication != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = structuredIndicationKey != null ? structuredIndicationKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (structuredIndication != null ? structuredIndication.hashCode() : 0);
        return result;
    }
}
