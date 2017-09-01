package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.variants.VariantMutationStatisticRepository;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:24
 */
public class VariantMutationStatisticService implements BaseService{
    @Autowired
    private VariantMutationStatisticRepository variantMutationStatisticRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Override
    public void save(JSONObject object) {

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
