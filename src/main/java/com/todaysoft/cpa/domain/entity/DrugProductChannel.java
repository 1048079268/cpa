package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:34
 */
@Entity
@Table(name = "kt_drug_product_channel")
public class DrugProductChannel {
    private String channelKey;
    private String approvalKey;
    private String channelName;
    private String channelUrl;
    private String channelPhone;
    private Double channelPriceMin;
    private Double channelPriceMax;
    private String channelPriceCurrency;
    private String channelPriceExplain;

    @Id
    @Column(name = "channel_key", nullable = false, length = 64)
    public String getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    @Basic
    @Column(name = "approval_key", nullable = true, length = 64)
    public String getApprovalKey() {
        return approvalKey;
    }

    public void setApprovalKey(String approvalKey) {
        this.approvalKey = approvalKey;
    }

    @Basic
    @Column(name = "channel_name", nullable = true, length = 500)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Basic
    @Column(name = "channel_url", nullable = true, length = -1)
    @Type(type = "text")
    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    @Basic
    @Column(name = "channel_phone", nullable = true, length = 50)
    public String getChannelPhone() {
        return channelPhone;
    }

    public void setChannelPhone(String channelPhone) {
        this.channelPhone = channelPhone;
    }

    @Basic
    @Column(name = "channel_price_min", nullable = true, precision = 3)
    public Double getChannelPriceMin() {
        return channelPriceMin;
    }

    public void setChannelPriceMin(Double channelPriceMin) {
        this.channelPriceMin = channelPriceMin;
    }

    @Basic
    @Column(name = "channel_price_max", nullable = true, precision = 3)
    public Double getChannelPriceMax() {
        return channelPriceMax;
    }

    public void setChannelPriceMax(Double channelPriceMax) {
        this.channelPriceMax = channelPriceMax;
    }

    @Basic
    @Column(name = "channel_price_currency", nullable = true, length = 20)
    public String getChannelPriceCurrency() {
        return channelPriceCurrency;
    }

    public void setChannelPriceCurrency(String channelPriceCurrency) {
        this.channelPriceCurrency = channelPriceCurrency;
    }

    @Basic
    @Column(name = "channel_price_explain", nullable = true, length = 100)
    public String getChannelPriceExplain() {
        return channelPriceExplain;
    }

    public void setChannelPriceExplain(String channelPriceExplain) {
        this.channelPriceExplain = channelPriceExplain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductChannel that = (DrugProductChannel) o;

        if (channelKey != null ? !channelKey.equals(that.channelKey) : that.channelKey != null) return false;
        if (approvalKey != null ? !approvalKey.equals(that.approvalKey) : that.approvalKey != null) return false;
        if (channelName != null ? !channelName.equals(that.channelName) : that.channelName != null) return false;
        if (channelUrl != null ? !channelUrl.equals(that.channelUrl) : that.channelUrl != null) return false;
        if (channelPhone != null ? !channelPhone.equals(that.channelPhone) : that.channelPhone != null) return false;
        if (channelPriceMin != null ? !channelPriceMin.equals(that.channelPriceMin) : that.channelPriceMin != null)
            return false;
        if (channelPriceMax != null ? !channelPriceMax.equals(that.channelPriceMax) : that.channelPriceMax != null)
            return false;
        if (channelPriceCurrency != null ? !channelPriceCurrency.equals(that.channelPriceCurrency) : that.channelPriceCurrency != null)
            return false;
        if (channelPriceExplain != null ? !channelPriceExplain.equals(that.channelPriceExplain) : that.channelPriceExplain != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = channelKey != null ? channelKey.hashCode() : 0;
        result = 31 * result + (approvalKey != null ? approvalKey.hashCode() : 0);
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (channelUrl != null ? channelUrl.hashCode() : 0);
        result = 31 * result + (channelPhone != null ? channelPhone.hashCode() : 0);
        result = 31 * result + (channelPriceMin != null ? channelPriceMin.hashCode() : 0);
        result = 31 * result + (channelPriceMax != null ? channelPriceMax.hashCode() : 0);
        result = 31 * result + (channelPriceCurrency != null ? channelPriceCurrency.hashCode() : 0);
        result = 31 * result + (channelPriceExplain != null ? channelPriceExplain.hashCode() : 0);
        return result;
    }
}
