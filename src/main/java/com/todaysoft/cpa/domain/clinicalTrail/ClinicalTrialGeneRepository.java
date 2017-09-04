package com.todaysoft.cpa.domain.clinicalTrail;

import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialGene;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialGenePK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:05
 */
public interface ClinicalTrialGeneRepository extends JpaRepository<ClinicalTrialGene,ClinicalTrialGenePK> {
}
