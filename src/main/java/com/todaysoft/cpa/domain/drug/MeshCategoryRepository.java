package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.MeshCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:55
 */
public interface MeshCategoryRepository extends JpaRepository<MeshCategory,String>{
    @Query("select m from MeshCategory m where m.createdWay=2")
    List<MeshCategory> findByCPA();
    MeshCategory findByMeshId(String meshId);

}
