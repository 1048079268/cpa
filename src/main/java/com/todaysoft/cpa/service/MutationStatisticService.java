package com.todaysoft.cpa.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cacer.Cancer;
import com.todaysoft.cpa.domain.cacer.CancerRepository;
import com.todaysoft.cpa.domain.variants.VariantMutationStatisticRepository;
import com.todaysoft.cpa.domain.variants.VariantRepository;
import com.todaysoft.cpa.domain.variants.entity.Variant;
import com.todaysoft.cpa.domain.variants.entity.VariantMutationStatistic;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:24
 */
@Service
public class MutationStatisticService implements BaseService{
    @Autowired
    private VariantMutationStatisticRepository variantMutationStatisticRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public void save(JSONObject object) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        Cancer cancer=cancerRepository.findByDoid(String.valueOf(statistic.getDoid()));
        Variant variant=variantRepository.findByCosmicId(statistic.getMutationId());
        boolean aFlag=cancer!=null&&variant!=null;
        if (aFlag){
            boolean bFlag= !StringUtils.isEmpty(cancer.getCancerKey())&&!StringUtils.isEmpty(variant.getVariantKey());
            if (bFlag){
                statistic.setCancerKey(cancer.getCancerKey());
                statistic.setVariantKey(variant.getVariantKey());
                variantMutationStatisticRepository.save(statistic);
            }
        }
    }

    @Override
    public void saveByDependence(JSONObject object, String dependenceKey) {}

    @Override
    public void initDB() {
        CPA.MUTATION_STATISTICS.name=cpaProperties.getMutationStatisticsName();
        CPA.MUTATION_STATISTICS.contentUrl=cpaProperties.getMutationStatisticsUrl();
        variantMutationStatisticRepository.findAll().stream().forEach(variantMutationStatistic -> {
            String id=variantMutationStatistic.getDoid()+"-"+variantMutationStatistic.getMutationId();
            CPA.MUTATION_STATISTICS.dbId.add(id);
        });
    }
}
