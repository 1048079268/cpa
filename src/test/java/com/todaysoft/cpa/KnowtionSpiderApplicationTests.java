package com.todaysoft.cpa;

import com.todaysoft.cpa.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KnowtionSpiderApplicationTests {
	@Value("${spring.mail.username}")
	private String sendFrom;
	@Value("${exception.mail.username}")
	private String sendTo;
	@Value("${logging.path}")
	private String errorPath;
	@Autowired
	private JavaMailSender mailSender;
	@Test
	public void contextLoads() throws MessagingException, UnsupportedEncodingException {
//		MimeMessage mimeMessage = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"utf-8");
//		helper.setFrom(sendFrom);
//		helper.setTo(sendTo);
//		helper.setSubject("CPA 错误日志-"+DateUtil.yesterday());
//		helper.setText("CPA错误日志，详情请查看附件！谢谢！");
//		File dir=new File(errorPath);
//		String regex="error-"+DateUtil.yesterday()+".*";
//		List<File> fileList=new ArrayList<>();
//		if (dir.exists()){
//			File[]files=dir.listFiles();
//			if (files!=null&&files.length>0){
//				Arrays.stream(files)
//						.filter(file -> file.exists() && file.length() > 0 && file.getName().matches(regex))
//						.forEach(file -> fileList.add(file));
//			}
//		}
//		if (fileList!=null&&fileList.size()>0){
//			for (int i=0;i<fileList.size();i++){
//				helper.addAttachment("cpa-"+fileList.get(i).getName(), fileList.get(i));
//			}
//			mailSender.send(mimeMessage);
//		}else {
//			System.out.println("不发送邮件");
//		}
	}

}
