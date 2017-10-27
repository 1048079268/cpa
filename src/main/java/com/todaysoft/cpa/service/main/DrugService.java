package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSON;
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
    public boolean save(JSONObject en,JSONObject cn) throws InterruptedException {
        //1.解析药物基本信息
        String drugKey=PkGenerator.generator(Drug.class);
        Drug checkDrugEn=en.toJavaObject(Drug.class);
        String enName=checkDrugEn.getNameEn();
        checkDrugEn = cnDrugRepository.findByName(enName);
        if (checkDrugEn!=null){
            drugKey=checkDrugEn.getDrugKey();
        }
        String finalDrugKey = drugKey;
        JsonObjectConverter<Drug> drugConverter=(json)->{
            Drug drug=json.toJavaObject(Drug.class);
            drug.setDrugKey(finalDrugKey);
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
            return drug;
        };
        Drug drugEn=drugConverter.convert(en);
        Drug drugCn=drugConverter.convert(cn);
        drugEn.setNameChinese(drugCn.getNameEn());
        drugCn.setNameChinese(drugCn.getNameEn());
        drugCn.setNameEn(drugEn.getNameEn());
        Drug drug=drugRepository.save(drugEn);
        cnDrugRepository.save(drugCn);
        if (drug==null){
            throw new DataException("保存主表失败->id="+en.getString("id"));
        }
        //remove 2.判断插入是否成功
        //3.药物别名
        String synonymKey=PkGenerator.generator(DrugSynonym.class);
        Map<Integer,String> synonymKeys=new HashMap<>();
        JsonArrayLangConverter<DrugSynonym> synonymConverter=(json,lang)->{
            JSONArray synonyms=json.getJSONArray("synonyms");
            List<DrugSynonym> synonymList=new ArrayList<>();
            if (synonyms!=null&&synonyms.size() > 0){
                for (int i=0;i<synonyms.size();i++){
                    DrugSynonym synonym=new DrugSynonym();
                    synonym.setSynonymKey(PkGenerator.md5(synonymKey+i));
                    /**
                     * 不规范，暂时这样
                     */
                    if (lang==1){
                        if (synonymKeys.containsKey(i)){
                            synonym.setSynonymKey(synonymKeys.get(i));
                        }
                    }
                    if (lang==2){
                        DrugSynonym drugSynonym = cnDrugSynonymRepository.findByDrugKeyAndSynonym(drug.getDrugKey(), synonyms.getString(i));
                        if (drugSynonym!=null){
                            synonymKeys.put(i,drugSynonym.getSynonymKey());
                            continue;
                        }
                    }
                    synonym.setDrugId(drug.getDrugId());
                    synonym.setDrugKey(drug.getDrugKey());
                    synonym.setDrugSynonym(synonyms.getString(i));
                    synonymList.add(synonym);
                }
            }
            return synonymList;
        };
        cnDrugSynonymRepository.save(synonymConverter.convert(cn,2));
        drugSynonymRepository.save(synonymConverter.convert(en,1));
        //4.药物外部id
        String externalIdKey=PkGenerator.generator(DrugSynonym.class);
        JsonArrayConverter<DrugExternalId> externalIdConverter=(json)->{
            JSONArray externalIds=json.getJSONArray("externalIds");
            List<DrugExternalId> externalIdList=new ArrayList<>();
            if (externalIds!=null&&externalIds.size() > 0){
                for (int i=0;i<externalIds.size();i++){
                    DrugExternalId externalId=externalIds.getJSONObject(i).toJavaObject(DrugExternalId.class);
                    externalId.setExternalIdKey(PkGenerator.md5(externalIdKey+i));
                    externalId.setDrugId(drug.getDrugId());
                    externalId.setDrugKey(drug.getDrugKey());
                    externalIdList.add(externalId);
                }
            }
            return externalIdList;
        };
        drugExternalIdRepository.save(externalIdConverter.convert(en));
        cnDrugExternalIdRepository.save(externalIdConverter.convert(cn));
        //5.药物其他名称
        String otherNameKey=PkGenerator.generator(DrugOtherName.class);
        JsonArrayConverter<DrugOtherName> otherNameConverter=(json)->{
            JSONArray otherNames=json.getJSONArray("otherNames");
            List<DrugOtherName> otherNameList=new ArrayList<>();
            if (otherNames!=null&&otherNames.size()>0){
                for (int i=0;i<otherNames.size();i++){
                    DrugOtherName otherName=new DrugOtherName();
                    otherName.setOtherNameKey(PkGenerator.md5(otherNameKey+i));
                    otherName.setDrugId(drug.getDrugId());
                    otherName.setDrugKey(drug.getDrugKey());
                    otherName.setOtherName(otherNames.getString(i));
                    otherNameList.add(otherName);
                }
                drugOtherNameRepository.save(otherNameList);
                cnDrugOtherNameRepository.save(otherNameList);
            }
            return otherNameList;
        };
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
        String keggPathwayKey=PkGenerator.generator(KeggPathway.class);
        JsonArrayConverter<DrugKeggPathway> pathwayConverter=(json)->{
            List<DrugKeggPathway> pathwayList=new ArrayList<>();
            JSONArray keggPathways=json.getJSONArray("keggPathways");
            if (keggPathways!=null&&keggPathways.size()>0){
                List<KeggPathway> keggPathwayList=new ArrayList<>();
                for (int i=0;i<keggPathways.size();i++){
                    String keggId=keggPathways.getJSONObject(i).getString("id");
                    KeggPathway pathway = new KeggPathway();
                    pathway.setCreateAt(System.currentTimeMillis());
                    pathway.setCreateWay(2);
                    pathway.setCheckState(1);
                    pathway.setPathwayKey(PkGenerator.md5(keggPathwayKey+i));
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
                }
            }
            return pathwayList;
        };
        drugKeggPathwayRepository.save(pathwayConverter.convert(en));
        cnDrugKeggPathwayRepository.save(pathwayConverter.convert(cn));
        //8.药物结构化适应症
        String indicationKey=PkGenerator.generator(Indication.class);
        JsonArrayConverter<DrugStructuredIndication> indicationConverter=(json)->{
            JSONArray structuredIndications=json.getJSONArray("structuredIndications");
            List<DrugStructuredIndication> structuredIndicationList=new ArrayList<>();
            if (structuredIndications!=null&&structuredIndications.size()>0){
                List<Indication> indicationList=new ArrayList<>();
                for (int i=0;i<structuredIndications.size();i++){
                    Indication indication=new Indication();
                    indication.setCheckState(1);
                    indication.setCreatedAt(System.currentTimeMillis());
                    indication.setCreatedWay(2);
                    indication.setIndicationKey(PkGenerator.md5(indicationKey+i));
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
                }
            }
            return structuredIndicationList;
        };
        drugStructuredIndicationRepository.save(indicationConverter.convert(en));
        cnDrugStructuredIndicationRepository.save(indicationConverter.convert(cn));
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
        String sideEffectKey=PkGenerator.generator(SideEffect.class);
        JsonArrayConverter<DrugAdverseReaction> adverseReactionConverter=(json)->{
            JSONArray adverseReactions=json.getJSONArray("adverseReactions");
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
                    sideEffect.setSideEffectKey(PkGenerator.md5(sideEffectKey+i));
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
                }
            }
            return adverseReactionsList;
        };
        cnDrugAdverseReactionRepository.save(adverseReactionConverter.convert(cn));
        drugAdverseReactionRepository.save(adverseReactionConverter.convert(en));
        //11.药物相互作用
        String interactionKey=PkGenerator.generator(DrugInteraction.class);
        Set<Integer> interactionId=new HashSet<>();
        JsonArrayConverter<DrugInteraction> interactionConverter=(json)->{
            List<DrugInteraction> interactionList=new ArrayList<>();
            JSONArray interactions=json.getJSONArray("interactions");
            if (interactions!=null&&interactions.size()>0){
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
        JSONArray productsCn=en.getJSONArray("products");
        if (productsEn!=null&&productsEn.size()>0){
            List<DrugProduct> productList=new ArrayList<>();
            for (int i=0;i<productsEn.size();i++){
                String productKey=PkGenerator.generator(DrugProduct.class);
                JsonObjectConverter<DrugProduct> productConverter=(json)->{
                    DrugProduct product=json.toJavaObject(DrugProduct.class);
                    product.setProductKey(productKey);
                    product.setDrugKey(drug.getDrugKey());
                    product.setDrugId(drug.getDrugId());
                    String marketingEnd=json.getString("marketingEnd");
                    String marketingStart=json.getString("marketingStart");
                    product.setMarketingEnd(DateUtil.stringToTimestamp(marketingEnd));
                    product.setMarketingStart(DateUtil.stringToTimestamp(marketingStart));
                    product.setCreatedAt(System.currentTimeMillis());
                    product.setCheckState(1);
                    product.setCreateWay(2);
                    return product;
                };
                DrugProduct product=drugProductRepository.save(productConverter.convert(productsEn.getJSONObject(i)));
                cnDrugProductRepository.save(productConverter.convert(productsCn.getJSONObject(i)));
                //12.1 药品外部id
                String etnIdKey=PkGenerator.generator(DrugProductEtnId.class);
                JsonObjectConverter<DrugProductEtnId> etnIdConverter=(json)->{
                    JSONObject productEtnIds=json.getJSONObject("externalIds");
                    if (productEtnIds!=null){
                        DrugProductEtnId productEtnId=productEtnIds.toJavaObject(DrugProductEtnId.class);
                        productEtnId.setEtnIdKey(etnIdKey);
                        productEtnId.setProductKey(product.getProductKey());
                        return productEtnId;
                    }
                    return null;
                };
                DrugProductEtnId etnIdEn = etnIdConverter.convert(productsEn.getJSONObject(i));
                if (etnIdEn!=null){
                    drugProductEtnIdRepository.save(etnIdEn);
                }
                DrugProductEtnId etnIdCn = etnIdConverter.convert(productsCn.getJSONObject(i));
                if (etnIdCn!=null){
                    cnDrugProductEtnIdRepository.save(etnIdCn);
                }
            }
        }
        //13.药物分类
        String meshCategoryKey=PkGenerator.generator(MeshCategory.class);
        JsonArrayConverter<DrugCategory> categoryConverter=(json)->{
            JSONArray categories=json.getJSONArray("categories");
            List<DrugCategory> drugCategoryList=new ArrayList<>();
            if (categories!=null&&categories.size()>0){
                List<MeshCategory> meshCategoryList=new ArrayList<>();
                for (int i=0;i<categories.size();i++){
                    MeshCategory meshCategory=categories.getObject(i,MeshCategory.class);
                    meshCategory.setMeshCategoryKey(PkGenerator.md5(meshCategoryKey+i));
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
                }
            }
            return drugCategoryList;
        };
        drugCategoryRepository.save(categoryConverter.convert(en));
        cnDrugCategoryRepository.save(categoryConverter.convert(cn));
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
        logger.info("【" + CPA.DRUG.name() + "】开始插入关联的临床实验->id="+drug.getDrugId());
        Page page=new Page(cpaProperties.getClinicalTrialUrl());
        page.putParam("drugId", String.valueOf(drug.getDrugId()));
        ContentParam param=new ContentParam(CPA.CLINICAL_TRIAL, clinicalTrialService,true,drug.getDrugKey());
        MainService.childrenTreadPool.execute(new IdThread(page,param));
        return true;
    }

    @Override
    public boolean saveByDependence(JSONObject object, JSONObject cn, String dependenceKey) {
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
