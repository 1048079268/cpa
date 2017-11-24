package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugOtherName;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface CnDrugOtherNameRepository extends JpaRepository<DrugOtherName,String> {
    DrugOtherName findByDrugKeyAndOtherName(String drugKey,String otherName);
}
