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
@Table(name = "kt_medication_plan_method_approval")
public class MedicationPlanMethodApproval {
    private String approvalKey;
    private String planMethodKey;
    private String approvalOrg;
    private String approvalUrl;

    @Id
    @Column(name = "approval_key")
    public String getApprovalKey() {
        return approvalKey;
    }

    public void setApprovalKey(String approvalKey) {
        this.approvalKey = approvalKey;
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
    @Column(name = "approval_org")
    public String getApprovalOrg() {
        return approvalOrg;
    }

    public void setApprovalOrg(String approvalOrg) {
        this.approvalOrg = approvalOrg;
    }

    @Basic
    @Column(name = "approval_url")
    @Type(type = "text")
    public String getApprovalUrl() {
        return approvalUrl;
    }

    public void setApprovalUrl(String approvalUrl) {
        this.approvalUrl = approvalUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanMethodApproval that = (MedicationPlanMethodApproval) o;
        return Objects.equals(approvalKey, that.approvalKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(approvalOrg, that.approvalOrg) &&
                Objects.equals(approvalUrl, that.approvalUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(approvalKey, planMethodKey, approvalOrg, approvalUrl);
    }
}
