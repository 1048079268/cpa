package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.DrugCategory;
import com.todaysoft.cpa.domain.drug.entity.DrugCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:57
 */
public interface DrugCategoryRepository extends JpaRepository<DrugCategory,DrugCategoryPK> {

}
