package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnIndicationRepository;
import com.todaysoft.cpa.domain.en.drug.IndicationRepository;
import com.todaysoft.cpa.domain.entity.Evidence;
import com.todaysoft.cpa.domain.entity.Indication;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.PkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    private KbUpdateService kbUpdateService;

    public void init() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Deprecated
    public List<Indication> saveList(List<Indication> indicationList) {
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
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Indication save(Indication cnIndication,Indication enIndication){
        Indication old = indicationRepository.findByCreatedWayAndMeddraConceptNameAndMeddraConceptType(2,enIndication.getMeddraConceptName(),enIndication.getMeddraConceptType());
        boolean isSaveCn=old==null;
        boolean isUseOldState=old!=null;
        String key= old==null?PkGenerator.generator(Indication.class):old.getIndicationKey();
        cnIndication.setIndicationKey(key);
        enIndication.setIndicationKey(key);
        if (isUseOldState){
            enIndication.setCreatedByName(old.getCreatedByName());
            enIndication.setCheckState(old.getCheckState());
            enIndication.setCreatedWay(old.getCreatedWay());
        }
        Indication indication = indicationRepository.save(enIndication);
        if (isSaveCn){
            cnIndicationRepository.save(cnIndication);
        }
        if (old==null&&indication.getCheckState()==1){
            kbUpdateService.send("kt_indication");
        }
        return indication;
    }
}
