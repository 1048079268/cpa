package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.Indication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:37
 */
public interface CnIndicationRepository extends JpaRepository<Indication,String> {
    List<Indication> findByCreatedWay(Integer createdWay);
    Indication findByCreatedWayAndMeddraConceptName(Integer createdWay, String meddraConceptName);
}
