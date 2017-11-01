package com.todaysoft.cpa;

import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.total.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @desc: 启动运行
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:10
 */
@Component
@Order(1)
public class Start implements CommandLineRunner {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    @Autowired
    private MainService mainService;
    @Autowired
    private CountService countService;
    @Override
    public void run(String... strings) throws IOException, InterruptedException {
        mainService.init();
//        mainService.manager();
//        countService.countProtein();
//        countService.countMedicationPlan();
        countService.countClinicalTrial();
        countService.countGene();
        logger.info("<<<<<<<<<启动完成>>>>>>>>>");
    }
}
