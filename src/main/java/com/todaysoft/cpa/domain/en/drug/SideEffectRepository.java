package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.SideEffect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:38
 */
public interface SideEffectRepository extends JpaRepository<SideEffect,String>{
    List<SideEffect> findByCreatedWay(Integer createdWay);
    SideEffect findByCreatedWayAndSideEffectNameAndKindOfTerm(Integer createdWay, String sideEffectName, String kindOfTerm);
}
