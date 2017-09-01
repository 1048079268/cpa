package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:35
 */
@Entity
@Table(name = "kt_drug_adverse_reaction")
@IdClass(DrugAdverseReactionPK.class)
public class DrugAdverseReaction {
    private String drugKey;
    private String sideEffectKey;
    private Integer drugId;
    private String adressName;
    private String ferquency;
    private String placeboFrequency;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "side_effect_key", nullable = false, length = 64)
    public String getSideEffectKey() {
        return sideEffectKey;
    }

    public void setSideEffectKey(String sideEffectKey) {
        this.sideEffectKey = sideEffectKey;
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

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (sideEffectKey != null ? !sideEffectKey.equals(that.sideEffectKey) : that.sideEffectKey != null)
            return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (adressName != null ? !adressName.equals(that.adressName) : that.adressName != null) return false;
        if (ferquency != null ? !ferquency.equals(that.ferquency) : that.ferquency != null) return false;
        if (placeboFrequency != null ? !placeboFrequency.equals(that.placeboFrequency) : that.placeboFrequency != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (sideEffectKey != null ? sideEffectKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (adressName != null ? adressName.hashCode() : 0);
        result = 31 * result + (ferquency != null ? ferquency.hashCode() : 0);
        result = 31 * result + (placeboFrequency != null ? placeboFrequency.hashCode() : 0);
        return result;
    }
}
