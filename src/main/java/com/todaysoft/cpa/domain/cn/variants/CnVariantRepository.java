package com.todaysoft.cpa.domain.cn.variants;

import com.todaysoft.cpa.domain.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:54
 */
public interface CnVariantRepository extends JpaRepository<Variant,String> {
    @Query("select v.variantId from Variant v where v.createdWay=2")
    Set<Integer> findIdByCPA();

    Variant findByCosmicIdAndCreatedWay(String cosmicId, Integer createdWay);

    Variant findByVariantIdAndCreatedWay(Integer variantId, Integer createdWay);

    Variant findByVariantId(Integer variantId);

    @Modifying
    @Transactional
    void deleteByVariantIdAndCreatedWay(Integer variantId, Integer createdWay);
}
