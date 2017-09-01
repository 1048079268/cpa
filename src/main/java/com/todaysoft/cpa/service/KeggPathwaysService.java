package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.DrugKeggPathwayRepository;
import com.todaysoft.cpa.domain.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.DrugKeggPathway;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 18:16
 */
@Service
public class KeggPathwaysService{
    private final ReentrantLock lock=new ReentrantLock();
    private static Map<String,KeggPathway> KEGG_PATHWAY_MAP=new HashMap();
    @Autowired
    private KeggPathwayRepository keggPathwayRepository;

    public void init(){
        keggPathwayRepository.findByCPA().stream().forEach(keggPathway -> {
            KEGG_PATHWAY_MAP.put(keggPathway.getKeggId(),keggPathway);
        });
    }

    public KeggPathway save(KeggPathway keggPathway){
        lock.lock();
        try {
            KeggPathway pathway;
            if (KEGG_PATHWAY_MAP.containsKey(keggPathway.getKeggId())){
                pathway=KEGG_PATHWAY_MAP.get(keggPathway.getKeggId());
            }else {
                pathway=keggPathwayRepository.save(keggPathway);
                KEGG_PATHWAY_MAP.put(pathway.getKeggId(),pathway);
            }
            return pathway;
        } finally {
            lock.unlock();
        }
    }
}
