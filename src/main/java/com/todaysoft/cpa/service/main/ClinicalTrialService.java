package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.clinicalTrail.CnClinicalTrailRepository;
import com.todaysoft.cpa.domain.cn.clinicalTrail.CnClinicalTrialCancerRepository;
import com.todaysoft.cpa.domain.cn.clinicalTrail.CnClinicalTrialOutcomeRepository;
import com.todaysoft.cpa.domain.cn.drug.CnDrugClinicalTrialRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrialCancerRepository;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrialOutcomeRepository;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.entity.ClinicalTrialCancer;
import com.todaysoft.cpa.domain.entity.ClinicalTrialOutcome;
import com.todaysoft.cpa.domain.entity.ClinicalTrial;
import com.todaysoft.cpa.domain.en.drug.DrugClinicalTrialRepository;
import com.todaysoft.cpa.domain.en.drug.DrugRepository;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.entity.DrugClinicalTrial;
import com.todaysoft.cpa.domain.entity.DrugClinicalTrialPK;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.*;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:08
 */
@Service
public class ClinicalTrialService extends BaseService {
    private static Logger logger= LoggerFactory.getLogger(ClinicalTrialService.class);
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private CnClinicalTrailRepository cnClinicalTrailRepository;
    @Autowired
    private CnClinicalTrialOutcomeRepository cnClinicalTrialOutcomeRepository;
    @Autowired
    private CnClinicalTrialCancerRepository cnClinicalTrialCancerRepository;
    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private ClinicalTrialOutcomeRepository clinicalTrialOutcomeRepository;
    @Autowired
    private ClinicalTrialCancerRepository clinicalTrialCancerRepository;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugClinicalTrialRepository drugClinicalTrialRepository;
    @Autowired
    private CnDrugClinicalTrialRepository cnDrugClinicalTrialRepository;
    @Autowired
    private KbUpdateService kbUpdateService;

    /**更新逻辑
    1.如果英文库有数据，就不更新中文库，且英文库状态与以前保存一致
    2.如果英文库没有数据，判断中文库是否有数据
    2.1 如果中文库有，有的话中文库状态保持不变，老库覆盖CPA
    2.2 如果中文库没有，则全部保存
     **/

