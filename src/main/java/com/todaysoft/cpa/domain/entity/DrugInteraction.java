package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

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
        if (o == null || getClass() != o.getClass()) return false;

        DrugInteraction that = (DrugInteraction) o;

        if (drugId != that.drugId) return false;
        if (interactionKey != null ? !interactionKey.equals(that.interactionKey) : that.interactionKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugIdInteraction != null ? !drugIdInteraction.equals(that.drugIdInteraction) : that.drugIdInteraction != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = interactionKey != null ? interactionKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (drugIdInteraction != null ? drugIdInteraction.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrugInteraction{" +
                "interactionKey='" + interactionKey + '\'' +
                ", drugKey='" + drugKey + '\'' +
                ", drugId=" + drugId +
                ", drugIdInteraction=" + drugIdInteraction +
                ", description='" + description + '\'' +
                '}';
    }
}
