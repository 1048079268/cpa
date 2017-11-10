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
import com.todaysoft.cpa.utils.*;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private static Logger logger= LoggerFactory.getLogger(MedicationPlanService.class);
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
    public boolean save(JSONObject en,JSONObject cn) throws InterruptedException {
        String planKey=PkGenerator.generator(MedicationPlan.class);
        MedicationPlan planCheck=cn.toJavaObject(MedicationPlan.class);
        String planNameCheck=planCheck.getRegimenName();
        MedicationPlan byName = cnMedicationPlanRepository.findByName(planNameCheck);
        if (byName!=null){
            logger.info("【" + CPA.REGIMEN.name() + "】与老库合并->id="+byName.getMedicinePlanId());
            planKey=byName.getMedicationPlanKey();
        }
        String finalPlanKey = planKey;
        JsonObjectConverter<MedicationPlan> planConverter=(json)->{
            MedicationPlan medicationPlan=json.toJavaObject(MedicationPlan.class);
            medicationPlan.setMedicationPlanKey(finalPlanKey);
            medicationPlan.setCheckState(1);
            medicationPlan.setCreatedAt(System.currentTimeMillis());
            medicationPlan.setCreatedWay(2);
            return medicationPlan;
        };
        MedicationPlan medicationPlan=medicationPlanRepository.save(planConverter.convert(en));
        MedicationPlan planCn = planConverter.convert(cn);
        planCn.setProgramNameC(planCn.getRegimenName());
        planCn.setRegimenName(medicationPlan.getRegimenName());
        cnMedicationPlanRepository.save(planCn);
        //相关研究
        String planStudyKey=PkGenerator.generator(PlanStudy.class);
        JsonArrayConverter<PlanStudy> studyConverter=(json)->{
            JSONArray studies=json.getJSONArray("studies");
            List<PlanStudy> studyList=new ArrayList<>();
            if (studies!=null&&studies.size()>0){
                for (int i=0;i<studies.size();i++){
                    PlanStudy study=studies.getObject(i,PlanStudy.class);
                    study.setMedicationPlanId(medicationPlan.getMedicinePlanId());
                    study.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                    study.setPlanStudyKey(PkGenerator.md5(planStudyKey+i));
                    studyList.add(study);
                }
            }
            return studyList;
        };
        planStudyRepository.save(studyConverter.convert(en));
        cnPlanStudyRepository.save(studyConverter.convert(cn));
        //药物
        JsonArrayConverter<PlanDrug> planDrugConverter=(json)->{
            JSONArray drugs=json.getJSONArray("drugs");
            List<PlanDrug> drugList=new ArrayList<>();
            if (drugs!=null&&drugs.size()>0){
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
            }
            return drugList;
        };
        planDrugRepository.save(planDrugConverter.convert(en));
        cnPlanDrugRepository.save(planDrugConverter.convert(cn));
        //疾病
        JsonObjectConverter<PlanCancer> planCancerConverter=(json)->{
            JSONObject doid=json.getJSONObject("doid");
            if (doid!=null){
                PlanCancer planCancer=doid.toJavaObject(PlanCancer.class);
                Cancer cancer=cancerRepository.findByDoid(String.valueOf(planCancer.getDoid()));
                if (cancer==null){
                    throw new DataException("未找到相应的疾病，info->doid="+planCancer.getDoid());
                }
                planCancer.setCancerKey(cancer.getCancerKey());
                planCancer.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                planCancer.setMedicinePlanId(medicationPlan.getMedicinePlanId());
                return planCancer;
            }
            return null;
        };
        PlanCancer planCancerEn = planCancerConverter.convert(en);
        PlanCancer planCancerCn = planCancerConverter.convert(cn);
        if (planCancerCn!=null&&planCancerEn!=null){
            planCancerRepository.save(planCancerEn);
            cnPlanCancerRepository.save(planCancerCn);
        }
        //参考文献
        String referenceKey=PkGenerator.generator(PlanReference.class);
        JsonArrayConverter<PlanReference> referenceConverter=(json)->{
            JSONArray references=json.getJSONArray("references");
            List<PlanReference> referenceList=new ArrayList<>();
            if (references!=null&&references.size()>0){
                for (int i=0;i<references.size();i++){
                    PlanReference reference=references.getObject(i,PlanReference.class);
                    reference.setPlanReferenceKey(PkGenerator.md5(referenceKey+i));
                    reference.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                    reference.setMedicinePlanId(medicationPlan.getMedicinePlanId());
                    referenceList.add(reference);
                }
            }
            return referenceList;
        };
        planReferenceRepository.save(referenceConverter.convert(en));
        cnPlanReferenceRepository.save(referenceConverter.convert(cn));
        //用法说明
        JSONArray instructionsEn=en.getJSONArray("instructions");
        JSONArray instructionsCn=cn.getJSONArray("instructions");
        if (instructionsEn!=null&&instructionsEn.size()>0){
            //定义PlanInstruction创建
            JsonObjectKeyConverter<PlanInstruction> instructionConverter=(json, key)->{
                PlanInstruction planInstruction=json.toJavaObject(PlanInstruction.class);
                planInstruction.setMedicationPlanId(medicationPlan.getMedicinePlanId());
                planInstruction.setMedicationPlanKey(medicationPlan.getMedicationPlanKey());
                planInstruction.setPlanInstructionKey(key);
                String drugIds= JsonUtil.jsonArrayToString(json.getJSONArray("drugIds"),",");
                planInstruction.setDrugIds(drugIds);
                return planInstruction;
            };
            for (int i=0;i<instructionsEn.size();i++){
                String instructionKey=PkGenerator.generator(PlanInstruction.class);
                PlanInstruction planInstruction=planInstructionRepository.save(instructionConverter.convert(instructionsEn.getJSONObject(i),instructionKey));
                cnPlanInstructionRepository.save(instructionConverter.convert(instructionsCn.getJSONObject(i),instructionKey));
                if (planInstruction!=null){
                    //用法说明药物列表
                    String messageKey=PkGenerator.generator(PlanInstructionMessage.class);
                    JsonArrayConverter<PlanInstructionMessage> messageConverter=(json)->{
                        JSONArray instructionList=json.getJSONArray("instructionList");
                        List<PlanInstructionMessage> instructionMessages=new ArrayList<>();
                        if (instructionList!=null&&instructionList.size()>0){
                            for (int j=0;j<instructionList.size();j++){
                                JSONObject jo=instructionList.getJSONObject(j);
                                PlanInstructionMessage message=instructionList.getObject(j,PlanInstructionMessage.class);
                                message.setInstructionId(planInstruction.getInstructionId());
                                message.setPlanInstructionKey(planInstruction.getPlanInstructionKey());
                                message.setPlanInstructionMessageKey(PkGenerator.md5(messageKey+j));
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
                        }
                        return instructionMessages;
                    };
                    planInstructionMessageRepository.save(messageConverter.convert(instructionsEn.getJSONObject(i)));
                    cnPlanInstructionMessageRepository.save(messageConverter.convert(instructionsCn.getJSONObject(i)));
                }
            }
        }
        return true;
    }

    @Override
    public boolean saveByDependence(JSONObject object,JSONObject cn, String dependenceKey) {
        return false;
    }

    @Async
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
