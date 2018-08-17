package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnSideEffectRepository;
import com.todaysoft.cpa.domain.en.drug.SideEffectRepository;
import com.todaysoft.cpa.domain.entity.SideEffect;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date: 2017/9/1 9:48
 */
@Service
public class SideEffectService{
    private static Logger logger= LoggerFactory.getLogger(SideEffectService.class);
    private final Lock lock=new ReentrantLock();
    private static final Map<String,SideEffect> SIDE_EFFECT_MAP=new HashMap<>();
    private static final Map<String,SideEffect> SIDE_EFFECT_MAP_OLD=new HashMap<>();
    @Autowired
    private SideEffectRepository sideEffectRepository;
    @Autowired
    private CnSideEffectRepository cnSideEffectRepository;
    @Autowired
    private KbUpdateService kbUpdateService;
    public void init() {
//        sideEffectRepository.findByCreatedWay(2).stream().forEach(sideEffect -> {
//            SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),sideEffect);
//        });
//        cnSideEffectRepository.findByCreatedWay(3).forEach(sideEffect -> {
//            SIDE_EFFECT_MAP_OLD.put(sideEffect.getSideEffectName(),sideEffect);
//        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Deprecated
    public List<SideEffect> saveList(List<SideEffect> sideEffectList) throws InterruptedException {
        List<SideEffect> resultList=new ArrayList<>();
        for (SideEffect sideEffect:sideEffectList){
            SideEffect effect;
            String key=sideEffect.getSideEffectName();
            if (SIDE_EFFECT_MAP.containsKey(key)){
                effect=SIDE_EFFECT_MAP.get(key);
            }else {
                effect=sideEffectRepository.save(sideEffect);
                cnSideEffectRepository.save(effect);
                SIDE_EFFECT_MAP.put(key,effect);
            }
            resultList.add(effect);
        }
        return resultList;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SideEffect save(SideEffect cnSideEffect,SideEffect enSideEffect){
        SideEffect old = cnSideEffectRepository.findByCreatedWayAndSideEffectNameAndKindOfTerm(2, enSideEffect.getSideEffectName(), enSideEffect.getKindOfTerm());
        boolean isSaveCn=old==null;
        boolean isUseOldState=old!=null;
        String generator = old==null?PkGenerator.generator(SideEffect.class):old.getSideEffectKey();
        cnSideEffect.setSideEffectKey(generator);
        enSideEffect.setSideEffectKey(generator);
        if (isUseOldState){
            cnSideEffect.setCheckState(old.getCheckState());
            cnSideEffect.setCreatedWay(old.getCreatedWay());
            cnSideEffect.setCreatedByName(old.getCreatedByName());
        }
        SideEffect effect=sideEffectRepository.save(enSideEffect);
        if (isSaveCn){
            cnSideEffectRepository.save(cnSideEffect);
        }
        if (old==null&&effect.getCheckState()==1){
            kbUpdateService.send("kt_side_effect");
        }
        return old;
        //TODO 暂时屏蔽与老库合并
//        String compareName=cnSideEffect.getSideEffectName();
//        if (SIDE_EFFECT_MAP_OLD.containsKey(compareName)){
//            SideEffect sideEffect = SIDE_EFFECT_MAP_OLD.get(compareName);
//            sideEffect.setCheckState(cnSideEffect.getCheckState());
//            sideEffect.setCreatedAt(cnSideEffect.getCreatedAt());
//            sideEffect.setCreatedWay(cnSideEffect.getCreatedWay());
//            cnSideEffectRepository.save(sideEffect);
//            enSideEffect.setSideEffectKey(sideEffect.getSideEffectKey());
//            enSideEffect=sideEffectRepository.save(enSideEffect);
//            SIDE_EFFECT_MAP_OLD.remove(compareName);
//            SIDE_EFFECT_MAP.put(compareName,enSideEffect);
//            logger.info("【SideEffect】与老库合并->key="+enSideEffect.getSideEffectKey());
//            return enSideEffect;
//        }else {
//            if (SIDE_EFFECT_MAP.containsKey(enSideEffect.getSideEffectName())){
//                return SIDE_EFFECT_MAP.get(enSideEffect.getSideEffectName());
//            }else {
//                enSideEffect=sideEffectRepository.save(enSideEffect);
//                cnSideEffectRepository.save(cnSideEffect);
//                SIDE_EFFECT_MAP_OLD.put(enSideEffect.getSideEffectName(),enSideEffect);
//                return enSideEffect;
//            }
//        }
    }
}
