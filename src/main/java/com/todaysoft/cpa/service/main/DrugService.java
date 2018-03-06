package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.clinicalTrail.CnClinicalTrailRepository;
import com.todaysoft.cpa.domain.cn.drug.*;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.en.drug.*;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.param.*;
import com.todaysoft.cpa.service.vice.*;
import com.todaysoft.cpa.utils.*;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayLangConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectKeyConverter;
import com.todaysoft.cpa.utils.dosage.Dosage;
import com.todaysoft.cpa.utils.dosage.DosageUtil;
import org.jsoup.helper.DescendableLinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @desc: 单独运行
 * @author: 鱼唇的人类
 * @date: 2017/8/8 12:07
 */
@Service
public class DrugService{
    private static Logger logger= LoggerFactory.getLogger(DrugService.class);
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private CnDrugAdverseReactionRepository cnDrugAdverseReactionRepository;
    @Autowired
    private CnDrugCategoryRepository cnDrugCategoryRepository;
    @Autowired
    private CnDrugExternalIdRepository cnDrugExternalIdRepository;
    @Autowired
    private CnDrugInteractionRepository cnDrugInteractionRepository;
    @Autowired
    private CnDrugInternationalBrandRepository cnDrugInternationalBrandRepository;
    @Autowired
    private CnDrugKeggPathwayRepository cnDrugKeggPathwayRepository;
    @Autowired
    private CnDrugProductRepository cnDrugProductRepository;
    @Autowired
    private CnDrugRepository cnDrugRepository;
    @Autowired
    private CnDrugSequenceRepository cnDrugSequenceRepository;
    @Autowired
    private CnDrugStructuredIndicationRepository cnDrugStructuredIndicationRepository;
    @Autowired
    private DrugAdverseReactionRepository drugAdverseReactionRepository;
    @Autowired
    private DrugCategoryRepository drugCategoryRepository;
    @Autowired
    private DrugExternalIdRepository drugExternalIdRepository;
    @Autowired
    private DrugInteractionRepository drugInteractionRepository;
    @Autowired
    private DrugInternationalBrandRepository drugInternationalBrandRepository;
    @Autowired
    private DrugKeggPathwayRepository drugKeggPathwayRepository;
    @Autowired
    private DrugProductRepository drugProductRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugSequenceRepository drugSequenceRepository;
    @Autowired
    private DrugStructuredIndicationRepository drugStructuredIndicationRepository;
    @Autowired
    private KeggPathwaysService keggPathwaysService;
    @Autowired
    private MeshCategoryService meshCategoryService;
    @Autowired
    private IndicationService indicationService;
    @Autowired
    private SideEffectService sideEffectService;
    @Autowired
    private ClinicalTrialService clinicalTrialService;
    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private DrugClinicalTrialRepository drugClinicalTrialRepository;
    @Autowired
    private CnClinicalTrailRepository cnClinicalTrailRepository;
    @Autowired
    private CnDrugClinicalTrialRepository cnDrugClinicalTrialRepository;
    @Autowired
    private DrugProductService drugProductService;
    @Autowired
    private DrugProductIngredientRepository drugProductIngredientRepository;
    @Autowired
    private CnDrugProductIngredientRepository cnDrugProductIngredientRepository;

