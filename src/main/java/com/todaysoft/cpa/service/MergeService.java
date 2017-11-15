package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.MergeInfo;
import com.todaysoft.cpa.service.main.DrugService;
import com.todaysoft.cpa.service.main.GeneService;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.ExceptionInfo;
import com.todaysoft.cpa.utils.FileUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.PrivateKey;
import java.util.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/15 10:13
 */
@Service
public class MergeService {
    private static Logger logger= LoggerFactory.getLogger(MergeService.class);
    @Value("${merge.scan.dir}")
    private String mergeScanDir;
    @Value("${merge.check.mail.sendTo}")
    private String mergeCheckSendTo;
    @Value("${spring.mail.username}")
    private String sendFrom;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private DrugService drugService;
    @Autowired
    private GeneService geneService;
    /**
     * 根据各项的重合数据创建excel
     * 如果有一项由重合数据就发送邮件给相关人员
     * @throws IOException
     */
    public void createExcelAndSend() throws IOException {
        Workbook wb = new XSSFWorkbook();
        SimpleCreateSheet simpleCreateSheet=mergeInfo -> {
            if (mergeInfo.checkList!=null&&mergeInfo.checkList.size()>1){
                Sheet sheet = wb.createSheet(mergeInfo.name().toLowerCase());
                List<List<String>> lists = mergeInfo.checkList;
                for (int i=0;i<lists.size();i++){
                    List<String> list=lists.get(i);
                    Row row = sheet.createRow(i);
                    for (int j=0;j<list.size();j++){
                        row.createCell(j).setCellValue(list.get(j));
                    }
                }
                return true;
            }
            return false;
        };
        boolean isCreateAndSend= simpleCreateSheet.createSheet(MergeInfo.DRUG);
        isCreateAndSend=isCreateAndSend||simpleCreateSheet.createSheet(MergeInfo.DRUG_PRODUCT);
        isCreateAndSend=isCreateAndSend||simpleCreateSheet.createSheet(MergeInfo.KEGG_PATHWAY);
        isCreateAndSend=isCreateAndSend||simpleCreateSheet.createSheet(MergeInfo.CLINICAL_TRIAL);
        isCreateAndSend=isCreateAndSend||simpleCreateSheet.createSheet(MergeInfo.GENE);
        if (!isCreateAndSend){
            wb.close();
            return;
        }
        logger.info("【MergeService】CPA与老库有重合数据，开始发送邮件...");
        String path=mergeScanDir+"/check.xlsx";
        FileOutputStream fos = new FileOutputStream(path);
        wb.write(fos);
        fos.close();
        wb.close();
        File file=new File(path);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true,"utf-8");
            helper.setFrom(sendFrom);
            helper.setTo(mergeCheckSendTo);
            helper.setSubject("CPA与老库重合数据"+ DateUtil.today());
            helper.setText("你好：这是CPA与老库重合的数据，详情请查看附件！谢谢！");
            helper.addAttachment("CPA与老库重合数据-"+DateUtil.today()+".xlsx", file);
            mailSender.send(mimeMessage);
            logger.info("【MergeService】邮件发送成功！");
        } catch (MessagingException e) {
            //更名保存在本地，以免丢失。
            File saveFile=new File(mergeScanDir+"/check-"+System.currentTimeMillis()+".xlsx");
            FileUtil.copyFile(file,saveFile);
            logger.error("【MergeService】邮件发送失败！");
            logger.error("【MergeService】附件保存在："+saveFile.getPath());
            logger.error("【MergeService】"+ ExceptionInfo.getErrorInfo(e));
        }
    }

    /**
     * @desc:  TODO 定时扫描合并文件并保存相应数据到数据库
     * @author: 鱼唇的人类
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scanAndSave() throws IOException, InvalidFormatException {
        //扫描文件并将数据写入各自的mergeList
        File dir=new File(mergeScanDir);
        if (!dir.exists()){
            return;
        }
        //文件名规则：merge[数字].xlsx
        File[] files = dir.listFiles(pathname -> pathname.getName().matches("merge\\d*\\.xlsx"));
        if (files==null||files.length==0){
            return;
        }
        for (File file : files) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            SimpleReadSheet readSheet=(mergeInfo)->{
                XSSFSheet sheet=workbook.getSheet(mergeInfo.name().toLowerCase());
                if (sheet!=null){
                    for (Row next : sheet) {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < next.getLastCellNum(); i++) {
                            list.add(next.getCell(i).getStringCellValue());
                        }
                        mergeInfo.mergeList.add(list);
                    }
                }
            };
            readSheet.read(MergeInfo.DRUG);
            readSheet.read(MergeInfo.DRUG_PRODUCT);
            readSheet.read(MergeInfo.KEGG_PATHWAY);
            readSheet.read(MergeInfo.CLINICAL_TRIAL);
            readSheet.read(MergeInfo.GENE);
        }
        //合并药物的审核结果
        Map<String,Map<String,Integer>> map=new HashMap<>();
        MergeInfo.DRUG.mergeList.forEach(list->{
            Map<String,Integer> value=new HashMap<>();
            value.put(list.get(0), Integer.valueOf(list.get(1)));
            map.put(list.get(0),value);
        });
        MergeInfo.DRUG_PRODUCT.mergeList.forEach(list->{
            if (map.containsKey(list.get(0))){
                map.get(list.get(0)).put(list.get(1), Integer.valueOf(list.get(2)));
            }else {
                Map<String,Integer> value=new HashMap<>();
                value.put(list.get(1), Integer.valueOf(list.get(2)));
                map.put(list.get(0),value);
            }
        });
        //药物
        map.forEach((key,value)->{
            boolean success = drugService.saveOne(key, value);
            if (success){
                MergeInfo.DRUG.mergeList.removeIf(next -> next.get(0).equals(key));
                MergeInfo.DRUG_PRODUCT.mergeList.removeIf(next -> next.get(0).equals(key));
                MergeInfo.KEGG_PATHWAY.mergeList.removeIf(next -> next.get(0).equals(key));
            }
        });
        //基因
        MergeInfo.GENE.mergeList.forEach(list->{
            boolean success = geneService.saveOne(CPA.GENE, list.get(0), Integer.parseInt(list.get(1)));
            if (success){
                MergeInfo.GENE.mergeList.removeIf(strings -> strings.get(0).equals(list.get(0)));
            }
        });
    }

    /**
     * 合并信息的初始化
     * 主要是清除审核集合还有初始化表头、删除老的重合数据文件（待审核文件）
     */
    public void mergeInit(){
        //drug
        MergeInfo.DRUG.checkList.clear();
        List<String> drug=new ArrayList<>();
        drug.set(0,"cpa_drug_id");
        drug.set(1,"cpa_drug_name");
        drug.set(2,"old_drug_key");
        drug.set(3,"old_drug_name");
        MergeInfo.DRUG.checkList.add(0,drug);
        //drugProduct
        MergeInfo.DRUG_PRODUCT.checkList.clear();
        List<String> drugProduct=new ArrayList<>();
        drugProduct.set(0,"cpa_drug_id");
        drugProduct.set(1,"cpa_drug_product_name");
        drugProduct.set(2,"old_drug_product_key");
        drugProduct.set(3,"old_drug_product_name");
        MergeInfo.DRUG_PRODUCT.checkList.add(0,drugProduct);
        //keggPathway
        MergeInfo.KEGG_PATHWAY.checkList.clear();
        List<String> keggPathway=new ArrayList<>();
        keggPathway.set(0,"cpa_drug_id");
        keggPathway.set(1,"cpa_kegg_pathway_id");
        keggPathway.set(2,"old_kegg_pathway_key");
        keggPathway.set(3,"old_kegg_pathway_name");
        MergeInfo.KEGG_PATHWAY.checkList.add(0,keggPathway);
        //clinicalTrial
        MergeInfo.CLINICAL_TRIAL.checkList.clear();
        List<String> clinicalTrial=new ArrayList<>();
        clinicalTrial.set(0,"cpa_clinicalTrial_id");
        clinicalTrial.set(1,"old_clinicalTrial_key");
        clinicalTrial.set(2,"old_clinicalTrial_id");
        MergeInfo.CLINICAL_TRIAL.checkList.add(0,clinicalTrial);
        //gene
        MergeInfo.GENE.checkList.clear();
        List<String> gene=new ArrayList<>();
        gene.set(0,"cpa_gene_id");
        gene.set(1,"cpa_gene_symbol");
        gene.set(2,"old_gene_key");
        gene.set(3,"old_gene_symbol");
        MergeInfo.GENE.checkList.add(0,gene);
        //删除重合数据（待审核文件）
        String mergeCheckPath=mergeScanDir+"/check.xlsx";
        File file=new File(mergeCheckPath);
        if (file.exists()){
            file.delete();
        }
    }


    @FunctionalInterface
    private interface SimpleCreateSheet{
        boolean createSheet(MergeInfo mergeInfo);
    }

    @FunctionalInterface
    private interface SimpleReadSheet{
        void read(MergeInfo mergeInfo);
    }
}
