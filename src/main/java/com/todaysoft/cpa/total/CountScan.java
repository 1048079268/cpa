package com.todaysoft.cpa.total;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.utils.JsoupUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/31 17:25
 */
public class CountScan {
    private static Logger logger= LoggerFactory.getLogger(CountScan.class);
    private CPA cpa;
    private CountFunction countFunction;
    //分页的offset参数为扫描起点
    private Page page;
    //分段抓取的终点
    private long destination=0L;

    public CountScan(CPA cpa, CountFunction countFunction,Page page) {
        this.cpa = cpa;
        this.countFunction = countFunction;
        this.page=page;
    }

    public Map<String,Long> scan() throws InterruptedException, IOException {
        Map<String, Long> totalMap = new HashMap<>();
        int count=page.getOffset();
        long total=0;
        while (true){
            //超时未做处理
            int retryTime=3;
            Connection.Response response = null;
            do{
                try {
                    response = Jsoup.connect(cpa.contentUrl)
                            .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                            .data("limit", String.valueOf(page.getLimit()))
                            .data("offset", String.valueOf(page.getOffset()))
                            .header("Authorization", GlobalVar.getAUTHORIZATION())
                            .header("Accept", "application/test")
                            .ignoreContentType(true)
                            .timeout(12000)// 设置连接超时时间
                            .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                            .execute();
                    retryTime=0;
                } catch (IOException e) {
                    Thread.sleep(1000);
                    retryTime--;
                    if (retryTime==0){
                        logger.error("抓取内容出错:"+cpa.name+"["+page.getOffset()+"/"+total+"]");
                        response=null;
                    }
                }
            }while (retryTime>0);
            if(response==null){
                continue;
            }
            String jsonStr=response.body();
            if (!StringUtils.isEmpty(jsonStr)){
                JSONObject jsonObject= JSON.parseObject(jsonStr);
                JSONArray array=jsonObject.getJSONObject("data").getJSONArray(cpa.name+"s");
                if (total==0){
                    total=jsonObject.getJSONObject("meta").getLong("total");
                    if (destination==0){
                        destination=total;
                    }
                }
                if (array!=null&&array.size()>0){
                    for (int i=0;i<array.size();i++){
                        String id=array.getJSONObject(i).getString("id");
                        String url=cpa.contentUrl+"/"+id;
                        String body = JsoupUtil.getBody(url);
                        if (StringUtils.isEmpty(body)){
                            continue;
                        }
                        JSONObject object=JSONObject.parseObject(body).getJSONObject("data").getJSONObject(cpa.name);
                        totalMap=countFunction.count(object,totalMap);
                        count++;
                        System.out.println("--------"+cpa.name+"["+count+"/"+total+"->id="+id+"]:");
                        totalMap.forEach((key, value) -> System.out.println(key + ":" + value));
                        //分段统计完成时的输出
                        if (count>=destination){
                            logger.info("--------"+cpa.name+"["+count+"/"+total+"->id="+id+"]:");
                            totalMap.forEach((key, value) -> logger.info(key + ":" + value));
                            break;
                        }
                    }
                }else {
                    break;
                }
            }
            page.offset();
        }
        return totalMap;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }
}
