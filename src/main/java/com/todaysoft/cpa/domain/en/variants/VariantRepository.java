package com.todaysoft.cpa.domain.en.variants;

import com.todaysoft.cpa.domain.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Query(value = "SELECT d.variant_id FROM kt_variant d WHERE d.variant_id > 0 GROUP BY d.variant_id HAVING COUNT(d.variant_id) > 1 LIMIT ?1,?2",nativeQuery = true)
    List<Integer> findDuplicateId(Integer start,Integer limit);

    @Modifying
    @Transactional
    void deleteByVariantIdAndCreatedWay(Integer variantId, Integer createdWay);
}
