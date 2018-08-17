package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnKeggPathwayRepository;
import com.todaysoft.cpa.domain.en.drug.KeggPathwayRepository;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.entity.KeggPathway;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.MergeException;
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
    @Autowired
    private KbUpdateService kbUpdateService;

    public void init(){
        cnKeggPathwayRepository.findByCreateWay(3).forEach(keggPathway -> {
            String idKey=keggPathway.getKeggId().replace("hsa|map","").trim();
            pathwayMap.put(idKey,keggPathway);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Deprecated
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
    public KeggPathway save(KeggPathway cnKeggPathway, KeggPathway enKeggPathway, Drug drug, Map<String,Integer> status){
        //此处使用en的做比较是因为老库的数据也是英文的
        String compareKey=enKeggPathway.getKeggId().replaceAll("path:hsa","").trim();
        KeggPathway oldEn = keggPathwayRepository.findByKeggId(enKeggPathway.getKeggId());
        KeggPathway oldCn = cnKeggPathwayRepository.findByKeggId(enKeggPathway.getKeggId());
        //是否使用老中文库数据状态
        boolean isUseOldCnState=oldCn!=null;
        //是否使用老英文库数据状态
        boolean isUseOldEnState=oldEn!=null;
        //是否保存中文数据
        boolean isSaveCn = oldEn==null;
        //是否是老库覆盖CPA数据
        boolean isOldBaseData= oldEn==null&&pathwayMap.containsKey(compareKey);
        //判断CPA
        String key= oldCn==null?PkGenerator.generator(KeggPathway.class):oldCn.getPathwayKey();
        cnKeggPathway.setPathwayKey(key);
        enKeggPathway.setPathwayKey(key);
        //更新状态
        if (isUseOldCnState){
            cnKeggPathway.setCreateWay(oldCn.getCreateWay());
            cnKeggPathway.setCheckState(oldCn.getCheckState());
            cnKeggPathway.setCreatedByName(oldCn.getCreatedByName());
        }
        if (isUseOldEnState){
            enKeggPathway.setCreateWay(oldEn.getCreateWay());
            enKeggPathway.setCheckState(oldEn.getCheckState());
            enKeggPathway.setCreatedByName(oldEn.getCreatedByName());
        }
        if (isOldBaseData){
            KeggPathway keggPathway = pathwayMap.get(compareKey);
            enKeggPathway.setPathwayKey(keggPathway.getPathwayKey());
            cnKeggPathway.setPathwayKey(keggPathway.getPathwayKey());
            enKeggPathway.setCheckState(4);
            cnKeggPathway.setKeggLink(keggPathway.getKeggLink());
            cnKeggPathway.setPathwayName(keggPathway.getPathwayName());
            cnKeggPathway.setSelleckLink(keggPathway.getSelleckLink());
        }
        KeggPathway pathway = keggPathwayRepository.save(enKeggPathway);
        if (isSaveCn){
            cnKeggPathwayRepository.save(cnKeggPathway);
        }
        if (oldEn==null&&pathway.getCheckState()==1){
            kbUpdateService.send("kt_kegg_pathway");
        }
        return pathway;
    }
}
