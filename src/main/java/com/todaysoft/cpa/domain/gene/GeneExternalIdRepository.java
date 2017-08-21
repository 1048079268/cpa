package com.todaysoft.cpa.domain.gene;

import com.todaysoft.cpa.domain.gene.entity.GeneExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:42
 */
public interface GeneExternalIdRepository extends JpaRepository<GeneExternalId,String> {
}
