package com.todaysoft.cpa.domain.variants;

import com.todaysoft.cpa.domain.variants.entity.VariantExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:55
 */
public interface VariantExternalIdRepository extends JpaRepository<VariantExternalId,String> {
}
