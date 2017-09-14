package com.todaysoft.cpa.domain.medicationPlan;

import com.todaysoft.cpa.domain.medicationPlan.entity.PlanDrug;
import com.todaysoft.cpa.domain.medicationPlan.entity.PlanDrugPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:55
 */
public interface PlanDrugRepository extends JpaRepository<PlanDrug,PlanDrugPK>{
}
