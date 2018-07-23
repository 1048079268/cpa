package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.mongo.DetailParam;
import com.todaysoft.cpa.mongo.MongoService;
import com.todaysoft.cpa.mongo.domain.MongoData;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.service.main.*;
import com.todaysoft.cpa.utils.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

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

    /**
     * 改啊改，改又改
     * 任务
     */
    public void task() throws InterruptedException {
        logger.info("开始执行同步数据到知识库任务");
        //一级
        final CountDownLatch latch1=new CountDownLatch(2);
        scan(CPA.GENE,geneService,latch1);
        scanDrug(latch1);
        latch1.await();
        //二级
        final CountDownLatch latch2=new CountDownLatch(3);
        scan(CPA.CLINICAL_TRIAL,clinicalTrialService,latch2);
        scan(CPA.VARIANT,variantService,latch2);
        scan(CPA.PROTEIN,proteinService,latch2);
        latch2.await();
        //三级
        final CountDownLatch latch3=new CountDownLatch(2);
        scan(CPA.EVIDENCE,evidenceService,latch3);
        scanMutationStatistic(latch3);
        latch3.await();
        logger.info("同步数据到知识库任务执行完毕");
    }

    /**
     * 扫描并保存其他类型
     * @param cpa
     * @param baseService
     * @param latch
     */
    private void scan(CPA cpa, BaseService baseService, final CountDownLatch latch){
        long count = mongoService.countKtData(cpa.enDbName());
        int i = (int) (count % DEFAULT_LIMIT);
        int j = (int) (count / DEFAULT_LIMIT);
        int totalPage=i==0?j:j+1;
        if (logger.isDebugEnabled()){
            totalPage=2;
        }
        Flux.range(0,totalPage)
                .parallel()
                .runOn(Schedulers.elastic())
                .map(page->mongoService.pageKtData(cpa.enDbName(),new PageRequest(page,DEFAULT_LIMIT)))
                .sequential()
                .doOnComplete(latch::countDown)
                .subscribe(list->{
                    final CountDownLatch downLatch=new CountDownLatch(1);
                    Flux.fromStream(list.stream())
                            .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                            .parallel()
                            .runOn(Schedulers.elastic())
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
                            .doOnComplete(downLatch::countDown)
                            .subscribe();
                    try {
                        downLatch.await();
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
                });
    }


    /**
     * 扫描并保存药物
     * @param latch
     */
    private void scanDrug(final CountDownLatch latch){
        CPA cpa=CPA.DRUG;
        long count = mongoService.countKtData(cpa.enDbName());
        int i = (int) (count % DEFAULT_LIMIT);
        int j = (int) (count / DEFAULT_LIMIT);
        int totalPage=i==0?j:j+1;
        if (logger.isDebugEnabled()){
            totalPage=2;
        }
        Flux.range(0,totalPage)
                .map(page->mongoService.pageKtData(cpa.enDbName(),new PageRequest(page,DEFAULT_LIMIT)))
                .doOnComplete(latch::countDown)
                .subscribe(list->{
                    list.stream()
                            .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                            .forEach(data->{
                                try {
                                    JSONObject en = JSONObject.parseObject(data.getData());
                                    MongoData mongoData = mongoService.get(data.getId(), cpa.cnDbName());
                                    JSONObject cn=JSONObject.parseObject(mongoData.getData());
                                    boolean save = drugService.save(en.getJSONObject(cpa.getName()), cn.getJSONObject(cpa.getName()), null);
                                    if (save){
                                        mongoService.updateSyncStatus(cpa.enDbName(),data.getId(),true);
                                        logger.info("["+cpa.name()+"]同步到Mysql成功,id="+data.getId());
                                    }
                                }catch (DataException e){
                                    logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                                }catch (Exception e) {
                                    logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId(),e);
                                }
                            });
                });
    }

    /**
     * 扫描并保存突变样本量
     * @param latch
     */
    private void scanMutationStatistic(final CountDownLatch latch){
        CPA cpa=CPA.MUTATION_STATISTICS;
        long count = mongoService.countKtData(cpa.getName());
        int i = (int) (count % DEFAULT_LIMIT);
        int j = (int) (count / DEFAULT_LIMIT);
        int totalPage=i==0?j:j+1;
        if (logger.isDebugEnabled()){
            totalPage=2;
        }
        Flux.range(0,totalPage)
                .parallel()
                .runOn(Schedulers.elastic())
                .map(page->mongoService.pageKtData(cpa.getName(),new PageRequest(page,DEFAULT_LIMIT)))
                .sequential()
                .doOnComplete(latch::countDown)
                .subscribe(list->{
                    list.stream()
                            .filter(mongoData -> !mongoData.getKtMysqlSyncStatus())
                            .forEach(data->{
                                try {
                                    JSONObject en = JSONObject.parseObject(data.getData());
                                    boolean save = mutationStatisticService.save(en,en, 0);
                                    if (save){
                                        mongoService.updateSyncStatus(cpa.getName(),data.getId(),true);
                                        logger.info("["+cpa.name()+"]同步到Mysql成功,id="+data.getId());
                                    }
                                }catch (DataException e){
                                    logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId()+",msg="+e.getMessage());
                                }catch (Exception e) {
                                    logger.error("["+cpa.name()+"]同步到Mysql失败,id="+data.getId(),e);
                                }
                            });
                });
    }

}
