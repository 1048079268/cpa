package com.todaysoft.cpa.domain.drug.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_adverse_reaction")
public class DrugAdverseReaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String adverseReactionKey;
    private String drugKey;
    private Integer drugId;
    @JSONField(name = "adrName")
    private String adressName;
    @JSONField(name = "frequency")
    private String ferquency;
    @JSONField(name = "placeboFrequency")
    private String placeboFrequency;

    @Id
    @Column(name = "adverse_reaction_key", nullable = false, length = 64)
    public String getAdverseReactionKey() {
        return adverseReactionKey;
    }

    public void setAdverseReactionKey(String adverseReactionKey) {
        this.adverseReactionKey = adverseReactionKey;
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
    @Column(name = "adress_name", nullable = true, length = 100)
    public String getAdressName() {
        return adressName;
    }

    public void setAdressName(String adressName) {
        this.adressName = adressName;
    }

    @Basic
    @Column(name = "ferquency", nullable = true, length = 100)
    public String getFerquency() {
        return ferquency;
    }

    public void setFerquency(String ferquency) {
        this.ferquency = ferquency;
    }

    @Basic
    @Column(name = "placebo_frequency", nullable = true, length = 100)
    public String getPlaceboFrequency() {
        return placeboFrequency;
    }

    public void setPlaceboFrequency(String placeboFrequency) {
        this.placeboFrequency = placeboFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugAdverseReaction that = (DrugAdverseReaction) o;

        if (adverseReactionKey != null ? !adverseReactionKey.equals(that.adverseReactionKey) : that.adverseReactionKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (adressName != null ? !adressName.equals(that.adressName) : that.adressName != null) return false;
        if (ferquency != null ? !ferquency.equals(that.ferquency) : that.ferquency != null) return false;
        if (placeboFrequency != null ? !placeboFrequency.equals(that.placeboFrequency) : that.placeboFrequency != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = adverseReactionKey != null ? adverseReactionKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (adressName != null ? adressName.hashCode() : 0);
        result = 31 * result + (ferquency != null ? ferquency.hashCode() : 0);
        result = 31 * result + (placeboFrequency != null ? placeboFrequency.hashCode() : 0);
        return result;
    }
}
