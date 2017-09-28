package com.todaysoft.cpa.domain.cn.medicationPlan;

import com.todaysoft.cpa.domain.entity.PlanInstruction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:56
 */
public interface CnPlanInstructionRepository extends JpaRepository<PlanInstruction,String> {
}
