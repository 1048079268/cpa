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
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.PkGenerator;
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
        evidence=cnEvidenceRepository.save(evidence);
        if (evidence==null){
            throw new DataException("保存主表失败->id="+object.getString("id"));
        }
        //参考文献
        JSONObject reference=object.getJSONObject("reference");
        if (reference!=null){
            EvidenceReference evidenceReference=reference.toJavaObject(EvidenceReference.class);
            evidenceReference.setEvidenceId(evidence.getEvidenceId());
            evidenceReference.setEvidenceKey(evidence.getEvidenceKey());
            evidenceReference.setEvidenceReferenceKey(PkGenerator.generator(EvidenceReference.class));
            evidenceReferenceRepository.save(evidenceReference);
            cnEvidenceReferenceRepository.save(evidenceReference);
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
            cnEvidenceDrugRepository.save(drugList);
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