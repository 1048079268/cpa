package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.DrugInteractionRepository;
import com.todaysoft.cpa.domain.drug.IndicationRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.Indication;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static Map<String,List<Indication>> INDICATION_MAP=new HashMap();
    @Autowired
    private IndicationRepository indicationRepository;

    public void init() {
        indicationRepository.findAll().stream().forEach(indication -> {
            String key=indication.getMeddraConceptName();
            List<Indication> indicationList;
            if (INDICATION_MAP.containsKey(key)){
                indicationList=INDICATION_MAP.get(key);
                indicationList.add(indication);
                INDICATION_MAP.replace(key,indicationList);
            }else {
                indicationList=new ArrayList<>();
                indicationList.add(indication);
                INDICATION_MAP.put(key,indicationList);
            }
        });
    }

    public List<Indication> save(Indication indication) {
        lock.lock();
        try {
            String key=indication.getMeddraConceptName();
            if (INDICATION_MAP.containsKey(key)){
                return INDICATION_MAP.get(key);
            }else {
                List<Indication> indicationList=new ArrayList<>();
                indication=indicationRepository.save(indication);
                indicationList.add(indication);
                INDICATION_MAP.put(key,indicationList);
                return INDICATION_MAP.get(key);
            }
        }finally {
            lock.unlock();
        }
    }
}
