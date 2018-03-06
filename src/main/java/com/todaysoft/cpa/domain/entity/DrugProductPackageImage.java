package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author lichy
 * @version 2018/3/6
 * @desc
 */
@Entity
@Table(name = "kt_drug_product_package_image", schema = "project_kb_en", catalog = "")
public class DrugProductPackageImage {
    private String packageImageKey;
    private String packageKey;
    private String imageUrl;

    @Id
    @Column(name = "package_image_key")
    public String getPackageImageKey() {
        return packageImageKey;
    }

    public void setPackageImageKey(String packageImageKey) {
        this.packageImageKey = packageImageKey;
    }

    @Basic
    @Column(name = "package_key")
    public String getPackageKey() {
        return packageKey;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
    }

    @Basic
    @Column(name = "image_url")
    @Type(type = "text")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugProductPackageImage that = (DrugProductPackageImage) o;

        if (packageImageKey != null ? !packageImageKey.equals(that.packageImageKey) : that.packageImageKey != null)
            return false;
        if (packageKey != null ? !packageKey.equals(that.packageKey) : that.packageKey != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = packageImageKey != null ? packageImageKey.hashCode() : 0;
        result = 31 * result + (packageKey != null ? packageKey.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
