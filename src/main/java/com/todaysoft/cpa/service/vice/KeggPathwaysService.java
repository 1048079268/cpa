package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnKeggPathwayRepository;
import com.todaysoft.cpa.domain.en.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.entity.KeggPathway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private static Map<String,KeggPathway> pathwayMap=new HashMap<>();
    @Autowired
    private KeggPathwayRepository keggPathwayRepository;
    @Autowired
    private CnKeggPathwayRepository cnKeggPathwayRepository;

    public void init(){
        cnKeggPathwayRepository.findByCreateWay(3).forEach(keggPathway -> {
            String key=keggPathway.getPathwayName().replaceAll("[\\u4e00-\\u9fa5]","").toLowerCase().trim();
            pathwayMap.put(key,keggPathway);
        });
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
                cnKeggPathwayRepository.save(keggPathway);
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
        List<KeggPathway> resultList=new ArrayList<>();
        for (KeggPathway pathway:keggPathwayList){
            KeggPathway keggPathway;
            if (KEGG_PATHWAY_MAP.containsKey(pathway.getKeggId())){
                keggPathway=KEGG_PATHWAY_MAP.get(pathway.getKeggId());
            }else {
                //要做查重
                KeggPathway check = pathwayMap.get(pathway.getPathwayName().toLowerCase().trim());
                if (check!=null){
                    //英文的主键要与中文主键一致
                    pathway.setPathwayKey(check.getPathwayKey());
                    keggPathway=keggPathwayRepository.save(pathway);
                    //中文的主id，创建信息等要和英文的一样
                    check.setKeggId(pathway.getKeggId());
                    check.setCheckState(pathway.getCheckState());
                    check.setCreateWay(pathway.getCreateWay());
                    check.setCreateAt(pathway.getCreateAt());
                    cnKeggPathwayRepository.save(check);
                }else {
                    keggPathway=keggPathwayRepository.save(pathway);
                    cnKeggPathwayRepository.save(keggPathway);
                }
                KEGG_PATHWAY_MAP.put(pathway.getKeggId(),keggPathway);
            }
            resultList.add(keggPathway);
        }
        return resultList;
    }
}
