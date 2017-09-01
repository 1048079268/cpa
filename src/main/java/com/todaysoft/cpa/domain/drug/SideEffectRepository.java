package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.Indication;
import com.todaysoft.cpa.domain.drug.entity.SideEffect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:38
 */
public interface SideEffectRepository extends JpaRepository<SideEffect,String>{
    List<SideEffect> findByCreatedWay(Integer createdWay);
}
