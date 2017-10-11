package com.todaysoft.cpa.domain.en.variants;

import com.todaysoft.cpa.domain.entity.VariantMutationStatistic;
import com.todaysoft.cpa.domain.entity.VariantMutationStatisticPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:24
 */
public interface VariantMutationStatisticRepository extends JpaRepository<VariantMutationStatistic,VariantMutationStatisticPK> {
}