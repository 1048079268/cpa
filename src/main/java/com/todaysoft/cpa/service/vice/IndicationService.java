package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnIndicationRepository;
import com.todaysoft.cpa.domain.en.drug.IndicationRepository;
import com.todaysoft.cpa.domain.entity.Indication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 10:15
 */
@Service
public class IndicationService{
    private final Lock lock=new ReentrantLock();
    private static Map<String,Indication> INDICATION_MAP=new HashMap();
    @Autowired
    private IndicationRepository indicationRepository;
    @Autowired
    private CnIndicationRepository cnIndicationRepository;

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
                cnIndicationRepository.save(indic);
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
        try {
//            lock.lock();
                List<Indication> retList=new ArrayList<>();
                for (Indication indication:indicationList){
                    Indication result;
                    String key=indication.getMeddraConceptName();
                    if (!INDICATION_MAP.containsKey(key)){
                        result=indicationRepository.save(indication);
                        cnIndicationRepository.save(result);
                        INDICATION_MAP.put(key,result);
                    }else {
                        result=INDICATION_MAP.get(key);
                    }
                    retList.add(result);
                }
                return retList;
        }finally {
//            lock.unlock();
        }
    }
}
