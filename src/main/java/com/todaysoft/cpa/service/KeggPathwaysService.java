package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 18:16
 */
@Service
public class KeggPathwaysService {
    private final ReentrantLock lock=new ReentrantLock();
    private static final Map<String,KeggPathway> PATH_ID=new HashMap();
    @Autowired
    private KeggPathwayRepository keggPathwayRepository;

    public void init(){
        keggPathwayRepository.findIdByCPA().stream().forEach(keggPathway -> {
            PATH_ID.put(keggPathway.getKeggId(),keggPathway);
        });
    }

    public KeggPathway save(KeggPathway keggPathway){
        lock.lock();
        try {
            if (PATH_ID.containsKey(keggPathway.getKeggId())){
                return PATH_ID.get(keggPathway.getKeggId());
            }
            KeggPathway ret=keggPathwayRepository.save(keggPathway);
            PATH_ID.put(ret.getKeggId(),ret);
            return ret;
        }finally {
            lock.unlock();
        }
    }
}
