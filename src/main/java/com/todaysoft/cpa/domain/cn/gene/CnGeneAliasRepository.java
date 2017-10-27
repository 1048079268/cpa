package com.todaysoft.cpa.domain.cn.gene;

import com.todaysoft.cpa.domain.entity.GeneAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:40
 */
public interface CnGeneAliasRepository extends JpaRepository<GeneAlias,String> {
    @Query("select g from GeneAlias g where g.geneKey=?1 and g.geneAlias=?2")
    GeneAlias findByGeneKeyAndGeneAlias(String geneKey,String geneAlias);
}
