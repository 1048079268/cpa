package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugKeggPathway;
import com.todaysoft.cpa.domain.drug.entity.DrugKeggPathwayPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:09
 */
public interface DrugKeggPathwayRepository extends JpaRepository<DrugKeggPathway,DrugKeggPathwayPK> {
}
