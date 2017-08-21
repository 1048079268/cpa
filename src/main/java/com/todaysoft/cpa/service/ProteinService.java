package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.domain.proteins.ProteinRepository;
import com.todaysoft.cpa.domain.proteins.ProteinSynonymRepository;
import com.todaysoft.cpa.domain.proteins.entity.Protein;
import com.todaysoft.cpa.domain.proteins.entity.ProteinSynonym;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:34
 */
@Service
public class ProteinService implements BaseService {
    private static Logger logger= LoggerFactory.getLogger(ProteinService.class);
    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private ProteinSynonymRepository proteinSynonymRepository;
    @Autowired
    private CPAProperties cpaProperties;

    @Override
    public void save(JSONObject object) {}

    @Override
    public void saveByDependence(JSONObject object, String dependenceKey) {
        Protein protein=object.toJavaObject(Protein.class);
        protein.setProteinKey(PkGenerator.generator(Protein.class));
        protein.setCreatedAt(System.currentTimeMillis());
        protein.setGeneKey(dependenceKey);
        protein.setCreateWay(2);
        protein=proteinRepository.save(protein);
        if (protein!=null){
            //3.药物别名
            JSONArray synonyms=object.getJSONArray("synonyms");
            if (synonyms!=null&&synonyms.size()>0){
                List<ProteinSynonym> synonymList=new ArrayList<>(synonyms.size());
                for (int i=0;i<synonyms.size();i++){
                    ProteinSynonym synonym=new ProteinSynonym();
                    synonym.setProteinSynonymKey(PkGenerator.generator(ProteinSynonym.class));
                    synonym.setProteinId(protein.getProteinId());
                    synonym.setProteinKey(protein.getProteinKey());
                    synonym.setSynonym(synonyms.getString(i));
                    synonymList.add(synonym);
                }
                proteinSynonymRepository.save(synonymList);
            }
            //END:全部插入完成，将id保存到相应集合
            logger.info("【"+ CPA.PROTEIN.name()+"】插入数据库成功,id="+protein.getProteinId());
            CPA.PROTEIN.dbId.add(String.valueOf(protein.getProteinId()));
        }
    }

    @Override
    public void initDB() {
        CPA.PROTEIN.name=cpaProperties.getProteinName();
        CPA.PROTEIN.contentUrl=cpaProperties.getProteinUrl();
        Set<Integer> ids=proteinRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.PROTEIN.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
