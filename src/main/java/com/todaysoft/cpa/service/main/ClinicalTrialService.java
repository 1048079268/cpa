package com.todaysoft.cpa.service.main;

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
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 14:08
 */
@Service
public class ClinicalTrialService extends BaseService {
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

    @Override
    @Transactional
    public boolean save(JSONObject en,JSONObject cn) throws InterruptedException {
        String clinicalTrialKey=PkGenerator.generator(ClinicalTrial.class);
        JsonObjectConverter<ClinicalTrial> converter=(json)->{
            ClinicalTrial clinicalTrial=json.toJavaObject(ClinicalTrial.class);
            clinicalTrial.setClinicalTrialKey(clinicalTrialKey);
            clinicalTrial.setCheckState(1);
            clinicalTrial.setCreatedAt(System.currentTimeMillis());
            clinicalTrial.setCreatedWay(2);
            clinicalTrial.setCountries(JsonUtil.jsonArrayToString(en.getJSONArray("countries"),","));
            return clinicalTrial;
        };
        ClinicalTrial clinicalTrialEn=clinicalTrailRepository.save(converter.convert(en));
        cnClinicalTrailRepository.save(converter.convert(cn));
        saveFixed(clinicalTrialEn,en,cn);
        //临床&药物
        JsonArrayConverter<DrugClinicalTrial> clinicalTrialConverter=(json)->{
            List<DrugClinicalTrial> clinicalTrialList=new ArrayList<>();
            JSONArray drugs = json.getJSONArray("drugs");
            if (drugs!=null&&drugs.size()>0){
                for (int i=0;i<drugs.size();i++){
                    String drugId= drugs.getString(i);
                    Drug drug = drugRepository.findByDrugId(Integer.valueOf(drugId));
                    if (drug==null){
                        throw new DataException("未找到相应药物，info->drugId="+drugId);
                    }
                    DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
                    drugClinicalTrial.setClinicalTrialId(clinicalTrialEn.getClinicalTrialId());
                    drugClinicalTrial.setClinicalTrialKey(clinicalTrialEn.getClinicalTrialKey());
                    drugClinicalTrial.setDrugId(drug.getDrugId());
                    drugClinicalTrial.setDrugKey(drug.getDrugKey());
                    clinicalTrialList.add(drugClinicalTrial);
                }
            }
            return clinicalTrialList;
        };
        drugClinicalTrialRepository.save(clinicalTrialConverter.convert(en));
        cnDrugClinicalTrialRepository.save(clinicalTrialConverter.convert(cn));
        return true;
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey) throws InterruptedException {
        String clinicalTrialKey=PkGenerator.generator(ClinicalTrial.class);
        JsonObjectConverter<ClinicalTrial> converter=(json)->{
            ClinicalTrial clinicalTrial=json.toJavaObject(ClinicalTrial.class);
            clinicalTrial.setClinicalTrialKey(clinicalTrialKey);
            clinicalTrial.setCheckState(1);
            clinicalTrial.setCreatedAt(System.currentTimeMillis());
            clinicalTrial.setCreatedWay(2);
            clinicalTrial.setCountries(JsonUtil.jsonArrayToString(en.getJSONArray("countries"),","));
            return clinicalTrial;
        };
        ClinicalTrial clinicalTrialEn=clinicalTrailRepository.save(converter.convert(en));
        cnClinicalTrailRepository.save(converter.convert(cn));
        saveFixed(clinicalTrialEn,en,cn);
        Drug drug=drugRepository.findOne(dependenceKey);
        if (drug!=null){
            DrugClinicalTrialPK pk=new DrugClinicalTrialPK();
            pk.setClinicalTrialKey(clinicalTrialEn.getClinicalTrialKey());
            pk.setDrugKey(drug.getDrugKey());
            if (drugClinicalTrialRepository.findOne(pk)==null){
                DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
                drugClinicalTrial.setDrugKey(dependenceKey);
                drugClinicalTrial.setDrugId(drug.getDrugId());
                drugClinicalTrial.setClinicalTrialKey(clinicalTrialEn.getClinicalTrialKey());
                drugClinicalTrial.setClinicalTrialId(clinicalTrialEn.getClinicalTrialId());
                drugClinicalTrialRepository.save(drugClinicalTrial);
                cnDrugClinicalTrialRepository.save(drugClinicalTrial);
            }
        }
        return true;
    }

    /**
     * 保存没有关联的部分数据
     * @param clinicalTrial
     */
    private void saveFixed(ClinicalTrial clinicalTrial,JSONObject en,JSONObject cn) throws InterruptedException {
        //成果
        String outcomesKey=PkGenerator.generator(ClinicalTrialOutcome.class);
        JsonArrayConverter<ClinicalTrialOutcome> outcomeJsonArrayConverter=(json)->{
            JSONArray outcomes=json.getJSONArray("outcomes");
            List<ClinicalTrialOutcome> outcomeList=new ArrayList<>();
            if (outcomes!=null&&outcomes.size()>0){
                for (int i=0;i<outcomes.size();i++){
                    ClinicalTrialOutcome outcome=outcomes.getObject(i,ClinicalTrialOutcome.class);
                    outcome.setClinicalTrailOutcomeKey(PkGenerator.md5(outcomesKey+i));
                    outcome.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                    outcome.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                    outcomeList.add(outcome);
                }
            }
            return outcomeList;
        };
        clinicalTrialOutcomeRepository.save(outcomeJsonArrayConverter.convert(en));
        cnClinicalTrialOutcomeRepository.save(outcomeJsonArrayConverter.convert(cn));
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
                        throw new DataException("未找到相应的疾病，info->doid="+doid);
                    }
                }
            }
            return trailCancerList;
        };
        clinicalTrialCancerRepository.save(cancerJsonArrayConverter.convert(en));
        cnClinicalTrialCancerRepository.save(cancerJsonArrayConverter.convert(cn));
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.CLINICAL_TRIAL.name=cpaProperties.getClinicalTrialName();
        CPA.CLINICAL_TRIAL.contentUrl=cpaProperties.getClinicalTrialUrl();
        CPA.CLINICAL_TRIAL.tempStructureMap=AcquireJsonStructure.getJsonKeyMap(cpaProperties.getClinicalTrialTempPath());
        Set<String> ids=clinicalTrailRepository.findIdByCPA();
        Iterator<String> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.CLINICAL_TRIAL.dbId.add(iterator.next());
        }
    }
}
