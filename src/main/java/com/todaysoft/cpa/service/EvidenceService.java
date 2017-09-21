package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import com.todaysoft.cpa.domain.drug.DrugRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.evidence.EvidenceDrugRepository;
import com.todaysoft.cpa.domain.evidence.EvidenceReferenceRepository;
import com.todaysoft.cpa.domain.evidence.EvidenceRepository;
import com.todaysoft.cpa.domain.evidence.entity.Evidence;
import com.todaysoft.cpa.domain.evidence.entity.EvidenceDrug;
import com.todaysoft.cpa.domain.evidence.entity.EvidenceReference;
import com.todaysoft.cpa.domain.variants.VariantRepository;
import com.todaysoft.cpa.domain.variants.entity.Variant;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.PkGenerator;
import netscape.javascript.JSObject;
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
 * @date: 2017/9/14 15:46
 */
@Service
public class EvidenceService implements BaseService {
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private EvidenceRepository evidenceRepository;
    @Autowired
    private EvidenceDrugRepository evidenceDrugRepository;
    @Autowired
    private EvidenceReferenceRepository evidenceReferenceRepository;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public boolean save(JSONObject object) throws InterruptedException {
        Evidence evidence=object.toJavaObject(Evidence.class);
        Variant variant=variantRepository.findByVariantIdAndCreatedWay(evidence.getVariantId(),2);
        if (variant==null){
            throw new DataException("未找到相应的突变，info->variantId="+evidence.getVariantId());
        }
        return saveByDependence(object,variant.getVariantKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        Evidence evidence=object.toJavaObject(Evidence.class);
        evidence.setVariantKey(dependenceKey);
        evidence.setEvidenceKey(PkGenerator.generator(Evidence.class));
        evidence.setCheckState(1);
        evidence.setCreatedAt(System.currentTimeMillis());
        evidence.setCreatedWay(2);
        JSONObject doidObj=object.getJSONObject("doid");
        if (doidObj!=null){
            String doid=doidObj.getString("id");
            Cancer cancer=cancerRepository.findByDoid(doid);
            if (cancer==null){
                throw new DataException("未找到相应的疾病，info->doid="+doid);
            }
            evidence.setCancerKey(cancer.getCancerKey());
            evidence.setDoid(Integer.valueOf(cancer.getDoid()));
            evidence.setDoidName(cancer.getCancerName());
        }
        evidence=evidenceRepository.save(evidence);
        if (evidence!=null){
            //参考文献
            JSONObject reference=object.getJSONObject("reference");
            if (reference!=null){
                EvidenceReference evidenceReference=reference.toJavaObject(EvidenceReference.class);
                evidenceReference.setEvidenceId(evidence.getEvidenceId());
                evidenceReference.setEvidenceKey(evidence.getEvidenceKey());
                evidenceReference.setEvidenceReferenceKey(PkGenerator.generator(EvidenceReference.class));
                evidenceReferenceRepository.save(evidenceReference);
            }
            //药物
            JSONArray drugIds=object.getJSONArray("drugIds");
            if (drugIds!=null&&drugIds.size()>0){
                List<EvidenceDrug> drugList=new ArrayList<>();
                for (int i=0;i<drugIds.size();i++){
                    Integer drugId=drugIds.getInteger(i);
                    Drug drug=drugRepository.findByDrugId(drugId);
                    if (drug==null){
                        throw new DataException("未找到相应的药物，info->drugId="+drugId);
                    }
                    EvidenceDrug evidenceDrug=new EvidenceDrug();
                    evidenceDrug.setDrugId(drug.getDrugId());
                    evidenceDrug.setDrugKey(drug.getDrugKey());
                    evidenceDrug.setEvidenceId(evidence.getEvidenceId());
                    evidenceDrug.setEvidenceKey(evidence.getEvidenceKey());
                    drugList.add(evidenceDrug);
                }
                evidenceDrugRepository.save(drugList);
            }
        }
        return true;
    }

    @Override
    public void initDB() {
        CPA.EVIDENCE.name=cpaProperties.getEvidenceName();
        CPA.EVIDENCE.contentUrl=cpaProperties.getEvidenceUrl();
        Set<Integer> ids=evidenceRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.EVIDENCE.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
