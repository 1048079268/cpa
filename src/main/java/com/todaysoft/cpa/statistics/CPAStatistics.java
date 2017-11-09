package com.todaysoft.cpa.statistics;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.JsoupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/9 10:36
 */
@Service
public class CPAStatistics {
    private static Logger logger= LoggerFactory.getLogger(CPAStatistics.class);

    @Async
    public Map<String,Long> statisticsByIdList(CPA cpa, List<String> idList, CountFunction countFunction) throws InterruptedException {
        logger.info("---start statistics:"+cpa.name);
        Map<String, Long> totalMap=new HashMap<>();
        for (int i = 0; i < idList.size(); i++) {
            String id = idList.get(i);
            String url = cpa.contentUrl + "/" + id;
            String body = JsoupUtil.getBody(url);
            if (StringUtils.isEmpty(body)) {
                logger.error("error-statistics:" + id);
                continue;
            }
            JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject(cpa.name);
            totalMap = countFunction.count(object, totalMap);
            System.out.println("---" + cpa.name+"["+i+"/"+idList.size()+"]id="+id);
//            totalMap.forEach((key, value) -> System.out.println(key + ":" + value));
        }
        //分段统计完成时的输出
        totalMap.forEach((key, value) ->logger.info(key + ":" + value));
        logger.info("---end statistics:"+cpa.name);
        return totalMap;
    }

    @Async
    public Map<String,Long> statisticsById(CPA cpa, String id, CountFunction countFunction,Map<String, Long> totalMap) throws InterruptedException {
        String url = cpa.contentUrl + "/" + id;
        String body = JsoupUtil.getBody(url);
        if (StringUtils.isEmpty(body)) {
            logger.error("error-statistics:["+cpa.name+"]"+id);
        }
        JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject(cpa.name);
        totalMap = countFunction.count(object, totalMap);
        return totalMap;
    }
}
