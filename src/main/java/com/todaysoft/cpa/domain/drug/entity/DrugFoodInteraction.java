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
@Table(name = "kt_drug_food_interaction")
public class DrugFoodInteraction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String foodInteractionKey;
    private String drugKey;
    private Integer drugId;
    private String foodInteraction;

    @Id
    @Column(name = "food_interaction_key", nullable = false, length = 64)
    @Type(type = "char")
    public String getFoodInteractionKey() {
        return foodInteractionKey;
    }

    public void setFoodInteractionKey(String foodInteractionKey) {
        this.foodInteractionKey = foodInteractionKey;
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
    @Column(name = "drug_id", nullable = true)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "food_interaction", nullable = true, length = -1)
    @Type(type = "text")
    public String getFoodInteraction() {
        return foodInteraction;
    }

    public void setFoodInteraction(String foodInteraction) {
        this.foodInteraction = foodInteraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugFoodInteraction that = (DrugFoodInteraction) o;

        if (foodInteractionKey != null ? !foodInteractionKey.equals(that.foodInteractionKey) : that.foodInteractionKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (foodInteraction != null ? !foodInteraction.equals(that.foodInteraction) : that.foodInteraction != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = foodInteractionKey != null ? foodInteractionKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (foodInteraction != null ? foodInteraction.hashCode() : 0);
        return result;
    }
}
