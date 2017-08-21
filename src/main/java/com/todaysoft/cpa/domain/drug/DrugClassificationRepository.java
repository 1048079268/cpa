package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugClassification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:08
 */
public interface DrugClassificationRepository extends JpaRepository<DrugClassification,String> {
}
