package com.todaysoft.cpa.domain.evidence;

import com.todaysoft.cpa.domain.evidence.entity.EvidenceDrug;
import com.todaysoft.cpa.domain.evidence.entity.EvidenceDrugPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:41
 */
public interface EvidenceDrugRepository extends JpaRepository<EvidenceDrug,EvidenceDrugPK>{
}
