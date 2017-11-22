package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:34
 */
public class DrugProductRoutePK implements Serializable {
    private String productKey;
    private String productRoute;

    @Column(name = "product_key", nullable = false, length = 64)
    @Id
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Column(name = "product_route", nullable = false, length = 200)
    @Id
    public String getProductRoute() {
        return productRoute;
    }

    public void setProductRoute(String productRoute) {
        this.productRoute = productRoute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductRoutePK that = (DrugProductRoutePK) o;

        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (productRoute != null ? !productRoute.equals(that.productRoute) : that.productRoute != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productKey != null ? productKey.hashCode() : 0;
        result = 31 * result + (productRoute != null ? productRoute.hashCode() : 0);
        return result;
    }
}
