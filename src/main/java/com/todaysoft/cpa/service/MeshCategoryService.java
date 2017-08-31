package com.todaysoft.cpa.service;

import com.todaysoft.cpa.domain.drug.MeshCategoryRepository;
import com.todaysoft.cpa.domain.drug.entity.KeggPathway;
import com.todaysoft.cpa.domain.drug.entity.MeshCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private static final Map<String,MeshCategory> MESH_ID=new HashMap<>();
    @Autowired
    private MeshCategoryRepository meshCategoryRepository;

    public void init(){
        meshCategoryRepository.findIdByCPA().stream().forEach(meshCategory->{
            MESH_ID.put(meshCategory.getMeshId(),meshCategory);
        });
    }
    public MeshCategory save(MeshCategory meshCategory){
        lock.lock();
        try {
            if (MESH_ID.containsKey(meshCategory.getMeshId())){
                return MESH_ID.get(meshCategory.getMeshId());
            }
            MeshCategory ret=meshCategoryRepository.save(meshCategory);
            MESH_ID.put(ret.getMeshId(),ret);
            return ret;
        }finally {
            lock.unlock();
        }
    }
}
