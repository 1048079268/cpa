package com.todaysoft.cpa.service;

import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.param.*;
import com.todaysoft.cpa.service.main.*;
import com.todaysoft.cpa.service.vice.*;
import com.todaysoft.cpa.thread.ContentManagerThread;
import com.todaysoft.cpa.thread.DrugThread;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.thread.MutationStatisticThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
    @Autowired
    private DrugService drugService;
    @Autowired
    private GeneService geneService;
    @Autowired
    private ProteinService proteinService;
    @Autowired
    private VariantService variantService;
    @Autowired
    private KeggPathwaysService keggPathwaysService;
    @Autowired
    private MeshCategoryService meshCategoryService;
    @Autowired
    private CancerService cancerService;
    @Autowired
    private ClinicalTrialService clinicalTrialService;
    @Autowired
    private MutationStatisticService mutationStatisticService;
    @Autowired
    private MedicationPlanService medicationPlanService;
    @Autowired
    private EvidenceService evidenceService;
    @Autowired
    private IndicationService indicationService;
    @Autowired
    private SideEffectService sideEffectService;
    @Autowired
    private ContentService contentService;

    public static ExecutorService childrenTreadPool;

    /**
     * @desc: 初始化数据
     */
    public void init() throws FileNotFoundException {
        GlobalVar.setContentQueue(new LinkedBlockingQueue<>(cpaProperties.getMaxBlockingNum()));
        GlobalVar.setDrugQueue(new LinkedBlockingQueue<>(cpaProperties.getMaxBlockingNum()));
        GlobalVar.setFailureQueue(new LinkedBlockingQueue<>(cpaProperties.getMaxFailureBlockingNum()));
        GlobalVar.setAUTHORIZATION(cpaProperties.getAuthorization());
        drugService.initDB();
        geneService.initDB();
        proteinService.initDB();
        variantService.initDB();
        clinicalTrialService.initDB();
        mutationStatisticService.initDB();
        evidenceService.initDB();
        medicationPlanService.initDB();
        meshCategoryService.init();
        keggPathwaysService.init();
        indicationService.init();
        sideEffectService.init();
    }

    /**
     * @desc: 线程管理异步方法，启动线程入口
     * 分为子业务线程、一级线程和二级线程
     * 一级二级线程除了抓取相应数据外，还有扫描并启动抓取流程的作用
     * 子线程是由其他线程启动的线程，数据库关系一般为一对一或者一对多，如一个基因对应一个蛋白，所以该蛋白线程由对应的基因线程启动
     * 一级线程的执行不依赖其他线程的结果
     * 二级线程的执行依赖一级线程的结果，数据库关系一般为多对多。
     * @throws InterruptedException
     */
    @Async
    public void manager() throws InterruptedException {
        //如果已经发送了结构改变邮件，那么就不执行接口抓取，直到解决该结构变化为止。
        if (!GlobalVar.SEND_STRUCTURE_EMAIL.get()){
            return;
        }
        //内容线程启动
        ContentManagerThread mainManager=new ContentManagerThread(cpaProperties.getMaxContentThreadNum(),contentService);
        mainManager.start();
        //子业务抓取id线程池(抓取id线程数量的控制主要是在子业务线程上)
        childrenTreadPool=Executors.newFixedThreadPool(cpaProperties.getMaxIdTreadNum());
        //TODO 单项测试
        boolean test=false;
        if (test){
            Thread drugContentThread=new Thread(new DrugThread(contentService));
            drugContentThread.start();
            ContentParam param=new ContentParam(CPA.DRUG,drugService);
            param.setId("3276");
            GlobalVar.getDrugQueue().put(param);
        }
        while (!test){
            //一线id抓取线程池（主）
            ExecutorService mainPool = Executors.newFixedThreadPool(2);
            //启动一级线程（暂时不去除线程池，以便以后扩展）
            mainPool.execute(drug());
            Thread drugContentThread=new Thread(new DrugThread(contentService));
            drugContentThread.start();
            logger.info("【manager】一级主线程全部启动完成");
            mainPool.shutdown();
            //检测一级线程
            while (true) {
                if (!GlobalVar.SEND_STRUCTURE_EMAIL.get()){
                    drugContentThread.interrupt();
                    mainManager.stopAll();
                    mainPool.shutdownNow();
                    childrenTreadPool.shutdownNow();
                    return;
                }
                //因为被依赖，所以要检测内容线程是否执行完成
                if (mainPool.isTerminated()&&drugContentThread.getState().equals(Thread.State.WAITING)) {
                    drugContentThread.interrupt();
                    logger.info("【manager】一级线程执行完成");
                    break;
                }
                //监控内容抓取线程，如果挂掉则重启
                if (!drugContentThread.isAlive()){
                    drugContentThread=new Thread(new DrugThread(contentService));
                    drugContentThread.start();
                }
                mainManager.checkAndRestart();
                Thread.sleep(10000);
            }
            //二级id抓取线程池（依赖于一级线程）
            ExecutorService secondPool = Executors.newFixedThreadPool(cpaProperties.getMaxIdTreadNum());
            //启动二级线程
            secondPool.execute(gene());
            secondPool.execute(clinicalTrail());
            secondPool.execute(regimen());
            logger.info("【manager】二级主线程全部启动完成");
            secondPool.shutdown();
            while (true) {
                if (!GlobalVar.SEND_STRUCTURE_EMAIL.get()){
                    mainManager.stopAll();
                    secondPool.shutdownNow();
                    childrenTreadPool.shutdownNow();
                    return;
                }
                if (secondPool.isTerminated()) {
                    logger.info("【manager】二级线程执行完成");
                    break;
                }
                Thread.sleep(10000);
                //监控内容抓取线程
                mainManager.checkAndRestart();
            }
            //三级线程池
            ExecutorService thirdPool = Executors.newFixedThreadPool(cpaProperties.getMaxIdTreadNum());
            //启动三级线程
            thirdPool.execute(variant());
            thirdPool.execute(protein());
            logger.info("【manager】三级主线程全部启动完成");
            thirdPool.shutdown();
            while (true) {
                if (!GlobalVar.SEND_STRUCTURE_EMAIL.get()){
                    mainManager.stopAll();
                    thirdPool.shutdownNow();
                    childrenTreadPool.shutdownNow();
                    return;
                }
                if (thirdPool.isTerminated()) {
                    logger.info("【manager】三级线程执行完成");
                    break;
                }
                Thread.sleep(10000);
                //监控内容抓取线程
                mainManager.checkAndRestart();
            }
            //三级线程池
            ExecutorService fourthPool = Executors.newFixedThreadPool(cpaProperties.getMaxIdTreadNum());
            //启动三级线程
            fourthPool.execute(evidence());
            fourthPool.execute(mutationStatistic());
            logger.info("【manager】四级主线程全部启动完成");
            fourthPool.shutdown();
            while (true) {
                if (!GlobalVar.SEND_STRUCTURE_EMAIL.get()){
                    mainManager.stopAll();
                    fourthPool.shutdownNow();
                    childrenTreadPool.shutdownNow();
                    return;
                }
                if (fourthPool.isTerminated()) {
                    logger.info("【manager】四级线程执行完成");
                    break;
                }
                Thread.sleep(10000);
                //监控内容抓取线程
                mainManager.checkAndRestart();
            }
            logger.info("【manager】全部执行完成，休眠【"+cpaProperties.getHeartbeat()/60000+"】分钟...");
            Thread.sleep(cpaProperties.getHeartbeat());
            logger.info("【manager】休眠结束，开始重启各线程...");
        }
    }

    //药物（一级线程）
    public Runnable drug(){
        Page page=new Page(cpaProperties.getDrugUrl());
        ContentParam param=new ContentParam(CPA.DRUG,drugService);
        return new IdThread(page,param);
    }

    /**
     * 基因
     * 二级线程
     * 基因的突变的证据依赖药物
     * @return
     */
    public Runnable gene(){
        Page page=new Page(cpaProperties.getGeneUrl());
        ContentParam param=new ContentParam(CPA.GENE,geneService);
        return new IdThread(page,param);
    }

    /**
     * 临床实验
     * 二级线程
     * 在药物线程中抓取过一遍，这里做补漏，因为有部分临床实验没有药物
     * @return
     */
    public Runnable clinicalTrail(){
        Page page=new Page(cpaProperties.getClinicalTrialUrl());
        ContentParam param=new ContentParam(CPA.CLINICAL_TRIAL, clinicalTrialService);
        return new IdThread(page,param);
    }

    /**
     * 用药方案
     * 二级线程
     * 依赖药物、疾病
     * @return
     */
    public Runnable regimen(){
        Page page=new Page(cpaProperties.getRegimenUrl());
        ContentParam param=new ContentParam(CPA.REGIMEN, medicationPlanService);
        return new IdThread(page,param);
    }

    /**
     * 基因突变
     * 三级线程
     * 主要补抓以前出错的记录
     * （em:在不启动该线程的情况下，id=1的基因插入成功，该基因有一个id=9的突变失败了，那么这个突变数据就永远不会被抓到）
     * 下面的原因相同
     * @return
     */
    public Runnable variant(){
        Page page=new Page(cpaProperties.getVariantUrl());
        ContentParam param=new ContentParam(CPA.VARIANT,variantService);
        return new IdThread(page,param);
    }

    /**
     * 蛋白
     * 三级线程
     * @return
     */
    public Runnable protein(){
        Page page=new Page(cpaProperties.getProteinUrl());
        ContentParam param=new ContentParam(CPA.PROTEIN,proteinService);
        return new IdThread(page,param);
    }

    /**
     * 证据
     * 四级线程
     * @return
     */
    public Runnable evidence(){
        Page page=new Page(cpaProperties.getEvidenceUrl());
        ContentParam param=new ContentParam(CPA.EVIDENCE,evidenceService);
        return new IdThread(page,param);
    }

    /**
     * 突变疾病样本量
     * 四级线程
     * @return
     */
    public Runnable mutationStatistic(){
        Page msPage=new Page(cpaProperties.getMutationStatisticsUrl());
        ContentParam msParam=new ContentParam(CPA.MUTATION_STATISTICS,mutationStatisticService);
        return new MutationStatisticThread(msPage,msParam,contentService);
    }
}
