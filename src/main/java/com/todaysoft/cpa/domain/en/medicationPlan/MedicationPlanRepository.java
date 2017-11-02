package com.todaysoft.cpa.domain.en.medicationPlan;

import com.todaysoft.cpa.domain.entity.MedicationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:33
 */
public interface MedicationPlanRepository extends JpaRepository<MedicationPlan,String>{
    @Query("select m.medicinePlanId from MedicationPlan m where m.createdWay=2")
    Set<Integer> findIdByCPA();

    @Query("select distinct mp from MedicationPlan mp ,PlanDrug pd ,Drug d " +
            "where mp.medicationPlanKey=pd.medicationPlanKey and d.drugKey=pd.drugKey and d.oncoDrug=true")
    List<MedicationPlan> statistics();
}
