package com.todaysoft.cpa.domain.medicationPlan;

import com.todaysoft.cpa.domain.medicationPlan.entity.MedicationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:33
 */
public interface MedicationPlanRepository extends JpaRepository<MedicationPlan,String>{
    @Query("select m from MedicationPlan m where m.createdWay=2")
    Set<String> findIdByCPA();
}
