package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
