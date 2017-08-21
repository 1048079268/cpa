package com.todaysoft.cpa.domain.drug.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_product_etn_id", schema = "project_kb")
public class DrugProductEtnId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String etnIdKey;
    private String productKey;
    @JSONField(name = "dpd_id")
    private String dpdId;
    @JSONField(name = "ndc_id")
    private String ndcId;
    @JSONField(name = "ndc_product_code")
    private String ndcProductCode;
    private String cfdaId;

    @Id
    @Column(name = "etn_id_key", nullable = false, length = 64)
    public String getEtnIdKey() {
        return etnIdKey;
    }

    public void setEtnIdKey(String etnIdKey) {
        this.etnIdKey = etnIdKey;
    }

    @Basic
    @Column(name = "product_key", nullable = true, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "dpd_id", nullable = true, length = 64)
    public String getDpdId() {
        return dpdId;
    }

    public void setDpdId(String dpdId) {
        this.dpdId = dpdId;
    }

    @Basic
    @Column(name = "ndc_id", nullable = true, length = 64)
    public String getNdcId() {
        return ndcId;
    }

    public void setNdcId(String ndcId) {
        this.ndcId = ndcId;
    }

    @Basic
    @Column(name = "ndc_product_code", nullable = true, length = 64)
    public String getNdcProductCode() {
        return ndcProductCode;
    }

    public void setNdcProductCode(String ndcProductCode) {
        this.ndcProductCode = ndcProductCode;
    }

    @Basic
    @Column(name = "cfda_id", nullable = true, length = 64)
    public String getCfdaId() {
        return cfdaId;
    }

    public void setCfdaId(String cfdaId) {
        this.cfdaId = cfdaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductEtnId that = (DrugProductEtnId) o;

        if (etnIdKey != null ? !etnIdKey.equals(that.etnIdKey) : that.etnIdKey != null) return false;
        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (dpdId != null ? !dpdId.equals(that.dpdId) : that.dpdId != null) return false;
        if (ndcId != null ? !ndcId.equals(that.ndcId) : that.ndcId != null) return false;
        if (ndcProductCode != null ? !ndcProductCode.equals(that.ndcProductCode) : that.ndcProductCode != null)
            return false;
        if (cfdaId != null ? !cfdaId.equals(that.cfdaId) : that.cfdaId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = etnIdKey != null ? etnIdKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        result = 31 * result + (dpdId != null ? dpdId.hashCode() : 0);
        result = 31 * result + (ndcId != null ? ndcId.hashCode() : 0);
        result = 31 * result + (ndcProductCode != null ? ndcProductCode.hashCode() : 0);
        result = 31 * result + (cfdaId != null ? cfdaId.hashCode() : 0);
        return result;
    }
}
