package com.todaysoft.cpa.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.compare.CompareJsonStructure;
import com.todaysoft.cpa.compare.JsonDataType;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.service.ContentService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.ExceptionInfo;
import com.todaysoft.cpa.utils.MergeException;
import com.todaysoft.cpa.utils.StructureChangeException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 11:38
 */
public class MutationStatisticThread implements Runnable {
    private static Logger logger= LoggerFactory.getLogger(MutationStatisticThread.class);
    private Page page;
    private ContentParam contentParam;
    private ContentService contentService;
    private CPA cpa;
    private Page savePage;
    private int  retryTimes=3;
    private int saveRetryTimes=3;
    private int insertCount=0;
    volatile boolean isRun=true;

    public MutationStatisticThread(Page page, ContentParam contentParam,ContentService contentService) {
        this.contentService=contentService;
        this.page = page;
        this.savePage=this.page;
        this.contentParam = contentParam;
        this.cpa=contentParam.getCpa();
    }

    @Override
    public void run() {
        try {
            logger.info("【"+cpa.name()+"】开始获取突变疾病样本量");
            //抓取直到没有数据
            while (isRun){
                try {
                    //保存这次查询的参数，以便错误后恢复环境
                    savePage=page;
                    Connection.Response response = Jsoup.connect(page.getUrl())
                            .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                            .data("limit", String.valueOf(page.getLimit()))
                            .data("offset", String.valueOf(page.getOffset()))
                            .data(page.getParam())
                            .header("Authorization", GlobalVar.getAUTHORIZATION())
                            .header("Accept", "application/test")
                            .ignoreContentType(true)
                            // 设置连接超时时间
                            .timeout(12000)
                            //设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                            .maxBodySize(0)
                            .execute();
                    String jsonStr=response.body();
                    //结构变化检测
                    try {
                        JSONObject checkBody=JSON.parseObject(jsonStr);
                        Map<String, JsonDataType> map = AcquireJsonStructure.getJsonKeyMap(null, checkBody);
                        CompareJsonStructure.compare(contentParam.getCpa().tempStructureMap,map);
                    }catch (StructureChangeException e){
                        contentService.sendStructureChangeInfo(e.getMessage(),contentParam);
                        logger.error("【" + contentParam.getCpa().name() + "】JSON结构变化", e);
                        return;
                    }
                    if (jsonStr!=null&&jsonStr.length()>0){
                        JSONObject jsonObject= JSON.parseObject(jsonStr);
                        JSONArray array=jsonObject.getJSONObject("data").getJSONArray(cpa.name);
                        if (array!=null&&array.size()>0) {
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject idObject=array.getJSONObject(i);
                                String doid=idObject.getString("doid");
                                String mutationId=idObject.getString("mutationId");
                                String id=doid+"-"+mutationId;
                                if (cpa.dbId.add(id)){
                                    insertCount++;
                                    boolean reSave;
                                    do {
                                        try {
                                            boolean success;
                                            if (contentParam.isHasDependence()){
                                                success=contentParam.getBaseService().saveByDependence(idObject,idObject,contentParam.getDependenceKey(),0);
                                            }else {
                                                success=contentParam.getBaseService().save(idObject,idObject,0);
                                            }
                                            if (success){
                                                logger.info("【"+cpa.name()+"】插入数据库成功:"+id);
                                            }else {
                                                cpa.dbId.remove(id);
                                            }
                                            reSave=false;
                                        }catch (Exception e){
                                            //发生异常后重试三次，如果还发生异常就抛出到分页重试
                                            if (saveRetryTimes>0){
                                                reSave=true;
                                                saveRetryTimes--;
                                            }else {
                                                cpa.dbId.remove(id);
                                                saveRetryTimes=3;
                                                if (e instanceof DataException){
                                                    logger.error("【exception】存入数据异常，info:["+contentParam.getCpa().name()+"]-->"+id+",cause:"+e.getMessage());
                                                }else if (e instanceof MergeException){
                                                    logger.error("【exception】存入数据异常，info:["+contentParam.getCpa().name()+"]-->"+contentParam.getId()+",cause:",e);
                                                }else {
                                                    logger.error("【exception】存入数据异常，info:["+contentParam.getCpa().name()+"]-->"+id,e);
                                                }
                                                reSave=false;
                                            }
                                        }
                                    }while (reSave);
                                }
                            }
                        }else {
                            break;
                        }
                    }
                    logger.info("【"+cpa.name()+"】完成一次突变疾病样本量抓取,开始执行分页偏移:"+insertCount);
                    page.offset();//执行偏移操作
                    if (logger.isDebugEnabled()){
                        if (insertCount>=10) {
                            break;
                        }
                    }
                    retryTimes=3;
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    if (contentParam==null){
                        logger.info("【"+cpa.name()+"】结束...");
                    }else{
                        contentParam.getCpa().dbId.remove(contentParam.getId());
                        logger.error("【"+cpa.name()+"】意外结束，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                    }
                    isRun=false;
                }catch (Exception e) {
                    //发生异常后恢复线程并进行重试3次
                    if (retryTimes>0){
                        logger.warn("【"+cpa.name()+"】发生异常，恢复环境...开始重试,第"+(4-retryTimes)+"次--param:offset="+savePage.getOffset()+"&limit="+savePage.getLimit());
                        page=savePage;
                        retryTimes--;
                    }else {
                        logger.error("【"+cpa.name()+"】【error:重试无效】--param:offset="+savePage.getOffset()+"&limit="+savePage.getLimit(),e);
                        logger.warn("【"+cpa.name()+"】【error:重试无效】开始执行下一次偏移");
                        page.offset();//执行偏移操作
                        retryTimes=3;
                    }
                    continue;
                }
            }
        }finally {
            logger.info("【"+cpa.name()+"】突变疾病样本量抓取完成,插入总量："+insertCount);
            insertCount=0;
        }
    }
}
