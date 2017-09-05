package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailCancerRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailOutcomeRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrailCancer;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrailOutcome;
import com.todaysoft.cpa.domain.clinicalTrail.entity.ClinicalTrial;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
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
public class ClinicalTrailService implements BaseService{
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private ClinicalTrailOutcomeRepository clinicalTrailOutcomeRepository;
    @Autowired
    private ClinicalTrailCancerRepository clinicalTrailCancerRepository;
    @Autowired
    private CancerService cancerService;

    @Override
    @Transactional
    public void save(JSONObject object){
        ClinicalTrial clinicalTrial=object.toJavaObject(ClinicalTrial.class);
        clinicalTrial.setClinicalTrialKey(PkGenerator.generator(ClinicalTrial.class));
        clinicalTrial.setCheckState(1);
        clinicalTrial.setCreatedAt(System.currentTimeMillis());
        clinicalTrial.setCreatedWay(2);
        clinicalTrial.setCountries(JsonUtil.jsonArrayToString(object.getJSONArray("countries")));
        clinicalTrial=clinicalTrailRepository.save(clinicalTrial);
        if (clinicalTrial!=null){
            JSONArray outcomes=object.getJSONArray("outcomes");
            List<ClinicalTrailOutcome> outcomeList=new ArrayList<>(outcomes.size());
            if (outcomes!=null&&outcomes.size()>0){
                for (int i=0;i<outcomes.size();i++){
                    ClinicalTrailOutcome outcome=outcomes.getObject(i,ClinicalTrailOutcome.class);
                    outcome.setClinicalTrailOutcomeKey(PkGenerator.generator(ClinicalTrailOutcome.class));
                    outcome.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                    outcome.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                    outcomeList.add(outcome);
                }
                clinicalTrailOutcomeRepository.save(outcomeList);
            }
            JSONArray diseases=object.getJSONArray("diseases");
            List<ClinicalTrailCancer> trailCancerList=new ArrayList<>();
            if (diseases!=null&&diseases.size()>0){
                for (int i=0;i<diseases.size();i++){
                    Cancer cancer=new Cancer();
                    cancer.setCancerName(diseases.getJSONObject(i).getString("name"));
                    cancer.setDoid(diseases.getJSONObject(i).getString("doid"));
                    cancer.setCancerKey(PkGenerator.generator(cancer.getClass()));
                    cancer.setCheckState(1);
                    cancer.setCreatedAt(System.currentTimeMillis());
                    cancer.setCreatedWay(2);
                    cancer=cancerService.save(cancer);
                    if (cancer!=null){
                        ClinicalTrailCancer clinicalTrailCancer=new ClinicalTrailCancer();
                        clinicalTrailCancer.setCancerKey(cancer.getCancerKey());
                        clinicalTrailCancer.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                        clinicalTrailCancer.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                        clinicalTrailCancer.setDoid(Integer.valueOf(cancer.getDoid()));
                        trailCancerList.add(clinicalTrailCancer);
                    }
                }
            }
            clinicalTrailCancerRepository.save(trailCancerList);
        }
    }

    @Override
    public void saveByDependence(JSONObject object, String dependenceKey) {

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
