package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 11:07
 */
@Service
public class CancerService {
    private final ReentrantLock lock=new ReentrantLock();
    private static Map<String,Cancer> CANCER_MAP=new HashMap<>();
    @Autowired
    private CancerRepository cancerRepository;
    public void  init (){
        cancerRepository.findAll().stream().forEach(cancer -> {
            CANCER_MAP.put(cancer.getDoid(),cancer);
        });
    }

    public Cancer save(Cancer cancer){
        lock.lock();
        try {
            Cancer cer;
            if (CANCER_MAP.containsKey(cancer.getDoid())){
                cer=CANCER_MAP.get(cancer.getDoid());
            }else {
                cer=cancerRepository.save(cancer);
                CANCER_MAP.put(cancer.getDoid(),cancer);
            }
            return cer;
        }finally {
            lock.unlock();
        }
    }
}
