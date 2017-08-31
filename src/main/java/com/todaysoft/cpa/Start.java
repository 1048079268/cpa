package com.todaysoft.cpa;

import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.param.Param;
import com.todaysoft.cpa.service.KeggPathwaysService;
import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.thread.ContentManagerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @desc: 启动运行
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:10
 */
@Component
@Order(1) //启动的顺序
public class Start implements CommandLineRunner {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    @Autowired
    private MainService mainService;
    @Override
    public void run(String... strings) throws Exception {
        mainService.init();
        mainService.manager();
        logger.info("<<<<<<<<<启动完成>>>>>>>>>");
    }
}
