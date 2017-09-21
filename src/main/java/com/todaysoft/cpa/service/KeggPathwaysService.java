package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import com.todaysoft.cpa.utils.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 18:16
 */
@Service
public class KeggPathwaysService{
    private final Lock lock=new ReentrantLock();
    private static Map<String,KeggPathway> KEGG_PATHWAY_MAP=new HashMap();
    @Autowired
    private KeggPathwayRepository keggPathwayRepository;

    public void init(){
        keggPathwayRepository.findByCPA().stream().forEach(keggPathway -> {
            KEGG_PATHWAY_MAP.put(keggPathway.getKeggId(),keggPathway);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public KeggPathway save(KeggPathway keggPathway){
        lock.lock();
        try {
            if (!KEGG_PATHWAY_MAP.containsKey(keggPathway.getKeggId())){
                KeggPathway pathway=keggPathwayRepository.save(keggPathway);
                if (pathway==null){
                    System.out.println("KeggPathwaysService:-->"+keggPathway.getKeggId());
                }
                KEGG_PATHWAY_MAP.put(keggPathway.getKeggId(),pathway);
            }
            return KEGG_PATHWAY_MAP.get(keggPathway.getKeggId());
        } finally {
            lock.unlock();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<KeggPathway> saveList(List<KeggPathway> keggPathwayList) throws InterruptedException {
        try {
//            lock.lockInterruptibly();
                List<KeggPathway> resultList=new ArrayList<>();
                for (KeggPathway pathway:keggPathwayList){
                    KeggPathway keggPathway;
                    if (KEGG_PATHWAY_MAP.containsKey(pathway.getKeggId())){
                        keggPathway=KEGG_PATHWAY_MAP.get(pathway.getKeggId());
                    }else {
                        keggPathway=keggPathwayRepository.save(pathway);
                        KEGG_PATHWAY_MAP.put(pathway.getKeggId(),keggPathway);
                    }
                    resultList.add(keggPathway);
                }
                return resultList;
        }finally {
//            lock.unlock();
        }
    }
}
