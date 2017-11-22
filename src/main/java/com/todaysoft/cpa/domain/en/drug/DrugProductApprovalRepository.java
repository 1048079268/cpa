package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugProductApproval;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:39
 */
public interface DrugProductApprovalRepository extends JpaRepository<DrugProductApproval,String> {
    DrugProductApproval findByProductKeyAndApprovalNumber(String productKey,String approvalNumber);
}
