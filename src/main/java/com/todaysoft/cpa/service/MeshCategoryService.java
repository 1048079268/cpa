package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.DrugCategoryRepository;
import com.todaysoft.cpa.domain.drug.MeshCategoryRepository;
import com.todaysoft.cpa.domain.drug.entity.Drug;
import com.todaysoft.cpa.domain.drug.entity.DrugCategory;
import com.todaysoft.cpa.domain.drug.entity.DrugCategoryPK;
import com.todaysoft.cpa.domain.drug.entity.MeshCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 13:01
 */
@Service
public class MeshCategoryService {
    private final ReentrantLock lock=new ReentrantLock();
    private static Map<String,MeshCategory> MESH_CATEGORY_MAP=new HashMap<>();
    @Autowired
    private MeshCategoryRepository meshCategoryRepository;

    public void init(){
        meshCategoryRepository.findByCPA().stream().forEach(meshCategory->{
            MESH_CATEGORY_MAP.put(meshCategory.getMeshId(),meshCategory);
        });
    }

    public MeshCategory save(MeshCategory meshCategory){
        lock.lock();
        try {
            MeshCategory category;
            if (MESH_CATEGORY_MAP.containsKey(meshCategory.getMeshId())){
                category=MESH_CATEGORY_MAP.get(meshCategory.getMeshId());
            }else {
                category=meshCategoryRepository.save(meshCategory);
                MESH_CATEGORY_MAP.put(category.getMeshId(),category);
            }
            return category;
        }finally {
            lock.unlock();
        }
    }
}
