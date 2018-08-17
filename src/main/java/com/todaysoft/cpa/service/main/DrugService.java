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
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.service.vice.*;
import com.todaysoft.cpa.utils.*;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayLangConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectKeyConverter;
import com.todaysoft.cpa.utils.dosage.Dosage;
import com.todaysoft.cpa.utils.dosage.DosageUtil;
import org.jsoup.helper.DescendableLinkedList;
import org.jsoup.select.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private KbUpdateService kbUpdateService;

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
        Drug checkDrug=en.toJavaObject(Drug.class);
        Drug oldCn = cnDrugRepository.findByNameEn(checkDrug.getNameEn());
        Drug oldEn = drugRepository.findByDrugId(checkDrug.getDrugId());
        //是否使用老中文库数据状态
        boolean isUseOldCnState=oldCn!=null;
        //是否使用老英文库数据状态
        boolean isUseOldEnState=oldEn!=null;
        //是否保存中文数据
        boolean isSaveCn = oldEn==null&&(oldCn==null||oldCn.getCreateWay()==3);
        //是否是老库覆盖CPA数据
        boolean isOldBaseData= oldEn==null && oldCn!=null && oldCn.getCreateWay()==3;
        String drugKey=oldCn==null?PkGenerator.generator(Drug.class):oldCn.getDrugKey();
        JsonObjectConverter<Drug> drugConverter=(json)->{
            Drug drug=json.toJavaObject(Drug.class);
            drug.setDrugKey(drugKey);
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
        //更新状态
        if (isUseOldCnState){
            drugCn.setCreateWay(oldCn.getCreateWay());
            drugCn.setCheckState(oldCn.getCheckState());
            drugCn.setCreatedByName(oldCn.getCreatedByName());
        }
        if (isUseOldEnState){
            drugEn.setCreateWay(oldEn.getCreateWay());
            drugEn.setCheckState(oldEn.getCheckState());
            drugEn.setCreatedByName(oldEn.getCreatedByName());
        }
        drugCn.setNameChinese(drugCn.getNameEn());
        drugCn.setNameEn(drugEn.getNameEn());
        //合并CPA和老库
        if (isOldBaseData){
            drugEn.setCheckState(4);
            if (!StringUtils.isEmpty(oldCn.getNameChinese())){
                drugCn.setNameChinese(oldCn.getNameChinese());
            }
            drugCn.setOtherNames(MergeUtil.mergeAlias(oldCn.getOtherNames(),drugCn.getOtherNames(),"<=>"));
            if (!StringUtils.isEmpty(oldCn.getDescription())){
                drugCn.setDescription(oldCn.getDescription());
            }
            if (!StringUtils.isEmpty(oldCn.getMechanismOfAction())){
                drugCn.setMechanismOfAction(oldCn.getMechanismOfAction());
            }
            if (!StringUtils.isEmpty(oldCn.getRouteOfElimination())){
                drugCn.setRouteOfElimination(oldCn.getRouteOfElimination());
            }
            if (!StringUtils.isEmpty(oldCn.getToxicity())){
                drugCn.setToxicity(oldCn.getToxicity());
            }
            if (!StringUtils.isEmpty(oldCn.getStructuredIndicationDesc())){
                drugCn.setStructuredIndicationDesc(oldCn.getStructuredIndicationDesc());
            }
            if (!StringUtils.isEmpty(oldCn.getAbsorption())){
                drugCn.setAbsorption(oldCn.getAbsorption());
            }
            if (!StringUtils.isEmpty(oldCn.getAttention())){
                drugCn.setAttention(oldCn.getAttention());
            }
            if (!StringUtils.isEmpty(oldCn.getVolumeOfDistribution())){
                drugCn.setVolumeOfDistribution(oldCn.getVolumeOfDistribution());
            }
            if (!StringUtils.isEmpty(oldCn.getMajorMetabolicSites())){
                drugCn.setMajorMetabolicSites(oldCn.getMajorMetabolicSites());
            }
            if (!StringUtils.isEmpty(oldCn.getProteinBinding())){
                drugCn.setProteinBinding(oldCn.getProteinBinding());
            }
            if (!StringUtils.isEmpty(oldCn.getHalfLife())){
                drugCn.setHalfLife(oldCn.getHalfLife());
            }
            if (!StringUtils.isEmpty(oldCn.getClearance())){
                drugCn.setClearance(oldCn.getClearance());
            }
            if (!StringUtils.isEmpty(oldCn.getPharmacodynamics())){
                drugCn.setPharmacodynamics(oldCn.getPharmacodynamics());
            }
        }
        Drug drug=drugRepository.save(drugEn);
        if (isSaveCn){
            cnDrugRepository.save(drugCn);
        }

        //---start PrimaryDrugExternalId--------------------
        List<DrugExternalId> hasExternalIds = drugExternalIdRepository.findByDrugKey(drug.getDrugKey());
        Map<Integer, String> externalIdKeys = new HashMap<>();
        if (hasExternalIds != null) {
            externalIdKeys.putAll(hasExternalIds.stream().collect(Collectors.toMap(DrugExternalId::hashCode, DrugExternalId::getExternalIdKey)));
        }
        String pExternalIdKey = PkGenerator.generator(DrugExternalId.class);
        JsonObjectConverter<DrugExternalId> pExternalIdConverter = (json) -> {
            DrugExternalId externalId = new DrugExternalId();
            externalId.setPrimary(true);
            externalId.setDrugId(drug.getDrugId());
            externalId.setDrugKey(drug.getDrugKey());
            externalId.setExternalId(json.getString("primaryExternalId"));
            externalId.setExternalIdSource(json.getString("primaryExternalSource"));
            externalId.setExternalIdKey(pExternalIdKey);
            return externalId;
        };
        DrugExternalId enDrugExternalId = pExternalIdConverter.convert(en);
        String pekey = externalIdKeys.get(enDrugExternalId.hashCode());
        if (pekey != null) {
            enDrugExternalId.setExternalIdKey(pekey);
        }
        drugExternalIdRepository.save(enDrugExternalId);
        if (isSaveCn) {
            DrugExternalId cDrugExternalId = pExternalIdConverter.convert(cn);
            if (pekey != null) {
                cDrugExternalId.setExternalIdKey(pekey);
            }
            cnDrugExternalIdRepository.save(cDrugExternalId);
        }
        //---end  PrimaryDrugExternalId---------------------
        //---start DrugExternalId--------------------
        String externalIdKey = PkGenerator.generator(DrugExternalId.class);
        JsonArrayConverter<DrugExternalId> externalIdConverter = (json) -> {
            JSONArray externalIds = json.getJSONArray("externalIds");
            List<DrugExternalId> externalIdList = new ArrayList<>();
            if (externalIds != null && externalIds.size() > 0) {
                for (int i = 0; i < externalIds.size(); i++) {
                    DrugExternalId externalId = externalIds.getJSONObject(i).toJavaObject(DrugExternalId.class);
                    externalId.setDrugId(drug.getDrugId());
                    externalId.setDrugKey(drug.getDrugKey());
                    externalId.setPrimary(false);
                    String key = externalIdKeys.get(externalId.hashCode());
                    externalId.setExternalIdKey(key!=null?key:PkGenerator.md5(externalIdKey + i));
                    externalIdList.add(externalId);
                }
            }
            return externalIdList;
        };
        List<DrugExternalId> enED = externalIdConverter.convert(en);
        drugExternalIdRepository.save(enED);
        if (isSaveCn) {
            List<DrugExternalId> cnDE = externalIdConverter.convert(cn);
            for (int i = 0; i < cnDE.size(); i++) {
                cnDE.get(i).setExternalIdKey(enED.get(i).getExternalIdKey());
            }
            cnDrugExternalIdRepository.save(cnDE);
        }
        //---end  DrugExternalId---------------------
        //---start 药物商品--------------------
        List<DrugInternationalBrand> dbDIBs = drugInternationalBrandRepository.findByDrugKey(drug.getDrugKey());
        Map<Integer,String> dibKeyMap=new HashMap<>();
        if (dbDIBs!=null){
            dibKeyMap.putAll(dbDIBs.stream().collect(Collectors.toMap(DrugInternationalBrand::hashCode, DrugInternationalBrand::getInternationalBrandKey)));
        }
        String internationalBrandKey = PkGenerator.generator(DrugInternationalBrand.class);
        JsonArrayConverter<DrugInternationalBrand> brandConverter = (json) -> {
            JSONArray internationalBrands = json.getJSONArray("internationalBrands");
            List<DrugInternationalBrand> internationalBrandList = new ArrayList<>();
            if (internationalBrands != null && internationalBrands.size() > 0) {
                for (int i = 0; i < internationalBrands.size(); i++) {
                    DrugInternationalBrand internationalBrand = new DrugInternationalBrand();
                    internationalBrand.setDrugId(drug.getDrugId());
                    internationalBrand.setDrugKey(drug.getDrugKey());
                    JSONArray brands = internationalBrands.getJSONArray(i);
                    if (brands != null && brands.size() == 2) {
                        internationalBrand.setInternationalBrand(brands.getString(0));
                        internationalBrand.setBrandCompany(brands.getString(1));
                        String key = dibKeyMap.get(internationalBrand.hashCode());
                        internationalBrand.setInternationalBrandKey(key!=null?key:PkGenerator.md5(internationalBrandKey + i));
                        internationalBrandList.add(internationalBrand);
                    }
                }
            }
            return internationalBrandList;
        };
        List<DrugInternationalBrand> enDIB = brandConverter.convert(en);
        drugInternationalBrandRepository.save(enDIB);
        if (isSaveCn) {
            List<DrugInternationalBrand> cnDIB = brandConverter.convert(cn);
            for (int i = 0; i < cnDIB.size(); i++) {
                cnDIB.get(i).setInternationalBrandKey(enDIB.get(i).getInternationalBrandKey());
            }
            cnDrugInternationalBrandRepository.save(cnDIB);
        }
        //---end  药物商品---------------------
        //---start 药物通路--------------------
        JSONArray keggPathwaysEn = en.getJSONArray("keggPathways");
        JSONArray keggPathwaysCn = cn.getJSONArray("keggPathways");
        if (keggPathwaysEn != null && keggPathwaysEn.size() > 0) {
            JsonObjectConverter<KeggPathway> pathwayConverter = (json) -> {
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
                KeggPathway keggPathway = keggPathwaysService.save(keggPathwayCn, keggPathwayEn, drug, status);
                if (keggPathway != null) {
                    DrugKeggPathway drugKeggPathway = new DrugKeggPathway();
                    drugKeggPathway.setPathwayKey(keggPathway.getPathwayKey());
                    drugKeggPathway.setDrugKey(drug.getDrugKey());
                    drugKeggPathway.setKeggId(keggPathway.getKeggId());
                    drugKeggPathway.setDrugId(drug.getDrugId());
                    drugKeggPathway.setPathwayName(keggPathway.getPathwayName());
                    drugKeggPathwayRepository.save(drugKeggPathway);
                    drugKeggPathway.setPathwayName(nameCn);
                    if (isSaveCn) {
                        cnDrugKeggPathwayRepository.save(drugKeggPathway);
                    }
                }
            }
        }
        //---end  药物通路---------------------
        //---start 药物结构化适应症--------------------
        JSONArray indicationsEn = en.getJSONArray("structuredIndications");
        JSONArray indicationsCn = cn.getJSONArray("structuredIndications");
        if (indicationsEn != null && indicationsEn.size() > 0) {
            for (int i = 0; i < indicationsEn.size(); i++) {
                Indication enIndication = new Indication();
                enIndication.setCheckState(1);
                enIndication.setCreatedAt(System.currentTimeMillis());
                enIndication.setCreatedWay(2);
                enIndication.setCreatedByName("CPA");
                String meddraConceptNameEn = indicationsEn.getString(i);
                enIndication.setMeddraConceptName(meddraConceptNameEn);
                Indication cnIndication = new Indication();
                cnIndication.setCheckState(1);
                cnIndication.setCreatedAt(System.currentTimeMillis());
                cnIndication.setCreatedWay(2);
                cnIndication.setCreatedByName("CPA");
                String meddraConceptNameCn = indicationsCn.getString(i);
                cnIndication.setMeddraConceptName(meddraConceptNameCn);
                Indication indication = indicationService.save(cnIndication, enIndication);
                if (indication != null) {
                    DrugStructuredIndication structuredIndication = new DrugStructuredIndication();
                    structuredIndication.setDrugId(drug.getDrugId());
                    structuredIndication.setDrugKey(drug.getDrugKey());
                    structuredIndication.setIndicationKey(indication.getIndicationKey());
                    drugStructuredIndicationRepository.save(structuredIndication);
                    if (isSaveCn) {
                        cnDrugStructuredIndicationRepository.save(structuredIndication);
                    }
                }
            }
        }
        //---end  药物结构化适应症---------------------
        //---start 药物临床实验--------------------
        JsonArrayConverter<DrugClinicalTrial> clinicalTrialConverter = (json) -> {
            JSONArray clinicalTrials = json.getJSONArray("clinicalTrials");
            List<DrugClinicalTrial> drugClinicalTrialList = new ArrayList<>();
            if (clinicalTrials != null && clinicalTrials.size() > 0) {
                for (int i = 0; i < clinicalTrials.size(); i++) {
                    String clinicalTrialId = clinicalTrials.getString(i);
                    String clinicalTrialKey = clinicalTrailRepository.findByClinicalTrialId(clinicalTrialId);
                    if (clinicalTrialKey != null && clinicalTrialKey.length() > 0) {
                        DrugClinicalTrial drugClinicalTrial = new DrugClinicalTrial();
                        drugClinicalTrial.setClinicalTrialId(clinicalTrialId);
                        drugClinicalTrial.setClinicalTrialKey(clinicalTrialKey);
                        drugClinicalTrial.setDrugId(drug.getDrugId());
                        drugClinicalTrial.setDrugKey(drug.getDrugKey());
                        drugClinicalTrialList.add(drugClinicalTrial);
                    }
                }
            }
            return drugClinicalTrialList;
        };
        drugClinicalTrialRepository.save(clinicalTrialConverter.convert(en));
        if (isSaveCn) {
            cnDrugClinicalTrialRepository.save(clinicalTrialConverter.convert(cn));
        }
        //---end  药物临床实验---------------------

        //---start 药物不良反应--------------------
        JSONArray reactionsEn = en.getJSONArray("adverseReactions");
        JSONArray reactionsCn = cn.getJSONArray("adverseReactions");
        if (reactionsEn != null && reactionsEn.size() > 0) {
            JsonObjectConverter<SideEffect> sideEffectConverter = (json) -> {
                SideEffect sideEffect = new SideEffect();
                DrugAdverseReaction adverseReaction = json.toJavaObject(DrugAdverseReaction.class);
                sideEffect.setCheckState(1);
                sideEffect.setCreatedAt(System.currentTimeMillis());
                sideEffect.setCreatedWay(2);
                sideEffect.setCreatedByName("CPA");
                sideEffect.setSideEffectName(adverseReaction.getAdressName());
                return sideEffect;
            };
            Set<String> uniqueSet = new HashSet<>();
            for (int i = 0; i < reactionsEn.size(); i++) {
                DrugAdverseReaction adverseReactionEn = reactionsEn.getObject(i, DrugAdverseReaction.class);
                DrugAdverseReaction adverseReactionCn = reactionsCn.getObject(i, DrugAdverseReaction.class);
                SideEffect sideEffectEn = sideEffectConverter.convert(reactionsEn.getJSONObject(i));
                SideEffect sideEffectCn = sideEffectConverter.convert(reactionsCn.getJSONObject(i));
                SideEffect sideEffect = sideEffectService.save(sideEffectCn, sideEffectEn);
                if (sideEffect != null && uniqueSet.add(sideEffect.getSideEffectKey())) {
                    adverseReactionEn.setDrugId(drug.getDrugId());
                    adverseReactionEn.setDrugKey(drug.getDrugKey());
                    adverseReactionEn.setSideEffectKey(sideEffect.getSideEffectKey());
                    drugAdverseReactionRepository.save(adverseReactionEn);
                    adverseReactionCn.setDrugId(drug.getDrugId());
                    adverseReactionCn.setDrugKey(drug.getDrugKey());
                    adverseReactionCn.setSideEffectKey(sideEffect.getSideEffectKey());
                    if (isSaveCn) {
                        cnDrugAdverseReactionRepository.save(adverseReactionCn);
                    }
                }
            }
        }
        //---end  药物不良反应---------------------
        //---start 药物相互作用--------------------
        List<DrugInteraction> diList = drugInteractionRepository.findByDrugKey(drug.getDrugKey());
        Map<Integer,String> diKeyMap=new HashMap<>();
        if (diList!=null){
            diKeyMap.putAll(diList.stream().collect(Collectors.toMap(DrugInteraction::hashCode, DrugInteraction::getInteractionKey)));
        }
        String interactionKey = PkGenerator.generator(DrugInteraction.class);
        JsonArrayConverter<DrugInteraction> interactionConverter = (json) -> {
            List<DrugInteraction> interactionList = new ArrayList<>();
            JSONArray interactions = json.getJSONArray("interactions");
            if (interactions != null && interactions.size() > 0) {
                Set<Integer> interactionId = new HashSet<>();
                for (int i = 0; i < interactions.size(); i++) {
                    DrugInteraction interaction = interactions.getObject(i, DrugInteraction.class);
                    //去重
                    if (interactionId.contains(interaction.getDrugIdInteraction())) {
                        continue;
                    }
                    interactionId.add(interaction.getDrugIdInteraction());
                    interaction.setDrugId(drug.getDrugId());
                    interaction.setDrugKey(drug.getDrugKey());
                    String key = diKeyMap.get(interaction.hashCode());
                    interaction.setInteractionKey(key!=null?key:PkGenerator.md5(interactionKey + i));
                    interactionList.add(interaction);
                }
            }
            return interactionList;
        };
        List<DrugInteraction> enDI = interactionConverter.convert(en);
        drugInteractionRepository.save(enDI);
        if (isSaveCn) {
            List<DrugInteraction> cnDI = interactionConverter.convert(cn);
            for (int i = 0; i < cnDI.size(); i++) {
                cnDI.get(i).setInteractionKey(enDI.get(i).getInteractionKey());
            }
            cnDrugInteractionRepository.save(cnDI);
        }
        //---end  药物相互作用---------------------
        //---start 药品--------------------
        JSONArray productsEn = en.getJSONArray("products");
        JSONArray productsCn = cn.getJSONArray("products");
        if (productsEn != null && productsEn.size() > 0) {
            Set<String> uniqSet = new HashSet<>();
            for (int i = 0; i < productsEn.size(); i++) {
                JSONObject objectEn = productsEn.getJSONObject(i);
                JSONObject objectCn = productsCn.getJSONObject(i);
                DrugProduct product = drugProductService.save(objectEn, objectCn, drug, status);
                if (product != null && uniqSet.add(product.getProductKey())) {
                    DrugProductIngredientPK pk = new DrugProductIngredientPK();
                    pk.setDrugKey(drug.getDrugKey());
                    pk.setProductKey(product.getProductKey());
                    DrugProductIngredient ingredient = new DrugProductIngredient();
                    ingredient.setDrugKey(drug.getDrugKey());
                    ingredient.setProductKey(product.getProductKey());
                    if (drugProductIngredientRepository.findOne(pk) == null) {
                        drugProductIngredientRepository.save(ingredient);
                    }
                    if (isSaveCn && cnDrugProductIngredientRepository.findOne(pk) == null) {
                        cnDrugProductIngredientRepository.save(ingredient);
                    }
                }
            }
        }
        //---end  药品---------------------
        //---start 药物分类--------------------
        JSONArray categoriesEn = en.getJSONArray("categories");
        JSONArray categoriesCn = cn.getJSONArray("categories");
        if (categoriesEn != null && categoriesEn.size() > 0) {
            JsonObjectConverter<MeshCategory> meshCategoryConverter = (json) -> {
                MeshCategory meshCategory = json.toJavaObject(MeshCategory.class);
                meshCategory.setCreatedAt(System.currentTimeMillis());
                meshCategory.setCreatedWay(2);
                meshCategory.setCheckState(1);
                meshCategory.setCreatedByName("CPA");
                return meshCategory;
            };
            for (int i = 0; i < categoriesEn.size(); i++) {
                MeshCategory meshCategoryEn = meshCategoryConverter.convert(categoriesEn.getJSONObject(i));
                MeshCategory meshCategoryCn = meshCategoryConverter.convert(categoriesCn.getJSONObject(i));
                MeshCategory meshCategory = meshCategoryService.save(meshCategoryCn, meshCategoryEn);
                if (meshCategory != null) {
                    DrugCategory drugCategory = new DrugCategory();
                    drugCategory.setDrugKey(drug.getDrugKey());
                    drugCategory.setDrugId(drug.getDrugId());
                    drugCategory.setMeshCategoryKey(meshCategory.getMeshCategoryKey());
                    drugCategory = drugCategoryRepository.save(drugCategory);
                    if (isSaveCn) {
                        cnDrugCategoryRepository.save(drugCategory);
                    }
                }
            }
        }
        //---end  药物分类---------------------
        //---start 药物序列--------------------
        List<DrugSequence> dsList = drugSequenceRepository.findByDrugKey(drug.getDrugKey());
        Map<String,String> dsKeyMap=new HashMap<>();
        if (diList!=null){
            dsKeyMap.putAll(dsList.stream().collect(Collectors.toMap(DrugSequence::getSequence, DrugSequence::getSequenceKey)));
        }
        String sequenceKey = PkGenerator.generator(DrugSequence.class);
        JsonArrayConverter<DrugSequence> sequenceConverter = (json) -> {
            JSONArray sequences = json.getJSONArray("sequences");
            List<DrugSequence> sequenceList = new ArrayList<>();
            if (sequences != null && sequences.size() > 0) {
                for (int i = 0; i < sequences.size(); i++) {
                    DrugSequence sequence = new DrugSequence();
                    sequence.setDrugId(drug.getDrugId());
                    sequence.setDrugKey(drug.getDrugKey());
                    sequence.setSequence(sequences.getString(i));
                    String key = dsKeyMap.get(sequence.getSequence());
                    sequence.setSequenceKey(key!=null?key:PkGenerator.md5(sequenceKey + i));
                    sequenceList.add(sequence);
                }
            }
            return sequenceList;
        };
        List<DrugSequence> enDS = sequenceConverter.convert(en);
        drugSequenceRepository.save(enDS);
        if (isSaveCn) {
            List<DrugSequence> cnDS = sequenceConverter.convert(cn);
            for (int i = 0; i < cnDS.size(); i++) {
                cnDS.get(i).setSequenceKey(enDS.get(i).getSequenceKey());
            }
            cnDrugSequenceRepository.save(cnDS);
        }
        //---end  药物序列---------------------
        //15.TODO（该字段全部为空，看不到结构，暂时不做） 药物食物不良反应
        // JSONArray foodInteractions=object.getJSONArray("foodInteractions");
        if (oldEn==null&&drugCn.getCheckState()==1){
            kbUpdateService.send("kt_drug");
        }
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
                logger.error("【MergeException】存入数据异常，info:["+CPA.DRUG.name()+"]-->"+id,e);
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
