package com.todaysoft.cpa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.ReadJson;
import com.todaysoft.cpa.domain.entity.DrugProduct;
import com.todaysoft.cpa.service.vice.DrugProductService;
import com.todaysoft.cpa.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KnowtionSpiderApplicationTests {
    private static Logger logger= LoggerFactory.getLogger(KnowtionSpiderApplicationTests.class);
	@Value("${spring.mail.username}")
	private String sendFrom;
	@Value("${exception.mail.username}")
	private String sendTo;
	@Value("${logging.path}")
	private String errorPath;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	DrugProductService drugProductService;
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

    @Test
	public void test() throws FileNotFoundException {
//        JSONObject temp= ReadJson.read("test/drugproduct-122.json");
//        JSONArray products = temp.getJSONArray("products");
//        Set<String> keys=new HashSet<>();
//        for (int i = 0; i < products.size(); i++) {
//            JSONObject product=products.getJSONObject(i);
//            DrugProduct drugProduct = drugProductService.save(product, product, null, new HashMap<>());
//            if (drugProduct==null){
//                logger.info("null:"+product.toJSONString());
//            }else if (keys.contains(drugProduct.getProductKey())){
//                logger.info(product.toJSONString());
//            }else {
//                keys.add(drugProduct.getProductKey());
//            }
//        }
    }

}
