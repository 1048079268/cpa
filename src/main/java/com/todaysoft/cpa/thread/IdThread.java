package com.todaysoft.cpa.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 11:28
 */
public class IdThread implements Runnable {
    private static Logger logger= LoggerFactory.getLogger(IdThread.class);
    private Page page;
    private Page savePage;//用以恢复现场
    private ContentParam contentParam;
    private CPA cpa;
    private int insertCount=0;
    private int  retryTimes=3;

    public IdThread(Page page,ContentParam contentParam) {
        this.page = page;
        this.contentParam=contentParam;
        this.cpa=contentParam.getCpa();
        this.page.init();
    }

    @Override
    public void run() {
        try {
            logger.info("【"+cpa.name()+"】开始进行增量对比...");
            while (true){//抓取直到没有数据
                try {
                    int metaOffset=0;
                    int metaTotal=0;
                    savePage=page;//保存这次查询的参数，以便错误后恢复环境
                    Connection.Response response = Jsoup.connect(page.getUrl())
                            .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                            .data("limit", String.valueOf(page.getLimit()))
                            .data("offset", String.valueOf(page.getOffset()))
                            .data(page.getParam())
                            .header("Authorization", GlobalVar.getAUTHORIZATION())
                            .header("Accept", "application/test")
                            .ignoreContentType(true)
                            .timeout(12000)// 设置连接超时时间
                            .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                            .execute();
                    String jsonStr=response.body();
                    if (jsonStr!=null&&jsonStr.length()>0){
                        JSONObject jsonObject= JSON.parseObject(jsonStr);
                        JSONArray array=jsonObject.getJSONObject("data").getJSONArray(cpa.name+"s");
                        JSONObject meta=jsonObject.getJSONObject("meta");
                        metaOffset=meta.getInteger("offset");
                        metaTotal=meta.getInteger("total");
                        if (array!=null&&array.size()>0){
                            for (int i=0;i<array.size();i++){
                                JSONObject idObject=array.getJSONObject(i);
                                String id=idObject.getString("id");
                                if (cpa.dbId.add(id)){
                                    //如果set里面没有该数据那就将该id存入队列
                                    ContentParam param=ContentParam.create(contentParam);
                                    param.setId(id);
                                    //如果是药物的单独放入药物队列
                                    if (cpa.equals(CPA.DRUG)){
                                        GlobalVar.getDrugQueue().put(param);
                                    }else {
                                        GlobalVar.getContentQueue().put(param);
                                    }
                                    insertCount++;
                                }
                            }
                        }else {
                            break;//如果列表为空则跳出循环
                        }
                    }
                    logger.info("【"+cpa.name()+"】完成一次id抓取,开始执行分页偏移，page["+metaOffset+"/"+metaTotal+"],数据量："+insertCount);
                    page.offset();//执行偏移操作
                    retryTimes=3;
                    if (logger.isDebugEnabled()){
                        if (contentParam.isHasDependence()){
                            if (insertCount>=10){
                                break;
                            }
                        }else {
                            if (insertCount>=20) {
                                break;
                            }
                        }
                    }
                    Thread.sleep(200);
                } catch (Exception e) {
                    //发生异常后恢复线程并进行重试3次
                    if (retryTimes>0){
                        logger.warn("【"+cpa.name()+"】发生异常，恢复环境...开始重试,第"+(4-retryTimes)+"次--param:offset="+savePage.getOffset()+"&limit="+savePage.getLimit());
                        page=savePage;//还原偏移参数
                        retryTimes--;
                    }else {
                        logger.error("【"+cpa.name()+"】【error:重试无效】url:"+page.getUrl()+"?offset="+savePage.getOffset()+"&limit="+savePage.getLimit()+"&other="+page.getParam(),e);
                        page.offset();
                        retryTimes=3;
                        logger.warn("【"+cpa.name()+"】【error:重试无效】开始执行下一次偏移");
                    }
                    e.printStackTrace();
                }
            }
        }finally {
            logger.info("【"+cpa.name()+"】增量对比完成，此次插入【"+insertCount+"】条数据");
            insertCount=0;
        }
    }
}
