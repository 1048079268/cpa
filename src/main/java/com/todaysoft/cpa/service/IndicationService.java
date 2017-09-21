package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.DrugInteractionRepository;
import com.todaysoft.cpa.domain.drug.IndicationRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.Indication;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import com.todaysoft.cpa.utils.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
            String key=indication.getMeddraConceptName();
            INDICATION_MAP.put(key,indication);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Indication save(Indication indication) {
        lock.lock();
        try {
            String key=indication.getMeddraConceptName();
            if (!INDICATION_MAP.containsKey(key)){
                Indication indic=indicationRepository.save(indication);
                if (indic==null){
                    System.out.println("IndicationService:-->"+indication.getMeddraConceptName());
                }
                INDICATION_MAP.put(key,indic);
            }
            return INDICATION_MAP.get(key);
        }finally {
            lock.unlock();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Indication> saveList(List<Indication> indicationList) throws InterruptedException {
        long start=0L;
        try {
            lock.lock();
            List<Indication> retList=new ArrayList<>();
            for (Indication indication:indicationList){
                Indication result;
                String key=indication.getMeddraConceptName();
                if (!INDICATION_MAP.containsKey(key)){
                    result=indicationRepository.save(indication);
                    INDICATION_MAP.put(key,result);
                }else {
                    result=INDICATION_MAP.get(key);
                }
                retList.add(result);
            }
            return retList;
        }finally {
            lock.unlock();
        }
    }
}
