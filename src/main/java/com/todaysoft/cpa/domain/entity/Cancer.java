package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:25
 */
@Entity
@Table(name = "kt_cancer")
public class Cancer {
    private String cancerKey;
    @JSONField(name = "id")
    private String doid;
    private String cancerDefinition;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;
    private String createdByName="CPA";

    @Basic
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Basic
    @Column(name = "doid", nullable = false, length = 11)
    public String getDoid() {
        return doid;
    }

    public void setDoid(String doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "cancer_definition", nullable = true, length = -1)
    @Type(type = "text")
    public String getCancerDefinition() {
        return cancerDefinition;
    }

    public void setCancerDefinition(String cancerDefinition) {
        this.cancerDefinition = cancerDefinition;
    }


    @Basic
    @Column(name = "created_at", nullable = true)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true)
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
    }

    @Basic
    @Column(name = "check_state", nullable = true)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancer)) return false;
        Cancer cancer = (Cancer) o;
        return Objects.equals(getCancerKey(), cancer.getCancerKey()) &&
                Objects.equals(getDoid(), cancer.getDoid()) &&
                Objects.equals(getCancerDefinition(), cancer.getCancerDefinition()) &&
                Objects.equals(getCreatedAt(), cancer.getCreatedAt()) &&
                Objects.equals(getCreatedWay(), cancer.getCreatedWay()) &&
                Objects.equals(getCheckState(), cancer.getCheckState()) &&
                Objects.equals(getCreatedByName(), cancer.getCreatedByName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCancerKey(), getDoid(),getCancerDefinition(), getCreatedAt(), getCreatedWay(), getCheckState(), getCreatedByName());
    }
}
