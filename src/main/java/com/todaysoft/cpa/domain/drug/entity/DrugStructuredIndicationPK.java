package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:35
 */
public class DrugStructuredIndicationPK implements Serializable {
    private String drugKey;
    private String indicationKey;

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "indication_key", nullable = false, length = 64)
    @Id
    public String getIndicationKey() {
        return indicationKey;
    }

    public void setIndicationKey(String indicationKey) {
        this.indicationKey = indicationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugStructuredIndicationPK that = (DrugStructuredIndicationPK) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (indicationKey != null ? !indicationKey.equals(that.indicationKey) : that.indicationKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (indicationKey != null ? indicationKey.hashCode() : 0);
        return result;
    }
}
