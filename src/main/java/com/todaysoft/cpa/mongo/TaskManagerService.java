package com.todaysoft.cpa.mongo;

import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.service.FluxManagerService;
import com.todaysoft.cpa.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 管理线程
 * @author liceyo
 * @version 2018/7/12
 */
@Service
public class TaskManagerService {
    private static Logger logger= LoggerFactory.getLogger(TaskManagerService.class);
    @Autowired
    private SaveDetailService saveDetailService;
    @Autowired
    private ScanListService scanListService;
    @Autowired
    private MongoService mongoService;
    @Autowired
    private FluxManagerService fluxManagerService;
    /**
     * 一般处理的类型
     */
    private final static CPA[] COMMONS={
            CPA.GENE, CPA.CLINICAL_TRIAL,
            CPA.DRUG, CPA.VARIANT,
            CPA.PROTEIN, CPA.REGIMEN,
            CPA.EVIDENCE

    };

    private final static CPA[] INCREMENTAL={
            CPA.GENE, CPA.CLINICAL_TRIAL,
            CPA.DRUG, CPA.VARIANT,CPA.EVIDENCE
    };

    /**
     * 处理逻辑
     * 1.先获取数据库存储的更新时间戳
     * 2.获取所有类型的增量更新数据（突变样本量、蛋白、用药方案没有增量功能也视为有）
     * 3.判断有增量更新功能的模块的接口数据量和数据库存储的数据量是否一致，不一致则全量扫描
     *   以防止有失败的数据没同步。
     *   先进行增量的原因是因为如果更新前两方的数据量一致，接口新增的数据会先通过增量获取到
     *   如果增量没有失败则不必再跑全量的扫描
     * @throws InterruptedException 异常
     */
    @Scheduled(fixedDelay = 600000,initialDelay = 1000)
    public void task() throws InterruptedException {
        logger.info("mongodb同步任务开始");
        String updateSince = mongoService.updateSince();
        String date = DateUtil.formatDate0(new Date());
        ExecutorService service = Executors.newFixedThreadPool(4);
        //同步接口数据
        final CountDownLatch latch=new CountDownLatch(CPA.values().length);
        //增量的
        for (CPA cpa : COMMONS) {
            service.submit(()->{
                Flux<List<DetailParam>> scan = scanListService.scan(cpa,updateSince);
                saveDetailService.saveContentToMongo(scan,latch);
            });
        }
        //针对MUTATION_STATISTICS的处理
        service.submit(()-> scanListService.scanAndSaveMutationStatistics(latch));
        latch.await();
        //更新时间
        mongoService.postUpdateSince(date);
        //
        final CountDownLatch latch2=new CountDownLatch(INCREMENTAL.length);
        for (CPA cpa : INCREMENTAL) {
            long cpaCount = scanListService.totalElement(cpa);
            long dbCount = mongoService.countModule(cpa);
            if (cpaCount>dbCount){
                service.submit(()->{
                    Flux<List<DetailParam>> page =scanListService.scan(cpa,"");
                    saveDetailService.saveContentToMongo(page,latch2);
                });
            }else {
                latch2.countDown();
            }
        }
        latch2.await();
        service.shutdown();
        logger.info("mongodb同步任务结束");
        fluxManagerService.task();
    }
}