package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugAdverseReaction;
import com.todaysoft.cpa.domain.entity.DrugAdverseReactionPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:08
 */
public interface DrugAdverseReactionRepository extends JpaRepository<DrugAdverseReaction,DrugAdverseReactionPK>{
}
