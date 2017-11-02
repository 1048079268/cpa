package com.todaysoft.cpa.domain.en.proteins;

import com.todaysoft.cpa.domain.entity.Protein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:26
 */
public interface ProteinRepository extends JpaRepository<Protein,String>{
    @Query("select p.proteinId from Protein p where p.createWay=2")
    Set<Integer> findIdByCPA();

    @Query("select distinct p from Protein p,Gene g where p.geneKey=g.geneKey and " +
            "g.createWay=2 and (g.cancerGene='oncogene/TSG' or g.cancerGene='TSG' or g.cancerGene='oncogene')")
    List<Protein> statistics();
}
