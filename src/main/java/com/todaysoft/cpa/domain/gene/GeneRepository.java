package com.todaysoft.cpa.domain.gene;

import com.todaysoft.cpa.domain.gene.entity.Gene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:26
 */
public interface GeneRepository extends JpaRepository<Gene,String>{
    @Query("select g.geneId from Gene g where g.createWay=2")
    Set<Integer> findIdByCPA();
}
