package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.mongo.MongoService;
import com.todaysoft.cpa.mongo.domain.MongoData;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.service.main.*;
import com.todaysoft.cpa.service.vice.DrugProductService;
import com.todaysoft.cpa.utils.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用flux来管理流程
 * @author liceyo
 * @version 2018/7/23
 */
@Service
public class FluxManagerService {
    private static Logger logger= LogManager.getLogger(FluxManagerService.class);
    @Autowired
    MongoService mongoService;

    private final static int DEFAULT_LIMIT=20;
    @Autowired
    private DrugService drugService;
    @Autowired
    private GeneService geneService;
    @Autowired
    private ProteinService proteinService;
    @Autowired
    private VariantService variantService;
    @Autowired
    private MutationStatisticService mutationStatisticService;
    @Autowired
    private MedicationPlanService medicationPlanService;
    @Autowired
    private EvidenceService evidenceService;
    @Autowired
    private ClinicalTrialService clinicalTrialService;
    @Autowired
    private DrugProductService drugProductService;
    @Value("${task.thread.pool.size}")
    public Integer taskThreadPoolSize=3;

    /**
     * 改啊改，改又改
     * 任务
     */
    public void task() throws InterruptedException {
        logger.info("开始执行同步数据到知识库任务");
        ExecutorService service = Executors.newFixedThreadPool(taskThreadPoolSize);
        ExecutorService childrenExecutors = Executors.newFixedThreadPool(taskThreadPoolSize);
        //一级
        final CountDownLatch latch1=new CountDownLatch(2);
        service.submit(()->scanScroll(CPA.GENE,geneService,latch1,childrenExecutors));
        service.submit(()->scanScroll(CPA.DRUG,null,latch1,childrenExecutors));
//        service.submit(()->scanDrug(latch1));
        latch1.await(3,TimeUnit.DAYS);
        //二级
        final CountDownLatch latch2=new CountDownLatch(3);
        service.submit(()->scanScroll(CPA.CLINICAL_TRIAL,clinicalTrialService,latch2,childrenExecutors));
        service.submit(()->scanScroll(CPA.VARIANT,variantService,latch2,childrenExecutors));
        service.submit(()->scanScroll(CPA.PROTEIN,proteinService,latch2,childrenExecutors));
        latch2.await(5,TimeUnit.DAYS);
        //三级
        final CountDownLatch latch3=new CountDownLatch(2);
        service.submit(()->scanScroll(CPA.EVIDENCE,evidenceService,latch3,childrenExecutors));
        service.submit(()->scanScroll(CPA.MUTATION_STATISTICS,null,latch3,childrenExecutors));
//        service.submit(()->scanMutationStatistic(latch3,childrenExecutors));
        latch3.await(3,TimeUnit.DAYS);
        service.shutdown();
        childrenExecutors.shutdown();
        logger.info("同步数据到知识库任务执行完毕");
    }

    /**
     * 清除重复数据
     */
    public void clearDupData(){
        Integer vDuplicate = variantService.deleteDuplicate();
        logger.warn("清除突变重复数据，num="+vDuplicate);
        Integer dpDuplicate = drugProductService.deleteDuplicate();
        logger.warn("清除药品重复数据，num="+dpDuplicate);
    }

    /**
     * 滚动扫描mongo,并保存数据
     * @param cpa 模块
     * @param baseService 模块保存方法
     * @param latch 计数器
     * @param service 执行线程池
     */
    private void scanScroll(CPA cpa, BaseService baseService, final CountDownLatch latch,ExecutorService service){
        try {
            AtomicReference<String> scrollId= new AtomicReference<>("0");
            AtomicBoolean run=new AtomicBoolean(true);
            while (run.get()){
                try {
                    Mono.justOrEmpty(mongoService.pageScroll(cpa.enDbName(), scrollId.get(),DEFAULT_LIMIT))
                            .retry(2)
                            .onErrorReturn(new ArrayList<>())
                            .subscribe(list->{
                                if (list==null||list.size()==0){
                                    run.set(false);
                                }else {
                                    MongoData last = list.get(list.size() - 1);
                                    scrollId.set(last.getId());
                                    switch (cpa){
                                        case DRUG:
                                            saveDrug(list);
                                            break;
                                        case MUTATION_STATISTICS:
                                            saveMutationStatistic(list);
                                            break;
                                        default:
                                            saveOther(list,service,baseService,cpa);
                                    }
                                }
                            });
                } catch (Exception e) {
                    logger.error("scanScroll-"+cpa.name(),e);
                }
            }
        } finally {
            latch.countDown();
        }

    }