    @Override
    @Transactional
    public boolean save(JSONObject en,JSONObject cn,int status) throws InterruptedException {
        ClinicalTrial checkClinicalTrial=cn.toJavaObject(ClinicalTrial.class);
        ClinicalTrial oldCn= cnClinicalTrailRepository.findById(checkClinicalTrial.getClinicalTrialId());
        ClinicalTrial oldEn = clinicalTrailRepository.findById(checkClinicalTrial.getClinicalTrialId());
        //是否使用老中文库数据状态
        boolean isUseOldCnState=oldCn!=null;
        //是否使用老英文库数据状态
        boolean isUseOldEnState=oldEn!=null;
        //是否保存中文数据
        boolean isSaveCn = oldEn==null&&(oldCn==null||oldCn.getCreatedWay()==3);
        //是否是老库覆盖CPA数据
        boolean isOldBaseData= oldEn==null && oldCn!=null && oldCn.getCreatedWay()==3;
        //判断使用哪个id
        String clinicalTrialKey=oldCn==null?PkGenerator.generator(ClinicalTrial.class):oldCn.getClinicalTrialKey();
        JsonObjectConverter<ClinicalTrial> converter=(json)->{
            ClinicalTrial clinicalTrial=json.toJavaObject(ClinicalTrial.class);
            clinicalTrial.setClinicalTrialKey(clinicalTrialKey);
            clinicalTrial.setCreatedAt(System.currentTimeMillis());
            clinicalTrial.setCountries(JsonUtil.jsonArrayToString(json.getJSONArray("countries"),","));
            clinicalTrial.setCheckState(1);
            clinicalTrial.setCreatedWay(2);
            clinicalTrial.setCreatedByName("CPA");
            return clinicalTrial;
        };
        ClinicalTrial clinicalTrialEn = converter.convert(en);
        ClinicalTrial clinicalTrialCn = converter.convert(cn);
        //更新状态
        if (isUseOldCnState){
            clinicalTrialCn.setCreatedWay(oldCn.getCreatedWay());
            clinicalTrialCn.setCheckState(oldCn.getCheckState());
            clinicalTrialCn.setCreatedByName(oldCn.getCreatedByName());
        }
        if (isUseOldEnState){
            clinicalTrialEn.setCreatedWay(oldEn.getCreatedWay());
            clinicalTrialEn.setCheckState(oldEn.getCheckState());
            clinicalTrialEn.setCreatedByName(oldEn.getCreatedByName());
        }
        //老库覆盖CPA中文库
        if (isOldBaseData){
            clinicalTrialEn.setCheckState(4);
            if (!StringUtils.isEmpty(oldCn.getTheTitle())){
                clinicalTrialCn.setTheTitle(oldCn.getTheTitle());
            }
            if (!StringUtils.isEmpty(oldCn.getTheStatus())){
                clinicalTrialCn.setTheStatus(oldCn.getTheStatus());
            }
            if (!StringUtils.isEmpty(oldCn.getThePhase())){
                clinicalTrialCn.setThePhase(oldCn.getThePhase());
            }
            if (!StringUtils.isEmpty(oldCn.getTheType())){
                clinicalTrialCn.setTheType(oldCn.getTheType());
            }
            if (!StringUtils.isEmpty(oldCn.getStartDate())){
                clinicalTrialCn.setStartDate(oldCn.getStartDate());
            }
            if (!StringUtils.isEmpty(oldCn.getCountries())){
                clinicalTrialCn.setCountries(oldCn.getCountries());
            }
            if (!StringUtils.isEmpty(oldCn.getTheUrl())){
                clinicalTrialCn.setTheUrl(oldCn.getTheUrl());
            }
            if (!StringUtils.isEmpty(oldCn.getPathologicalState())){
                clinicalTrialCn.setPathologicalState(oldCn.getPathologicalState());
            }
            if (!StringUtils.isEmpty(oldCn.getExistTreatment())){
                clinicalTrialCn.setExistTreatment(oldCn.getExistTreatment());
            }
            clinicalTrialCn.setThePmid(MergeUtil.cover(oldCn.getThePmid(),clinicalTrialCn.getThePmid()));
            clinicalTrialCn.setTestCenter(MergeUtil.cover(oldCn.getTestCenter(),clinicalTrialCn.getTestCenter()));
            clinicalTrialCn.setOrganization(MergeUtil.cover(oldCn.getOrganization(),clinicalTrialCn.getOrganization()));
        }
        clinicalTrialEn=clinicalTrailRepository.save(clinicalTrialEn);
        if (isSaveCn){
            cnClinicalTrailRepository.save(clinicalTrialCn);
        }
        saveFixed(clinicalTrialEn,en,cn,isSaveCn);
        //临床&药物
        ClinicalTrial finalClinicalTrialEn = clinicalTrialEn;
        JsonArrayConverter<DrugClinicalTrial> clinicalTrialConverter=(json)->{
            List<DrugClinicalTrial> clinicalTrialList=new ArrayList<>();
            JSONArray drugs = json.getJSONArray("drugs");
            if (drugs!=null&&drugs.size()>0){
                for (int i=0;i<drugs.size();i++){
                    String drugId= drugs.getString(i);
                    Drug drug = drugRepository.findByDrugId(Integer.valueOf(drugId));
                    if (drug==null){
                        throw new DataException("未找到相应的药物，info->drugId="+drugId);
                    }
                    DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
                    drugClinicalTrial.setClinicalTrialId(finalClinicalTrialEn.getClinicalTrialId());
                    drugClinicalTrial.setClinicalTrialKey(finalClinicalTrialEn.getClinicalTrialKey());
                    drugClinicalTrial.setDrugId(drug.getDrugId());
                    drugClinicalTrial.setDrugKey(drug.getDrugKey());
                    clinicalTrialList.add(drugClinicalTrial);
                }
            }
            return clinicalTrialList;
        };
        drugClinicalTrialRepository.save(clinicalTrialConverter.convert(en));
        if (isSaveCn){
            cnDrugClinicalTrialRepository.save(clinicalTrialConverter.convert(cn));
        }
        if (oldEn==null&&clinicalTrialCn.getCheckState()==1){
            kbUpdateService.send("kt_clinical_trial");
        }
        return true;
    }

