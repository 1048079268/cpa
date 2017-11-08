package com.todaysoft.cpa.service.vice;

import com.todaysoft.cpa.domain.cn.drug.CnMeshCategoryRepository;
import com.todaysoft.cpa.domain.en.drug.MeshCategoryRepository;
import com.todaysoft.cpa.domain.entity.MeshCategory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Lock lock=new ReentrantLock();
    private static Map<String,MeshCategory> MESH_CATEGORY_MAP=new HashMap<>();
    @Autowired
    private MeshCategoryRepository meshCategoryRepository;
    @Autowired
    private CnMeshCategoryRepository cnMeshCategoryRepository;

    public void init(){
        meshCategoryRepository.findByCPA().stream().forEach(meshCategory->{
            MESH_CATEGORY_MAP.put(meshCategory.getMeshId(),meshCategory);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public MeshCategory save(MeshCategory meshCategory){
        lock.lock();
        try {
            if (!MESH_CATEGORY_MAP.containsKey(meshCategory.getMeshId())){
                MeshCategory category=meshCategoryRepository.save(meshCategory);
                cnMeshCategoryRepository.save(category);
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MeshCategory> saveList(List<MeshCategory> meshCategoryList) throws InterruptedException {
        try {
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
        }finally {
        }
    }
}
