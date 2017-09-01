package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.SideEffectRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.SideEffect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:48
 */
@Service
public class SideEffectService{
    private final ReentrantLock lock=new ReentrantLock();
    private static final Map<String,List<SideEffect>> SIDE_EFFECT_MAP=new HashMap<>();
    @Autowired
    private SideEffectRepository sideEffectRepository;
    public void init() {
        sideEffectRepository.findAll().stream().forEach(sideEffect -> {
            if (SIDE_EFFECT_MAP.containsKey(sideEffect.getSideEffectName())){
                List<SideEffect> sideEffectList=SIDE_EFFECT_MAP.get(sideEffect.getSideEffectName());
                sideEffectList.add(sideEffect);
                SIDE_EFFECT_MAP.replace(sideEffect.getSideEffectName(),sideEffectList);
            }else {
                List<SideEffect> sideEffectList=new ArrayList<>();
                sideEffectList.add(sideEffect);
                SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),sideEffectList);
            }
        });
    }

    public List<SideEffect> save(SideEffect sideEffect){
        lock.lock();
        try {
            if (SIDE_EFFECT_MAP.containsKey(sideEffect.getSideEffectName())){
                return SIDE_EFFECT_MAP.get(sideEffect.getSideEffectName());
            }else {
                List<SideEffect> sideEffectList=new ArrayList<>();
                sideEffect=sideEffectRepository.save(sideEffect);
                sideEffectList.add(sideEffect);
                SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),sideEffectList);
                return SIDE_EFFECT_MAP.get(sideEffect.getSideEffectName());
            }
        }finally {
            lock.unlock();
        }
    }
}
