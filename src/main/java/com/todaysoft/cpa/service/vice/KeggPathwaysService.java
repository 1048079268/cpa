package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnKeggPathwayRepository;
import com.todaysoft.cpa.domain.en.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.entity.KeggPathway;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private static Logger logger= LoggerFactory.getLogger(KeggPathwaysService.class);
    private final Lock lock=new ReentrantLock();
    private static Map<String,KeggPathway> KEGG_PATHWAY_MAP=new HashMap();
    private static Map<String,KeggPathway> pathwayMap=new HashMap<>();
    @Autowired
    private KeggPathwayRepository keggPathwayRepository;
    @Autowired
    private CnKeggPathwayRepository cnKeggPathwayRepository;

    @Async
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public KeggPathway save(KeggPathway cnKeggPathway,KeggPathway enKeggPathway){
        String key= PkGenerator.generator(KeggPathway.class);
        cnKeggPathway.setPathwayKey(key);
        enKeggPathway.setPathwayKey(key);
        //此处使用en的做比较是因为老库的数据也是英文的
        String compareName=enKeggPathway.getPathwayName().toLowerCase().trim();
        //中文老库已有该记录
        if (pathwayMap.containsKey(compareName)) {
            //中文的状态与英文状态一致
            KeggPathway pathway = pathwayMap.get(compareName);
            pathway.setCreateWay(cnKeggPathway.getCreateWay());
            pathway.setCheckState(cnKeggPathway.getCheckState());
            pathway.setCreateAt(cnKeggPathway.getCreateAt());
            pathway.setKeggId(cnKeggPathway.getKeggId());
            cnKeggPathwayRepository.save(pathway);
            //英文主键与中文库主键一致
            enKeggPathway.setPathwayKey(pathway.getPathwayKey());
            enKeggPathway=keggPathwayRepository.save(enKeggPathway);
            pathwayMap.remove(compareName);
            KEGG_PATHWAY_MAP.put(enKeggPathway.getKeggId(),enKeggPathway);
            logger.info("【KeggPathway】与老库合并->id="+enKeggPathway.getKeggId());
            return enKeggPathway;
        } else {
            //老库没有记录的话查询keggId有没有重的
            if (KEGG_PATHWAY_MAP.containsKey(enKeggPathway.getKeggId())){
                return KEGG_PATHWAY_MAP.get(enKeggPathway.getKeggId());
            }else {
                KeggPathway keggPathway = keggPathwayRepository.save(enKeggPathway);
                cnKeggPathwayRepository.save(cnKeggPathway);
                KEGG_PATHWAY_MAP.put(keggPathway.getKeggId(),keggPathway);
                //返回英文库数据
                return keggPathway;
            }
        }
    }
}
