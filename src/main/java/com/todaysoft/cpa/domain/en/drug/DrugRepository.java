package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:02
 */
public interface DrugRepository extends JpaRepository<Drug,String>{
    @Query(value = "select distinct d.drugId from Drug d where 1=1 and d.createWay=2")
    Set<Integer> findIdByCPA();

    @Query("select d from Drug d where d.drugId=?1 and d.createWay=2")
    Drug findByDrugId(Integer drugId);

    @Query("select d.drugKey from Drug d where d.createWay=2 and d.oncoDrug=true")
    Set<String> statisticsKey();

    @Query("select d from Drug d where d.createWay=2 and d.oncoDrug=true")
    Set<Drug> statistics();
}
