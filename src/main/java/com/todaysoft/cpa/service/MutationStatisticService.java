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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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

    @Override
    public boolean save(JSONObject object) {
        return false;
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        Cancer cancer=cancerRepository.findByDoid(String.valueOf(statistic.getDoid()));
        boolean aFlag=cancer!=null;
        if (aFlag){
            boolean bFlag= !StringUtils.isEmpty(cancer.getCancerKey());
            if (bFlag){
                statistic.setCancerKey(cancer.getCancerKey());
                statistic.setVariantKey(dependenceKey);
                variantMutationStatisticRepository.save(statistic);
                return true;
            }
        }
        return false;
    }

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