    /**
     * 扫描并保存其他类型
     * @param cpa
     * @param baseService
     * @param latch
     */
    private void scan(CPA cpa, BaseService baseService, final CountDownLatch latch,ExecutorService service){
        try {
            long count = mongoService.countKtData(cpa.enDbName());
            int i = (int) (count % DEFAULT_LIMIT);
            int j = (int) (count / DEFAULT_LIMIT);
            int totalPage=i==0?j:j+1;
            Flux.range(0,totalPage)
                    .parallel()
                    .runOn(Schedulers.fromExecutor(service))
                    .map(page->mongoService.pageKtData(cpa.enDbName(),new PageRequest(page,DEFAULT_LIMIT)))
                    .sequential()
                    .toStream()
                    .forEach(list->{
                        Flux.fromStream(list.stream())
                                .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                                .map(data->{
                                    try {
                                        JSONObject en = JSONObject.parseObject(data.getData());
                                        MongoData mongoData = mongoService.get(data.getId(), cpa.cnDbName());
                                        JSONObject cn = JSONObject.parseObject(mongoData.getData());
                                        boolean save = baseService.save(en.getJSONObject(cpa.getName()), cn.getJSONObject(cpa.getName()), 0);
                                        if (save) {
                                            mongoService.updateSyncStatus(cpa.enDbName(), data.getId(), true);
                                            logger.info("["+cpa.name()+"]同步到Mysql成功,id="+data.getId());
                                        }
                                        return save;
                                    }catch (DataException e){
                                        logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                                    }catch (Exception e) {
                                        logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId(),e);
                                    }
                                    return false;
                                })
                                .toStream()
                                .forEach(r->{});
                    });
        } catch (Exception e) {
            logger.error(cpa.name()+"未知错误",e);
        }finally {
            latch.countDown();
        }
    }


    /**
     * 扫描并保存药物
     * @param latch
     */
    private void scanDrug(final CountDownLatch latch){
        CPA cpa=CPA.DRUG;
        try {
            long count = mongoService.countKtData(cpa.enDbName());
            int i = (int) (count % DEFAULT_LIMIT);
            int j = (int) (count / DEFAULT_LIMIT);
            int totalPage=i==0?j:j+1;
            Flux.range(0,totalPage)
                    .map(page->mongoService.pageKtData(cpa.enDbName(),new PageRequest(page,DEFAULT_LIMIT)))
                    .toStream()
                    .forEach(list -> {
                        saveDrug(list);
                    });
        } catch (Exception e) {
            logger.error(cpa.name()+"未知错误",e);
        }finally {
            latch.countDown();
        }
    }

    /**
     * 扫描并保存突变样本量
     * @param latch
     */
    private void scanMutationStatistic(final CountDownLatch latch,ExecutorService service){
        CPA cpa=CPA.MUTATION_STATISTICS;
        try {
            long count = mongoService.countKtData(cpa.getName());
            int i = (int) (count % DEFAULT_LIMIT);
            int j = (int) (count / DEFAULT_LIMIT);
            int totalPage=i==0?j:j+1;
            Flux.range(0,totalPage)
                    .parallel()
                    .runOn(Schedulers.fromExecutor(service))
                    .map(page->mongoService.pageKtData(cpa.getName(),new PageRequest(page,DEFAULT_LIMIT)))
                    .map(list->{
                        saveMutationStatistic(list);
                        return true;
                    })
                    .sequential()
                    .toStream()
                    .forEach(bool->{});
        } catch (Exception e) {
            logger.error(cpa.name()+"未知错误",e);
        }finally {
            latch.countDown();
        }
    }

    /**
     * 保存其他模块
     * @param list
     * @param service
     * @param baseService
     * @param cpa
     */
    private void  saveOther(List<MongoData> list,ExecutorService service,BaseService baseService,CPA cpa){
        Flux.fromStream(list.stream())
                .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                .parallel()
                .runOn(Schedulers.fromExecutor(service))
                .map(data->{
                    try {
                        JSONObject en = JSONObject.parseObject(data.getData());
                        MongoData mongoData = mongoService.get(data.getId(), cpa.cnDbName());
                        JSONObject cn = JSONObject.parseObject(mongoData.getData());
                        boolean save = baseService.save(en.getJSONObject(cpa.getName()), cn.getJSONObject(cpa.getName()), 0);
                        if (save) {
                            mongoService.updateSyncStatus(cpa.enDbName(), data.getId(), true);
                            logger.info("["+cpa.name()+"]同步到Mysql成功,id="+data.getId());
                        }
                        return save;
                    }catch (DataException e){
                        logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                    }catch (Exception e) {
                        logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId(),e);
                    }
                    return false;
                })
                .sequential()
                .toStream()
                .forEach(r->{});
    }

    /**
     * 保存药物列表
     * @param list
     */
    private void saveDrug(List<MongoData> list){
        list.stream()
                .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                .forEach(data->{
                    try {
                        JSONObject en = JSONObject.parseObject(data.getData());
                        MongoData mongoData = mongoService.get(data.getId(), CPA.DRUG.cnDbName());
                        JSONObject cn=JSONObject.parseObject(mongoData.getData());
                        boolean save = drugService.save(en.getJSONObject(CPA.DRUG.getName()), cn.getJSONObject(CPA.DRUG.getName()), null);
                        if (save){
                            mongoService.updateSyncStatus(CPA.DRUG.enDbName(),data.getId(),true);
                            logger.info("["+CPA.DRUG.name()+"]同步到Mysql成功,id="+data.getId());
                        }
                    }catch (DataException e){
                        logger.error("["+CPA.DRUG.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                    }catch (Exception e) {
                        logger.error("["+CPA.DRUG.name()+"]同步到Mysql失败,id="+data.getId(),e);
                    }
                });
    }

    /**
     * 保存突变样本量
     * @param list
     */
    private void saveMutationStatistic(List<MongoData> list){
        list.stream()
                .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                .forEach(data->{
                    try {
                        JSONObject en = JSONObject.parseObject(data.getData());
                        boolean save = mutationStatisticService.save(en,en, 0);
                        if (save){
                            mongoService.updateSyncStatus(CPA.MUTATION_STATISTICS.getName(),data.getId(),true);
                            logger.info("["+CPA.MUTATION_STATISTICS.name()+"]同步到Mysql成功,id="+data.getId());
                        }else {
                            logger.info("["+CPA.MUTATION_STATISTICS.name()+"]同步未成功,条件不足");
                        }
                    }catch (DataException e){
                        logger.error("["+CPA.MUTATION_STATISTICS.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                    }catch (Exception e) {
                        logger.error("["+CPA.MUTATION_STATISTICS.name()+"]同步到Mysql失败,id="+data.getId(),e);
                    }
                });
    }
}
