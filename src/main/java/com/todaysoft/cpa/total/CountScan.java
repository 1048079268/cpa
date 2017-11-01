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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/31 17:25
 */
public class CountScan {
    private CPA cpa;
    private CountFunction countFunction;
    private Page page;

    public CountScan(CPA cpa, CountFunction countFunction,Page page) {
        this.cpa = cpa;
        this.countFunction = countFunction;
        this.page=page;
    }

    public Map<String,Long> scan() throws IOException, InterruptedException {
        Map<String, Long> totalMap = new HashMap<>();
        int count=1;
        long total=page.getOffset();
        while (true){
            Connection.Response response = Jsoup.connect(cpa.contentUrl)
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .data("limit", String.valueOf(page.getLimit()))
                    .data("offset", String.valueOf(page.getOffset()))
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String jsonStr=response.body();
            if (!StringUtils.isEmpty(jsonStr)){
                JSONObject jsonObject= JSON.parseObject(jsonStr);
                JSONArray array=jsonObject.getJSONObject("data").getJSONArray(cpa.name+"s");
                if (count==1){
                    total=jsonObject.getJSONObject("meta").getLong("total");
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
                        System.out.println("--------"+cpa.name+"["+count+"/"+total+"->id="+id+"]:");
                        count++;
                        totalMap.forEach((key, value) -> System.out.println(key + ":" + value));
                    }
                }else {
                    break;
                }
            }
            page.offset();
        }
        return totalMap;
    }
}
