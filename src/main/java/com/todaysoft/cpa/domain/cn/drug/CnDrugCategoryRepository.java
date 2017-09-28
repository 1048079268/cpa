package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugCategory;
import com.todaysoft.cpa.domain.entity.DrugCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:57
 */
public interface CnDrugCategoryRepository extends JpaRepository<DrugCategory,DrugCategoryPK> {

}
