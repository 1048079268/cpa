package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.evidence.CnEvidenceDrugRepository;
import com.todaysoft.cpa.domain.cn.evidence.CnEvidenceReferenceRepository;
import com.todaysoft.cpa.domain.cn.evidence.CnEvidenceRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.drug.DrugRepository;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.en.evidence.EvidenceDrugRepository;
import com.todaysoft.cpa.domain.en.evidence.EvidenceReferenceRepository;
import com.todaysoft.cpa.domain.en.evidence.EvidenceRepository;
import com.todaysoft.cpa.domain.entity.Evidence;
import com.todaysoft.cpa.domain.entity.EvidenceDrug;
import com.todaysoft.cpa.domain.entity.EvidenceReference;
import com.todaysoft.cpa.domain.en.variants.VariantRepository;
import com.todaysoft.cpa.domain.entity.Variant;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.PkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:46
 */
@Service
public class EvidenceService extends BaseService {
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private EvidenceRepository evidenceRepository;
    @Autowired
    private EvidenceDrugRepository evidenceDrugRepository;
    @Autowired
    private EvidenceReferenceRepository evidenceReferenceRepository;
    @Autowired
    private CnEvidenceRepository cnEvidenceRepository;
    @Autowired
    private CnEvidenceDrugRepository cnEvidenceDrugRepository;
    @Autowired
    private CnEvidenceReferenceRepository cnEvidenceReferenceRepository;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private KbUpdateService kbUpdateService;
    @Override
    public boolean save(JSONObject object,JSONObject cn,int status) throws InterruptedException {
        Evidence evidence=object.toJavaObject(Evidence.class);
        String variantKey=variantRepository.findByVariantIdAndCreatedWay(evidence.getVariantId(),2);
        if (variantKey==null||variantKey.length()==0){
            throw new DataException("未找到相应的突变，info->variantId="+evidence.getVariantId());
        }
        return saveByDependence(object,cn,variantKey,status);
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey,int status) throws InterruptedException {
        Evidence object = en.toJavaObject(Evidence.class);
        Evidence old = evidenceRepository.findByEvidenceId(object.getEvidenceId());
        boolean isSaveCn= old==null;
        boolean isUseOldState= old!=null;
        String evidenceKey=old==null?PkGenerator.generator(Evidence.class):old.getEvidenceKey();
        JsonObjectConverter<Evidence> evidenceConverter=(json)->{
            Evidence evidence=json.toJavaObject(Evidence.class);
            evidence.setVariantKey(dependenceKey);
            evidence.setEvidenceKey(evidenceKey);
            evidence.setCreatedAt(System.currentTimeMillis());
            if (isUseOldState){
                evidence.setCheckState(old.getCheckState());
                evidence.setCreatedWay(old.getCreatedWay());
                evidence.setCreatedByName(old.getCreatedByName());
            }else {
                evidence.setCheckState(1);
                evidence.setCreatedWay(2);
                evidence.setCreatedByName("CPA");
            }
            JSONObject doidObj=json.getJSONObject("doid");
            if (doidObj!=null){
                String doid=doidObj.getString("id");
                Cancer cancer=cancerRepository.findByDoid(doid);
                if (cancer==null){
                    throw new DataException("未找到相应的疾病，info->doid="+doid);
                }
                evidence.setCancerKey(cancer.getCancerKey());
                evidence.setDoid(Integer.valueOf(cancer.getDoid()));
                evidence.setDoidName(doidObj.getString("name"));
            }
            return evidence;
        };
        Evidence evidence=evidenceRepository.save(evidenceConverter.convert(en));
        if (isSaveCn){
            cnEvidenceRepository.save(evidenceConverter.convert(cn));
        }
        //参考文献
        List<EvidenceReference> referenceByEvidenceKey = evidenceReferenceRepository.findByEvidenceKey(evidence.getEvidenceKey());
        Map<Integer,String> referenceKeyMap=new HashMap<>();
        if (referenceByEvidenceKey!=null){
            referenceKeyMap.putAll(referenceByEvidenceKey.stream().collect(Collectors.toMap(EvidenceReference::hashCode,EvidenceReference::getEvidenceReferenceKey)));
        }
        JsonObjectConverter<EvidenceReference> referenceConverter=(json)->{
            JSONObject reference=json.getJSONObject("reference");
            if (reference!=null){
                EvidenceReference evidenceReference=reference.toJavaObject(EvidenceReference.class);
                evidenceReference.setEvidenceId(evidence.getEvidenceId());
                evidenceReference.setEvidenceKey(evidence.getEvidenceKey());
                evidenceReference.setEvidenceReferenceKey(PkGenerator.generator(EvidenceReference.class));
                return evidenceReference;
            }
            return null;
        };
        EvidenceReference enReference = referenceConverter.convert(en);
        String rKey = referenceKeyMap.get(enReference.hashCode());
        if (rKey!=null){
            enReference.setEvidenceReferenceKey(rKey);
        }
        evidenceReferenceRepository.save(enReference);
        if (isSaveCn){
            EvidenceReference cnReference = referenceConverter.convert(cn);
            if (rKey!=null){
                cnReference.setEvidenceReferenceKey(rKey);
            }
            cnEvidenceReferenceRepository.save(cnReference);
        }
        //药物
        JsonArrayConverter<EvidenceDrug> evidenceDrugConverter=(json)->{
            JSONArray drugIds=json.getJSONArray("drugIds");
            List<EvidenceDrug> drugList=new ArrayList<>();
            if (drugIds!=null&&drugIds.size()>0){
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
            }
            return drugList;
        };
        evidenceDrugRepository.save(evidenceDrugConverter.convert(en));
        if (isSaveCn){
            cnEvidenceDrugRepository.save(evidenceDrugConverter.convert(cn));
        }
        if (evidence.getCheckState()==1){
            kbUpdateService.send("kt_evidence");
        }
        return true;
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.EVIDENCE.name=cpaProperties.getEvidenceName();
        CPA.EVIDENCE.contentUrl=cpaProperties.getEvidenceUrl();
        CPA.EVIDENCE.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getEvidenceTempPath());
        Set<Integer> ids=evidenceRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.EVIDENCE.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
