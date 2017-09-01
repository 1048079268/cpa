package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.domain.drug.*;
import com.todaysoft.cpa.domain.drug.entity.*;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.JsonUtil;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/8 12:07
 */
@Service
public class DrugService implements BaseService {
    private static Logger logger= LoggerFactory.getLogger(DrugService.class);
    @Autowired
    private CPAProperties cpaProperties;
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

    @Override
    @Transactional
    public void save(JSONObject object) {
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
        //2.判断插入是否成功
        if (drug!=null){
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
            }
            //7.药物通路
            List<DrugKeggPathway> pathwayList=new ArrayList<>();
            JSONArray keggPathways=object.getJSONArray("keggPathways");
            if (keggPathways!=null&&keggPathways.size()>0){
                for (int i=0;i<keggPathways.size();i++){
                    String keggId=keggPathways.getJSONObject(i).getString("id");
                    KeggPathway pathway = new KeggPathway();
                    pathway.setCreateAt(System.currentTimeMillis());
                    pathway.setCreateWay(2);
                    pathway.setCheckState(1);
                    pathway.setPathwayKey(PkGenerator.generator(KeggPathway.class));
                    pathway.setKeggId(keggId.trim());
                    pathway.setPathwayName(keggPathways.getJSONObject(i).getString("name"));
                    pathway=keggPathwaysService.save(pathway);
                    DrugKeggPathway drugKeggPathway=new DrugKeggPathway();
                    drugKeggPathway.setPathwayKey(pathway.getPathwayKey());
                    drugKeggPathway.setDrugKey(drug.getDrugKey());
                    drugKeggPathway.setKeggId(pathway.getKeggId());
                    drugKeggPathway.setDrugId(drug.getDrugId());
                    drugKeggPathway.setPathwayName(pathway.getPathwayName());
                    pathwayList.add(drugKeggPathway);
                }
            }
            //TODO 8.药物结构化适应症
            JSONArray structuredIndications=object.getJSONArray("structuredIndications");
            if (structuredIndications!=null&&structuredIndications.size()>0){
                List<DrugStructuredIndication> structuredIndicationList=new ArrayList<>(structuredIndications.size());
                for (int i=0;i<structuredIndications.size();i++){
//                    DrugStructuredIndication structuredIndication=new DrugStructuredIndication();
//                    structuredIndication.setDrugId(drug.getDrugId());
//                    structuredIndication.setDrugKey(drug.getDrugKey());
//                    structuredIndication.setIndicationKey(PkGenerator.generator(DrugStructuredIndication.class));
//                    structuredIndication.setStructuredIndication(structuredIndications.getString(i));
//                    structuredIndicationList.add(structuredIndication);
                }
                drugStructuredIndicationRepository.save(structuredIndicationList);
            }
            // TODO 9.药物临床实验
            JSONArray clinicalTrials=object.getJSONArray("clinicalTrials");
            if (clinicalTrials!=null&&clinicalTrials.size()>0){
            }
            //TODO 10.药物不良反应
            JSONArray adverseReactions=object.getJSONArray("adverseReactions");
            if (adverseReactions!=null&&adverseReactions.size()>0){
                List<DrugAdverseReaction> adverseReactionsList=new ArrayList<>(adverseReactions.size());
                for (int i=0;i<adverseReactions.size();i++){
                    //
//                    DrugAdverseReaction adverseReaction=adverseReactions.getJSONObject(i).toJavaObject(DrugAdverseReaction.class);
//                    adverseReaction.setDrugId(drug.getDrugId());
//                    adverseReaction.setDrugKey(drug.getDrugKey());
//                    adverseReaction.setSideEffectKey(PkGenerator.generator(DrugAdverseReaction.class));
//                    adverseReactionsList.add(adverseReaction);
                }
                drugAdverseReactionRepository.save(adverseReactionsList);
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
                    if (product!=null){
                        //12.1 药品外部id
                        JSONObject productEtnIds=products.getJSONObject(i).getJSONObject("externalIds");
                        if (productEtnIds!=null){
                            DrugProductEtnId productEtnId=productEtnIds.toJavaObject(DrugProductEtnId.class);
                            productEtnId.setEtnIdKey(PkGenerator.generator(DrugProductEtnId.class));
                            productEtnId.setProductKey(product.getProductKey());
                            drugProductEtnIdRepository.save(productEtnId);
                        }
                    }
                }
            }
            //13.药物分类
            JSONArray categories=object.getJSONArray("categories");
            List<DrugCategory> drugCategoryList=new ArrayList<>();
            if (categories!=null&&categories.size()>0){
                for (int i=0;i<categories.size();i++){
                    MeshCategory meshCategory=categories.getObject(i,MeshCategory.class);
                    meshCategory.setMeshCategoryKey(PkGenerator.generator(MeshCategory.class));
                    meshCategory.setCreatedAt(System.currentTimeMillis());
                    meshCategory.setCreatedWay(2);
                    meshCategory.setCheckState(1);
                    meshCategory=meshCategoryService.save(meshCategory);
                    DrugCategory drugCategory=new DrugCategory();
                    drugCategory.setDrugKey(drug.getDrugKey());
                    drugCategory.setDrugId(drug.getDrugId());
                    drugCategory.setMeshId(meshCategory.getMeshId());
                    drugCategory.setMeshCategoryKey(meshCategory.getMeshCategoryKey());
                    drugCategory.setCategoryName(meshCategory.getCategoryName());
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
            }
            //15.TODO（该字段全部为空，看不到结构，暂时不做） 药物食物不良反应
//            JSONArray foodInteractions=object.getJSONArray("foodInteractions");
//            if (foodInteractions!=null&&foodInteractions.size()>0){
//                List<DrugFoodInteraction> foodInteractionList=new ArrayList<>(foodInteractions.size());
//                for (int i=0;i<foodInteractions.size();i++){
//                    DrugFoodInteraction foodInteraction=new DrugFoodInteraction();
//                    foodInteraction.setFoodInteractionKey(PkGenerator.generator(DrugFoodInteraction.class));
//                    foodInteraction.setDrugId(drug.getDrugId());
//                    foodInteraction.setDrugKey(drug.getDrugKey());
//                    //foodInteraction.setFoodInteraction();//该字段还没有解析
//                    foodInteractionList.add(foodInteraction);
//                }
//                drugFoodInteractionRepository.save(foodInteractionList);
//            }
            //多对多关系表的插入
            drugKeggPathwayRepository.save(pathwayList);
            drugCategoryRepository.save(drugCategoryList);
        }
    }

    @Override
    public void saveByDependence(JSONObject object, String dependenceKey) {
    }

    @Override
    public void initDB() {
        CPA.DRUG.name=cpaProperties.getDrugName();
        CPA.DRUG.contentUrl=cpaProperties.getDrugUrl();
        Set<Integer> ids=drugRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.DRUG.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
