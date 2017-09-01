package com.todaysoft.cpa.domain.drug;

import com.todaysoft.cpa.domain.drug.entity.Indication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:37
 */
public interface IndicationRepository extends JpaRepository<Indication,String> {
    List<Indication> findByCreatedWay(Integer createdWay);
}
