package com.todaysoft.cpa.domain.cn.proteins;

import com.todaysoft.cpa.domain.entity.Protein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:26
 */
public interface CnProteinRepository extends JpaRepository<Protein,String>{
    @Query("select p.proteinId from Protein p where p.createWay=2")
    Set<Integer> findIdByCPA();

    Protein findByProteinId(String proteinId);
}
