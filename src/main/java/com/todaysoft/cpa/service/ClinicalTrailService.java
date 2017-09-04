package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailOutcomeRepository;
import com.todaysoft.cpa.domain.clinicalTrail.ClinicalTrailRepository;
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
            for (int i=0;i<outcomes.size();i++){
                ClinicalTrailOutcome outcome=outcomes.getObject(i,ClinicalTrailOutcome.class);
                outcome.setClinicalTrailOutcomeKey(PkGenerator.generator(ClinicalTrailOutcome.class));
                outcome.setClinicalTrailId(clinicalTrial.getClinicalTrialId());
                outcome.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                outcomeList.add(outcome);
            }
            clinicalTrailOutcomeRepository.save(outcomeList);
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
