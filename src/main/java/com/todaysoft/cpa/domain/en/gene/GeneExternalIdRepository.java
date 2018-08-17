package com.todaysoft.cpa.domain.en.gene;

import com.todaysoft.cpa.domain.entity.GeneExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:42
 */
public interface GeneExternalIdRepository extends JpaRepository<GeneExternalId,String> {
    List<GeneExternalId> findByGeneKey(String geneKey);
}
