package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.SideEffectRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.SideEffect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public SideEffect save(SideEffect sideEffect, Drug drug) {
        return null;
    }
}
