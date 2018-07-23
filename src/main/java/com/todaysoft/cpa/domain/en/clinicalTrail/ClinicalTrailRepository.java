package com.todaysoft.cpa.domain.en.clinicalTrail;

import com.todaysoft.cpa.domain.entity.ClinicalTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:52
 */
public interface ClinicalTrailRepository extends JpaRepository<ClinicalTrial,String>{
    @Query("select c.clinicalTrialId from ClinicalTrial c where c.createdWay=2")
    Set<String> findIdByCPA();

    @Query("select c.clinicalTrialKey from ClinicalTrial c where c.clinicalTrialId=?1 and c.createdWay=2")
    String findByClinicalTrialId(String clinicalTrialId);

    @Query("select distinct c from ClinicalTrial c ,DrugClinicalTrial dc ,Drug d " +
            "where c.clinicalTrialKey=dc.clinicalTrialKey and dc.drugKey=d.drugKey and d.oncoDrug=true")
    List<ClinicalTrial> statistics();

    @Query("select c from ClinicalTrial c where c.clinicalTrialId=?1")
    ClinicalTrial findById(String clinicalTrialId);
}
