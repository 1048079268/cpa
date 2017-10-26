package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.proteins.CnProteinRepository;
import com.todaysoft.cpa.domain.cn.proteins.CnProteinSynonymRepository;
import com.todaysoft.cpa.domain.en.gene.GeneRepository;
import com.todaysoft.cpa.domain.entity.Gene;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.domain.en.proteins.ProteinRepository;
import com.todaysoft.cpa.domain.en.proteins.ProteinSynonymRepository;
import com.todaysoft.cpa.domain.entity.Protein;
import com.todaysoft.cpa.domain.entity.ProteinSynonym;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
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
public class ProteinService extends BaseService {
    private static Logger logger= LoggerFactory.getLogger(ProteinService.class);
    @Autowired
    private CnProteinRepository cnProteinRepository;
    @Autowired
    private CnProteinSynonymRepository cnProteinSynonymRepository;
    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private ProteinSynonymRepository proteinSynonymRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private GeneRepository geneRepository;

    @Override
    public boolean save(JSONObject object,JSONObject cn) {
        Protein protein=object.toJavaObject(Protein.class);
        Gene gene=geneRepository.findByGeneIdAndCreateWay(protein.getGeneId(),2);
        if (gene==null){
            throw new DataException("未找到相应的基因，info->geneId="+protein.getGeneId());
        }
        return saveByDependence(object,cn,gene.getGeneKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject object,JSONObject cn, String dependenceKey) {
        Protein protein=object.toJavaObject(Protein.class);
        protein.setProteinKey(PkGenerator.generator(Protein.class));
        protein.setCreatedAt(System.currentTimeMillis());
        protein.setGeneKey(dependenceKey);
        protein.setCreateWay(2);
        protein=proteinRepository.save(protein);
        protein=cnProteinRepository.save(protein);
        if (protein==null){
            throw new DataException("保存主表失败->id="+object.getString("id"));
        }
        //3.别名
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
            cnProteinSynonymRepository.save(synonymList);
        }
        return true;
    }

    @Override
    public void initDB() throws FileNotFoundException {
        CPA.PROTEIN.name=cpaProperties.getProteinName();
        CPA.PROTEIN.contentUrl=cpaProperties.getProteinUrl();
        CPA.PROTEIN.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getProteinTempPath());
        Set<Integer> ids=proteinRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.PROTEIN.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
