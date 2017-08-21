package com.todaysoft.cpa.domain.proteins;

import com.todaysoft.cpa.domain.proteins.entity.Protein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:26
 */
public interface ProteinRepository extends JpaRepository<Protein,String>{
    @Query("select p.proteinId from Protein p where p.createWay=2")
    Set<Integer> findIdByCPA();
}
