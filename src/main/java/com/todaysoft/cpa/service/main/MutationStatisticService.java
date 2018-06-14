package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.variants.CnVariantTumorTypeRepository;
import com.todaysoft.cpa.domain.en.variants.VariantTumorTypeRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.variants.VariantRepository;
import com.todaysoft.cpa.domain.entity.Variant;
import com.todaysoft.cpa.domain.entity.VariantMutationStatistic;
import com.todaysoft.cpa.domain.entity.VariantTumorType;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:24
 */
@Service
public class MutationStatisticService extends BaseService {
    @Autowired
    private CnVariantTumorTypeRepository cnVariantTumorTypeRepository;
    @Autowired
    private VariantTumorTypeRepository variantTumorTypeRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private KbUpdateService kbUpdateService;

    @Override
    public boolean save(JSONObject object,JSONObject cn,int status) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        String variantKey=variantRepository.findByCosmicIdAndCreatedWay(statistic.getCosmicId(),2);
        if (variantKey==null||variantKey.length()==0){
            throw new DataException("未找到相应的突变，info->cosmicId="+statistic.getCosmicId());
        }
        return saveByDependence(object,cn,variantKey,status);
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object,JSONObject cn, String dependenceKey,int status) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        if (statistic.getDoid()==null){
            return false;
        }
        List<VariantTumorType> variantTumorTypeList = variantTumorTypeRepository.findByDoidAndVariantKey(Integer.valueOf(statistic.getDoid()), dependenceKey);
        for (VariantTumorType variantTumorType : variantTumorTypeList) {
            if (variantTumorType!=null){
                variantTumorType.setNumOfSamples(statistic.getNumOfSamples());
                variantTumorTypeRepository.save(variantTumorType);
                VariantTumorType tumorType = cnVariantTumorTypeRepository.findOne(variantTumorType.getTypeKey());
                tumorType.setNumOfSamples(statistic.getNumOfSamples());
                cnVariantTumorTypeRepository.save(tumorType);
            }else {
                throw new DataException("未找到相应的突变肿瘤类型记录，info->doid="+statistic.getDoid());
            }
        }
        return true;
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.MUTATION_STATISTICS.name=cpaProperties.getMutationStatisticsName();
        CPA.MUTATION_STATISTICS.contentUrl=cpaProperties.getMutationStatisticsUrl();
        CPA.MUTATION_STATISTICS.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getMutationStatisticsTempPath());
        variantTumorTypeRepository.findMutationStatistics().forEach(variantMutationStatistic -> {
            String id=variantMutationStatistic.getDoid()+"-"+variantMutationStatistic.getCosmicId();
            CPA.MUTATION_STATISTICS.dbId.add(id);
        });
    }
}
