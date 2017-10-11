package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.medicationPlan.*;
import com.todaysoft.cpa.domain.en.medicationPlan.*;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.drug.DrugRepository;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.JsonUtil;
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
 * @date: 2017/9/12 10:09
 */
@Service
public class MedicationPlanService extends BaseService {
    @Autowired
    private CnMedicationPlanRepository cnMedicationPlanRepository;
    @Autowired
    private CnPlanCancerRepository cnPlanCancerRepository;
    @Autowired
    private CnPlanDrugRepository cnPlanDrugRepository;
    @Autowired
    private CnPlanInstructionMessageRepository cnPlanInstructionMessageRepository;
    @Autowired
    private CnPlanInstructionRepository cnPlanInstructionRepository;
    @Autowired
    private CnPlanReferenceRepository cnPlanReferenceRepository;
    @Autowired
    private CnPlanStudyRepository cnPlanStudyRepository;
    @Autowired
    private MedicationPlanRepository medicationPlanRepository;
    @Autowired
    private PlanCancerRepository planCancerRepository;
    @Autowired
    private PlanDrugRepository planDrugRepository;
    @Autowired
    private PlanInstructionMessageRepository planInstructionMessageRepository;
    @Autowired
    private PlanInstructionRepository planInstructionRepository;
    @Autowired
    private PlanReferenceRepository planReferenceRepository;
    @Autowired
    private PlanStudyRepository planStudyRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private CPAProperties cpaProperties;

    @Override
    @Transactional
    public boolean save(JSONObject object) throws InterruptedException {
        MedicationPlan medicationPlan=object.toJavaObject(MedicationPlan.class);
        medicationPlan.setMedicationPlanKey(PkGenerator.generator(MedicationPlan.class));
        medicationPlan.setCheckState(1);
        medicationPlan.setCreatedAt(System.currentTimeMillis());
        medicationPlan.setCreatedWay(2);
        medicationPlan=medicationPlanRepository.save(medicationPlan);
        medicationPlan=cnMedicationPlanRepository.save(medicationPlan);
        if (medicationPlan==null){
            throw new DataException("保存主表失败->id="+object.getString("id"));
        }
        //相关研究
        JSONArray studies=object.getJSONArray("studies");
        if (studies!=null&&studies.size()>0){
            List<PlanStudy> studyList=new ArrayList<>();
            for (int i=0;i<studies.size();i++){
                PlanStudy study=studies.getObject(i,PlanStudy.class);
                study.setMedicationPlanId(medicationPlan.getMedicinePlanId());
                study.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                study.setPlanStudyKey(PkGenerator.generator(PlanStudy.class));
                studyList.add(study);
            }
            planStudyRepository.save(studyList);
            cnPlanStudyRepository.save(studyList);
        }
        //药物
        JSONArray drugs=object.getJSONArray("drugs");
        if (drugs!=null&&drugs.size()>0){
            List<PlanDrug> drugList=new ArrayList<>();
            for (int i=0;i<drugs.size();i++){
                String drugId=drugs.getString(i);
                PlanDrug planDrug=new PlanDrug();
                Drug drug = drugRepository.findByDrugId(Integer.valueOf(drugId));
                if (drug!=null){
                    planDrug.setDrugId(drug.getDrugId());
                    planDrug.setDrugKey(drug.getDrugKey());
                    planDrug.setMedicationPlanId(medicationPlan.getMedicinePlanId());
                    planDrug.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                    drugList.add(planDrug);
                }
            }
            planDrugRepository.save(drugList);
            cnPlanDrugRepository.save(drugList);
        }
        //疾病
        JSONObject doid=object.getJSONObject("doid");
        if (doid!=null){
            PlanCancer planCancer=doid.toJavaObject(PlanCancer.class);
            Cancer cancer=cancerRepository.findByDoid(String.valueOf(planCancer.getDoid()));
            if (cancer==null){
                throw new DataException("未找到相应疾病，info->doid="+planCancer.getDoid());
            }
            planCancer.setCancerKey(cancer.getCancerKey());
            planCancer.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
            planCancer.setMedicinePlanId(medicationPlan.getMedicinePlanId());
            planCancerRepository.save(planCancer);
            cnPlanCancerRepository.save(planCancer);
        }
        //参考文献
        JSONArray references=object.getJSONArray("references");
        if (references!=null&&references.size()>0){
            List<PlanReference> referenceList=new ArrayList<>();
            for (int i=0;i<references.size();i++){
                PlanReference reference=references.getObject(i,PlanReference.class);
                reference.setPlanReferenceKey(PkGenerator.generator(PlanReference.class));
                reference.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                reference.setMedicinePlanId(medicationPlan.getMedicinePlanId());
                referenceList.add(reference);
            }
            planReferenceRepository.save(referenceList);
            cnPlanReferenceRepository.save(referenceList);
        }
        //用法说明
        JSONArray instructions=object.getJSONArray("instructions");
        if (instructions!=null&&instructions.size()>0){
            for (int i=0;i<instructions.size();i++){
                PlanInstruction planInstruction=instructions.getObject(i,PlanInstruction.class);
                planInstruction.setMedicationPlanId(medicationPlan.getMedicinePlanId());
                planInstruction.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                planInstruction.setPlanInstructionKey(PkGenerator.generator(PlanInstruction.class));
                String drugIds= JsonUtil.jsonArrayToString(instructions.getJSONObject(i).getJSONArray("drugIds"),",");
                planInstruction.setDrugIds(drugIds);
                planInstruction=planInstructionRepository.save(planInstruction);
                planInstruction=cnPlanInstructionRepository.save(planInstruction);
                if (planInstruction!=null){
                    //用法说明药物列表
                    JSONArray instructionList=instructions.getJSONObject(i).getJSONArray("instructionList");
                    if (instructionList!=null&&instructionList.size()>0){
                        List<PlanInstructionMessage> instructionMessages=new ArrayList<>();
                        for (int j=0;j<instructionList.size();j++){
                            JSONObject jo=instructionList.getJSONObject(j);
                            PlanInstructionMessage message=instructionList.getObject(j,PlanInstructionMessage.class);
                            message.setInstructionId(planInstruction.getInstructionId());
                            message.setPlanInstructionKey(planInstruction.getPlanInstructionKey());
                            message.setPlanInstructionMessageKey(PkGenerator.generator(PlanInstructionMessage.class));
                            String route=JsonUtil.jsonArrayToString(jo.getJSONArray("route"),",");
                            String duration=JsonUtil.jsonArrayToString(jo.getJSONArray("duration"),",");
                            String freq=JsonUtil.jsonArrayToString(jo.getJSONArray("freq"),",");
                            String dosage=JsonUtil.jsonArrayToString(jo.getJSONArray("dosage"),",");
                            String childDrugIds=JsonUtil.jsonArrayToString(jo.getJSONArray("drugIds"),",");
                            message.setTheDosage(dosage);
                            message.setTheRoute(route);
                            message.setTheDuration(duration);
                            message.setTheFrequency(freq);
                            message.setDrugIds(childDrugIds);
                            instructionMessages.add(message);
                        }
                        planInstructionMessageRepository.save(instructionMessages);
                        cnPlanInstructionMessageRepository.save(instructionMessages);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        return false;
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.REGIMEN.name=cpaProperties.getRegimenName();
        CPA.REGIMEN.contentUrl=cpaProperties.getRegimenUrl();
        CPA.REGIMEN.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getRegimenTempPath());
        Set<Integer> ids=medicationPlanRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.REGIMEN.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
