package com.todaysoft.cpa.domain.cn.medicationPlan;

import com.todaysoft.cpa.domain.entity.PlanCancer;
import com.todaysoft.cpa.domain.entity.PlanCancerPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:55
 */
public interface CnPlanCancerRepository extends JpaRepository<PlanCancer,PlanCancerPK>{
}
