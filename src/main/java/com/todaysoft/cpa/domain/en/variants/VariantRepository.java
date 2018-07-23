package com.todaysoft.cpa.domain.en.variants;

import com.todaysoft.cpa.domain.entity.Variant;
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

    @Query("select v.variantKey from Variant v where v.createdWay=?2 and v.cosmicId=?1")
    String findByCosmicIdAndCreatedWay(String cosmicId,Integer createdWay);

    @Query("select v.variantKey from Variant v where v.createdWay=?2 and v.variantId=?1")
    String findByVariantIdAndCreatedWay(Integer variantId,Integer createdWay);

    Variant findByVariantId(Integer variantId);
}
