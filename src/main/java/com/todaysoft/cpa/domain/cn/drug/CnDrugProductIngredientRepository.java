package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugProductIngredient;
import com.todaysoft.cpa.domain.entity.DrugProductIngredientPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:40
 */
public interface CnDrugProductIngredientRepository extends JpaRepository<DrugProductIngredient,DrugProductIngredientPK> {
}
