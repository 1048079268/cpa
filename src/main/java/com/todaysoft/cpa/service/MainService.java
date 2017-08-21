package com.todaysoft.cpa.service;

import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.thread.ContentManagerThread;
import com.todaysoft.cpa.thread.IdThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 12:48
 */
@Service
public class MainService {
    private static Logger logger= LoggerFactory.getLogger(MainService.class);
    @Autowired
    private CPAProperties cpaProperties;
    @Async
    public void manager() throws InterruptedException {
        while (true){
            //启动内容和异常管理线程
            ContentManagerThread contentManage=new ContentManagerThread(cpaProperties.getMaxThreadNum());
            contentManage.start();
            //开始各业务的抓取id线程
            ExecutorService exe = Executors.newFixedThreadPool(cpaProperties.getMaxThreadNum());
            exe.execute(gene());
            exe.execute(drug());
            logger.info("【manager】线程全部启动完成");
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()&&contentManage.isAllWaiting()) {
                    logger.info("【manager】全部执行完成，休眠【"+cpaProperties.getHeartbeat()/60000+"】分钟...");
                    contentManage.stopAll();
                    break;
                }else {
                    Thread.sleep(10000);
                    contentManage.checkAndRestart();
                }
            }
            Thread.sleep(cpaProperties.getHeartbeat());
            logger.info("【manager】休眠结束，开始重启各线程...");
        }
    }

    @Autowired
    private DrugService drugService;
    public void initDrug(){
        drugService.initDB();
    }
    public Thread drug(){
        Page page=new Page(cpaProperties.getDrugUrl());
        ContentParam param=new ContentParam(CPA.DRUG,drugService);
        return new Thread(new IdThread(page,param));
    }
    @Autowired
    private GeneService geneService;
    public void initGene(){
        geneService.initDB();
    }
    public Thread gene(){
        Page page=new Page(cpaProperties.getGeneUrl());
        ContentParam param=new ContentParam(CPA.GENE,geneService);
        return new Thread(new IdThread(page,param));
    }
    @Autowired
    private ProteinService proteinService;
    public void initProtein(){
        proteinService.initDB();
    }
}
