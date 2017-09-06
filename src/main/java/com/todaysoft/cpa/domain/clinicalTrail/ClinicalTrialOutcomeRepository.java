package com.todaysoft.cpa.domain.clinicalTrail;

import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:04
 */
public interface ClinicalTrialOutcomeRepository extends JpaRepository<ClinicalTrialOutcome,String>{
}
