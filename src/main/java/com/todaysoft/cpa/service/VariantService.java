package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import com.todaysoft.cpa.domain.variants.VariantExternalIdRepository;
import com.todaysoft.cpa.domain.variants.VariantRepository;
import com.todaysoft.cpa.domain.variants.VariantTumorTypeDoidRepository;
import com.todaysoft.cpa.domain.variants.VariantTumorTypeRepository;
import com.todaysoft.cpa.domain.variants.entity.Variant;
import com.todaysoft.cpa.domain.variants.entity.VariantTumorType;
import com.todaysoft.cpa.domain.variants.entity.VariantTumorTypeDoid;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CancerRepository cancerRepository;

    @Override
    public void save(JSONObject object) {}

    @Override
    @Transactional
    public void saveByDependence(JSONObject object, String dependenceKey) {
        Variant variant = JSONObject.toJavaObject(object, Variant.class);
        variant.setVariantKey(PkGenerator.generator(Variant.class));
        variant.setGeneKey(dependenceKey);
        variant.setCreatedWay(2);
        variant.setCreatedAt(System.currentTimeMillis());
        variant=variantRepository.save(variant);
        if (variant!=null){
            JSONArray tumorTypes=object.getJSONArray("tumorTypes");
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
                            VariantTumorTypeDoid tumorTypeDoid=doid.toJavaObject(VariantTumorTypeDoid.class);
                            tumorTypeDoid.setTypeKey(variantTumorType.getTypeKey());
                            tumorTypeDoid.setVariantId(variant.getVariantId());
                            //TODO 待定
                            List<Cancer> cancerList = cancerRepository.findByDoid(String.valueOf(tumorTypeDoid.getDoid()));
                            if (cancerList!=null&&cancerList.size()>0){
                                tumorTypeDoid.setCancerKey(cancerList.get(0).getCancerKey());
                            }else {
                                throw  new DataException("未找到关联的doid");
                            }
                            variantTumorTypeDoidRepository.save(tumorTypeDoid);
                        }
                    }
                }
            }
        }
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
