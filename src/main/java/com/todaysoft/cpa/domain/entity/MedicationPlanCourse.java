package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_course")
public class MedicationPlanCourse {
    private String planCourseKey;
    private String planMethodKey;
    private Integer medicationPlanId;
    private String courseCount;
    private Integer courseDayCount;
    private String courseDescription;
    private Integer courseOrder;
    private Boolean reviewRequired;
    private String drugIds;

    @Id
    @Column(name = "plan_course_key")
    public String getPlanCourseKey() {
        return planCourseKey;
    }

    public void setPlanCourseKey(String planCourseKey) {
        this.planCourseKey = planCourseKey;
    }

    @Basic
    @Column(name = "plan_method_key")
    public String getPlanMethodKey() {
        return planMethodKey;
    }

    public void setPlanMethodKey(String planMethodKey) {
        this.planMethodKey = planMethodKey;
    }

    @Basic
    @Column(name = "medication_plan_id")
    public Integer getMedicationPlanId() {
        return medicationPlanId;
    }

    public void setMedicationPlanId(Integer medicationPlanId) {
        this.medicationPlanId = medicationPlanId;
    }

    @Basic
    @Column(name = "course_count")
    @Type(type = "text")
    public String getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(String courseCount) {
        this.courseCount = courseCount;
    }

    @Basic
    @Column(name = "course_day_count")
    public Integer getCourseDayCount() {
        return courseDayCount;
    }

    public void setCourseDayCount(Integer courseDayCount) {
        this.courseDayCount = courseDayCount;
    }

    @Basic
    @Column(name = "course_description")
    @Type(type = "text")
    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    @Basic
    @Column(name = "course_order")
    public Integer getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(Integer courseOrder) {
        this.courseOrder = courseOrder;
    }

    @Basic
    @Column(name = "review_required")
    public Boolean getReviewRequired() {
        return reviewRequired;
    }

    public void setReviewRequired(Boolean reviewRequired) {
        this.reviewRequired = reviewRequired;
    }

    @Basic
    @Column(name = "drug_ids")
    public String getDrugIds() {
        return drugIds;
    }

    public void setDrugIds(String drugIds) {
        this.drugIds = drugIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanCourse that = (MedicationPlanCourse) o;
        return Objects.equals(planCourseKey, that.planCourseKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicationPlanId, that.medicationPlanId) &&
                Objects.equals(courseCount, that.courseCount) &&
                Objects.equals(courseDayCount, that.courseDayCount) &&
                Objects.equals(courseDescription, that.courseDescription) &&
                Objects.equals(courseOrder, that.courseOrder) &&
                Objects.equals(reviewRequired, that.reviewRequired) &&
                Objects.equals(drugIds, that.drugIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planCourseKey, planMethodKey, medicationPlanId, courseCount, courseDayCount, courseDescription, courseOrder, reviewRequired, drugIds);
    }
}
