package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_interaction")
public class DrugInteraction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String interactionKey;
    private String drugKey;
    private int drugId;
    @JSONField(name = "drugID")
    private Integer drugIdInteraction;
    @JSONField(name = "description")
    private String description;

    @Id
    @Column(name = "interaction_key", nullable = false, length = 64)
    public String getInteractionKey() {
        return interactionKey;
    }

    public void setInteractionKey(String interactionKey) {
        this.interactionKey = interactionKey;
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
    @Column(name = "drug_id_interaction", nullable = true)
    public Integer getDrugIdInteraction() {
        return drugIdInteraction;
    }

    public void setDrugIdInteraction(Integer drugIdInteraction) {
        this.drugIdInteraction = drugIdInteraction;
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
        if (!(o instanceof DrugInteraction)) return false;
        DrugInteraction that = (DrugInteraction) o;
        return getDrugId() == that.getDrugId() &&
                Objects.equals(getDrugKey(), that.getDrugKey()) &&
                Objects.equals(getDrugIdInteraction(), that.getDrugIdInteraction()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDrugKey(), getDrugId(), getDrugIdInteraction(), getDescription());
    }
}
