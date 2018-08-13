package com.todaysoft.cpa.mongo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.compare.CompareJsonStructure;
import com.todaysoft.cpa.compare.JsonDataType;
import com.todaysoft.cpa.mongo.domain.MongoData;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.GlobalVar;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.persistence.Id;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 获取内容服务
 * @author liceyo
 * @version 2018/7/12
 */
@Service
public class SaveDetailService {
    private static Logger logger= LoggerFactory.getLogger(SaveDetailService.class);
    @Autowired
    private MongoService mongoService;

    /**
     * 保存到mongodb
     * @param flux id数据
     * @param latch 用以标识是否结束
     */
    public void saveContentToMongo(Flux<List<DetailParam>> flux, final CountDownLatch latch){
        flux
                .filter(list -> list!=null&&list.size()>0)
                //如果订阅完成计数器减一
                .doOnComplete(latch::countDown)
                //订阅
                .subscribe(list -> {
                    final  CountDownLatch downLatch=new CountDownLatch(1);
                    //创建id流
                    Flux.fromStream(list.stream())
                            //切换为多线程模式
                            .parallel()
                            //使用复用(elastic)模式运行
                            .runOn(Schedulers.elastic())
                            //过滤不更新的数据
                            .filter(param->{
                                CPA cpa=param.getCpa();
                                String dataId=param.getDataId();
                                String updateSince = param.getUpdateSince();
                                MongoData mongoData = mongoService.get(dataId, cpa.enDbName());
                                //如果mongodb已有数据
                                if (mongoData!=null){
                                    //如果是蛋白或者用药方案则直接返回false，两者的updateSince都不可用
                                    if (CPA.PROTEIN.equals(cpa) || CPA.REGIMEN.equals(cpa)){
                                        return false;
                                    }
                                    String mongoDataUpdateSince = mongoData.getUpdateSince();
                                    //判断新跑的数据是不是比数据库新
                                    int dbHash=StringUtils.isEmpty(mongoDataUpdateSince)?0:mongoDataUpdateSince.hashCode();
                                    int hash=StringUtils.isEmpty(updateSince)?0:updateSince.hashCode();
                                    logger.debug("["+cpa.name()+"]更新"+mongoDataUpdateSince+"-to-"+updateSince+",id="+dataId+",isUpdate="+(hash>dbHash));
                                    return hash>dbHash;
                                }
                                return true;
                            })
                            //获取数据
                            .map(this::getData)
                            //保存数据
                            .map(this::saveToMongo)
                            //切换为单线程模式（可能不准确，应该是顺序模式）
                            .sequential()
                            //如果订阅完成，当前计数器减一
                            .doOnComplete(downLatch::countDown)
                            //订阅
                            .subscribe(param->{
                                CPA cpa=param.getCpa();
                                if (param.isSaveSuccess()){
                                    logger.debug("["+cpa.name()+"]保存成功，id="+param.getDataId());
                                }else {
                                    logger.error("["+cpa.name()+"]保存失败,id="+param.getDataId());
//                                    mongoService.upsertErrorDetail(param.toMongoDetail());
                                }
                            });
                    try {
                        //等待流处理完成
                        downLatch.await();
                    } catch (InterruptedException e) {
                        logger.error("等待保存数据到mongodb出错",e);
                    }
                });

    }

    /**
     * 根据id获取中英文数据
     * @return 数据
     */
    private DetailParam getData(DetailParam detailParam){
        CPA cpa=detailParam.getCpa();
        String id=detailParam.getDataId();
        return Mono.just(detailParam)
                //获取中英文数据
                .map(param -> {
                    param.setCnData(getBodyById(cpa, id, GlobalVar.getLangZh()));
                    param.setEnData(getBodyById(cpa, id, GlobalVar.getLangEn()));
                    return param;
                })
                //如果异常重试三次
                .retry(3)
                //如果还是异常打印异常信息
                .doOnError(e -> {
                    logger.error("[" + cpa.name() + "]获取内容出错,id=" + id, e);
                    detailParam.setErrorInfo("获取内容出错,msg="+e.getMessage());
                })
                .onErrorReturn(detailParam)
                //json结构对比
                .map(param->{
                    JSONObject enData = param.getEnData();
                    if (enData!=null&&!enData.isEmpty()){
                        Map<String, JsonDataType> map = AcquireJsonStructure.getJsonKeyMap(null, enData);
                        CompareJsonStructure.compare(cpa.tempStructureMap,map);
                    }
                    return param;
                })
                .doOnError(e->{
                    logger.error("[" + cpa.name() + "]JSON结构变化,id=" + id, e);
                    detailParam.setCnData(null);
                    detailParam.setEnData(null);
                    detailParam.setErrorInfo("JSON结构变化,msg="+e.getMessage());
                })
                .onErrorReturn(detailParam)
                .block();

    }

    /**
     * 保存中英文数据到Mongodb
     * @param detailParam 数据
     * @return 是否保存成功
     */
    private DetailParam saveToMongo(DetailParam detailParam){
        String updateSince=detailParam.getUpdateSince();
        CPA cpa=detailParam.getCpa();
        return Mono.just(detailParam)
                //保存数据
                .map(data -> {
                    String dataId = data.getDataId();
                    JSONObject enData = data.getEnData();
                    JSONObject cnData = data.getCnData();
                    boolean cn = enData != null && !enData.isEmpty();
                    boolean en = cnData != null && !cnData.isEmpty();
                    if (cn && en) {
                        //需要保存更新时间
                        enData.put("updateSince",updateSince);
                        //保存知识库的mysql是否同步
                        enData.put("ktMysqlSyncStatus",false);
                        String saveZH = mongoService.save(dataId, cnData, cpa.cnDbName());
                        cn = !StringUtils.isEmpty(saveZH);
                        //保存中文成功后保存才保存英文，防止保存中文失败
                        en=false;
                        if (cn){
                            String saveEN = mongoService.save(dataId, enData, cpa.enDbName());
                            en = !StringUtils.isEmpty(saveEN);
                        }
                        //判断是否保存成功
                        data.setSaveSuccess(cn&&en);
                        return data;
                    }
                    return data;
                })
                //异常重试一次
                .retry(1)
                //还是异常打印日志
                .doOnError(e -> {
                    logger.error("[" + cpa.name() + "]保存到MongoDB出错,id=" + detailParam.getId(), e);
                    detailParam.setErrorInfo("保存到MongoDB出错,msg="+e.getMessage());
                })
                //如果异常返回失败数据
                .onErrorReturn(detailParam)
                .block();
    }

    /**
     * 获取详情
     * @param cpa 模块
     * @param id is
     * @param language 语言
     * @return json
     */
    private JSONObject getBodyById(CPA cpa, String id,String language) {
        try {
            Connection.Response response = Jsoup.connect(cpa.contentUrl+"/"+id)
                    .data("language",language)
                    .data("timestamps","true")
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String body = response.body();
            JSONObject object = JSON.parseObject(body);
            JSONObject enObj = object.getJSONObject("data").getJSONObject(cpa.name);
            if (enObj==null||enObj.isEmpty()){
                return null;
            }
            return object;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取详情
     * @param cpa 模块
     * @param id is
     * @return json
     */
    private JSONObject getBodyById(CPA cpa, String id) {
        try {
            Connection.Response response = Jsoup.connect(cpa.contentUrl+"/"+id)
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String body = response.body();
            return JSONObject.parseObject(body);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
