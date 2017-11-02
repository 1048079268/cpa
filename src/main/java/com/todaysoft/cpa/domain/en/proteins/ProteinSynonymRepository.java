package com.todaysoft.cpa.domain.en.proteins;

import com.todaysoft.cpa.domain.entity.ProteinSynonym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:30
 */
public interface ProteinSynonymRepository extends JpaRepository<ProteinSynonym,String> {
    @Query("select distinct ps from ProteinSynonym ps,Protein p,Gene g where g.geneKey=p.geneKey and ps.proteinKey=p.proteinKey and " +
            "p.createWay=2 and (g.cancerGene='oncogene/TSG' or g.cancerGene='TSG' or g.cancerGene='oncogene')")
    List<ProteinSynonym> statistics();
}
