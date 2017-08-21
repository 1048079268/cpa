package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.domain.gene.*;
import com.todaysoft.cpa.domain.gene.entity.*;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.thread.IdThread;
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
 * @date: 2017/8/14 11:21
 */
@Service
public class GeneService implements BaseService {
    private static Logger logger = LoggerFactory.getLogger(GeneService.class);
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private GeneAliasRepository geneAliasRepository;
    @Autowired
    private GeneExternalIdRepository geneExternalIdRepository;
    @Autowired
    private GeneLocationRepository geneLocationRepository;
    @Autowired
    private GeneOtherNameRepository geneOtherNameRepository;
    @Autowired
    private ProteinService proteinService;
    @Autowired
    private CPAProperties cpaProperties;

    @Override
    public void save(JSONObject object) {
        //1.基因
        Gene gene = object.toJavaObject(Gene.class);
        gene.setGeneKey(PkGenerator.generator(Gene.class));
        gene.setCreateAt(System.currentTimeMillis());
        gene.setCreateWay(2);
        gene = geneRepository.save(gene);
        if (gene != null) {
            //2.基因别名
            JSONArray aliases = object.getJSONArray("aliases");
            if (aliases != null && aliases.size() > 0) {
                List<GeneAlias> aliasList = new ArrayList<>(aliases.size());
                for (int i = 0; i < aliases.size(); i++) {
                    GeneAlias alias = new GeneAlias();
                    alias.setGeneAliasKey(PkGenerator.generator(GeneAlias.class));
                    alias.setGeneId(gene.getGeneId());
                    alias.setGeneKey(gene.getGeneKey());
                    alias.setGeneAlias(aliases.getString(i));
                    aliasList.add(alias);
                }
                geneAliasRepository.save(aliasList);
            }
            //3.基因外部id
            JSONArray externalIds = object.getJSONArray("externalIds");
            if (externalIds != null && externalIds.size() > 0) {
                List<GeneExternalId> externalIdsList = new ArrayList<>(externalIds.size());
                for (int i = 0; i < externalIds.size(); i++) {
                    GeneExternalId externalId = externalIds.getObject(i, GeneExternalId.class);
                    externalId.setGeneExternalIdKey(PkGenerator.generator(GeneExternalId.class));
                    externalId.setGeneId(gene.getGeneId());
                    externalId.setGeneKey(gene.getGeneKey());
                    externalIdsList.add(externalId);
                }
                geneExternalIdRepository.save(externalIdsList);
            }
            //4.基因位置
            JSONArray locations = object.getJSONArray("locations");
            if (locations != null && locations.size() > 0) {
                List<GeneLocation> locationList = new ArrayList<>(locations.size());
                for (int i = 0; i < locations.size(); i++) {
                    GeneLocation location = locations.getObject(i, GeneLocation.class);
                    location.setGeneLocationKey(PkGenerator.generator(GeneLocation.class));
                    location.setGeneKey(gene.getGeneKey());
                    location.setGeneId(gene.getGeneId());
                    locationList.add(location);
                }
                geneLocationRepository.save(locationList);
            }
            //5.基因其他名字
            JSONArray otherNames = object.getJSONArray("otherNames");
            if (otherNames != null && otherNames.size() > 0) {
                List<GeneOtherName> otherNameList = new ArrayList<>(otherNames.size());
                for (int i = 0; i < otherNames.size(); i++) {
                    GeneOtherName otherName = new GeneOtherName();
                    otherName.setGeneOtherNameKey(PkGenerator.generator(GeneOtherName.class));
                    otherName.setGeneId(gene.getGeneId());
                    otherName.setGeneKey(gene.getGeneKey());
                    otherName.setOtherName(otherNames.getString(i));
                    otherNameList.add(otherName);
                }
                geneOtherNameRepository.save(otherNameList);
            }
            //END:全部插入完成，将id保存到相应集合
            logger.info("【" + CPA.GENE.name() + "】插入数据库成功,id=" + gene.getGeneId());
            CPA.GENE.dbId.add(String.valueOf(gene.getGeneId()));
            //插入与该id关联的蛋白质
            logger.info("【" + CPA.GENE.name() + "】开始插入关联的蛋白质");
            Page page=new Page(CPA.GENE.contentUrl+"/"+gene.getGeneId()+"/"+CPA.PROTEIN.name+"s");
            ContentParam param=new ContentParam(CPA.PROTEIN,proteinService,true,gene.getGeneKey());
            new Thread(new IdThread(page,param)).start();
        }
    }

    @Override
    public void saveByDependence (JSONObject object, String dependenceKey){}

    @Override
    public void initDB () {
        CPA.GENE.name=cpaProperties.getGeneName();
        CPA.GENE.contentUrl=cpaProperties.getGeneUrl();
        Set<Integer> ids=geneRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.GENE.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
