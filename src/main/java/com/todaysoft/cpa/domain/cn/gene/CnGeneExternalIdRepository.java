package com.todaysoft.cpa.domain.cn.gene;

import com.todaysoft.cpa.domain.entity.GeneExternalId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:42
 */
public interface CnGeneExternalIdRepository extends JpaRepository<GeneExternalId,String> {
}
