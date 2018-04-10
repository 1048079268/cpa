package com.todaysoft.cpa.domain.en.variants;

import com.todaysoft.cpa.domain.entity.VariantMutationStatistic;
import com.todaysoft.cpa.domain.entity.VariantTumorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:56
 */
public interface VariantTumorTypeRepository extends JpaRepository<VariantTumorType,String> {
    @Query("select new com.todaysoft.cpa.domain.entity.VariantMutationStatistic(v2.typeKey,v3.doid,v1.cosmicId) " +
            "from Variant v1,VariantTumorType v2,VariantTumorTypeDoid  v3 " +
            "where v1.createdWay=2 and v1.variantKey=v2.variantKey and v2.typeKey =v3.typeKey ")
    List<VariantMutationStatistic> findMutationStatistics();

    @Query("select v1 from VariantTumorType v1,Variant v2,VariantTumorTypeDoid v3 " +
            "where v2.createdWay=2 and v1.variantKey=v2.variantKey and v1.typeKey=v3.typeKey " +
            "and v2.cosmicId=?2 and v3.doid=?1")
    VariantTumorType findByDoidAndCosmicId(String doid,String cosminId);

    @Query("select v1 from VariantTumorType v1,VariantTumorTypeDoid v2 " +
            "where v1.variantKey=?2 and v1.typeKey=v2.typeKey and v2.doid=?1")
    List<VariantTumorType> findByDoidAndVariantKey(Integer doid,String variantKey);
}
