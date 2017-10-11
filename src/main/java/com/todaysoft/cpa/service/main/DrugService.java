package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.clinicalTrail.CnClinicalTrailRepository;
import com.todaysoft.cpa.domain.cn.drug.*;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.en.drug.*;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.param.*;
import com.todaysoft.cpa.service.*;
import com.todaysoft.cpa.service.vice.IndicationService;
import com.todaysoft.cpa.service.vice.KeggPathwaysService;
import com.todaysoft.cpa.service.vice.MeshCategoryService;
import com.todaysoft.cpa.service.vice.SideEffectService;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.utils.*;
import org.jsoup.helper.DescendableLinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/8 12:07
 */
@Service
public class DrugService extends BaseService {
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
    private CnDrugOtherNameRepository cnDrugOtherNameRepository;
    @Autowired
    private CnDrugProductEtnIdRepository cnDrugProductEtnIdRepository;
    @Autowired
    private CnDrugProductRepository cnDrugProductRepository;
    @Autowired
    private CnDrugRepository cnDrugRepository;
    @Autowired
    private CnDrugSequenceRepository cnDrugSequenceRepository;
    @Autowired
    private CnDrugStructuredIndicationRepository cnDrugStructuredIndicationRepository;
    @Autowired
    private CnDrugSynonymRepository cnDrugSynonymRepository;
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
    private DrugOtherNameRepository drugOtherNameRepository;
    @Autowired
    private DrugProductEtnIdRepository drugProductEtnIdRepository;
    @Autowired
    private DrugProductRepository drugProductRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugSequenceRepository drugSequenceRepository;
    @Autowired
    private DrugStructuredIndicationRepository drugStructuredIndicationRepository;
    @Autowired
    private DrugSynonymRepository drugSynonymRepository;
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

