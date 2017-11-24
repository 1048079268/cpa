package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_action_target")
public class DrugActionTarget implements Serializable {
    private static final long serialVersionUID = 1L;
    private String actionTargetKey;
    private String drugKey;
    private int drugId;
    private String actionTarget;

    @Id
    @Column(name = "action_target_key", nullable = false, length = 64)
    public String getActionTargetKey() {
        return actionTargetKey;
    }

    public void setActionTargetKey(String actionTargetKey) {
        this.actionTargetKey = actionTargetKey;
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
    @Column(name = "gene_key", nullable = true, length = 128)
    public String getActionTarget() {
        return actionTarget;
    }

    public void setActionTarget(String actionTarget) {
        this.actionTarget = actionTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugActionTarget that = (DrugActionTarget) o;

        if (drugId != that.drugId) return false;
        if (actionTargetKey != null ? !actionTargetKey.equals(that.actionTargetKey) : that.actionTargetKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (actionTarget != null ? !actionTarget.equals(that.actionTarget) : that.actionTarget != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = actionTargetKey != null ? actionTargetKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (actionTarget != null ? actionTarget.hashCode() : 0);
        return result;
    }
}
