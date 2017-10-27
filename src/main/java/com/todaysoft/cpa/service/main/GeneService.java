package com.todaysoft.cpa.service.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.gene.*;
import com.todaysoft.cpa.domain.en.gene.*;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.param.CPAProperties;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.ContentParam;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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
public class GeneService extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(GeneService.class);
    @Autowired
    private CnGeneRepository cnGeneRepository;
    @Autowired
    private CnGeneAliasRepository cnGeneAliasRepository;
    @Autowired
    private CnGeneExternalIdRepository cnGeneExternalIdRepository;
    @Autowired
    private CnGeneLocationRepository cnGeneLocationRepository;
    @Autowired
    private CnGeneOtherNameRepository cnGeneOtherNameRepository;
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
    private VariantService variantService;
    @Autowired
    private CPAProperties cpaProperties;

    @Override
    public boolean save(JSONObject en,JSONObject cn) throws InterruptedException {
        //1.基因
        String geneKey=PkGenerator.generator(Gene.class);
        //查重覆盖
        Gene checkGene = en.toJavaObject(Gene.class);
        Gene byName = cnGeneRepository.findByName(checkGene.getGeneSymbol());
        if (byName!=null){
            geneKey=byName.getGeneKey();
        }
        String finalGeneKey = geneKey;
        JsonObjectConverter<Gene> geneConverter=(json)->{
            Gene gene = json.toJavaObject(Gene.class);
            gene.setGeneKey(finalGeneKey);
            gene.setCreateAt(System.currentTimeMillis());
            gene.setCreateWay(2);
            return gene;
        };
        Gene gene = geneRepository.save(geneConverter.convert(en));
        cnGeneRepository.save(geneConverter.convert(cn));
        if (gene==null){
            throw new DataException("保存主表失败->id="+en.getString("id"));
        }
        //2.基因别名
        String aliasesKey=PkGenerator.generator(GeneAlias.class);
        JsonArrayLangConverter<GeneAlias> aliasConverter=(json,lang)->{
            JSONArray aliases = json.getJSONArray("aliases");
            List<GeneAlias> aliasList = new ArrayList<>();
            if (aliases != null && aliases.size() > 0) {
                for (int i = 0; i < aliases.size(); i++) {
                    GeneAlias alias = new GeneAlias();
                    alias.setGeneAliasKey(PkGenerator.md5(aliasesKey+i));
                    GeneAlias geneAlias = cnGeneAliasRepository.findByGeneKeyAndGeneAlias(gene.getGeneKey(), aliases.getString(i));
                    if (geneAlias!=null){
                        if (lang==1){
                            alias.setGeneAliasKey(geneAlias.getGeneAliasKey());
                        }
                        if (lang==2){
                            continue;
                        }
                    }
                    alias.setGeneId(gene.getGeneId());
                    alias.setGeneKey(gene.getGeneKey());
                    alias.setGeneAlias(aliases.getString(i));
                    aliasList.add(alias);
                }
            }
            return aliasList;
        };
        geneAliasRepository.save(aliasConverter.convert(en,1));
        cnGeneAliasRepository.save(aliasConverter.convert(cn,2));
        //3.基因外部id
        String externalIdKey=PkGenerator.generator(GeneExternalId.class);
        JsonArrayConverter<GeneExternalId> externalIdConverter=(json)->{
            JSONArray externalIds = json.getJSONArray("externalIds");
            List<GeneExternalId> externalIdsList = new ArrayList<>();
            if (externalIds != null && externalIds.size() > 0) {
                for (int i = 0; i < externalIds.size(); i++) {
                    GeneExternalId externalId = externalIds.getObject(i, GeneExternalId.class);
                    externalId.setGeneExternalIdKey(PkGenerator.md5(externalIdKey+i));
                    externalId.setGeneId(gene.getGeneId());
                    externalId.setGeneKey(gene.getGeneKey());
                    externalIdsList.add(externalId);
                }
            }
            return externalIdsList;
        };
        geneExternalIdRepository.save(externalIdConverter.convert(en));
        cnGeneExternalIdRepository.save(externalIdConverter.convert(cn));
        //4.基因位置
        String locationKey=PkGenerator.generator(GeneLocation.class);
        JsonArrayConverter<GeneLocation> locationConverter=(json)->{
            JSONArray locations = json.getJSONArray("locations");
            List<GeneLocation> locationList = new ArrayList<>();
            if (locations != null && locations.size() > 0) {
                for (int i = 0; i < locations.size(); i++) {
                    GeneLocation location = locations.getObject(i, GeneLocation.class);
                    location.setGeneLocationKey(PkGenerator.md5(locationKey+i));
                    location.setGeneKey(gene.getGeneKey());
                    location.setGeneId(gene.getGeneId());
                    locationList.add(location);
                }
            }
            return locationList;
        };
        geneLocationRepository.save(locationConverter.convert(en));
        cnGeneLocationRepository.save(locationConverter.convert(cn));
        //5.基因其他名字
        String otherNameKey=PkGenerator.generator(GeneOtherName.class);
        JsonArrayConverter<GeneOtherName> otherNameConverter=(json)->{
            JSONArray otherNames = json.getJSONArray("otherNames");
            List<GeneOtherName> otherNameList = new ArrayList<>();
            if (otherNames != null && otherNames.size() > 0) {
                for (int i = 0; i < otherNames.size(); i++) {
                    GeneOtherName otherName = new GeneOtherName();
                    otherName.setGeneOtherNameKey(PkGenerator.md5(otherNameKey+i));
                    otherName.setGeneId(gene.getGeneId());
                    otherName.setGeneKey(gene.getGeneKey());
                    otherName.setOtherName(otherNames.getString(i));
                    otherNameList.add(otherName);
                }
            }
            return otherNameList;
        };
        geneOtherNameRepository.save(otherNameConverter.convert(en));
        cnGeneOtherNameRepository.save(otherNameConverter.convert(cn));
        //插入与该id关联的蛋白质
        logger.info("【" + CPA.GENE.name() + "】开始插入关联的蛋白质");
        Page proteinPage=new Page(CPA.GENE.contentUrl+"/"+gene.getGeneId()+"/"+CPA.PROTEIN.name+"s");
        ContentParam proteinParam=new ContentParam(CPA.PROTEIN,proteinService,true,gene.getGeneKey());
        MainService.childrenTreadPool.execute(new IdThread(proteinPage,proteinParam));
        //插入与该id关联的突变
        logger.info("【" + CPA.GENE.name() + "】开始插入关联的突变");
        Page variantPage=new Page(CPA.GENE.contentUrl+"/"+gene.getGeneId()+"/"+CPA.VARIANT.name+"s");
        ContentParam variantParam=new ContentParam(CPA.VARIANT,variantService,true,gene.getGeneKey());
        MainService.childrenTreadPool.execute(new IdThread(variantPage,variantParam));
        return true;
    }

    @Override
    public boolean saveByDependence (JSONObject object,JSONObject cn, String dependenceKey){
        return false;
    }

    @Override
    public void initDB () throws FileNotFoundException {
        CPA.GENE.name=cpaProperties.getGeneName();
        CPA.GENE.contentUrl=cpaProperties.getGeneUrl();
        CPA.GENE.tempStructureMap= AcquireJsonStructure.getJsonKeyMap(cpaProperties.getGeneTempPath());
        Set<Integer> ids=geneRepository.findIdByCPA();
        Iterator<Integer> iterator=ids.iterator();
        while (iterator.hasNext()){
            CPA.GENE.dbId.add(String.valueOf(iterator.next()));
        }
    }
}
