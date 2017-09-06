package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugClinicalTrial;
import com.todaysoft.cpa.domain.drug.entity.DrugClinicalTrialPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/6 13:52
 */
public interface DrugClinicalTrialRepository extends JpaRepository<DrugClinicalTrial,DrugClinicalTrialPK>{
}
