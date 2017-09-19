package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.MeshCategoryRepository;
import com.todaysoft.cpa.domain.drug.entity.MeshCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
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

    @Transactional
    public MeshCategory save(MeshCategory meshCategory){
        lock.lock();
        try {
            if (!MESH_CATEGORY_MAP.containsKey(meshCategory.getMeshId())){
                MeshCategory category=meshCategoryRepository.save(meshCategory);
                if (category==null){
                    System.out.println("MeshCategoryService:-->"+meshCategory.getMeshId());
                }
                MESH_CATEGORY_MAP.put(meshCategory.getMeshId(),category);
            }
            return MESH_CATEGORY_MAP.get(meshCategory.getMeshId());
        }finally {
            lock.unlock();
        }
    }
}