    /**
     * @param en
     * @param cn
     * @param status
     *  键是合并的对比项如药物id,通路id，药品名称
     *  值是判断其是否合并的
     *     如果键值不存在或者值为0，则抛出异常并提交审核
     *     如果值为1，表示合并
     *     如果值为2，表示不合并
     * @return
     * @throws InterruptedException
     */
    @Transactional
    public boolean save(JSONObject en,JSONObject cn,Map<String,Integer> status) throws InterruptedException {
        //1.解析药物基本信息
        String drugKey=PkGenerator.generator(Drug.class);
        Drug checkDrug=cn.toJavaObject(Drug.class);
        String cnName=checkDrug.getNameEn();
        boolean merge=false;
        Drug checkDrugCn = cnDrugRepository.findByName(cnName);
        if (checkDrugCn!=null){
            Integer s=status.get(String.valueOf(checkDrug.getDrugId()));
            if ( s== null||s==0) {
                if (MergeInfo.DRUG.sign.add(String.valueOf(checkDrug.getDrugId()))){
                    List<String> list=new ArrayList<>(4);
                    list.add(0, String.valueOf(checkDrug.getDrugId()));
                    list.add(1,checkDrug.getNameEn());
                    list.add(2,checkDrugCn.getDrugKey());
                    list.add(3,checkDrugCn.getNameEn());
                    MergeInfo.DRUG.checkList.add(list);
                }
                throw new MergeException("【" + CPA.DRUG.name() + "】与老库重合，等待审核->id="+checkDrug.getDrugId());
            }else if (s==1){
                merge=true;
                logger.info("【" + CPA.DRUG.name() + "】与老库合并->id="+checkDrug.getDrugId());
                drugKey=checkDrugCn.getDrugKey();
            }
        }
        boolean finalMerge = merge;
        String finalDrugKey = drugKey;
        JsonObjectConverter<Drug> drugConverter=(json)->{
            Drug drug=json.toJavaObject(Drug.class);
            drug.setDrugKey(finalDrugKey);
            //别名
            JSONArray otherNames=json.getJSONArray("otherNames");
            JSONArray synonyms=json.getJSONArray("synonyms");
            if (otherNames==null){
                otherNames=new JSONArray();
            }
            if (synonyms!=null&&synonyms.size()>0){
                otherNames.addAll(synonyms);
            }
            JSONArray jsonArray=new JSONArray();
            if (otherNames.size()>0){
                Set<String> mergeOtherName=new HashSet<>();
                for (int i=0;i<otherNames.size();i++){
                    //转大写去重
                    if (!mergeOtherName.add(otherNames.getString(i).toUpperCase())){
                        continue;
                    }
                    jsonArray.add(otherNames.getString(i));
                }
            }
            drug.setOtherNames(JsonUtil.jsonArrayToString(jsonArray,"<=>"));
            drug.setMolecularWeight(JsonUtil.jsonArrayToString(json.getJSONArray("molecularWeight")));
            drug.setChemicalFormula(JsonUtil.jsonArrayToString(json.getJSONArray("chemicalFormula")));
            Date createdAt=json.getDate("createdAt");
            Long createTime=System.currentTimeMillis();
            if (createdAt!=null){
                createTime=createdAt.getTime();
            }
            drug.setCreatedAt(createTime);
            drug.setCheckState(1);
            drug.setCreateWay(2);
            drug.setCreatedByName("CPA");
            return drug;
        };
        Drug drugEn=drugConverter.convert(en);
        Drug drugCn=drugConverter.convert(cn);
        drugCn.setNameChinese(drugCn.getNameEn());
        drugCn.setNameEn(drugEn.getNameEn());
        Drug drug=drugRepository.save(drugEn);
        if (checkDrugCn!=null&&finalMerge){
            if (!StringUtils.isEmpty(checkDrugCn.getNameEn())){
                drugCn.setNameEn(checkDrugCn.getNameEn());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getNameChinese())){
                drugCn.setNameChinese(checkDrugCn.getNameChinese());
            }
            if (checkDrugCn.getOncoDrug()!=null){
                drugCn.setOncoDrug(checkDrugCn.getOncoDrug());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getDescription())){
                drugCn.setDescription(checkDrugCn.getDescription());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getChemicalFormula())){
                drugCn.setChemicalFormula(checkDrugCn.getChemicalFormula());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getMolecularWeight())){
                drugCn.setMolecularWeight(checkDrugCn.getMolecularWeight());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getMechanismOfAction())){
                drugCn.setMechanismOfAction(checkDrugCn.getMechanismOfAction());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getToxicity())){
                drugCn.setToxicity(checkDrugCn.getToxicity());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getStructuredIndicationDesc())){
                drugCn.setStructuredIndicationDesc(checkDrugCn.getStructuredIndicationDesc());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getAbsorption())){
                drugCn.setAbsorption(checkDrugCn.getAbsorption());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getVolumeOfDistribution())){
                drugCn.setVolumeOfDistribution(checkDrugCn.getVolumeOfDistribution());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getProteinBinding())){
                drugCn.setProteinBinding(checkDrugCn.getProteinBinding());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getHalfLife())){
                drugCn.setHalfLife(checkDrugCn.getHalfLife());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getClearance())){
                drugCn.setClearance(checkDrugCn.getClearance());
            }
            if (!StringUtils.isEmpty(checkDrugCn.getPharmacodynamics())){
                drugCn.setPharmacodynamics(checkDrugCn.getPharmacodynamics());
            }
        }
        cnDrugRepository.save(drugCn);
        //remove 2.判断插入是否成功
        //3.药物别名 与其他名称合并
