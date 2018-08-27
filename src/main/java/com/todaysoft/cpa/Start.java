package com.todaysoft.cpa;

import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.compare.JsonDataType;
import com.todaysoft.cpa.mongo.TaskManagerService;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.service.FluxManagerService;
import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.service.vice.KeggPathwaysService;
import com.todaysoft.cpa.statistics.StatisticsService;
import com.todaysoft.cpa.statistics.CountService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

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
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private TaskManagerService taskManagerService;
    @Autowired
    private Environment environment;
    @Autowired
    private KeggPathwaysService keggPathwaysService;
    @Autowired
    private FluxManagerService fluxManagerService;
    @Override
    public void run(String... strings) throws IOException, InterruptedException, InvalidFormatException {
        initCpaModule();
        //启动清除重复数据
        fluxManagerService.clearDupData();
//        mainService.manager();
        logger.info("<<<<<<<<<启动完成>>>>>>>>>");
    }

    /**
     * 初始化各模块信息
     */
    private void initCpaModule(){
        //通路需要初始化
        keggPathwaysService.init();
        GlobalVar.setAUTHORIZATION(environment.getProperty("api.authorization"));
        //初始化各模块信息
        for (CPA module : CPA.values()) {
            module.setName(environment.getProperty("api."+module.getPropField()+"Name"));
            module.setContentUrl(environment.getProperty("api."+module.getPropField()+"Url"));
            try {
                Map<String, JsonDataType> jsonKeyMap = AcquireJsonStructure.getJsonKeyMap(environment.getProperty("api." + module.getPropField() + "TempPath"));
                module.setTempStructureMap(jsonKeyMap);
            } catch (FileNotFoundException e) {
                logger.error("["+module+"] Json 模板加载错误",e);
            }
        }
    }
}
