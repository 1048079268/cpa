package com.todaysoft.cpa.domain.drug.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_sequence", schema = "project_kb")
public class DrugSequence implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sequenceKey;
    private String drugKey;
    private int drugId;
    private String sequence;

    @Id
    @Column(name = "sequence_key", nullable = false, length = 64)
    public String getSequenceKey() {
        return sequenceKey;
    }

    public void setSequenceKey(String sequenceKey) {
        this.sequenceKey = sequenceKey;
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
    @Column(name = "sequence", nullable = false, length = -1)
    @Type(type = "text")
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugSequence that = (DrugSequence) o;

        if (drugId != that.drugId) return false;
        if (sequenceKey != null ? !sequenceKey.equals(that.sequenceKey) : that.sequenceKey != null) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sequenceKey != null ? sequenceKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        return result;
    }
}
