package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrialCancerRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrialOutcomeRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialCancer;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrialOutcome;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrial;
import com.todaysoft.cpa.domain.drug.DrugClinicalTrialRepository;
import com.todaysoft.cpa.domain.drug.DrugRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.DrugClinicalTrial;
import com.todaysoft.cpa.domain.drug.entity.DrugClinicalTrialPK;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.JsonUtil;
import com.todaysoft.cpa.utils.PkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ClinicalTrialService implements BaseService{
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private ClinicalTrialOutcomeRepository clinicalTrialOutcomeRepository;
    @Autowired
    private ClinicalTrialCancerRepository clinicalTrialCancerRepository;
    @Autowired
    private CancerService cancerService;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugClinicalTrialRepository drugClinicalTrialRepository;

    @Override
    @Transactional
    public boolean save(JSONObject object){
        ClinicalTrial clinicalTrial=object.toJavaObject(ClinicalTrial.class);
        clinicalTrial.setClinicalTrialKey(PkGenerator.generator(ClinicalTrial.class));
        clinicalTrial.setCheckState(1);
        clinicalTrial.setCreatedAt(System.currentTimeMillis());
        clinicalTrial.setCreatedWay(2);
        clinicalTrial.setCountries(JsonUtil.jsonArrayToString(object.getJSONArray("countries"),","));
        clinicalTrial=clinicalTrailRepository.save(clinicalTrial);
        if (clinicalTrial!=null){
            saveFixed(clinicalTrial,object);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        ClinicalTrial clinicalTrial=object.toJavaObject(ClinicalTrial.class);
        clinicalTrial.setClinicalTrialKey(PkGenerator.generator(ClinicalTrial.class));
        clinicalTrial.setCheckState(1);
        clinicalTrial.setCreatedAt(System.currentTimeMillis());
        clinicalTrial.setCreatedWay(2);
        clinicalTrial.setCountries(JsonUtil.jsonArrayToString(object.getJSONArray("countries"),","));
        clinicalTrial=clinicalTrailRepository.save(clinicalTrial);
        if (clinicalTrial!=null){
            saveFixed(clinicalTrial,object);
            Drug drug=drugRepository.findOne(dependenceKey);
            if (drug!=null){
                DrugClinicalTrialPK pk=new DrugClinicalTrialPK();
                pk.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                pk.setDrugKey(drug.getDrugKey());
                if (drugClinicalTrialRepository.findOne(pk)==null){
                    DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
                    drugClinicalTrial.setDrugKey(dependenceKey);
                    drugClinicalTrial.setDrugId(drug.getDrugId());
                    drugClinicalTrial.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                    drugClinicalTrial.setClinicalTrialId(clinicalTrial.getClinicalTrialId());
                    drugClinicalTrialRepository.save(drugClinicalTrial);
                }
            }
        }
        return true;
    }

    /**
     * 保存没有关联的部分数据
     * @param clinicalTrial
     * @param object
     */
    private void saveFixed(ClinicalTrial clinicalTrial,JSONObject object){
        //成果
        JSONArray outcomes=object.getJSONArray("outcomes");
        List<ClinicalTrialOutcome> outcomeList=new ArrayList<>();
        if (outcomes!=null&&outcomes.size()>0){
            for (int i=0;i<outcomes.size();i++){
                ClinicalTrialOutcome outcome=outcomes.getObject(i,ClinicalTrialOutcome.class);
                outcome.setClinicalTrailOutcomeKey(PkGenerator.generator(ClinicalTrialOutcome.class));
                outcome.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                outcome.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                outcomeList.add(outcome);
            }
            clinicalTrialOutcomeRepository.save(outcomeList);
        }
        //与疾病关联
        JSONArray diseases=object.getJSONArray("diseases");
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
        clinicalTrialCancerRepository.save(trailCancerList);
    }

    @Override
    public void initDB() {
        CPA.CLINICAL_TRIAL.name=cpaProperties.getClinicalTrialName();
        CPA.CLINICAL_TRIAL.contentUrl=cpaProperties.getClinicalTrialUrl();
        Set<String> ids=clinicalTrailRepository.findIdByCPA();
        Iterator<String> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.CLINICAL_TRIAL.dbId.add(iterator.next());
        }
    }
}
