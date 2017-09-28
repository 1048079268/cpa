package com.todaysoft.cpa.domain.cn.clinicalTrail;

import com.todaysoft.cpa.domain.entity.ClinicalTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:52
 */
public interface CnClinicalTrailRepository extends JpaRepository<ClinicalTrial,String>{
    @Query("select c.clinicalTrialId from ClinicalTrial c where c.createdWay=2")
    Set<String> findIdByCPA();

    ClinicalTrial findByClinicalTrialId(String clinicalTrialId);
}
