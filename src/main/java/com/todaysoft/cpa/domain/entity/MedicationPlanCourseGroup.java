package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_course_group")
public class MedicationPlanCourseGroup {
    private String groupKey;
    private String planCourseKey;
    private String groupName;
    private Integer groupIndex;

    @Id
    @Column(name = "group_key")
    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    @Basic
    @Column(name = "plan_course_key")
    public String getPlanCourseKey() {
        return planCourseKey;
    }

    public void setPlanCourseKey(String planCourseKey) {
        this.planCourseKey = planCourseKey;
    }

    @Basic
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "group_index")
    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanCourseGroup that = (MedicationPlanCourseGroup) o;
        return Objects.equals(groupKey, that.groupKey) &&
                Objects.equals(planCourseKey, that.planCourseKey) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(groupIndex, that.groupIndex);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupKey, planCourseKey, groupName, groupIndex);
    }
}