    @Override
    @Transactional
    public boolean save(JSONObject object) throws InterruptedException {
        //1.解析药物基本信息
        Drug drug=object.toJavaObject(Drug.class);
        drug.setDrugKey(PkGenerator.generator(Drug.class));
        drug.setMolecularWeight(JsonUtil.jsonArrayToString(object.getJSONArray("molecularWeight")));
        drug.setChemicalFormula(JsonUtil.jsonArrayToString(object.getJSONArray("chemicalFormula")));
        Date createdAt=object.getDate("createdAt");
        Long createTime=System.currentTimeMillis();
        if (createdAt!=null){
            createTime=createdAt.getTime();
        }
        drug.setCreatedAt(createTime);
        drug.setCheckState(1);
        drug.setCreateWay(2);
        drug=drugRepository.save(drug);
        drug=cnDrugRepository.save(drug);
        if (drug==null){
            throw new DataException("保存主表失败->id="+object.getString("id"));
        }
        //remove 2.判断插入是否成功
        //3.药物别名
        JSONArray synonyms=object.getJSONArray("synonyms");
        if (synonyms!=null&&synonyms.size()>0){
            List<DrugSynonym> synonymList=new ArrayList<>(synonyms.size());
            for (int i=0;i<synonyms.size();i++){
                DrugSynonym synonym=new DrugSynonym();
                synonym.setSynonymKey(PkGenerator.generator(DrugSynonym.class));
                synonym.setDrugId(drug.getDrugId());
                synonym.setDrugKey(drug.getDrugKey());
                synonym.setDrugSynonym(synonyms.getString(i));
                synonymList.add(synonym);
            }
            drugSynonymRepository.save(synonymList);
            cnDrugSynonymRepository.save(synonymList);
        }
        //4.药物外部id
        JSONArray externalIds=object.getJSONArray("externalIds");
        if (externalIds!=null&&externalIds.size()>0){
            List<DrugExternalId> externalIdList=new ArrayList<>(externalIds.size());
            for (int i=0;i<externalIds.size();i++){
                DrugExternalId externalId=externalIds.getJSONObject(i).toJavaObject(DrugExternalId.class);
                externalId.setExternalIdKey(PkGenerator.generator(DrugSynonym.class));
                externalId.setDrugId(drug.getDrugId());
                externalId.setDrugKey(drug.getDrugKey());
                externalIdList.add(externalId);
            }
            drugExternalIdRepository.save(externalIdList);
            cnDrugExternalIdRepository.save(externalIdList);
        }
        //5.药物其他名称
        JSONArray otherNames=object.getJSONArray("otherNames");
        if (otherNames!=null&&otherNames.size()>0){
            List<DrugOtherName> otherNameList=new ArrayList<>(otherNames.size());
            for (int i=0;i<otherNames.size();i++){
                DrugOtherName otherName=new DrugOtherName();
                otherName.setOtherNameKey(PkGenerator.generator(DrugOtherName.class));
                otherName.setDrugId(drug.getDrugId());
                otherName.setDrugKey(drug.getDrugKey());
                otherName.setOtherName(otherNames.getString(i));
                otherNameList.add(otherName);
            }
            drugOtherNameRepository.save(otherNameList);
            cnDrugOtherNameRepository.save(otherNameList);
        }
        //6.药物商品
        JSONArray internationalBrands=object.getJSONArray("internationalBrands");
        if (internationalBrands!=null&&internationalBrands.size()>0){
            List<DrugInternationalBrand> internationalBrandList=new ArrayList<>(internationalBrands.size());
            for (int i=0;i<internationalBrands.size();i++){
                DrugInternationalBrand internationalBrand=new DrugInternationalBrand();
                internationalBrand.setInternationalBrandKey(PkGenerator.generator(DrugInternationalBrand.class));
                internationalBrand.setDrugId(drug.getDrugId());
                internationalBrand.setDrugKey(drug.getDrugKey());
                JSONArray brands=internationalBrands.getJSONArray(i);
                if (brands!=null&&brands.size()==2){
                    internationalBrand.setInternationalBrand(brands.getString(0));
                    internationalBrand.setBrandCompany(brands.getString(1));
                    internationalBrandList.add(internationalBrand);
                }
            }
            drugInternationalBrandRepository.save(internationalBrandList);
            cnDrugInternationalBrandRepository.save(internationalBrandList);
        }
        //7.药物通路
        List<DrugKeggPathway> pathwayList=new ArrayList<>();
        JSONArray keggPathways=object.getJSONArray("keggPathways");
        if (keggPathways!=null&&keggPathways.size()>0){
            List<KeggPathway> keggPathwayList=new ArrayList<>();
            for (int i=0;i<keggPathways.size();i++){
                String keggId=keggPathways.getJSONObject(i).getString("id");
                KeggPathway pathway = new KeggPathway();
                pathway.setCreateAt(System.currentTimeMillis());
                pathway.setCreateWay(2);
                pathway.setCheckState(1);
                pathway.setPathwayKey(PkGenerator.generator(KeggPathway.class));
                pathway.setKeggId(keggId.trim());
                pathway.setPathwayName(keggPathways.getJSONObject(i).getString("name"));
                keggPathwayList.add(pathway);
            }
            keggPathwayList=keggPathwaysService.saveList(keggPathwayList);
            if (keggPathwayList!=null&&keggPathwayList.size()>0){
                for (KeggPathway pathway:keggPathwayList){
                    if (pathway!=null){
                        DrugKeggPathway drugKeggPathway=new DrugKeggPathway();
                        drugKeggPathway.setPathwayKey(pathway.getPathwayKey());
                        drugKeggPathway.setDrugKey(drug.getDrugKey());
                        drugKeggPathway.setKeggId(pathway.getKeggId());
                        drugKeggPathway.setDrugId(drug.getDrugId());
                        drugKeggPathway.setPathwayName(pathway.getPathwayName());
                        pathwayList.add(drugKeggPathway);
                    }
                }
                drugKeggPathwayRepository.save(pathwayList);
                cnDrugKeggPathwayRepository.save(pathwayList);
            }
        }
        //8.药物结构化适应症
        JSONArray structuredIndications=object.getJSONArray("structuredIndications");
        List<DrugStructuredIndication> structuredIndicationList=new ArrayList<>();
        if (structuredIndications!=null&&structuredIndications.size()>0){
            List<Indication> indicationList=new ArrayList<>();
            for (int i=0;i<structuredIndications.size();i++){
                Indication indication=new Indication();
                indication.setCheckState(1);
                indication.setCreatedAt(System.currentTimeMillis());
                indication.setCreatedWay(2);
                indication.setIndicationKey(PkGenerator.generator(Indication.class));
                indication.setMeddraConceptName(structuredIndications.getString(i));
                indicationList.add(indication);
            }
            indicationList=indicationService.saveList(indicationList);
            if (indicationList!=null&&indicationList.size()>0){
                for (Indication indication:indicationList){
                    if (indication!=null){
                        DrugStructuredIndication structuredIndication=new DrugStructuredIndication();
                        structuredIndication.setDrugId(drug.getDrugId());
                        structuredIndication.setDrugKey(drug.getDrugKey());
                        structuredIndication.setIndicationKey(indication.getIndicationKey());
                        structuredIndicationList.add(structuredIndication);
                    }
                }
                drugStructuredIndicationRepository.save(structuredIndicationList);
                cnDrugStructuredIndicationRepository.save(structuredIndicationList);
            }
        }
        // 9.药物临床实验(见底部)
        JSONArray clinicalTrials=object.getJSONArray("clinicalTrials");
        if (clinicalTrials!=null&&clinicalTrials.size()>0){
            List<DrugClinicalTrial> drugClinicalTrialList=new DescendableLinkedList<>();
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
            drugClinicalTrialRepository.save(drugClinicalTrialList);
            cnDrugClinicalTrialRepository.save(drugClinicalTrialList);
        }
        //10.药物不良反应
        JSONArray adverseReactions=object.getJSONArray("adverseReactions");
        List<DrugAdverseReaction> adverseReactionsList=new ArrayList<>();
        if (adverseReactions!=null&&adverseReactions.size()>0){
            List<SideEffect> sideEffectList=new ArrayList<>();
            Map<String,DrugAdverseReaction> reactionMap=new HashMap<>();
            for (int i=0;i<adverseReactions.size();i++){
                SideEffect sideEffect=new SideEffect();
                DrugAdverseReaction adverseReaction=adverseReactions.getObject(i,DrugAdverseReaction.class);
                reactionMap.put(adverseReaction.getAdressName(),adverseReaction);
                sideEffect.setCheckState(1);
                sideEffect.setCreatedAt(System.currentTimeMillis());
                sideEffect.setCreatedWay(2);
                sideEffect.setSideEffectName(adverseReaction.getAdressName());
                sideEffect.setSideEffectKey(PkGenerator.generator(SideEffect.class));
                sideEffectList.add(sideEffect);
            }
            sideEffectList=sideEffectService.saveList(sideEffectList);
            if (sideEffectList!=null&&sideEffectList.size()>0){
                for (SideEffect sideEffect:sideEffectList){
                    if (sideEffect!=null){
                        DrugAdverseReaction drugAdverseReaction=reactionMap.get(sideEffect.getSideEffectName());
                        drugAdverseReaction.setDrugId(drug.getDrugId());
                        drugAdverseReaction.setDrugKey(drug.getDrugKey());
                        drugAdverseReaction.setSideEffectKey(sideEffect.getSideEffectKey());
                        adverseReactionsList.add(drugAdverseReaction);
                    }
                }
                cnDrugAdverseReactionRepository.save(adverseReactionsList);
                drugAdverseReactionRepository.save(adverseReactionsList);
            }
        }
        //11.药物相互作用
        JSONArray interactions=object.getJSONArray("interactions");
        if (interactions!=null&&interactions.size()>0){
            Set<Integer> interactionId=new HashSet<>();
            List<DrugInteraction> interactionList=new ArrayList<>(interactions.size());
            for (int i=0;i<interactions.size();i++){
                DrugInteraction interaction=interactions.getObject(i,DrugInteraction.class);
                //去重
                if (interactionId.contains(interaction.getDrugIdInteraction())){
                    continue;
                }
                interactionId.add(interaction.getDrugIdInteraction());
                interaction.setInteractionKey(PkGenerator.generator(DrugInteraction.class));
                interaction.setDrugId(drug.getDrugId());
                interaction.setDrugKey(drug.getDrugKey());
                interactionList.add(interaction);
            }
            drugInteractionRepository.save(interactionList);
            cnDrugInteractionRepository.save(interactionList);
        }
        //12.药品
        JSONArray products=object.getJSONArray("products");
        if (products!=null&&products.size()>0){
            List<DrugProduct> productList=new ArrayList<>(products.size());
            for (int i=0;i<products.size();i++){
                DrugProduct product=products.getJSONObject(i).toJavaObject(DrugProduct.class);
                product.setProductKey(PkGenerator.generator(DrugProduct.class));
                product.setDrugKey(drug.getDrugKey());
                product.setDrugId(drug.getDrugId());
                String marketingEnd=products.getJSONObject(i).getString("marketingEnd");
                String marketingStart=products.getJSONObject(i).getString("marketingStart");
                product.setMarketingEnd(DateUtil.stringToTimestamp(marketingEnd));
                product.setMarketingStart(DateUtil.stringToTimestamp(marketingStart));
                product.setCreatedAt(System.currentTimeMillis());
                product.setCheckState(1);
                product.setCreateWay(2);
                product=drugProductRepository.save(product);
                cnDrugProductRepository.save(product);
                //12.1 药品外部id
                JSONObject productEtnIds=products.getJSONObject(i).getJSONObject("externalIds");
                if (productEtnIds!=null){
                    DrugProductEtnId productEtnId=productEtnIds.toJavaObject(DrugProductEtnId.class);
                    productEtnId.setEtnIdKey(PkGenerator.generator(DrugProductEtnId.class));
                    productEtnId.setProductKey(product.getProductKey());
                    drugProductEtnIdRepository.save(productEtnId);
                    cnDrugProductEtnIdRepository.save(productEtnId);
                }
            }
        }
        //13.药物分类
        JSONArray categories=object.getJSONArray("categories");
        List<DrugCategory> drugCategoryList=new ArrayList<>();
        if (categories!=null&&categories.size()>0){
            List<MeshCategory> meshCategoryList=new ArrayList<>();
            for (int i=0;i<categories.size();i++){
                MeshCategory meshCategory=categories.getObject(i,MeshCategory.class);
                meshCategory.setMeshCategoryKey(PkGenerator.generator(MeshCategory.class));
                meshCategory.setCreatedAt(System.currentTimeMillis());
                meshCategory.setCreatedWay(2);
                meshCategory.setCheckState(1);
                meshCategoryList.add(meshCategory);
            }
            meshCategoryList=meshCategoryService.saveList(meshCategoryList);
            if (meshCategoryList!=null&&meshCategoryList.size()>0){
                for (MeshCategory meshCategory:meshCategoryList){
                    if (meshCategory!=null){
                        DrugCategory drugCategory=new DrugCategory();
                        drugCategory.setDrugKey(drug.getDrugKey());
                        drugCategory.setDrugId(drug.getDrugId());
                        drugCategory.setMeshId(meshCategory.getMeshId());
                        drugCategory.setMeshCategoryKey(meshCategory.getMeshCategoryKey());
                        drugCategory.setCategoryName(meshCategory.getCategoryName());
                        drugCategoryList.add(drugCategory);
                    }
                }
                drugCategoryRepository.save(drugCategoryList);
                cnDrugCategoryRepository.save(drugCategoryList);
            }
        }
        //14.药物序列
        JSONArray sequences=object.getJSONArray("sequences");
        if (sequences!=null&&sequences.size()>0){
            List<DrugSequence> sequenceList=new ArrayList<>(sequences.size());
            for (int i=0;i<sequences.size();i++){
                DrugSequence sequence=new DrugSequence();
                sequence.setSequenceKey(PkGenerator.generator(DrugSequence.class));
                sequence.setDrugId(drug.getDrugId());
                sequence.setDrugKey(drug.getDrugKey());
                sequence.setSequence(sequences.getString(i));
                sequenceList.add(sequence);
            }
            drugSequenceRepository.save(sequenceList);
            cnDrugSequenceRepository.save(sequenceList);
        }
        //15.TODO（该字段全部为空，看不到结构，暂时不做） 药物食物不良反应
        // JSONArray foodInteractions=object.getJSONArray("foodInteractions");
        logger.info("【" + CPA.DRUG.name() + "】开始插入关联的临床实验->id="+drug.getDrugId());
        Page page=new Page(cpaProperties.getClinicalTrialUrl());
        page.putParam("drugId", String.valueOf(drug.getDrugId()));
        ContentParam param=new ContentParam(CPA.CLINICAL_TRIAL, clinicalTrialService,true,drug.getDrugKey());
        MainService.childrenTreadPool.execute(new IdThread(page,param));
        return true;
    }

    @Override
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        return false;
    }

    @Override
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
}