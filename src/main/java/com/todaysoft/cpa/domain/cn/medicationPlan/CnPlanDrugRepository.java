package com.todaysoft.cpa.domain.cn.medicationPlan;

import com.todaysoft.cpa.domain.entity.PlanDrug;
import com.todaysoft.cpa.domain.entity.PlanDrugPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:55
 */
public interface CnPlanDrugRepository extends JpaRepository<PlanDrug,PlanDrugPK>{
}
