package com.todaysoft.cpa.domain.en.gene;

import com.todaysoft.cpa.domain.entity.GeneOtherName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:44
 */
public interface GeneOtherNameRepository extends JpaRepository<GeneOtherName,String> {
    @Query("select go.otherName from GeneOtherName go ,Gene g where g.geneKey=go.geneKey and " +
            "g.createWay=2 and (g.cancerGene='oncogene/TSG' or g.cancerGene='TSG' or g.cancerGene='oncogene')")
    List<String> statistics();
}
