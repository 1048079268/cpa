package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:11
 */
public interface KeggPathwayRepository extends JpaRepository<KeggPathway,String> {
    @Query("select k from KeggPathway k where k.createWay=2")
    List<KeggPathway>findByCPA();

    KeggPathway findByKeggId(String keggId);
}
