package com.todaysoft.cpa.service;

import com.todaysoft.cpa.merge.MergeSheetOperator;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.service.main.ClinicalTrialService;
import com.todaysoft.cpa.service.main.DrugService;
import com.todaysoft.cpa.service.main.GeneService;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.ExceptionInfo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
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

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/15 10:13
 */
@Service
public class MergeService {
    private static Logger logger= LoggerFactory.getLogger(MergeService.class);
    //用来避免scanAndSave()方法重复执行扫描和保存操作
    private final static AtomicBoolean isScan=new AtomicBoolean(false);
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
    @Autowired
    private ClinicalTrialService clinicalTrialService;
    /**
     * 根据各项的重合数据创建excel
     * 如果有一项由重合数据就发送邮件给相关人员
     * @throws IOException
     */
    public void createExcelAndSend() throws IOException {
        Workbook wb = new XSSFWorkbook();
        MergeSheetOperator operator = mergeInfo -> {
            if (mergeInfo.checkList != null && mergeInfo.checkList.size() > 1) {
                Sheet sheet = wb.createSheet(mergeInfo.name().toLowerCase());
                List<List<String>> lists = mergeInfo.checkList;
                for (int i = 0; i < lists.size(); i++) {
                    List<String> list = lists.get(i);
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < list.size(); j++) {
                        row.createCell(j).setCellValue(list.get(j));
                    }
                }
                return true;
            }
            return false;
        };
        boolean isCreateAndSend = operator.operate(MergeInfo.DRUG);
        isCreateAndSend = operator.operate(MergeInfo.DRUG_PRODUCT)||isCreateAndSend;
        isCreateAndSend = operator.operate(MergeInfo.KEGG_PATHWAY)||isCreateAndSend;
        isCreateAndSend = operator.operate(MergeInfo.CLINICAL_TRIAL)||isCreateAndSend;
        isCreateAndSend = operator.operate(MergeInfo.GENE)||isCreateAndSend;
        //扫描以前发送失败的邮件的附件，如果有，在本次发送时一起发送，发送成功后删除
        File dir=new File(mergeScanDir);
        File[] files=null;
        if (dir.exists()){
            files = dir.listFiles(pathname -> pathname.getName().matches("^superposition-\\d*\\.xlsx$"));
        }
        if (!isCreateAndSend&&(files==null||files.length==0)) {
            wb.close();
            return;
        }
        logger.info("【MergeService】CPA与老库有重合数据，开始发送邮件...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        baos.flush();
        byte[] bt = baos.toByteArray();
        InputStream is = new ByteArrayInputStream(bt, 0, bt.length);
        baos.close();
        DataSource source = new ByteArrayDataSource(is, "application/msexcel");
        is.close();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        boolean isSendSuccess=true;
        try {
            helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(sendFrom);
            helper.setTo(mergeCheckSendTo);
            helper.setSubject("CPA与老库重合数据" + DateUtil.today());
            helper.setText("你好：这是CPA与老库重合的数据，详情请查看附件！谢谢！");
            String fileName = "superposition-" + DateUtil.today() + ".xlsx";
            if (isCreateAndSend){
                helper.addAttachment(MimeUtility.encodeText(fileName), source);
            }
            //发送上次发送失败的
            if (files!=null&&files.length>0){
                for (File file : files) {
                    helper.addAttachment(MimeUtility.encodeText(file.getName()),file);
                }
            }
            mailSender.send(mimeMessage);
            logger.info("【MergeService】邮件发送成功！");
        } catch (MessagingException e) {
            isSendSuccess=false;
            //发生异常后保存在本地，以免丢失
            String path = mergeScanDir + "/superposition-" + System.currentTimeMillis() + ".xlsx";
            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
            wb.close();
            logger.error("【MergeService】邮件发送失败！",e);
            logger.error("【MergeService】附件保存在：" + path);
        }
        if (isSendSuccess&&files!=null&&files.length>0){
            for (File file : files) {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * @desc:  定时扫描合并文件并保存相应数据到数据库
     * @author: 鱼唇的人类
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void scanAndSave() throws IOException, InvalidFormatException {
        if (isScan.get()){
            return;
        }
        try {
            isScan.set(true);
            //扫描文件并将数据写入各自的mergeList
            File dir=new File(mergeScanDir);
            if (!dir.exists()){
                return;
            }
            //文件名规则：merge[数字].xlsx
            File[] files = dir.listFiles(pathname -> pathname.getName().matches("^merge\\d*\\.xlsx$"));
            if (files==null||files.length==0){
                return;
            }
            //扫描文件并转换为相应的审核结果数据
            for (File file : files) {
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                MergeSheetOperator readSheet=(mergeInfo)->{
                    XSSFSheet sheet=workbook.getSheet(mergeInfo.name().toLowerCase());
                    if (sheet!=null){
                        for (Row row : sheet) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < row.getLastCellNum(); i++) {
                                Cell cell = row.getCell(i);
                                switch (cell.getCellTypeEnum()){
                                    case STRING:
                                        list.add(cell.getStringCellValue());
                                        break;
                                    case NUMERIC:
                                        list.add(String.valueOf((int) cell.getNumericCellValue()));
                                        break;
                                    case BOOLEAN:
                                        list.add(String.valueOf(cell.getBooleanCellValue()));
                                        break;
                                }
                            }
                            mergeInfo.mergeList.add(list);
                        }
                    }
                    return true;
                };
                readSheet.operate(MergeInfo.DRUG);
                readSheet.operate(MergeInfo.DRUG_PRODUCT);
                readSheet.operate(MergeInfo.KEGG_PATHWAY);
                readSheet.operate(MergeInfo.CLINICAL_TRIAL);
                readSheet.operate(MergeInfo.GENE);
                workbook.close();
            }
            //合并药物的审核结果
            Map<String,Map<String,Integer>> map=new HashMap<>();
            MergeInfo.DRUG.mergeList.forEach(list->{
                Map<String,Integer> value=new HashMap<>();
                value.put(list.get(0), Integer.valueOf(list.get(1)));
                map.put(list.get(0),value);
            });
            //合并药品的审核结果
            MergeInfo.DRUG_PRODUCT.mergeList.forEach(list->{
                if (map.containsKey(list.get(0))){
                    //解释：<-->是拼接符:药品批准号 作为标识主键
                    map.get(list.get(0)).put(list.get(2), Integer.valueOf(list.get(3)));
                }else {
                    Map<String,Integer> value=new HashMap<>();
                    value.put(list.get(2), Integer.valueOf(list.get(3)));
                    map.put(list.get(0),value);
                }
            });
            //合并通路的审核结果
            MergeInfo.KEGG_PATHWAY.mergeList.forEach(list->{
                if (map.containsKey(list.get(0))){
                    map.get(list.get(0)).put(list.get(1), Integer.valueOf(list.get(2)));
                }else {
                    Map<String,Integer> value=new HashMap<>();
                    value.put(list.get(1), Integer.valueOf(list.get(2)));
                    map.put(list.get(0),value);
                }
            });
            //入库成功的数据要从相应的mergeList移除，剩下的就是未处理或者入库失败的数据
            //药物
            map.forEach((key,value)->{
                drugService.saveOne(key, value);
            });
            //基因
            Set<List<String>> geneMergeSet=new HashSet<>();
            geneMergeSet.addAll(MergeInfo.GENE.mergeList);
            geneMergeSet.forEach(list->{
                geneService.saveOne(CPA.GENE,MergeInfo.GENE, list.get(0), Integer.parseInt(list.get(1)));
            });
            //临床试验
            Set<List<String>> clinicalTrialMergeSet=new HashSet<>();
            clinicalTrialMergeSet.addAll(MergeInfo.CLINICAL_TRIAL.mergeList);
            clinicalTrialMergeSet.forEach(list->{
                clinicalTrialService.saveOne(CPA.CLINICAL_TRIAL,MergeInfo.CLINICAL_TRIAL, list.get(0), Integer.parseInt(list.get(1)));
            });
            //执行入库完成后如果有未完成的数据就写入文件等待下一次扫描
            Workbook wb = new XSSFWorkbook();
            MergeSheetOperator operator=mergeInfo -> {
                if (mergeInfo.mergeList!=null&&mergeInfo.mergeList.size()>0){
                    Sheet sheet = wb.createSheet(mergeInfo.name().toLowerCase());
                    Iterator<List<String>> iterator = mergeInfo.mergeList.iterator();
                    int i=0;
                    while (iterator.hasNext()){
                        List<String> next = iterator.next();
                        Row row = sheet.createRow(i);
                        i++;
                        for (int j=0;j<next.size();j++){
                            row.createCell(j).setCellValue(next.get(j));
                        }
                    }
                    return true;
                }
                return false;
            };
            //判断是否有未保存成功的
            boolean isSave= operator.operate(MergeInfo.DRUG);
            isSave= operator.operate(MergeInfo.DRUG_PRODUCT)||isSave;
            isSave= operator.operate(MergeInfo.KEGG_PATHWAY)||isSave;
            isSave= operator.operate(MergeInfo.CLINICAL_TRIAL)||isSave;
            isSave= operator.operate(MergeInfo.GENE)||isSave;
            //删除已处理的文件
            for (File file : files) {
                if (file.exists()) {
                    file.delete();
                }
            }
            if (!isSave){
                return;
            }
            //保存未处理的数据，merge.xlsx是保留文件，不能占用
            String path=mergeScanDir+"/merge.xlsx";
            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
            wb.close();
        }finally {
            isScan.set(false);
        }
    }

    /**
     * 合并信息的初始化
     * 主要是清除审核集合还有初始化表头、删除老的重合数据文件（待审核文件）
     */
    public void mergeInit(){
        //drug
        MergeInfo.DRUG.checkList.clear();
        List<String> drug=new ArrayList<>();
        drug.add(0,"cpa_drug_id");
        drug.add(1,"cpa_drug_name");
        drug.add(2,"old_drug_key");
        drug.add(3,"old_drug_name");
        MergeInfo.DRUG.checkList.add(0,drug);
        //drugProduct
        MergeInfo.DRUG_PRODUCT.checkList.clear();
        List<String> drugProduct=new ArrayList<>();
        drugProduct.add(0,"cpa_drug_id");
        drugProduct.add(1,"cpa_drug_name");
        drugProduct.add(2,"cpa_drug_product_name");
        drugProduct.add(3,"cpa_drug_product_approval_number");
        drugProduct.add(4,"old_drug_product_key");
        drugProduct.add(5,"old_drug_product_name");
        drugProduct.add(6,"old_drug_product_approval_number");
        MergeInfo.DRUG_PRODUCT.checkList.add(0,drugProduct);
        //keggPathway
        MergeInfo.KEGG_PATHWAY.checkList.clear();
        List<String> keggPathway=new ArrayList<>();
        keggPathway.add(0,"cpa_drug_id");
        keggPathway.add(1,"cpa_drug_name");
        keggPathway.add(2,"cpa_kegg_pathway_id");
        keggPathway.add(3,"cpa_kegg_pathway_name");
        keggPathway.add(4,"old_kegg_pathway_key");
        keggPathway.add(5,"old_kegg_pathway_id");
        keggPathway.add(6,"old_kegg_pathway_name");
        MergeInfo.KEGG_PATHWAY.checkList.add(0,keggPathway);
        //clinicalTrial
        MergeInfo.CLINICAL_TRIAL.checkList.clear();
        List<String> clinicalTrial=new ArrayList<>();
        clinicalTrial.add(0,"cpa_clinicalTrial_id");
        clinicalTrial.add(1,"old_clinicalTrial_key");
        clinicalTrial.add(2,"old_clinicalTrial_id");
        MergeInfo.CLINICAL_TRIAL.checkList.add(0,clinicalTrial);
        //gene
        MergeInfo.GENE.checkList.clear();
        List<String> gene=new ArrayList<>();
        gene.add(0,"cpa_gene_id");
        gene.add(1,"cpa_gene_symbol");
        gene.add(2,"old_gene_key");
        gene.add(3,"old_gene_symbol");
        MergeInfo.GENE.checkList.add(0,gene);
    }
}
