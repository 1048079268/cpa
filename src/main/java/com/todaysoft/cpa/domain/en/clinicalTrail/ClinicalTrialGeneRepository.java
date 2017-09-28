package com.todaysoft.cpa.domain.en.clinicalTrail;

import com.todaysoft.cpa.domain.entity.ClinicalTrialGene;
import com.todaysoft.cpa.domain.entity.ClinicalTrialGenePK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:05
 */
public interface ClinicalTrialGeneRepository extends JpaRepository<ClinicalTrialGene,ClinicalTrialGenePK> {
}