    /**
     * 已弃用
     * @param en
     * @param cn
     * @param dependenceKey
     * @param status
     *    0 表示如果与老库有重合的话就终止运行并并写入待审核列表
     *    1 表示如果与老库有重合的话就与老库合并
     *    2 表示如果与老库有重合的话也不与老库合并
     * @return
     * @throws InterruptedException
     */
    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey,int status) throws InterruptedException {
//        String clinicalTrialKey=PkGenerator.generator(ClinicalTrial.class);
//        ClinicalTrial checkClinicalTrial=cn.toJavaObject(ClinicalTrial.class);
//        ClinicalTrial byTitle = cnClinicalTrailRepository.findById(checkClinicalTrial.getClinicalTrialId());
//        if (byTitle!=null){
//            logger.info("【" + CPA.CLINICAL_TRIAL.name() + "】与老库合并->id="+byTitle.getClinicalTrialId());
//            clinicalTrialKey=byTitle.getClinicalTrialKey();
//        }
//        String finalClinicalTrialKey = clinicalTrialKey;
//        JsonObjectConverter<ClinicalTrial> converter=(json)->{
//            ClinicalTrial clinicalTrial=json.toJavaObject(ClinicalTrial.class);
//            clinicalTrial.setClinicalTrialKey(finalClinicalTrialKey);
//            clinicalTrial.setCheckState(1);
//            clinicalTrial.setCreatedAt(System.currentTimeMillis());
//            clinicalTrial.setCreatedWay(2);
//            clinicalTrial.setCreatedByName("CPA");
//            clinicalTrial.setCountries(JsonUtil.jsonArrayToString(en.getJSONArray("countries"),","));
//            return clinicalTrial;
//        };
//        ClinicalTrial clinicalTrialEn=clinicalTrailRepository.save(converter.convert(en));
//        ClinicalTrial clinicalTrialCn = converter.convert(cn);
//        //老库覆盖CPA中文库
//        if (byTitle!=null){
//            if (!StringUtils.isEmpty(byTitle.getTheTitle())){
//                clinicalTrialCn.setTheTitle(byTitle.getTheTitle());
//            }
//            if (!StringUtils.isEmpty(byTitle.getTheStatus())){
//                clinicalTrialCn.setTheStatus(byTitle.getTheStatus());
//            }
//            if (!StringUtils.isEmpty(byTitle.getThePhase())){
//                clinicalTrialCn.setThePhase(byTitle.getThePhase());
//            }
//            if (!StringUtils.isEmpty(byTitle.getTheType())){
//                clinicalTrialCn.setTheType(byTitle.getTheType());
//            }
//            if (!StringUtils.isEmpty(byTitle.getStartDate())){
//                clinicalTrialCn.setStartDate(byTitle.getStartDate());
//            }
//            if (!StringUtils.isEmpty(byTitle.getCountries())){
//                clinicalTrialCn.setCountries(byTitle.getCountries());
//            }
//            if (!StringUtils.isEmpty(byTitle.getTheUrl())){
//                clinicalTrialCn.setTheUrl(byTitle.getTheUrl());
//            }
//        }
//        cnClinicalTrailRepository.save(clinicalTrialCn);
//        saveFixed(clinicalTrialEn,en,cn,true);
//        Drug drug=drugRepository.findOne(dependenceKey);
//        if (drug!=null){
//            DrugClinicalTrialPK pk=new DrugClinicalTrialPK();
//            pk.setClinicalTrialKey(clinicalTrialEn.getClinicalTrialKey());
//            pk.setDrugKey(drug.getDrugKey());
//            if (drugClinicalTrialRepository.findOne(pk)==null){
//                DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
//                drugClinicalTrial.setDrugKey(dependenceKey);
//                drugClinicalTrial.setDrugId(drug.getDrugId());
//                drugClinicalTrial.setClinicalTrialKey(clinicalTrialEn.getClinicalTrialKey());
//                drugClinicalTrial.setClinicalTrialId(clinicalTrialEn.getClinicalTrialId());
//                drugClinicalTrialRepository.save(drugClinicalTrial);
//                cnDrugClinicalTrialRepository.save(drugClinicalTrial);
//            }
//        }
//        return true;
        return false;
    }

    /**
     * 保存没有关联的部分数据
     * @param clinicalTrial
     */
    private void saveFixed(ClinicalTrial clinicalTrial,JSONObject en,JSONObject cn,boolean isSaveCn) throws InterruptedException {
        //成果
        List<ClinicalTrialOutcome> hasOutcomes = clinicalTrialOutcomeRepository.findByClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
        Map<Integer, String> outcomeMap=new HashMap<>();
        if (hasOutcomes!=null){
            outcomeMap = hasOutcomes.stream().collect(Collectors.toMap(ClinicalTrialOutcome::hashCode, ClinicalTrialOutcome::getClinicalTrailOutcomeKey));
        }
        String outcomesKey=PkGenerator.generator(ClinicalTrialOutcome.class);
        Map<Integer, String> finalOutcomeMap = outcomeMap;
        JsonArrayConverter<ClinicalTrialOutcome> outcomeJsonArrayConverter=(json)->{
            JSONArray outcomes=json.getJSONArray("outcomes");
            List<ClinicalTrialOutcome> outcomeList=new ArrayList<>();
            if (outcomes!=null&&outcomes.size()>0){
                for (int i=0;i<outcomes.size();i++){
                    ClinicalTrialOutcome outcome=outcomes.getObject(i,ClinicalTrialOutcome.class);
                    outcome.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                    outcome.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                    String key = finalOutcomeMap.get(outcome.hashCode());
                    outcome.setClinicalTrailOutcomeKey(key==null?PkGenerator.md5(outcomesKey+i):key);
                    outcomeList.add(outcome);
                }
            }
            return outcomeList;
        };
        List<ClinicalTrialOutcome> enOutcomes = outcomeJsonArrayConverter.convert(en);
        clinicalTrialOutcomeRepository.save(enOutcomes);
        if (isSaveCn){
            List<ClinicalTrialOutcome> cnOutcomes = outcomeJsonArrayConverter.convert(cn);
            for (int i = 0; i < cnOutcomes.size(); i++) {
                cnOutcomes.get(i).setClinicalTrailOutcomeKey(enOutcomes.get(i).getClinicalTrailOutcomeKey());
            }
            cnClinicalTrialOutcomeRepository.save(cnOutcomes);
        }
        //与疾病关联
        JsonArrayConverter<ClinicalTrialCancer> cancerJsonArrayConverter=(json)->{
            JSONArray diseases=json.getJSONArray("diseases");
            List<ClinicalTrialCancer> trailCancerList=new ArrayList<>();
            if (diseases!=null&&diseases.size()>0){
                for (int i=0;i<diseases.size();i++){
                    String doid=diseases.getJSONObject(i).getString("doid");
                    Cancer cancer = cancerRepository.findByDoid(doid);
                    if (cancer!=null){
                        ClinicalTrialCancer clinicalTrialCancer =new ClinicalTrialCancer();
                        clinicalTrialCancer.setCancerKey(cancer.getCancerKey());
                        clinicalTrialCancer.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                        clinicalTrialCancer.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                        clinicalTrialCancer.setDoid(Integer.valueOf(cancer.getDoid()));
                        trailCancerList.add(clinicalTrialCancer);
                    }else {
                        throw new DataException("未找到相应的疾病,info->doid="+doid);
                    }
                }
            }
            return trailCancerList;
        };
        clinicalTrialCancerRepository.save(cancerJsonArrayConverter.convert(en));
        if (isSaveCn){
            cnClinicalTrialCancerRepository.save(cancerJsonArrayConverter.convert(cn));
        }
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.CLINICAL_TRIAL.name=cpaProperties.getClinicalTrialName();
        CPA.CLINICAL_TRIAL.contentUrl=cpaProperties.getClinicalTrialUrl();
        CPA.CLINICAL_TRIAL.tempStructureMap=AcquireJsonStructure.getJsonKeyMap(cpaProperties.getClinicalTrialTempPath());
        Set<String> ids=clinicalTrailRepository.findIdByCPA();
        CPA.CLINICAL_TRIAL.dbId.addAll(ids);
    }

}
