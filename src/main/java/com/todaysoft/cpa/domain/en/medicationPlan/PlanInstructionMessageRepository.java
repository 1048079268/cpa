package com.todaysoft.cpa.domain.en.medicationPlan;

import com.todaysoft.cpa.domain.entity.MedicationPlan;
import com.todaysoft.cpa.domain.entity.PlanInstructionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:57
 */
public interface PlanInstructionMessageRepository extends JpaRepository<PlanInstructionMessage,String> {
    @Query("select distinct pim from PlanInstructionMessage pim , PlanInstruction  pi ,PlanDrug pd ,Drug d " +
            "where pim.planInstructionKey=pi.planInstructionKey and pi.medicationPlanId=pd.medicationPlanId " +
            "and pd.drugKey=d.drugKey and d.oncoDrug=true")
    List<PlanInstructionMessage> statistics();
}
