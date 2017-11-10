package com.todaysoft.cpa.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.compare.CompareJsonStructure;
import com.todaysoft.cpa.compare.JsonDataType;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.ContentService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.ExceptionInfo;
import com.todaysoft.cpa.utils.JsoupUtil;
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
 * @date: 2017/9/21 14:22
 */
public class DrugThread implements Runnable {
    private static Logger logger= LoggerFactory.getLogger(DrugThread.class);
    private int retryTimes=2;
    private ContentService contentService;
    volatile boolean isRun=true;

    public DrugThread(ContentService contentService) {
        this.contentService = contentService;
    }

    @Override
    public void run() {
        while (isRun){
            ContentParam contentParam=null;
            BaseService baseService =null;
            int retryTime=retryTimes;
            try {
                contentParam= GlobalVar.getDrugQueue().take();
                //加入重试机制
                while (true){
                    try {
                        baseService =contentParam.getBaseService();
                        String url=contentParam.getCpa().contentUrl + "/" + contentParam.getId();
                        String enJson= JsoupUtil.getBody(url,"en");
                        String cnJson= JsoupUtil.getBody(url,"zn");
                        //结构变化检测
                        try {
                            JSONObject checkBody=JSON.parseObject(enJson);
                            Map<String, JsonDataType> map = AcquireJsonStructure.getJsonKeyMap(null, checkBody);
                            CompareJsonStructure.compare(contentParam.getCpa().tempStructureMap,map);
                        }catch (StructureChangeException e){
                            contentService.sendStructureChangeInfo(e.getMessage(),contentParam);
                            logger.error("【" + contentParam.getCpa().name() + "】JSON结构变化"+ ExceptionInfo.getErrorInfo(e));
                            return;
                        }
                        if (enJson != null && enJson.length() > 0) {
                            //处理英文JSON
                            JSONObject enObj = JSON.parseObject(enJson).getJSONObject("data").getJSONObject(contentParam.getCpa().name);
                            if (enObj == null || enObj.toJSONString().length() <= 0) {
                                logger.info("【drug】【" + contentParam.getCpa().name() + "】未抓取到内容-->id="+contentParam.getId());
                                break;
                            }
                            //处理中文JSON
                            JSONObject cnObj=null;
                            if (cnJson!=null&&cnJson.length()>0){
                                cnObj = JSON.parseObject(cnJson).getJSONObject("data").getJSONObject(contentParam.getCpa().name);
                            }
                            if (cnObj==null){
                                cnObj=enObj;
                            }
                            if (contentParam.isHasDependence()){
                                baseService.saveByDependence(enObj,cnObj,contentParam.getDependenceKey());
                            }else {
                                baseService.save(enObj,cnObj);
                            }
                            logger.info("【"+ contentParam.getCpa().name()+"】插入数据库成功,id="+contentParam.getId());
                        }
                        Thread.sleep(1000);
                        break;
                    }catch (InterruptedException e){
                        throw new InterruptedException(e.getMessage());
                    } catch (Exception ex){
                        if (retryTime>0){
                            retryTime--;
                            logger.info("【drug】失败，开始重试,info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                            continue;
                        }else {
                            contentParam.getCpa().dbId.remove(contentParam.getId());
                            if (ex instanceof DataException){
                                logger.error("【exception】存入数据异常，info:["+contentParam.getCpa().name()+"]-->"+contentParam.getId()+",cause:"+ex.getMessage());
                            }else {
                                logger.error("【exception】存入数据异常，info:["+contentParam.getCpa().name()+"]-->"+contentParam.getId());
                                logger.error("【exception】"+ ExceptionInfo.getErrorInfo(ex));
                            }
                            break;
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (contentParam==null){
                    logger.info("【drug】结束...");
                }else{
                    contentParam.getCpa().dbId.remove(contentParam.getId());
                    logger.error("【drug】意外结束，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                }
                isRun=false;
            }
        }
    }
}
