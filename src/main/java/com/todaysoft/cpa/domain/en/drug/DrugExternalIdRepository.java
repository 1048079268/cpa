package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:09
 */
public interface DrugExternalIdRepository extends JpaRepository<DrugExternalId,String> {
    List<DrugExternalId> findByDrugKey(String drugKey);
}
