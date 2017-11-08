package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.KeggPathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:11
 */
public interface CnKeggPathwayRepository extends JpaRepository<KeggPathway,String> {
    @Query("select k from KeggPathway k where k.createWay=2")
    List<KeggPathway>findByCPA();

    KeggPathway findByKeggId(String keggId);
    List<KeggPathway> findByCreateWay(Integer createWay);
}
