package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_details")
public class DrugDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    private String detailKey;
    private String drugKey;
    private Integer drugId;
    private String phenotype;
    private String twoFoldExample;
    private String phenotypeInfluence;
    private String recommendedDose;
    private String recommendedCategory;

    @Id
    @Column(name = "detail_key", nullable = false, length = 64)
    public String getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(String detailKey) {
        this.detailKey = detailKey;
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
    @Column(name = "phenotype", nullable = true, length = 128)
    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    @Basic
    @Column(name = "two_fold_example", nullable = true, length = 128)
    public String getTwoFoldExample() {
        return twoFoldExample;
    }

    public void setTwoFoldExample(String twoFoldExample) {
        this.twoFoldExample = twoFoldExample;
    }

    @Basic
    @Column(name = "phenotype_influence", nullable = true, length = 128)
    public String getPhenotypeInfluence() {
        return phenotypeInfluence;
    }

    public void setPhenotypeInfluence(String phenotypeInfluence) {
        this.phenotypeInfluence = phenotypeInfluence;
    }

    @Basic
    @Column(name = "recommended_dose", nullable = true, length = 32)
    public String getRecommendedDose() {
        return recommendedDose;
    }

    public void setRecommendedDose(String recommendedDose) {
        this.recommendedDose = recommendedDose;
    }

    @Basic
    @Column(name = "recommended_category", nullable = true, length = 32)
    public String getRecommendedCategory() {
        return recommendedCategory;
    }

    public void setRecommendedCategory(String recommendedCategory) {
        this.recommendedCategory = recommendedCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugDetails that = (DrugDetails) o;

        if (detailKey != null ? !detailKey.equals(that.detailKey) : that.detailKey != null) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (phenotype != null ? !phenotype.equals(that.phenotype) : that.phenotype != null) return false;
        if (twoFoldExample != null ? !twoFoldExample.equals(that.twoFoldExample) : that.twoFoldExample != null)
            return false;
        if (phenotypeInfluence != null ? !phenotypeInfluence.equals(that.phenotypeInfluence) : that.phenotypeInfluence != null)
            return false;
        if (recommendedDose != null ? !recommendedDose.equals(that.recommendedDose) : that.recommendedDose != null)
            return false;
        if (recommendedCategory != null ? !recommendedCategory.equals(that.recommendedCategory) : that.recommendedCategory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = detailKey != null ? detailKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (phenotype != null ? phenotype.hashCode() : 0);
        result = 31 * result + (twoFoldExample != null ? twoFoldExample.hashCode() : 0);
        result = 31 * result + (phenotypeInfluence != null ? phenotypeInfluence.hashCode() : 0);
        result = 31 * result + (recommendedDose != null ? recommendedDose.hashCode() : 0);
        result = 31 * result + (recommendedCategory != null ? recommendedCategory.hashCode() : 0);
        return result;
    }
}
