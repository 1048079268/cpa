package com.todaysoft.cpa.domain.variants.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:23
 */
@Entity
@Table(name = "kt_variant_mutation_statistic")
@IdClass(VariantMutationStatisticPK.class)
public class VariantMutationStatistic {
    private String cancerKey;
    private String variantKey;
    private Integer doid;
    private String mutationId;
    private Integer numOfSamples;

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Id
    @Column(name = "variant_key", nullable = false, length = 64)
    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    @Basic
    @Column(name = "doid", nullable = true)
    public Integer getDoid() {
        return doid;
    }

    public void setDoid(Integer doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "mutation_id", nullable = true, length = 200)
    public String getMutationId() {
        return mutationId;
    }

    public void setMutationId(String mutationId) {
        this.mutationId = mutationId;
    }

    @Basic
    @Column(name = "num_of_samples", nullable = true)
    public Integer getNumOfSamples() {
        return numOfSamples;
    }

    public void setNumOfSamples(Integer numOfSamples) {
        this.numOfSamples = numOfSamples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantMutationStatistic statistic = (VariantMutationStatistic) o;

        if (cancerKey != null ? !cancerKey.equals(statistic.cancerKey) : statistic.cancerKey != null) return false;
        if (variantKey != null ? !variantKey.equals(statistic.variantKey) : statistic.variantKey != null) return false;
        if (doid != null ? !doid.equals(statistic.doid) : statistic.doid != null) return false;
        if (mutationId != null ? !mutationId.equals(statistic.mutationId) : statistic.mutationId != null) return false;
        if (numOfSamples != null ? !numOfSamples.equals(statistic.numOfSamples) : statistic.numOfSamples != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cancerKey != null ? cancerKey.hashCode() : 0;
        result = 31 * result + (variantKey != null ? variantKey.hashCode() : 0);
        result = 31 * result + (doid != null ? doid.hashCode() : 0);
        result = 31 * result + (mutationId != null ? mutationId.hashCode() : 0);
        result = 31 * result + (numOfSamples != null ? numOfSamples.hashCode() : 0);
        return result;
    }
}
