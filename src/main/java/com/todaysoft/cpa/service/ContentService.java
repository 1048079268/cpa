package com.todaysoft.cpa.service;

import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc: 抓取内容线程中处理业务的类
 * @author: 鱼唇的人类
 * @date: 2017/10/10 16:50
 */
@Component
public class ContentService {
    private static Logger logger= LoggerFactory.getLogger(ContentService.class);
    @Value("${jsonStructureChange.mail.sendTo}")
    private String[] sendTo;
    @Value("${spring.mail.username}")
    private String sendFrom;
    @Autowired
    private JavaMailSender mailSender;

    public synchronized void sendStructureChangeInfo(String changeInfo, ContentParam param){
        if (GlobalVar.SEND_STRUCTURE_EMAIL.get()){
            GlobalVar.SEND_STRUCTURE_EMAIL.set(false);
            StringBuffer content=new StringBuffer();
            content.append("<h3>【CPA JSON结构改变】</h3><h4>比对项：");
            content.append(param.getCpa().name+",id="+param.getId());
            content.append("</h4><div><h4>结构改变信息：</h4>");
            Arrays.stream(changeInfo.trim().split(";")).forEach(s -> {
                content.append("<li>"+s.trim()+"</li>");
            });
            content.append("</div>");
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(sendFrom);
                helper.setTo(sendTo);
                helper.setSubject("【CPA JSON结构改变】-"+DateUtil.today());
                helper.setText(content.toString(), true);
                mailSender.send(message);
                logger.info("已发送结构改变通知邮件");
            } catch (MessagingException e) {
                logger.error("发送结构改变通知邮件时发生异常！\n", ExceptionInfo.getErrorInfo(e));
            }
        }else {
            logger.info("已发送结构改变通知，不再发送邮件："+changeInfo.trim());
        }
    }
}
