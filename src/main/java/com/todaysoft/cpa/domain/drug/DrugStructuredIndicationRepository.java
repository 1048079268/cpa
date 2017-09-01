package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugStructuredIndication;
import com.todaysoft.cpa.domain.drug.entity.DrugStructuredIndicationPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface DrugStructuredIndicationRepository extends JpaRepository<DrugStructuredIndication,DrugStructuredIndicationPK>{
}
