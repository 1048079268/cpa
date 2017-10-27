package com.todaysoft.cpa.domain.cn.gene;

import com.todaysoft.cpa.domain.entity.Gene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:26
 */
public interface CnGeneRepository extends JpaRepository<Gene,String>{
    @Query("select g.geneId from Gene g where g.createWay=2")
    Set<Integer> findIdByCPA();

    Gene findByGeneIdAndCreateWay(Integer geneId, Integer createWay);

    @Query("select g from Gene g where g.geneSymbol=?1 and g.createWay=3")
    Gene findByName(String name);
}
