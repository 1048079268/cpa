package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.DrugInteractionRepository;
import com.todaysoft.cpa.domain.drug.IndicationRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.Indication;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 10:15
 */
@Service
public class IndicationService{
    private final ReentrantLock lock=new ReentrantLock();
    private static Map<String,Indication> INDICATION_MAP=new HashMap();
    @Autowired
    private IndicationRepository indicationRepository;
    public void init() {
        indicationRepository.findByCreatedWay(2).stream().forEach(indication -> {
//            INDICATION_MAP.put(indication.)
        });
    }

    public Indication save(Indication indication, Drug drug) {
        return null;
    }
}
