package com.todaysoft.cpa.domain.en.gene;

import com.todaysoft.cpa.domain.entity.Gene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:26
 */
public interface GeneRepository extends JpaRepository<Gene,String>{
    @Query("select g.geneId from Gene g where g.createWay=2")
    Set<Integer> findIdByCPA();

    Gene findByGeneIdAndCreateWay(Integer geneId,Integer createWay);
    @Query("select g.entrezGeneSummary from Gene g " +
            "where g.createWay=2 and (g.cancerGene='oncogene/TSG' or g.cancerGene='TSG' or g.cancerGene='oncogene')")
    List<String> statistics();

    @Query("select g.geneKey from Gene g " +
            "where g.createWay=2 and (g.cancerGene='oncogene/TSG' or g.cancerGene='TSG' or g.cancerGene='oncogene')")
    Set<String> statisticsKey();

    Gene findByGeneId(Integer geneId);
}
