package com.todaysoft.cpa.utils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.statistics.CountScan;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @desc: 抓取CPA的
 * @author: 鱼唇的人类
 * @date: 2017/10/26 10:05
 */
public class JsoupUtil {
    private static Logger logger= LoggerFactory.getLogger(JsoupUtil.class);
    public static String getBody(String url, Map<String,String> param) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                .data(param)
                .header("Authorization", GlobalVar.getAUTHORIZATION())
                .header("Accept", "application/test")
                .ignoreContentType(true)
                .timeout(12000)// 设置连接超时时间
                .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                .execute();
        return response.body();
    }

    public static String getBody(String url,String language) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .data("language",language)
                .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                .header("Authorization", GlobalVar.getAUTHORIZATION())
                .header("Accept", "application/test")
                .ignoreContentType(true)
                .timeout(12000)// 设置连接超时时间
                .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                .execute();
        return response.body();
    }

    /**
     * 专用于CountScan类
     * @see CountScan
     * @param url
     * @return
     * @throws InterruptedException
     */
    public static String getBody(String url) throws InterruptedException {
        Connection.Response response = null;
        int retryTime=3;
        do{
            try {
                response = Jsoup.connect(url)
                        .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                        .header("Authorization", GlobalVar.getAUTHORIZATION())
                        .header("Accept", "application/test")
                        .ignoreContentType(true)
                        .timeout(120000)// 设置连接超时时间
                        .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                        .execute();
                retryTime=0;
            } catch (IOException e) {
                Thread.sleep(1000);
                retryTime--;
                if (retryTime==0){
                    logger.error("[抓取内容出错]"+url);
                    response=null;
                }
            }
        }while (retryTime>0);
        if (response==null){
            return null;
        }
        return response.body();
    }

    /**
     * 获取json对象
     * @param cpa
     * @param id
     * @param language
     * @return
     * @throws IOException
     */
    public static JSONObject getJsonByUrl(CPA cpa, String id, String language) throws IOException {
        String url=cpa.contentUrl+"/"+id;
        String body = JsoupUtil.getBody(url, language);
        if (!StringUtils.isEmpty(body)){
            JSONObject data = JSON.parseObject(body).getJSONObject("data").getJSONObject(cpa.name);
            if (data!=null){
                return data;
            }
        }
        return null;
    }

}
