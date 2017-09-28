package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:09
 */
public interface CnDrugExternalIdRepository extends JpaRepository<DrugExternalId,String> {
}
