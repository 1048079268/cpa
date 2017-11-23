package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/22 18:11
 */
@Entity
@Table(name = "kt_drug_product_route")
public class DrugProductRoute {
    private String routeKey;
    private String productKey;
    private String productRoute;

    @Id
    @Column(name = "route_key", nullable = false, length = 64)
    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    @Basic
    @Column(name = "product_key", nullable = false, length = 64)
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Basic
    @Column(name = "product_route", nullable = false, length = 200)
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

        DrugProductRoute that = (DrugProductRoute) o;

        if (routeKey != null ? !routeKey.equals(that.routeKey) : that.routeKey != null) return false;
        if (productKey != null ? !productKey.equals(that.productKey) : that.productKey != null) return false;
        if (productRoute != null ? !productRoute.equals(that.productRoute) : that.productRoute != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = routeKey != null ? routeKey.hashCode() : 0;
        result = 31 * result + (productKey != null ? productKey.hashCode() : 0);
        result = 31 * result + (productRoute != null ? productRoute.hashCode() : 0);
        return result;
    }
}
