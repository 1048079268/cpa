package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.MeshCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:55
 */
public interface MeshCategoryRepository extends JpaRepository<MeshCategory,String>{
    @Query("select m from MeshCategory m where m.createdWay=2")
    List<MeshCategory> findByCPA();

    MeshCategory findByMeshId(String meshId);

    @Transactional
    @Procedure(name = "proc_mesh_category")
    void saveRel(@Param("meshKey") String meshKey,@Param("meshId") String meshId,
                 @Param("categoryName") String categoryName,@Param("drugKey") String drugKey,
                 @Param("drugId") Integer drugId);
}
