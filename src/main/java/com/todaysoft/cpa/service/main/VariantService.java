package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.variants.CnVariantRepository;
import com.todaysoft.cpa.domain.cn.variants.CnVariantTumorTypeDoidRepository;
import com.todaysoft.cpa.domain.cn.variants.CnVariantTumorTypeRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.gene.GeneRepository;
import com.todaysoft.cpa.domain.entity.Gene;
import com.todaysoft.cpa.domain.en.variants.VariantRepository;
import com.todaysoft.cpa.domain.en.variants.VariantTumorTypeDoidRepository;
import com.todaysoft.cpa.domain.en.variants.VariantTumorTypeRepository;
import com.todaysoft.cpa.domain.entity.Variant;
import com.todaysoft.cpa.domain.entity.VariantTumorType;
import com.todaysoft.cpa.domain.entity.VariantTumorTypeDoid;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.ContentService;
import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.thread.MutationStatisticThread;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 9:58
 */
@Service
public class VariantService extends BaseService {
    private static Logger logger= LoggerFactory.getLogger(VariantService.class);
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private CnVariantTumorTypeRepository cnVariantTumorTypeRepository;
    @Autowired
    private CnVariantRepository cnVariantRepository;
    @Autowired
    private CnVariantTumorTypeDoidRepository cnVariantTumorTypeDoidRepository;
    @Autowired
    private VariantTumorTypeRepository variantTumorTypeRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private VariantTumorTypeDoidRepository variantTumorTypeDoidRepository;
    @Autowired
    private MutationStatisticService mutationStatisticService;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private  EvidenceService evidenceService;
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private ContentService contentService;

    @Override
    public boolean save(JSONObject object,JSONObject cn) {
        Variant variant = JSONObject.toJavaObject(object, Variant.class);
        Gene gene=geneRepository.findByGeneIdAndCreateWay(variant.getGeneId(),2);
        if(gene==null){
            throw new DataException("未找到相应的基因，info->geneId="+variant.getGeneId());
        }
        return saveByDependence(object,cn,gene.getGeneKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey) {
        String variantKey=PkGenerator.generator(Variant.class);
        JsonObjectConverter<Variant> variantConverter=(json)->{
            Variant variant = json.toJavaObject(Variant.class);
            variant.setVariantKey(variantKey);
            variant.setGeneKey(dependenceKey);
            variant.setCreatedWay(2);
            variant.setCheckState(1);
            variant.setCreatedAt(System.currentTimeMillis());
            return variant;
        };
        Variant variant =variantRepository.save(variantConverter.convert(en));
        cnVariantRepository.save(variantConverter.convert(cn));
        if (variant==null){
            throw new DataException("保存主表失败->id="+en.getString("id"));
        }
        JSONArray enTumorTypes=en.getJSONArray("tumorTypes");
        JSONArray cnTumorTypes=cn.getJSONArray("tumorTypes");
        if (enTumorTypes!=null&&enTumorTypes.size()>0){
            for (int i=0;i<enTumorTypes.size();i++){
                String typeKey=PkGenerator.generator(VariantTumorType.class);
                JsonObjectConverter<VariantTumorType> tumorTypeConverter=(json)->{
                    if (json==null){
                        return null;
                    }
                    VariantTumorType variantTumorType=json.toJavaObject(VariantTumorType.class);
                    variantTumorType.setTypeKey(typeKey);
                    variantTumorType.setVariantId(variant.getVariantId());
                    variantTumorType.setVariantKey(variant.getVariantKey());
                    return variantTumorType;
                };
                VariantTumorType variantTumorTypeEn = tumorTypeConverter.convert(enTumorTypes.getJSONObject(i));
                VariantTumorType variantTumorTypeCn = tumorTypeConverter.convert(cnTumorTypes.getJSONObject(i));
                if (variantTumorTypeEn!=null&&variantTumorTypeCn!=null){
                    variantTumorTypeRepository.save(variantTumorTypeEn);
                    cnVariantTumorTypeRepository.save(variantTumorTypeCn);
                }
                JsonObjectConverter<VariantTumorTypeDoid> doidConverter=(json)->{
                    if (variantTumorTypeEn!=null&&json!=null){
                        String cancerDoid=json.getString("id");
                        Cancer cancer = cancerRepository.findByDoid(cancerDoid);
                        if (cancer==null){
                            throw new DataException("未找到相应疾病，info->doid="+cancerDoid);
                        }
                        VariantTumorTypeDoid tumorTypeDoid=new VariantTumorTypeDoid();
                        tumorTypeDoid.setName(cancer.getCancerName());
                        tumorTypeDoid.setDoid(Integer.valueOf(cancer.getDoid()));
                        tumorTypeDoid.setTypeKey(variantTumorTypeEn.getTypeKey());
                        tumorTypeDoid.setVariantId(variant.getVariantId());
                        tumorTypeDoid.setCancerKey(cancer.getCancerKey());
                        return tumorTypeDoid;
                    }
                    return null;
                };
                if (enTumorTypes.getJSONObject(i)!=null){
                    VariantTumorTypeDoid doidEn = doidConverter.convert(enTumorTypes.getJSONObject(i).getJSONObject("doid"));
                    VariantTumorTypeDoid doidCn = doidConverter.convert(enTumorTypes.getJSONObject(i).getJSONObject("doid"));
                    if (doidCn!=null&&doidEn!=null){
                        variantTumorTypeDoidRepository.save(doidEn);
                        cnVariantTumorTypeDoidRepository.save(doidCn);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(variant.getCosmicId())){
            logger.info("【" + CPA.VARIANT.name() + "】开始插入关联的突变疾病样本量");
            Page msPage=new Page(CPA.MUTATION_STATISTICS.contentUrl);
            msPage.putParam("cosmicId",variant.getCosmicId());
            ContentParam msParam=new ContentParam(CPA.MUTATION_STATISTICS,mutationStatisticService,true,variant.getVariantKey());
            MainService.childrenTreadPool.execute(new MutationStatisticThread(msPage,msParam,contentService));
        }
        //插入与该id关联的证据
        logger.info("【" + CPA.VARIANT.name() + "】开始插入关联的证据");
        Page evidencePage=new Page(CPA.VARIANT.contentUrl+"/"+variant.getVariantId()+"/"+CPA.EVIDENCE.name+"s");
        ContentParam evidenceParam=new ContentParam(CPA.EVIDENCE,evidenceService,true,variant.getVariantKey());
        MainService.childrenTreadPool.execute(new IdThread(evidencePage,evidenceParam));
        return true;
    }

    @Async
    @Override
    public void initDB() throws FileNotFoundException {
        CPA.VARIANT.name=cpaProperties.getVariantName();
        CPA.VARIANT.contentUrl=cpaProperties.getVariantUrl();
        CPA.VARIANT.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getVariantTempPath());
        Set<Integer> ids=variantRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.VARIANT.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
