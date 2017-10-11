package com.todaysoft.cpa.service;

import com.todaysoft.cpa.param.GlobalVar;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc: 抓取内容线程中处理业务的类
 * @author: 鱼唇的人类
 * @date: 2017/10/10 16:50
 */
@Service
public class ContentService {
    public void sendStructureChangeInfo(String changeInfo){
        if (GlobalVar.SEND_STRUCTURE_EMAIL.get()){
            System.out.println("发送邮件："+changeInfo);
            GlobalVar.SEND_STRUCTURE_EMAIL.set(false);
        }else {
            System.out.println("不发送邮件："+changeInfo);
        }
    }
}
