package com.todaysoft.cpa.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Param;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 12:15
 */
public class ExceptionThread implements Runnable {
    private static Logger logger= LoggerFactory.getLogger(ExceptionThread.class);

    @Override
    public void run() {
        while (true){
            ContentParam contentParam=null;
            BaseService baseService =null;
            try {
                contentParam= Param.getFailureQueue().take();
                baseService =contentParam.getBaseService();
                Document doc = Jsoup.connect(contentParam.getCpa().contentUrl + "/" + contentParam.getId())
                        .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                        .header("Authorization", Param.getAUTHORIZATION())
                        .header("Accept", "application/test")
                        .ignoreContentType(true)
                        .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                        .timeout(120000)// 设置连接超时时间
                        .get();
                String jsonStr = doc.body().text();
                if (jsonStr != null && jsonStr.length() > 0) {
                    JSONObject jsonObject = JSON.parseObject(jsonStr).getJSONObject("data").getJSONObject(contentParam.getCpa().name);
                    if (jsonObject == null || jsonObject.toJSONString().length() <= 0) {
                        logger.info("【exception】【" + contentParam.getCpa().name() + "】未抓取到内容-->id="+contentParam.getId());
                        continue;
                    }
                    try {
                        if (contentParam.isHasDependence()){
                            baseService.saveByDependence(jsonObject,contentParam.getDependenceKey());
                        }else {
                            baseService.save(jsonObject);
                        }
                        logger.info("【"+ contentParam.getCpa().name()+"】插入数据库成功,id="+contentParam.getId());
                    }catch (Exception e){
                        logger.error("【exception】存入数据异常，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                        logger.error("【exception】"+ ExceptionInfo.getErrorInfo(e));
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                if (contentParam==null){
                    logger.info("【exception】结束...");
                }else{
                    contentParam.getCpa().dbId.remove(contentParam.getId());
                    logger.error("【exception】意外结束，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                }
            } catch (Exception e) {
                if (contentParam==null){
                    logger.info("【exception】异常结束，未获取到队列内容，等待父线程重启");
                }else{
                    contentParam.getCpa().dbId.remove(contentParam.getId());
                    logger.error("【exception】抓取异常，info:"+contentParam.getCpa().name()+"-->"+contentParam.getId());
                }
            }
        }
    }
}
