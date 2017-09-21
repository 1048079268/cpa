package com.todaysoft.cpa.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import com.todaysoft.cpa.domain.gene.GeneRepository;
import com.todaysoft.cpa.domain.gene.entity.Gene;
import com.todaysoft.cpa.domain.variants.VariantExternalIdRepository;
import com.todaysoft.cpa.domain.variants.VariantRepository;
import com.todaysoft.cpa.domain.variants.VariantTumorTypeDoidRepository;
import com.todaysoft.cpa.domain.variants.VariantTumorTypeRepository;
import com.todaysoft.cpa.domain.variants.entity.Variant;
import com.todaysoft.cpa.domain.variants.entity.VariantTumorType;
import com.todaysoft.cpa.domain.variants.entity.VariantTumorTypeDoid;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.thread.MutationStatisticThread;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date: 2017/8/31 9:58
 */
@Service
public class VariantService implements BaseService{
    private static Logger logger= LoggerFactory.getLogger(VariantService.class);
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private VariantTumorTypeRepository variantTumorTypeRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private VariantTumorTypeDoidRepository variantTumorTypeDoidRepository;
    @Autowired
    private MutationStatisticService mutationStatisticService;
    @Autowired
    private CancerService cancerService;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private  EvidenceService evidenceService;
    @Autowired
    private GeneRepository geneRepository;

    @Override
    public boolean save(JSONObject object) {
        Variant variant = JSONObject.toJavaObject(object, Variant.class);
        Gene gene=geneRepository.findByGeneIdAndCreateWay(variant.getGeneId(),2);
        if(gene==null){
            throw new DataException("未找到相应的基因，info->geneId="+variant.getGeneId());
        }
        return saveByDependence(object,gene.getGeneKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        Variant variant = JSONObject.toJavaObject(object, Variant.class);
        variant.setVariantKey(PkGenerator.generator(Variant.class));
        variant.setGeneKey(dependenceKey);
        variant.setCreatedWay(2);
        variant.setCheckState(1);
        variant.setCreatedAt(System.currentTimeMillis());
        variant=variantRepository.save(variant);
        if (variant!=null){
            JSONArray tumorTypes=object.getJSONArray("tumorTypes");
            List<VariantTumorTypeDoid> doidList=new ArrayList<>();
            if (tumorTypes!=null&&tumorTypes.size()>0) {
                for (int i=0;i<tumorTypes.size();i++){
                    JSONObject tumorType=tumorTypes.getJSONObject(i);
                    if (tumorType!=null){
                        VariantTumorType variantTumorType=tumorType.toJavaObject(VariantTumorType.class);
                        variantTumorType.setTypeKey(PkGenerator.generator(VariantTumorType.class));
                        variantTumorType.setVariantId(variant.getVariantId());
                        variantTumorType.setVariantKey(variant.getVariantKey());
                        variantTumorType=variantTumorTypeRepository.save(variantTumorType);
                        JSONObject doid=tumorType.getJSONObject("doid");
                        if (variantTumorType!=null&&doid!=null){
                            String cancerDoid=doid.getString("id");
                            Cancer cancer = cancerRepository.findByDoid(cancerDoid);
                            if (cancer==null){
                                throw new DataException("未找到相应疾病，info->doid="+cancerDoid);
                            }
                            VariantTumorTypeDoid tumorTypeDoid=new VariantTumorTypeDoid();
                            tumorTypeDoid.setName(cancer.getCancerName());
                            tumorTypeDoid.setDoid(Integer.valueOf(cancer.getDoid()));
                            tumorTypeDoid.setTypeKey(variantTumorType.getTypeKey());
                            tumorTypeDoid.setVariantId(variant.getVariantId());
                            tumorTypeDoid.setCancerKey(cancer.getCancerKey());
                            doidList.add(tumorTypeDoid);
                        }
                    }
                }
            }
            variantTumorTypeDoidRepository.save(doidList);
            if (!StringUtils.isEmpty(variant.getCosmicId())){
                logger.info("【" + CPA.VARIANT.name() + "】开始插入关联的突变疾病样本量");
                Page msPage=new Page(CPA.MUTATION_STATISTICS.contentUrl);
                msPage.putParam("cosmicId",variant.getCosmicId());
                ContentParam msParam=new ContentParam(CPA.MUTATION_STATISTICS,mutationStatisticService,true,variant.getVariantKey());
                MainService.childrenTreadPool.execute(new MutationStatisticThread(msPage,msParam));
            }
            //插入与该id关联的证据
            logger.info("【" + CPA.VARIANT.name() + "】开始插入关联的证据");
            Page evidencePage=new Page(CPA.VARIANT.contentUrl+"/"+variant.getVariantId()+"/"+CPA.EVIDENCE.name+"s");
            ContentParam evidenceParam=new ContentParam(CPA.EVIDENCE,evidenceService,true,variant.getVariantKey());
            MainService.childrenTreadPool.execute(new IdThread(evidencePage,evidenceParam));
        }
        return true;
    }

    @Override
    public void initDB() {
        CPA.VARIANT.name=cpaProperties.getVariantName();
        CPA.VARIANT.contentUrl=cpaProperties.getVariantUrl();
        Set<Integer> ids=variantRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.VARIANT.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
