package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugSynonym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:11
 */
public interface CnDrugSynonymRepository extends JpaRepository<DrugSynonym,String> {
    @Query("select d from DrugSynonym d where d.drugKey=?1 and d.drugSynonym=?2")
    DrugSynonym findByDrugKeyAndSynonym(String drugKey,String synonym);
}
