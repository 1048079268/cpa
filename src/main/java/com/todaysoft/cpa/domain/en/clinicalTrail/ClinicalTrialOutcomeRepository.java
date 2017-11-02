package com.todaysoft.cpa.domain.en.clinicalTrail;

import com.todaysoft.cpa.domain.entity.ClinicalTrialOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:04
 */
public interface ClinicalTrialOutcomeRepository extends JpaRepository<ClinicalTrialOutcome,String>{
    @Query("select distinct co from ClinicalTrialOutcome  co ,DrugClinicalTrial dc ,Drug d " +
            "where co.clinicalTrialKey=dc.clinicalTrialKey and dc.drugKey=d.drugKey and d.oncoDrug=true")
    List<ClinicalTrialOutcome> statistics();
}
