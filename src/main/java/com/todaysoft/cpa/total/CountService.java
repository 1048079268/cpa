package com.todaysoft.cpa.total;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.entity.Protein;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.JsonUtil;
import com.todaysoft.cpa.utils.WordCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/31 17:59
 */
@Service
public class CountService {
    private static Logger logger= LoggerFactory.getLogger(CountService.class);
    @Async
    public void countProtein() throws IOException, InterruptedException {
        CountFunction countFunction=(json,map)->{
            SimpleCount simpleCount=(key,text)->{
                if (!map.containsKey(key)){
                    map.put(key,0L);
                }
                if (!StringUtils.isEmpty(text)){
                    long count= WordCountUtil.count(text);
                    map.replace(key,map.get(key)+count);
                }
            };
            String functionDescription=json.getString("functionDescription");
            simpleCount.count("functionDescription",functionDescription);
            String tissueSpecificity=json.getString("tissueSpecificity");
            simpleCount.count("tissueSpecificity",tissueSpecificity);
            String synonyms= JsonUtil.jsonArrayToString(json.getJSONArray("synonyms"),";");
            simpleCount.count("synonyms",synonyms);
            return map;
        };
        CountScan countScan=new CountScan(CPA.PROTEIN,countFunction);
        Map<String, Long> countMap = countScan.scan();
        logger.info("----Protein----");
        countMap.forEach((key, value) -> logger.info(key + ":" + value));
        logger.info("----Protein----");
    }

    @Async
    public void countMedicationPlan() throws IOException, InterruptedException {
        CountFunction countFunction=(json,map)->{
            SimpleCount simpleCount=(key,text)->{
                if (!map.containsKey(key)){
                    map.put(key,0L);
                }
                if (!StringUtils.isEmpty(text)){
                    long count= WordCountUtil.count(text);
                    map.replace(key,map.get(key)+count);
                }
            };
            String chemotherapyDescription=json.getString("chemotherapyDescription");
            simpleCount.count("chemotherapyDescription",chemotherapyDescription);
            String chemotherapyType=json.getString("chemotherapyType");
            simpleCount.count("chemotherapyType",chemotherapyType);
            JSONArray instructions = json.getJSONArray("instructions");
            if (instructions!=null&&instructions.size()>0){
                for (int i=0;i<instructions.size();i++){
                    JSONArray instructionList = instructions.getJSONObject(i).getJSONArray("instructionList");
                    if (instructionList!=null&&instructionList.size()>0){
                        for (int j=0;j<instructionList.size();j++){
                            JSONObject jsonObject = instructionList.getJSONObject(j);
                            if (jsonObject!=null){
                                String text = jsonObject.getString("text");
                                if (!StringUtils.isEmpty(text)){
                                    simpleCount.count("text",text);
                                }
                            }
                        }
                    }
                }

            }
            return map;
        };
        CountScan countScan=new CountScan(CPA.REGIMEN,countFunction);
        Map<String, Long> countMap = countScan.scan();
        logger.info("----MedicationPlan----");
        countMap.forEach((key, value) -> logger.info(key + ":" + value));
        logger.info("----MedicationPlan----");
    }

    @Async
    public void countGene() throws IOException, InterruptedException {
        CountFunction countFunction=(json,map)-> {
            SimpleCount simpleCount = (key, text) -> {
                if (!map.containsKey(key)) {
                    map.put(key, 0L);
                }
                if (!StringUtils.isEmpty(text)) {
                    long count = WordCountUtil.count(text);
                    map.replace(key, map.get(key) + count);
                }
            };
            String entrezGeneSummary=json.getString("entrezGeneSummary");
            simpleCount.count("entrezGeneSummary",entrezGeneSummary);
            String otherNames=JsonUtil.jsonArrayToString(json.getJSONArray("otherNames"),";");
            simpleCount.count("otherNames",otherNames);
            return map;
        };
        CountScan countScan=new CountScan(CPA.GENE,countFunction);
        Map<String, Long> countMap = countScan.scan();
        logger.info("----Gene----");
        countMap.forEach((key, value) -> logger.info(key + ":" + value));
        logger.info("----Gene----");
    }

    @Async
    public void countClinicalTrial() throws IOException, InterruptedException {
        CountFunction countFunction=(json,map)-> {
            SimpleCount simpleCount = (key, text) -> {
                if (!map.containsKey(key)) {
                    map.put(key, 0L);
                }
                if (!StringUtils.isEmpty(text)) {
                    long count = WordCountUtil.count(text);
                    map.replace(key, map.get(key) + count);
                }
            };
            String title=json.getString("title");
            simpleCount.count("title",title);
            JSONArray outcomes = json.getJSONArray("outcomes");
            if (outcomes!=null&&outcomes.size()>0){
                for (int i=0;i<outcomes.size();i++){
                    String classification = outcomes.getJSONObject(i).getString("classification");
                    if (!StringUtils.isEmpty(classification)){
                        simpleCount.count("classification",classification);
                    }
                    String outcomesTitle = outcomes.getJSONObject(i).getString("title");
                    if (!StringUtils.isEmpty(outcomesTitle)){
                        simpleCount.count("outcomesTitle",outcomesTitle);
                    }
                }
            }
            return map;
        };
        CountScan countScan=new CountScan(CPA.CLINICAL_TRIAL,countFunction);
        Map<String, Long> countMap = countScan.scan();
        logger.info("----ClinicalTrial----");
        countMap.forEach((key, value) -> logger.info(key + ":" + value));
        logger.info("----ClinicalTrial----");
    }

    @FunctionalInterface
    private interface SimpleCount{
        void count(String key,String text);
    }
}
