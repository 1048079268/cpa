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
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
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
    public boolean save(JSONObject object,JSONObject cn) throws InterruptedException {
        Protein protein=object.toJavaObject(Protein.class);
        Gene gene=geneRepository.findByGeneIdAndCreateWay(protein.getGeneId(),2);
        if (gene==null){
            throw new DataException("未找到相应的基因，info->geneId="+protein.getGeneId());
        }
        return saveByDependence(object,cn,gene.getGeneKey());
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey) throws InterruptedException {
        String proteinKey=PkGenerator.generator(Protein.class);
        JsonObjectConverter<Protein> proteinConverter=(json)->{
            Protein protein=json.toJavaObject(Protein.class);
            protein.setProteinKey(proteinKey);
            protein.setCreatedAt(System.currentTimeMillis());
            protein.setGeneKey(dependenceKey);
            protein.setCreateWay(2);
            return protein;
        };
        Protein protein = proteinRepository.save(proteinConverter.convert(en));
        cnProteinRepository.save(proteinConverter.convert(cn));
        if (protein==null){
            throw new DataException("保存主表失败->id="+en.getString("id"));
        }
        //3.别名
        String synonymKey=PkGenerator.generator(ProteinSynonym.class);
        JsonArrayConverter<ProteinSynonym> synonymConverter=(json)->{
            JSONArray synonyms=json.getJSONArray("synonyms");
            List<ProteinSynonym> synonymList=new ArrayList<>();
            if (synonyms!=null&&synonyms.size()>0){
                for (int i=0;i<synonyms.size();i++){
                    ProteinSynonym synonym=new ProteinSynonym();
                    synonym.setProteinSynonymKey(PkGenerator.md5(synonymKey+i));
                    synonym.setProteinId(protein.getProteinId());
                    synonym.setProteinKey(protein.getProteinKey());
                    synonym.setSynonym(synonyms.getString(i));
                    synonymList.add(synonym);
                }
            }
            return synonymList;
        };
        proteinSynonymRepository.save(synonymConverter.convert(en));
        cnProteinSynonymRepository.save(synonymConverter.convert(cn));
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
