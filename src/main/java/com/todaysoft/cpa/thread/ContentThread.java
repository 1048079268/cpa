package com.todaysoft.cpa.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:33
 */
public class ContentThread implements Runnable{
    private static Logger logger= LoggerFactory.getLogger(ContentThread.class);

    @Override
    public void run() {
        while (true){
            ContentParam contentParam=null;
            BaseService baseService =null;
            try {
                contentParam= GlobalVar.getContentQueue().take();
                baseService =contentParam.getBaseService();
                Connection.Response response= Jsoup.connect(contentParam.getCpa().contentUrl + "/" + contentParam.getId())
                        .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                        .header("Authorization", GlobalVar.getAUTHORIZATION())
                        .header("Accept", "application/test")
                        .ignoreContentType(true)
                        .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                        .timeout(120000)// 设置连接超时时间
                        .execute();
                String jsonStr = response.body();
                if (jsonStr != null && jsonStr.length() > 0) {
                    JSONObject jsonObject = JSON.parseObject(jsonStr).getJSONObject("data").getJSONObject(contentParam.getCpa().name);
                    if (jsonObject == null || jsonObject.toJSONString().length() <= 0) {
                        logger.info("【content】【" + contentParam.getCpa().name() + "】未抓取到内容-->id="+contentParam.getId());
                        continue;
                    }
                    if (contentParam.isHasDependence()){
                        baseService.saveByDependence(jsonObject,contentParam.getDependenceKey());
                    }else {
                        baseService.save(jsonObject);
                    }
                    logger.info("【"+ contentParam.getCpa().name()+"】插入数据库成功,id="+contentParam.getId());
                }
            } catch (InterruptedException e) {
                if (contentParam==null){
                    logger.info("【content】结束...");
                }else{
                    logger.info("【content】意外结束，写入异常重试队列，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                    writeFailureQueue(contentParam);
                }
            } catch (Exception e) {
                if (contentParam==null){
                    logger.info("【content】异常结束，未获取到队列内容，交由父线程处理");
                }else{
                    logger.info("【content】抓取异常，写入异常重试队列，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                    writeFailureQueue(contentParam);
                }
            }
        }
    }

    /**
     * @desc: 写入失败队列
     * @author: 鱼唇的人类
     * @param contentParam
     */
    private void writeFailureQueue(ContentParam contentParam){
        try {
            GlobalVar.getFailureQueue().put(contentParam);
        } catch (InterruptedException e) {
            contentParam.getCpa().dbId.remove(contentParam.getId());
            logger.error("写入异常重试队列失败:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
            logger.error("【"+contentParam.getCpa().name()+"】"+ ExceptionInfo.getErrorInfo(e));
        }
    }
}
