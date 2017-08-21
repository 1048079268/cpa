package com.todaysoft.cpa.service;

import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

/**
 * @desc: 定时发送错误信息
 * @author: 鱼唇的人类
 * @date: 2017/8/10 14:22
 */
@Component
public class SendError {
    private static Logger logger= LoggerFactory.getLogger(SendError.class);
    @Value("${exception.mail.username}")
    private String sendTo;
    @Value("${logging.path}")
    private String errorPath;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * @desc: 发送昨天的错误日志
     *  每天1:00发送
     *  如果昨天的日志为空就不发
     * @author: 鱼唇的人类
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendError(){
        logger.info("【定时邮件任务】--开始发送CPA错误信息");
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"utf-8");
            helper.setFrom(sendTo);
            helper.setTo(sendTo);
            helper.setSubject("CPA 错误日志-"+ DateUtil.yesterday());
            helper.setText("CPA错误日志，详情请查看附件！谢谢！");
            File dir=new File(errorPath);
            if (!dir.exists()){
                logger.error("【定时邮件任务】--找不到日志文件");
            }
            String regex="error-"+DateUtil.yesterday()+".*";
            List<File> fileList=new ArrayList<>();
            if (dir.exists()){
                File[]files=dir.listFiles();
                if (files!=null&&files.length>0){
                    Arrays.stream(files)
                            .filter(file -> file.exists() && file.length() > 0 && file.getName().matches(regex))
                            .forEach(file -> fileList.add(file));
                }
            }
            if (fileList!=null&&fileList.size()>0){
                for (int i=0;i<fileList.size();i++){
                    helper.addAttachment("cpa-"+fileList.get(i).getName(), fileList.get(i));
                }
                mailSender.send(mimeMessage);
                logger.info("【定时任务】--邮件发送成功");
            }
        }catch (MessagingException e){
            logger.error("【定时邮件任务】--发送邮件失败");
            logger.error("【定时邮件任务】--"+ ExceptionInfo.getErrorInfo(e).toString());
        }finally {
            logger.info("【定时任务】--CPA错误信息邮件发送完成");
        }
    }
}
