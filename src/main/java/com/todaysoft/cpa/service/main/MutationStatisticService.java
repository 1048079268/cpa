package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.variants.CnVariantMutationStatisticRepository;
import com.todaysoft.cpa.domain.entity.Cancer;
import com.todaysoft.cpa.domain.en.cacer.CancerRepository;
import com.todaysoft.cpa.domain.en.variants.VariantMutationStatisticRepository;
import com.todaysoft.cpa.domain.en.variants.VariantRepository;
import com.todaysoft.cpa.domain.entity.Variant;
import com.todaysoft.cpa.domain.entity.VariantMutationStatistic;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 14:24
 */
@Service
public class MutationStatisticService extends BaseService {
    @Autowired
    private VariantMutationStatisticRepository variantMutationStatisticRepository;
    @Autowired
    private CnVariantMutationStatisticRepository cnVariantMutationStatisticRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private CancerRepository cancerRepository;
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public boolean save(JSONObject object) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        Variant variant=variantRepository.findByCosmicIdAndCreatedWay(statistic.getMutationId(),2);
        if (variant==null){
            throw new DataException("未找到相应的突变，info->cosmicId="+statistic.getMutationId());
        }
        return saveByDependence(object,variant.getVariantKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object, String dependenceKey) {
        VariantMutationStatistic statistic=object.toJavaObject(VariantMutationStatistic.class);
        if (statistic.getDoid()==null){
            return false;
        }
        Cancer cancer=cancerRepository.findByDoid(String.valueOf(statistic.getDoid()));
        if (cancer!=null&&!StringUtils.isEmpty(cancer.getCancerKey())){
                statistic.setCancerKey(cancer.getCancerKey());
                statistic.setVariantKey(dependenceKey);
                variantMutationStatisticRepository.save(statistic);
                cnVariantMutationStatisticRepository.save(statistic);
                return true;
        }else {
            throw new DataException("找不到相应的疾病：-->comsic:"+statistic.getMutationId()+",doid:"+statistic.getDoid());
        }
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.MUTATION_STATISTICS.name=cpaProperties.getMutationStatisticsName();
        CPA.MUTATION_STATISTICS.contentUrl=cpaProperties.getMutationStatisticsUrl();
        CPA.MUTATION_STATISTICS.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getMutationStatisticsTempPath());
        variantMutationStatisticRepository.findAll().stream().forEach(variantMutationStatistic -> {
            String id=variantMutationStatistic.getDoid()+"-"+variantMutationStatistic.getMutationId();
            CPA.MUTATION_STATISTICS.dbId.add(id);
        });
    }
}
