package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugProduct;
import com.todaysoft.cpa.domain.drug.entity.DrugProductEtnId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface DrugProductEtnIdRepository extends JpaRepository<DrugProductEtnId,String> {
}
