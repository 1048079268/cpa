package com.todaysoft.cpa.domain.variants;

import com.todaysoft.cpa.domain.variants.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:54
 */
public interface VariantRepository extends JpaRepository<Variant,String> {
    @Query("select v.variantId from Variant v where v.createdWay=2")
    Set<Integer> findIdByCPA();

    Variant findByCosmicId(String cosmicId);
}
