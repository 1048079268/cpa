package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface DrugSequenceRepository extends JpaRepository<DrugSequence,String> {
    List<DrugSequence> findByDrugKey(String drugKey);
}
