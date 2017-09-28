package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.cn.drug.CnSideEffectRepository;
import com.todaysoft.cpa.domain.en.drug.SideEffectRepository;
import com.todaysoft.cpa.domain.entity.SideEffect;
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
 * @date: 2017/9/1 9:48
 */
@Service
public class SideEffectService{
    private final Lock lock=new ReentrantLock();
    private static final Map<String,SideEffect> SIDE_EFFECT_MAP=new HashMap<>();
    @Autowired
    private SideEffectRepository sideEffectRepository;
    @Autowired
    private CnSideEffectRepository cnSideEffectRepository;
    public void init() {
        sideEffectRepository.findByCreatedWay(2).stream().forEach(sideEffect -> {
            SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),sideEffect);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SideEffect save(SideEffect sideEffect){
        lock.lock();
        try {
            if (!SIDE_EFFECT_MAP.containsKey(sideEffect.getSideEffectName())){
                SideEffect effect=sideEffectRepository.save(sideEffect);
                cnSideEffectRepository.save(effect);
                if (effect==null){
                    System.out.println("SideEffectService:-->"+effect.getSideEffectName());
                }
                SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),effect);
            }
            return SIDE_EFFECT_MAP.get(sideEffect.getSideEffectName());
        }finally {
            lock.unlock();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<SideEffect> saveList(List<SideEffect> sideEffectList) throws InterruptedException {
        try {
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
        }finally {
//            lock.unlock();
        }
    }
}
