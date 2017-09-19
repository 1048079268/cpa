package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.SideEffectRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.SideEffect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final Map<String,SideEffect> SIDE_EFFECT_MAP=new HashMap<>();
    @Autowired
    private SideEffectRepository sideEffectRepository;
    public void init() {
        sideEffectRepository.findByCreatedWay(2).stream().forEach(sideEffect -> {
            SIDE_EFFECT_MAP.put(sideEffect.getSideEffectName(),sideEffect);
        });
    }

    @Transactional
    public SideEffect save(SideEffect sideEffect){
        lock.lock();
        try {
            if (!SIDE_EFFECT_MAP.containsKey(sideEffect.getSideEffectName())){
                SideEffect effect=sideEffectRepository.save(sideEffect);
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
}
