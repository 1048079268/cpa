package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnMeshCategoryRepository;
import com.todaysoft.cpa.domain.en.drug.MeshCategoryRepository;
import com.todaysoft.cpa.domain.entity.MeshCategory;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 13:01
 */
@Service
public class MeshCategoryService {
    private static Logger logger= LoggerFactory.getLogger(MeshCategoryService.class);
    private final Lock lock=new ReentrantLock();
    private static Map<String,MeshCategory> MESH_CATEGORY_MAP=new HashMap<>();
    private static Map<String,MeshCategory> meshCategoryMapOldDB=new HashMap<>();
    @Autowired
    private MeshCategoryRepository meshCategoryRepository;
    @Autowired
    private CnMeshCategoryRepository cnMeshCategoryRepository;

    public void init(){
        meshCategoryRepository.findByCPA().stream().forEach(meshCategory->{
            MESH_CATEGORY_MAP.put(meshCategory.getMeshId(),meshCategory);
        });
        cnMeshCategoryRepository.findByCreatedWay(3).forEach(meshCategory -> {
            meshCategoryMapOldDB.put(meshCategory.getCategoryName(),meshCategory);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MeshCategory> saveList(List<MeshCategory> meshCategoryList) throws InterruptedException {
        List<MeshCategory> resultList=new ArrayList<>();
        for (MeshCategory meshCategory:meshCategoryList){
            MeshCategory category;
            if (MESH_CATEGORY_MAP.containsKey(meshCategory.getMeshId())){
                category=MESH_CATEGORY_MAP.get(meshCategory.getMeshId());
            }else {
                category=meshCategoryRepository.save(meshCategory);
                cnMeshCategoryRepository.save(category);
                MESH_CATEGORY_MAP.put(meshCategory.getMeshId(),category);
            }
            resultList.add(category);
        }
        return resultList;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public MeshCategory save(MeshCategory cnMeshCategory,MeshCategory enMeshCategory){
        String key= PkGenerator.generator(MeshCategory.class);
        cnMeshCategory.setMeshCategoryKey(key);
        enMeshCategory.setMeshCategoryKey(key);
        //TODO 暂时屏蔽与老库合并
//        if (meshCategoryMapOldDB.containsKey(cnMeshCategory.getCategoryName())){
//            MeshCategory meshCategory = meshCategoryMapOldDB.get(cnMeshCategory.getCategoryName());
//            meshCategory.setCheckState(cnMeshCategory.getCheckState());
//            meshCategory.setCreatedWay(cnMeshCategory.getCreatedWay());
//            //该处的id与英文库保持一致
//            meshCategory.setMeshId(cnMeshCategory.getMeshId());
//            meshCategory.setCreatedAt(cnMeshCategory.getCreatedAt());
//            cnMeshCategoryRepository.save(meshCategory);
//            enMeshCategory.setMeshCategoryKey(meshCategory.getMeshCategoryKey());
//            enMeshCategory=meshCategoryRepository.save(enMeshCategory);
//            meshCategoryMapOldDB.remove(cnMeshCategory.getCategoryName());
//            MESH_CATEGORY_MAP.put(enMeshCategory.getMeshId(),enMeshCategory);
//            logger.info("【MeshCategory】与老库合并->id="+enMeshCategory.getMeshId());
//            return enMeshCategory;
//        }else {
            if (MESH_CATEGORY_MAP.containsKey(enMeshCategory.getMeshId())){
                return MESH_CATEGORY_MAP.get(enMeshCategory.getMeshId());
            }else {
                enMeshCategory=meshCategoryRepository.save(enMeshCategory);
                cnMeshCategoryRepository.save(cnMeshCategory);
                MESH_CATEGORY_MAP.put(enMeshCategory.getMeshId(),enMeshCategory);
                return enMeshCategory;
            }
//        }
    }
}
