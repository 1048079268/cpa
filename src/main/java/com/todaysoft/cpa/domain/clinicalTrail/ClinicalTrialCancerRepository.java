package com.todaysoft.cpa.domain.clinicalTrail;

import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrial;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialCancer;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialCancerPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:04
 */
public interface ClinicalTrialCancerRepository extends JpaRepository<ClinicalTrialCancer,ClinicalTrialCancerPK> {
}
