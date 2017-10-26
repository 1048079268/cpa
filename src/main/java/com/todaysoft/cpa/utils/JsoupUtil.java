package com.todaysoft.cpa.utils;

import com.todaysoft.cpa.param.GlobalVar;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * @desc: 抓取CPA的
 * @author: 鱼唇的人类
 * @date: 2017/10/26 10:05
 */
public class JsoupUtil {
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
                .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                .data("language",language)
                .header("Authorization", GlobalVar.getAUTHORIZATION())
                .header("Accept", "application/test")
                .ignoreContentType(true)
                .timeout(12000)// 设置连接超时时间
                .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                .execute();
        return response.body();
    }
}
