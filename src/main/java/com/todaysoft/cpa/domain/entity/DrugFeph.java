package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_feph")
public class DrugFeph implements Serializable {
    private static final long serialVersionUID = 1L;
    private String drugFephKey;
    private String drugKey;
    private Integer drugId;
    private String fephType;
    private Byte requireGeneTest;
    private String detail;
    private String detectionSubitem;
    private String detectionItem;

    @Id
    @Column(name = "drug_feph_key", nullable = false, length = 64)
    @Type(type = "char")
    public String getDrugFephKey() {
        return drugFephKey;
    }

    public void setDrugFephKey(String drugFephKey) {
        this.drugFephKey = drugFephKey;
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
    @Column(name = "feph_type", nullable = true, length = 8)
    public String getFephType() {
        return fephType;
    }

    public void setFephType(String fephType) {
        this.fephType = fephType;
    }

    @Basic
    @Column(name = "require_gene_test", nullable = true)
    public Byte getRequireGeneTest() {
        return requireGeneTest;
    }

    public void setRequireGeneTest(Byte requireGeneTest) {
        this.requireGeneTest = requireGeneTest;
    }

    @Basic
    @Column(name = "detail", nullable = true, length = -1)
    @Type(type = "text")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "detection_subitem", nullable = true, length = 64)
    public String getDetectionSubitem() {
        return detectionSubitem;
    }

    public void setDetectionSubitem(String detectionSubitem) {
        this.detectionSubitem = detectionSubitem;
    }

    @Basic
    @Column(name = "detection_item", nullable = true, length = 64)
    public String getDetectionItem() {
        return detectionItem;
    }

    public void setDetectionItem(String detectionItem) {
        this.detectionItem = detectionItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugFeph drugFeph = (DrugFeph) o;

        if (drugFephKey != null ? !drugFephKey.equals(drugFeph.drugFephKey) : drugFeph.drugFephKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(drugFeph.drugKey) : drugFeph.drugKey != null) return false;
        if (drugId != null ? !drugId.equals(drugFeph.drugId) : drugFeph.drugId != null) return false;
        if (fephType != null ? !fephType.equals(drugFeph.fephType) : drugFeph.fephType != null) return false;
        if (requireGeneTest != null ? !requireGeneTest.equals(drugFeph.requireGeneTest) : drugFeph.requireGeneTest != null)
            return false;
        if (detail != null ? !detail.equals(drugFeph.detail) : drugFeph.detail != null) return false;
        if (detectionSubitem != null ? !detectionSubitem.equals(drugFeph.detectionSubitem) : drugFeph.detectionSubitem != null)
            return false;
        if (detectionItem != null ? !detectionItem.equals(drugFeph.detectionItem) : drugFeph.detectionItem != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugFephKey != null ? drugFephKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (fephType != null ? fephType.hashCode() : 0);
        result = 31 * result + (requireGeneTest != null ? requireGeneTest.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        result = 31 * result + (detectionSubitem != null ? detectionSubitem.hashCode() : 0);
        result = 31 * result + (detectionItem != null ? detectionItem.hashCode() : 0);
        return result;
    }
}
