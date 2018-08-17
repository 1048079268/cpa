package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 操作成功向知识库发送数据状态
 * @author liceyo
 * @version 2018/6/6
 */
@Service
public class KbUpdateService {
    private static Logger logger= LoggerFactory.getLogger(KbUpdateService.class);

    @Value("${kb.update.data_count.url}")
    private String sendUrl;
    @Autowired
    private RestTemplate restTemplate;


    /*
      优化方案，使用汇总表实现计数
      表结构：type(char 16 id)、slot(tinyint 2 id)、cnt(int default 0)
      sql:
         insert into kt_data_count(type,slot,cnt)
         values ('type',rand()*9,1)
         on duplicate key update cnt=cnt+1;
      type 可以使用转化为tinyint类型，更快
      还可以转换为 类型、随机值、月度、数据来源、状态、计数，除计数外都是主键
      使用随机值是为了防止太多的行锁竞争
      月度：DATE_FORMAT(CURRENT_DATE(),'%Y-%m')
     */

    /**
     * 新增的数据
     * @param type 类型
     */
    public void send(String type){
        if (!logger.isDebugEnabled()){
            restTemplate.postForEntity(sendUrl, type, String.class);
        }
    }
}
