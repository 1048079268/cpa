package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:09
 */
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction,String> {
    @Query("select distinct di from DrugInteraction di,Drug d where di.drugKey=d.drugKey and d.oncoDrug=true ")
    List<DrugInteraction> statistics();
}