//        String synonymKey=PkGenerator.generator(DrugSynonym.class);
//        Map<Integer,String> synonymKeys=new HashMap<>();
//        JsonArrayLangConverter<DrugSynonym> synonymConverter=(json, lang)->{
//            JSONArray synonyms=json.getJSONArray("synonyms");
//            List<DrugSynonym> synonymList=new ArrayList<>();
//            if (synonyms!=null&&synonyms.size() > 0){
//                for (int i=0;i<synonyms.size();i++){
//                    DrugSynonym synonym=new DrugSynonym();
//                    synonym.setSynonymKey(PkGenerator.md5(synonymKey+i));
//                    if (finalMerge){
//                        if (lang==1){
//                            if (synonymKeys.containsKey(i)){
//                                synonym.setSynonymKey(synonymKeys.get(i));
//                            }
//                        }
//                        if (lang==2){
//                            DrugSynonym drugSynonym = cnDrugSynonymRepository.findByDrugKeyAndSynonym(drug.getDrugKey(), synonyms.getString(i));
//                            if (drugSynonym!=null){
//                                synonym.setSynonymKey(drugSynonym.getSynonymKey());
//                                synonymKeys.put(i,drugSynonym.getSynonymKey());
//                                continue;
//                            }
//                        }
//                    }
//                    synonym.setDrugId(drug.getDrugId());
//                    synonym.setDrugKey(drug.getDrugKey());
//                    synonym.setDrugSynonym(synonyms.getString(i));
//                    synonymList.add(synonym);
//                }
//            }
//            return synonymList;
//        };
//        cnDrugSynonymRepository.save(synonymConverter.convert(cn,2));
//        drugSynonymRepository.save(synonymConverter.convert(en,1));
        //4.药物外部id
        //保存主要外部数据库id
        String pExternalIdKey=PkGenerator.generator(DrugExternalId.class);
        JsonObjectConverter<DrugExternalId> pExternalIdConverter=(json)->{
            DrugExternalId externalId=new DrugExternalId();
            externalId.setPrimary(true);
            externalId.setDrugId(drug.getDrugId());
            externalId.setDrugKey(drug.getDrugKey());
            externalId.setExternalIdKey(pExternalIdKey);
            externalId.setExternalId(json.getString("primaryExternalId"));
            externalId.setExternalIdSource(json.getString("primaryExternalSource"));
            return externalId;
        };
        drugExternalIdRepository.save(pExternalIdConverter.convert(en));
        cnDrugExternalIdRepository.save(pExternalIdConverter.convert(cn));
        //外部数据库id
        String externalIdKey=PkGenerator.generator(DrugExternalId.class);
        JsonArrayConverter<DrugExternalId> externalIdConverter=(json)->{
            JSONArray externalIds=json.getJSONArray("externalIds");
            List<DrugExternalId> externalIdList=new ArrayList<>();
            if (externalIds!=null&&externalIds.size() > 0){
                for (int i=0;i<externalIds.size();i++){
                    DrugExternalId externalId=externalIds.getJSONObject(i).toJavaObject(DrugExternalId.class);
                    externalId.setExternalIdKey(PkGenerator.md5(externalIdKey+i));
                    externalId.setDrugId(drug.getDrugId());
                    externalId.setDrugKey(drug.getDrugKey());
                    externalId.setPrimary(false);
                    externalIdList.add(externalId);
                }
            }
            return externalIdList;
        };
        drugExternalIdRepository.save(externalIdConverter.convert(en));
        cnDrugExternalIdRepository.save(externalIdConverter.convert(cn));
        //6.药物商品
        String internationalBrandKey=PkGenerator.generator(DrugInternationalBrand.class);
        JsonArrayConverter<DrugInternationalBrand> brandConverter=(json)->{
            JSONArray internationalBrands=json.getJSONArray("internationalBrands");
            List<DrugInternationalBrand> internationalBrandList=new ArrayList<>();
            if (internationalBrands!=null&&internationalBrands.size()>0){
                for (int i=0;i<internationalBrands.size();i++){
                    DrugInternationalBrand internationalBrand=new DrugInternationalBrand();
                    internationalBrand.setInternationalBrandKey(PkGenerator.md5(internationalBrandKey+i));
                    internationalBrand.setDrugId(drug.getDrugId());
                    internationalBrand.setDrugKey(drug.getDrugKey());
                    JSONArray brands=internationalBrands.getJSONArray(i);
                    if (brands!=null&&brands.size()==2){
                        internationalBrand.setInternationalBrand(brands.getString(0));
                        internationalBrand.setBrandCompany(brands.getString(1));
                        internationalBrandList.add(internationalBrand);
                    }
                }
            }
            return internationalBrandList;
        };
        drugInternationalBrandRepository.save(brandConverter.convert(en));
        cnDrugInternationalBrandRepository.save(brandConverter.convert(cn));
        //7.药物通路
        JSONArray keggPathwaysEn=en.getJSONArray("keggPathways");
        JSONArray keggPathwaysCn=cn.getJSONArray("keggPathways");
        if (keggPathwaysEn!=null&&keggPathwaysEn.size()>0){
            JsonObjectConverter<KeggPathway> pathwayConverter=(json)->{
                KeggPathway pathway = new KeggPathway();
                pathway.setCreateAt(System.currentTimeMillis());
                pathway.setCreateWay(2);
                pathway.setCheckState(1);
                pathway.setCreatedByName("CPA");
                pathway.setKeggId(json.getString("id").trim());
                pathway.setPathwayName(json.getString("name"));
                return pathway;
            };
            for (int i = 0; i < keggPathwaysEn.size(); i++) {
                String nameCn = keggPathwaysCn.getJSONObject(i).getString("name");
                KeggPathway keggPathwayEn = pathwayConverter.convert(keggPathwaysEn.getJSONObject(i));
                KeggPathway keggPathwayCn = pathwayConverter.convert(keggPathwaysCn.getJSONObject(i));
                KeggPathway keggPathway = keggPathwaysService.save(keggPathwayCn,keggPathwayEn,drug,status);
                if(keggPathway!=null){
                    DrugKeggPathway drugKeggPathway=new DrugKeggPathway();
                    drugKeggPathway.setPathwayKey(keggPathway.getPathwayKey());
                    drugKeggPathway.setDrugKey(drug.getDrugKey());
                    drugKeggPathway.setKeggId(keggPathway.getKeggId());
                    drugKeggPathway.setDrugId(drug.getDrugId());
                    drugKeggPathway.setPathwayName(keggPathway.getPathwayName());
                    drugKeggPathwayRepository.save(drugKeggPathway);
                    drugKeggPathway.setPathwayName(nameCn);
                    cnDrugKeggPathwayRepository.save(drugKeggPathway);
                }
            }
        }
        //8.药物结构化适应症
        JSONArray indicationsEn = en.getJSONArray("structuredIndications");
        JSONArray indicationsCn = cn.getJSONArray("structuredIndications");
        if (indicationsEn!=null&&indicationsEn.size()>0){
            for (int i = 0; i < indicationsEn.size(); i++) {
                Indication enIndication=new Indication();
                enIndication.setCheckState(1);
                enIndication.setCreatedAt(System.currentTimeMillis());
                enIndication.setCreatedWay(2);
                String meddraConceptNameEn=indicationsEn.getString(i);
                enIndication.setMeddraConceptName(meddraConceptNameEn);
                Indication cnIndication=new Indication();
                cnIndication.setCheckState(1);
                cnIndication.setCreatedAt(System.currentTimeMillis());
                cnIndication.setCreatedWay(2);
                String meddraConceptNameCn=indicationsCn.getString(i);
                cnIndication.setMeddraConceptName(meddraConceptNameCn);
                Indication indication = indicationService.save(cnIndication, enIndication);
                if (indication!=null){
                    DrugStructuredIndication structuredIndication=new DrugStructuredIndication();
                    structuredIndication.setDrugId(drug.getDrugId());
                    structuredIndication.setDrugKey(drug.getDrugKey());
                    structuredIndication.setIndicationKey(indication.getIndicationKey());
                    drugStructuredIndicationRepository.save(structuredIndication);
                    cnDrugStructuredIndicationRepository.save(structuredIndication);
                }
            }
        }
        // 9.药物临床实验(见底部)
        JsonArrayConverter<DrugClinicalTrial> clinicalTrialConverter=(json)->{
            JSONArray clinicalTrials=json.getJSONArray("clinicalTrials");
            List<DrugClinicalTrial> drugClinicalTrialList=new DescendableLinkedList<>();
            if (clinicalTrials!=null&&clinicalTrials.size()>0){
                for (int i=0;i<clinicalTrials.size();i++){
                    String clinicalTrialId=clinicalTrials.getString(i);
                    ClinicalTrial clinicalTrial=clinicalTrailRepository.findByClinicalTrialId(clinicalTrialId);
                    if (clinicalTrial!=null){
                        DrugClinicalTrialPK pk=new DrugClinicalTrialPK();
                        pk.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                        pk.setDrugKey(drug.getDrugKey());
                        if (drugClinicalTrialRepository.findOne(pk)==null){
                            DrugClinicalTrial drugClinicalTrial=new DrugClinicalTrial();
                            drugClinicalTrial.setClinicalTrialId(clinicalTrialId);
                            drugClinicalTrial.setClinicalTrialKey(clinicalTrial.getClinicalTrialKey());
                            drugClinicalTrial.setDrugId(drug.getDrugId());
                            drugClinicalTrial.setDrugKey(drug.getDrugKey());
                            drugClinicalTrialList.add(drugClinicalTrial);
                        }
                    }
                }
            }
            return drugClinicalTrialList;
        };
        drugClinicalTrialRepository.save(clinicalTrialConverter.convert(en));
        cnDrugClinicalTrialRepository.save(clinicalTrialConverter.convert(cn));
        //10.药物不良反应
        JSONArray reactionsEn = en.getJSONArray("adverseReactions");
        JSONArray reactionsCn = cn.getJSONArray("adverseReactions");
        if (reactionsEn!=null&&reactionsEn.size()>0){
            JsonObjectConverter<SideEffect> sideEffectConverter=(json)->{
                SideEffect sideEffect=new SideEffect();
                DrugAdverseReaction adverseReaction=json.toJavaObject(DrugAdverseReaction.class);
                sideEffect.setCheckState(1);
                sideEffect.setCreatedAt(System.currentTimeMillis());
                sideEffect.setCreatedWay(2);
                sideEffect.setSideEffectName(adverseReaction.getAdressName());
                return sideEffect;
            };
            for (int i = 0; i < reactionsEn.size(); i++) {
                DrugAdverseReaction adverseReactionEn=reactionsEn.getObject(i,DrugAdverseReaction.class);
                DrugAdverseReaction adverseReactionCn=reactionsCn.getObject(i,DrugAdverseReaction.class);
                SideEffect sideEffectEn = sideEffectConverter.convert(reactionsEn.getJSONObject(i));
                SideEffect sideEffectCn = sideEffectConverter.convert(reactionsCn.getJSONObject(i));
                SideEffect sideEffect = sideEffectService.save(sideEffectCn, sideEffectEn);
                if (sideEffect!=null){
                    adverseReactionEn.setDrugId(drug.getDrugId());
                    adverseReactionEn.setDrugKey(drug.getDrugKey());
                    adverseReactionEn.setSideEffectKey(sideEffect.getSideEffectKey());
                    drugAdverseReactionRepository.save(adverseReactionEn);
                    adverseReactionCn.setDrugId(drug.getDrugId());
                    adverseReactionCn.setDrugKey(drug.getDrugKey());
                    adverseReactionCn.setSideEffectKey(sideEffect.getSideEffectKey());
                    cnDrugAdverseReactionRepository.save(adverseReactionCn);
                }
            }
        }
        //11.药物相互作用
        String interactionKey=PkGenerator.generator(DrugInteraction.class);
        JsonArrayConverter<DrugInteraction> interactionConverter=(json)->{
            List<DrugInteraction> interactionList=new ArrayList<>();
            JSONArray interactions=json.getJSONArray("interactions");
            if (interactions!=null&&interactions.size()>0){
                Set<Integer> interactionId=new HashSet<>();
                for (int i=0;i<interactions.size();i++){
                    DrugInteraction interaction=interactions.getObject(i,DrugInteraction.class);
                    //去重
                    if (interactionId.contains(interaction.getDrugIdInteraction())){
                        continue;
                    }
                    interactionId.add(interaction.getDrugIdInteraction());
                    interaction.setInteractionKey(PkGenerator.md5(interactionKey+i));
                    interaction.setDrugId(drug.getDrugId());
                    interaction.setDrugKey(drug.getDrugKey());
                    interactionList.add(interaction);
                }
            }
            return interactionList;
        };
        drugInteractionRepository.save(interactionConverter.convert(en));
        cnDrugInteractionRepository.save(interactionConverter.convert(cn));
        //12.药品
        JSONArray productsEn=en.getJSONArray("products");
        JSONArray productsCn=cn.getJSONArray("products");
        if (productsEn!=null&&productsEn.size()>0){
            for (int i=0;i<productsEn.size();i++){
                JSONObject objectEn = productsEn.getJSONObject(i);
                JSONObject objectCn = productsCn.getJSONObject(i);
                DrugProduct product = drugProductService.save(objectEn, objectCn, drug, status);
                if (product!=null){
                    DrugProductIngredientPK pk=new DrugProductIngredientPK();
                    pk.setDrugKey(drug.getDrugKey());
                    pk.setProductKey(product.getProductKey());
                    DrugProductIngredient ingredient=new DrugProductIngredient();
                    ingredient.setDrugKey(drug.getDrugKey());
                    ingredient.setProductKey(product.getProductKey());
                    if (drugProductIngredientRepository.findOne(pk)==null){
                        drugProductIngredientRepository.save(ingredient);
                    }
                    if (cnDrugProductIngredientRepository.findOne(pk)==null){
                        cnDrugProductIngredientRepository.save(ingredient);
                    }
                }
//                DrugProductApproval productApproval = drugProductService.save(objectEn, objectCn, drug, status);
//                //药物药品
//                DrugProductIngredientPK pk=new DrugProductIngredientPK();
//                pk.setDrugKey(drug.getDrugKey());
//                pk.setProductKey(productApproval.getProductKey());
//                DrugProductIngredient ingredient=new DrugProductIngredient();
//                ingredient.setDrugKey(drug.getDrugKey());
//                ingredient.setProductKey(productApproval.getProductKey());
//                if (drugProductIngredientRepository.findOne(pk)==null){
//                    drugProductIngredientRepository.save(ingredient);
//                }
//                if (cnDrugProductIngredientRepository.findOne(pk)==null){
//                    cnDrugProductIngredientRepository.save(ingredient);
//                }
//                JsonObjectConverter<DrugProductIngredientContent> contentConverter=(json)->{
//                    DrugProductIngredientContent content=new DrugProductIngredientContent();
//                    content.setDrugKey(drug.getDrugKey());
//                    content.setProductKey(productApproval.getProductKey());
//                    content.setApprovalKey(productApproval.getApprovalKey());
//                    String dosageForm = json.getString("dosageForm");
//                    String dosageStrength = json.getString("dosageStrength");
//                    Dosage dosage = DosageUtil.splitDosage(dosageStrength, dosageForm);
//                    content.setContentExplain(dosage.getOriginal());
//                    if (dosage.getState()==2){
//                        content.setContentUnit(dosage.getContentUnit());
//                        content.setContentValue(Double.valueOf(dosage.getContentValue()));
//                    }else if (dosage.getState()==3){
//                        content.setContentConcentration(Double.valueOf(dosage.getConcentration()));
//                    }else if(dosage.getState()==4){
//                        content.setContentUnit(dosage.getContentUnit());
//                        content.setContentValue(Double.valueOf(dosage.getContentValue()));
//                        content.setContentConcentration(Double.valueOf(dosage.getConcentration()));
//                    }
//                    return content;
//                };
//                //成分含量
//                DrugProductIngredientContentPK contentPK=new DrugProductIngredientContentPK();
//                contentPK.setDrugKey(drug.getDrugKey());
//                contentPK.setProductKey(productApproval.getProductKey());
//                contentPK.setApprovalKey(productApproval.getApprovalKey());
//                if (drugProductIngredientContentRepository.findOne(contentPK)==null){
//                    drugProductIngredientContentRepository.save(contentConverter.convert(objectEn));
//                }
//                if (cnDrugProductIngredientContentRepository.findOne(contentPK)==null){
//                    cnDrugProductIngredientContentRepository.save(contentConverter.convert(objectCn));
//                }
            }
        }
        //13.药物分类
        JSONArray categoriesEn = en.getJSONArray("categories");
        JSONArray categoriesCn = cn.getJSONArray("categories");
        if (categoriesEn!=null&&categoriesEn.size()>0){
            JsonObjectConverter<MeshCategory> meshCategoryConverter=(json)->{
                MeshCategory meshCategory=json.toJavaObject(MeshCategory.class);
                meshCategory.setCreatedAt(System.currentTimeMillis());
                meshCategory.setCreatedWay(2);
                meshCategory.setCheckState(1);
                return meshCategory;
            };
            for (int i = 0; i < categoriesEn.size(); i++) {
                MeshCategory meshCategoryEn = meshCategoryConverter.convert(categoriesEn.getJSONObject(i));
                MeshCategory meshCategoryCn = meshCategoryConverter.convert(categoriesCn.getJSONObject(i));
                MeshCategory meshCategory = meshCategoryService.save(meshCategoryCn, meshCategoryEn);
                if (meshCategory!=null){
                    DrugCategory drugCategory=new DrugCategory();
                    drugCategory.setDrugKey(drug.getDrugKey());
                    drugCategory.setDrugId(drug.getDrugId());
                    drugCategory.setMeshCategoryKey(meshCategory.getMeshCategoryKey());
                    drugCategory=drugCategoryRepository.save(drugCategory);
                    cnDrugCategoryRepository.save(drugCategory);
                }
            }
        }
        //14.药物序列
        String sequenceKey=PkGenerator.generator(DrugSequence.class);
        JsonArrayConverter<DrugSequence> sequenceConverter=(json)->{
            JSONArray sequences=json.getJSONArray("sequences");
            List<DrugSequence> sequenceList=new ArrayList<>();
            if (sequences!=null&&sequences.size()>0){
                for (int i=0;i<sequences.size();i++){
                    DrugSequence sequence=new DrugSequence();
                    sequence.setSequenceKey(PkGenerator.md5(sequenceKey+i));
                    sequence.setDrugId(drug.getDrugId());
                    sequence.setDrugKey(drug.getDrugKey());
                    sequence.setSequence(sequences.getString(i));
                    sequenceList.add(sequence);
                }
            }
            return sequenceList;
        };
        drugSequenceRepository.save(sequenceConverter.convert(en));
        cnDrugSequenceRepository.save(sequenceConverter.convert(cn));
        //15.TODO（该字段全部为空，看不到结构，暂时不做） 药物食物不良反应
        // JSONArray foodInteractions=object.getJSONArray("foodInteractions");
        return true;
    }

    public void initDB() throws FileNotFoundException {
        CPA.DRUG.name=cpaProperties.getDrugName();
        CPA.DRUG.contentUrl=cpaProperties.getDrugUrl();
        CPA.DRUG.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getDrugTempPath());
        Set<Integer> ids=drugRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            String id=String.valueOf(iterator.next());
            CPA.DRUG.dbId.add(id);
        }
    }

    public boolean saveOne(String id,Map<String,Integer> status){
        JSONObject en = null;
        JSONObject cn=null;
        try {
            en = JsoupUtil.getJsonByUrl(CPA.DRUG, id, "en");
            cn = JsoupUtil.getJsonByUrl(CPA.DRUG, id, "zn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (en==null){
            return false;
        }
        if (cn==null){
            cn=en;
        }
        boolean success=false;
        try {
            if (CPA.DRUG.dbId.add(id)){
                success=this.save(en,cn,status);
                if (!success){
                    CPA.DRUG.dbId.remove(id);
                }
            }
        }catch (Exception e){
            CPA.DRUG.dbId.remove(id);
            if (e instanceof DataException||e instanceof MergeException){
                logger.error("【MergeException】存入数据异常，info:["+CPA.DRUG.name()+"]-->"+id+",cause:"+e.getMessage());
            } else {
                logger.error("【MergeException】存入数据异常，info:["+CPA.DRUG.name()+"]-->"+id);
                logger.error("【MergeException】"+ ExceptionInfo.getErrorInfo(e));
            }
        }
        if (success){
            MergeInfo.DRUG.sign.remove(id);
            status.forEach((key,value)->{
                if (MergeInfo.KEGG_PATHWAY.sign.contains(key)){
                    MergeInfo.KEGG_PATHWAY.sign.remove(key);
                }
                if (MergeInfo.DRUG_PRODUCT.sign.contains(key)){
                    MergeInfo.DRUG_PRODUCT.sign.remove(key);
                }
            });
            MergeInfo.DRUG.mergeList.removeIf(next -> next.get(0).equals(id));
            MergeInfo.DRUG_PRODUCT.mergeList.removeIf(next -> next.get(0).equals(id));
            MergeInfo.KEGG_PATHWAY.mergeList.removeIf(next -> next.get(0).equals(id));
        }
        return success;
    }
}
