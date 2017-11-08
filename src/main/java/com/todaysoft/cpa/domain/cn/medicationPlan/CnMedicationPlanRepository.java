package com.todaysoft.cpa.domain.cn.medicationPlan;

import com.todaysoft.cpa.domain.entity.MedicationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:33
 */
public interface CnMedicationPlanRepository extends JpaRepository<MedicationPlan,String>{
    @Query("select m.medicinePlanId from MedicationPlan m where m.createdWay=2")
    Set<Integer> findIdByCPA();

    @Query("select m from MedicationPlan m where(m.regimenName=?1 and m.programNameC=?1)  and m.createdWay=3")
    MedicationPlan findByName(String name);
}
