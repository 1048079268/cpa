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

    @Override
    public boolean save(JSONObject en,JSONObject cn,int status) throws InterruptedException {
        //1.基因
        String geneKey=PkGenerator.generator(Gene.class);
        //查重覆盖
        Gene checkGene = cn.toJavaObject(Gene.class);
        Gene byName = cnGeneRepository.findByName(checkGene.getGeneSymbol());
        if (byName!=null){
            if (!MergeInfo.GENE.isNeedArtificialCheck){
                logger.info("【" + CPA.GENE.name() + "】与老库合并->id="+checkGene.getGeneId());
                geneKey=byName.getGeneKey();
                status=1;
            }else if (status==0){
                if (MergeInfo.GENE.sign.add(String.valueOf(checkGene.getGeneId()))){
                    List<String> list=new ArrayList<>(4);
                    list.add(0, String.valueOf(checkGene.getGeneId()));
                    list.add(1,checkGene.getGeneSymbol());
                    list.add(2,byName.getGeneKey());
                    list.add(3,byName.getGeneSymbol());
                    MergeInfo.GENE.checkList.add(list);
                }
                throw new MergeException("【" + CPA.GENE.name() + "】与老库重合，等待审核->id="+checkGene.getGeneId());
            }else if (status==1){
                logger.info("【" + CPA.GENE.name() + "】与老库合并->id="+checkGene.getGeneId());
                geneKey=byName.getGeneKey();
            }
        }
        String finalGeneKey = geneKey;
        JsonObjectConverter<Gene> geneConverter=(json)->{
            Gene gene = json.toJavaObject(Gene.class);
            gene.setTheAlias(JsonUtil.jsonArrayToString(json.getJSONArray("aliases"),"<=>"));
            gene.setOtherNames(JsonUtil.jsonArrayToString(json.getJSONArray("otherNames"),"<=>"));
            gene.setGeneKey(finalGeneKey);
            gene.setCreateAt(System.currentTimeMillis());
            gene.setCreateWay(2);
            return gene;
        };
        Gene geneEn = geneConverter.convert(en);
        Gene geneCn = geneConverter.convert(cn);
        //合并数据
        if (byName!=null&&status==1){
            geneCn.setCheckState(byName.getCheckState());
            geneCn.setCreateWay(byName.getCreateWay());
            geneCn.setCreatedByName(byName.getCreatedByName());
            geneEn.setCheckState(4);
            geneCn.setTheAlias(MergeUtil.mergeAlias(byName.getTheAlias(),geneCn.getTheAlias(),"<=>"));
            if (!StringUtils.isEmpty(byName.getGeneType())){
                geneCn.setGeneType(byName.getGeneType());
            }
            if (!StringUtils.isEmpty(byName.getGeneFullName())){
                geneCn.setGeneFullName(byName.getGeneFullName());
            }
            if (!StringUtils.isEmpty(byName.getEntrezGeneSummary())){
                geneCn.setEntrezGeneSummary(byName.getEntrezGeneSummary());
            }
            if (!StringUtils.isEmpty(byName.getCytogeneticBand())){
                geneCn.setCytogeneticBand(byName.getCytogeneticBand());
            }
            if (byName.getHasCosmicMutations()!=null){
                geneCn.setHasCosmicMutations(byName.getHasCosmicMutations());
            }
            if (!StringUtils.isEmpty(byName.getCancerGene())){
                geneCn.setCancerGene(byName.getCancerGene());
            }
        }
        Gene gene = geneRepository.save(geneEn);
        cnGeneRepository.save(geneCn);
        //外部数据库
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
        if (status!=0){
            return true;
        }
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
