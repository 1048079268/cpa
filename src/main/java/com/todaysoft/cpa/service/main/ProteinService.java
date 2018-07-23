package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.proteins.CnProteinRepository;
import com.todaysoft.cpa.domain.en.gene.GeneRepository;
import com.todaysoft.cpa.domain.entity.Gene;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.domain.en.proteins.ProteinRepository;
import com.todaysoft.cpa.domain.entity.Protein;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.JsonUtil;
import com.todaysoft.cpa.utils.PkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.Iterator;
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
    private ProteinRepository proteinRepository;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private KbUpdateService kbUpdateService;

    @Override
    public boolean save(JSONObject object,JSONObject cn,int status) throws InterruptedException {
        Protein protein=object.toJavaObject(Protein.class);
        Gene gene=geneRepository.findByGeneIdAndCreateWay(protein.getGeneId(),2);
        if (gene==null){
            throw new DataException("未找到相应的基因，info->geneId="+protein.getGeneId());
        }
        return saveByDependence(object,cn,gene.getGeneKey(),status);
    }

    @Override
    @Transactional
    public boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey,int status) throws InterruptedException {
        Protein object = en.toJavaObject(Protein.class);
        Protein old = proteinRepository.findByProteinId(object.getProteinId());
        boolean isSaveCn= old==null;
        boolean isUseOldState= old!=null;
        String proteinKey=old==null?PkGenerator.generator(Protein.class):old.getProteinKey();
        JsonObjectConverter<Protein> proteinConverter=(json)->{
            Protein protein=json.toJavaObject(Protein.class);
            protein.setProteinKey(proteinKey);
            protein.setOtherNames(JsonUtil.jsonArrayToString(json.getJSONArray("synonyms"),"<=>"));
            protein.setCreatedAt(System.currentTimeMillis());
            protein.setGeneKey(dependenceKey);
            if (isUseOldState){
                protein.setCreateWay(old.getCreateWay());
                protein.setCheckState(old.getCheckState());
                protein.setCreatedByName(old.getCreatedByName());
            }else {
                protein.setCreateWay(2);
                protein.setCheckState(1);
                protein.setCreatedByName("CPA");
            }
            return protein;
        };
        Protein protein = proteinRepository.save(proteinConverter.convert(en));
        if (isSaveCn){
            cnProteinRepository.save(proteinConverter.convert(cn));
        }
        if (protein.getCheckState()==1){
            kbUpdateService.send("kt_protein");
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
