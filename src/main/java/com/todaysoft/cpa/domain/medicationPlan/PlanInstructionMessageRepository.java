package com.todaysoft.cpa.domain.medicationPlan;

import com.todaysoft.cpa.domain.medicationPlan.entity.PlanInstructionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:57
 */
public interface PlanInstructionMessageRepository extends JpaRepository<PlanInstructionMessage,String> {
}