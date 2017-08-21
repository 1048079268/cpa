package com.todaysoft.cpa.domain.proteins;

import com.todaysoft.cpa.domain.proteins.entity.ProteinSynonym;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:30
 */
public interface ProteinSynonymRepository extends JpaRepository<ProteinSynonym,String> {
}
