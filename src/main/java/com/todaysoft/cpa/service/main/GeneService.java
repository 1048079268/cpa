package com.todaysoft.cpa.service.main;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.domain.cn.gene.*;
import com.todaysoft.cpa.domain.en.gene.*;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.param.*;
import com.todaysoft.cpa.service.BaseService;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.service.MainService;
import com.todaysoft.cpa.thread.IdThread;
import com.todaysoft.cpa.utils.*;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonArrayLangConverter;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

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
    private CnGeneExternalIdRepository cnGeneExternalIdRepository;
    @Autowired
    private CnGeneLocationRepository cnGeneLocationRepository;
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private GeneExternalIdRepository geneExternalIdRepository;
    @Autowired
    private GeneLocationRepository geneLocationRepository;
    @Autowired
    private ProteinService proteinService;
    @Autowired
    private VariantService variantService;
    @Autowired
    private CPAProperties cpaProperties;
    @Autowired
    private KbUpdateService kbUpdateService;

    @Override
    public boolean save(JSONObject en,JSONObject cn,int status) throws InterruptedException {
        Gene checkGene = cn.toJavaObject(Gene.class);
        Gene oldCn = cnGeneRepository.findByGeneSymbol(checkGene.getGeneSymbol());
        Gene oldEn = geneRepository.findByGeneId(checkGene.getGeneId());
        //是否使用老中文库数据状态
        boolean isUseOldCnState=oldCn!=null;
        //是否使用老英文库数据状态
        boolean isUseOldEnState=oldEn!=null;
        //是否保存中文数据
        boolean isSaveCn = oldEn==null&&(oldCn==null||oldCn.getCreateWay()==3);
        //是否是老库覆盖CPA数据
        boolean isOldBaseData= oldEn==null && oldCn!=null && oldCn.getCreateWay()==3;
        String geneKey=oldCn==null?PkGenerator.generator(Gene.class):oldCn.getGeneKey();
        JsonObjectConverter<Gene> geneConverter=(json)->{
            Gene gene = json.toJavaObject(Gene.class);
            gene.setTheAlias(JsonUtil.jsonArrayToString(json.getJSONArray("aliases"),"<=>"));
            gene.setOtherNames(JsonUtil.jsonArrayToString(json.getJSONArray("otherNames"),"<=>"));
            gene.setGeneKey(geneKey);
            gene.setCreateAt(System.currentTimeMillis());
            gene.setCreateWay(2);
            gene.setCheckState(1);
            gene.setCreatedByName("CPA");
            return gene;
        };
        Gene geneEn = geneConverter.convert(en);
        Gene geneCn = geneConverter.convert(cn);
        if (isUseOldCnState) {
            geneCn.setCheckState(oldCn.getCheckState());
            geneCn.setCreateWay(oldCn.getCreateWay());
            geneCn.setCreatedByName(oldCn.getCreatedByName());
        }
        if (isUseOldEnState){
            geneEn.setCheckState(oldEn.getCheckState());
            geneEn.setCreateWay(oldEn.getCreateWay());
            geneEn.setCreatedByName(oldEn.getCreatedByName());
        }
        //合并数据
        if (isOldBaseData){
            geneEn.setCheckState(4);
            geneCn.setTheAlias(MergeUtil.mergeAlias(oldCn.getTheAlias(),geneCn.getTheAlias(),"<=>"));
            if (!StringUtils.isEmpty(oldCn.getGeneType())){
                geneCn.setGeneType(oldCn.getGeneType());
            }
            if (!StringUtils.isEmpty(oldCn.getGeneFullName())){
                geneCn.setGeneFullName(oldCn.getGeneFullName());
            }
            if (!StringUtils.isEmpty(oldCn.getEntrezGeneSummary())){
                geneCn.setEntrezGeneSummary(oldCn.getEntrezGeneSummary());
            }
            if (!StringUtils.isEmpty(oldCn.getCytogeneticBand())){
                geneCn.setCytogeneticBand(oldCn.getCytogeneticBand());
            }
            if (oldCn.getHasCosmicMutations()!=null){
                geneCn.setHasCosmicMutations(oldCn.getHasCosmicMutations());
            }
            if (!StringUtils.isEmpty(oldCn.getCancerGene())){
                geneCn.setCancerGene(oldCn.getCancerGene());
            }
        }
        Gene gene = geneRepository.save(geneEn);
        if (isSaveCn) {
            cnGeneRepository.save(geneCn);
        }
        //外部数据库
        List<GeneExternalId> geneExternalIds = geneExternalIdRepository.findByGeneKey(gene.getGeneKey());
        Map<Integer,String> geiKeyMap=new HashMap<>();
        if (geneExternalIds!=null){
            geiKeyMap.putAll(geneExternalIds.stream().collect(Collectors.toMap(GeneExternalId::hashCode, GeneExternalId::getGeneExternalIdKey)));
        }
        String externalIdKey=PkGenerator.generator(GeneExternalId.class);
        JsonArrayConverter<GeneExternalId> externalIdConverter=(json)->{
            JSONArray externalIds = json.getJSONArray("externalIds");
            List<GeneExternalId> externalIdsList = new ArrayList<>();
            if (externalIds != null && externalIds.size() > 0) {
                for (int i = 0; i < externalIds.size(); i++) {
                    GeneExternalId externalId = externalIds.getObject(i, GeneExternalId.class);
                    externalId.setGeneId(gene.getGeneId());
                    externalId.setGeneKey(gene.getGeneKey());
                    String key=geiKeyMap.get(externalId.hashCode());
                    externalId.setGeneExternalIdKey(key!=null?key:PkGenerator.md5(externalIdKey+i));
                    externalIdsList.add(externalId);
                }
            }
            return externalIdsList;
        };
        List<GeneExternalId> enGEI = externalIdConverter.convert(en);
        geneExternalIdRepository.save(enGEI);
        if(isSaveCn){
            List<GeneExternalId> cnGEI = externalIdConverter.convert(cn);
            for (int i = 0; i < cnGEI.size(); i++) {
                cnGEI.get(i).setGeneExternalIdKey(enGEI.get(i).getGeneExternalIdKey());
            }
            cnGeneExternalIdRepository.save(cnGEI);
        }
        //4.基因位置
        List<GeneLocation> glList = geneLocationRepository.findByGeneKey(gene.getGeneKey());
        Map<Integer,String> glKeyMap=new HashMap<>();
        if (glList!=null){
            glKeyMap.putAll(glList.stream().collect(Collectors.toMap(GeneLocation::hashCode, GeneLocation::getGeneLocationKey)));
        }
        String locationKey=PkGenerator.generator(GeneLocation.class);
        JsonArrayConverter<GeneLocation> locationConverter=(json)->{
            JSONArray locations = json.getJSONArray("locations");
            List<GeneLocation> locationList = new ArrayList<>();
            if (locations != null && locations.size() > 0) {
                for (int i = 0; i < locations.size(); i++) {
                    GeneLocation location = locations.getObject(i, GeneLocation.class);
                    location.setGeneKey(gene.getGeneKey());
                    location.setGeneId(gene.getGeneId());
                    String key = glKeyMap.get(location.hashCode());
                    location.setGeneLocationKey(key!=null?key:PkGenerator.md5(locationKey+i));
                    locationList.add(location);
                }
            }
            return locationList;
        };
        List<GeneLocation> enLocation = locationConverter.convert(en);
        geneLocationRepository.save(enLocation);
        if (isSaveCn){
            List<GeneLocation> cnLocation = locationConverter.convert(cn);
            for (int i = 0; i < cnLocation.size(); i++) {
                cnLocation.get(i).setGeneLocationKey(enLocation.get(i).getGeneLocationKey());
            }
            cnGeneLocationRepository.save(cnLocation);
        }
        if (status!=0){
            return true;
        }
//        //插入与该id关联的蛋白质
//        logger.info("【" + CPA.GENE.name() + "】开始插入关联的蛋白质");
//        Page proteinPage=new Page(CPA.GENE.contentUrl+"/"+gene.getGeneId()+"/"+CPA.PROTEIN.name+"s");
//        ContentParam proteinParam=new ContentParam(CPA.PROTEIN,proteinService,true,gene.getGeneKey());
//        MainService.childrenTreadPool.execute(new IdThread(proteinPage,proteinParam));
//        //插入与该id关联的突变
//        logger.info("【" + CPA.GENE.name() + "】开始插入关联的突变");
//        Page variantPage=new Page(CPA.GENE.contentUrl+"/"+gene.getGeneId()+"/"+CPA.VARIANT.name+"s");
//        ContentParam variantParam=new ContentParam(CPA.VARIANT,variantService,true,gene.getGeneKey());
//        MainService.childrenTreadPool.execute(new IdThread(variantPage,variantParam));
        if (geneCn.getCheckState()==1){
            kbUpdateService.send("kt_gene");
        }
        return true;
    }

    @Override
    public boolean saveByDependence (JSONObject object,JSONObject cn, String dependenceKey,int status){
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
