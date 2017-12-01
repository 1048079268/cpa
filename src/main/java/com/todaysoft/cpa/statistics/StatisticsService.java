package com.todaysoft.cpa.statistics;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrialOutcomeRepository;
import com.todaysoft.cpa.domain.en.drug.DrugInteractionRepository;
import com.todaysoft.cpa.domain.en.drug.DrugRepository;
import com.todaysoft.cpa.domain.en.gene.GeneRepository;
import com.todaysoft.cpa.domain.en.medicationPlan.MedicationPlanRepository;
import com.todaysoft.cpa.domain.en.medicationPlan.PlanInstructionMessageRepository;
import com.todaysoft.cpa.domain.en.proteins.ProteinRepository;
import com.todaysoft.cpa.domain.entity.*;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.Page;
import com.todaysoft.cpa.utils.WordCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/2 15:02
 */
@Service
public class StatisticsService {
    private static Logger logger= LoggerFactory.getLogger(StatisticsService.class);
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugInteractionRepository drugInteractionRepository;
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private MedicationPlanRepository medicationPlanRepository;
    @Autowired
    private PlanInstructionMessageRepository planInstructionMessageRepository;
    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private ClinicalTrialOutcomeRepository clinicalTrialOutcomeRepository;
    @Autowired
    private CPAStatistics cpaStatistics;

    private List<Drug> statisticsDrugList;
    private List<Gene> statisticsGeneList;
    private List<ClinicalTrial> statisticsClinicalTrialList;
    private Set<String> clinicalTrialIdList;

    public void init() throws IOException {
        logger.info("<<<<<<<<<<<<<INIT>>>>>>>>>>>>");
        statisticsDrugList=new ArrayList<>();
        CsvReader csvReader=new CsvReader("statistics/oncodrug.csv");
        csvReader.readHeaders();
        while (csvReader.readRecord()){
            Drug drug=new Drug();
            drug.setCreatedByName(null);
            drug.setNameEn(csvReader.get("name"));
            statisticsDrugList.add(drug);
        }
        logger.info("drug:"+statisticsDrugList.size());
        statisticsGeneList=new ArrayList<>();
        CsvReader csvReader1=new CsvReader("statistics/cancer+related+genes.csv");
        csvReader1.readHeaders();
        while (csvReader1.readRecord()){
            Gene gene=new Gene();
            gene.setCreatedByName(null);
            gene.setGeneSymbol(csvReader1.get("gene_symbol"));
            statisticsGeneList.add(gene);
        }
        logger.info("gene:"+statisticsGeneList.size());
        clinicalTrialIdList=new HashSet<>();
        CsvReader csvReader2=new CsvReader("statistics/cancer_related_trials.csv");
        csvReader2.readHeaders();
        while (csvReader2.readRecord()){
            clinicalTrialIdList.add(csvReader2.get("nct_id"));
        }
        logger.info("clinicalTrial:"+clinicalTrialIdList.size());
    }

    public void statistics() throws IOException, InterruptedException {
        logger.info("<<<<<<<<<<<<<statistics>>>>>>>>>>>>".toUpperCase());
//        //drug
//        List<Drug> drugList=new ArrayList<>();
//        drugList.add(drugRepository.findByDrugId(5472));
//        statisticsDrugList.forEach(drug -> {
//            Example<Drug> example=Example.of(drug);
//            List<Drug> drugs = drugRepository.findAll(example);
//            drugList.addAll(drugs);
//        });
//        logger.info("drug:"+drugList.size());
//        statisticsDrug(drugList);
//        //medicationPlan
//        Set<String> drugIds=new HashSet<>();
//        drugList.forEach(drug -> drugIds.add(String.valueOf(drug.getDrugId())));
//        statisticsRegimen(drugIds);
        //gene
        List<Gene> geneList=new ArrayList<>();
        statisticsGeneList.forEach(gene -> {
            Example<Gene> example=Example.of(gene);
            List<Gene> genes = geneRepository.findAll(example);
            geneList.addAll(genes);
        });
        logger.info("gene:"+geneList.size());
        statisticsGene(geneList);
        //protein
        List<Protein> proteinList=new ArrayList<>();
        geneList.forEach(gene -> {
            Protein protein=new Protein();
            protein.setCreatedByName(null);
            protein.setGeneKey(gene.getGeneKey());
            Example<Protein> proteinExample=Example.of(protein);
            List<Protein> proteins = proteinRepository.findAll(proteinExample);
            if (proteins==null||proteins.size()==0){
                logger.error("protein-notfound:"+gene.getGeneId());
            }else {
                proteinList.addAll(proteins);
            }
        });
        logger.info("protein:"+proteinList.size());
        statisticsProtein(proteinList);
//
//        //clinicalTrial
//        statisticsClinicalTrail(clinicalTrialIdList);
    }

    @Async
    private void statisticsGene(List<Gene> geneList){
        final long[] entrezGeneSummary = {0L};
        final long[] otherName = {0L};
        geneList.forEach(gene ->{
            entrezGeneSummary[0] +=WordCountUtil.count(gene.getEntrezGeneSummary());
            otherName[0]+=WordCountUtil.count(gene.getOtherNames());
        });
        logger.info("gene:");
        logger.info("entrezGeneSummary:"+entrezGeneSummary[0]);
        logger.info("otherName:"+otherName[0]);
        logger.info("sub:"+(entrezGeneSummary[0]+otherName[0]));
    }

    @Async
    private void statisticsDrug(List<Drug> drugList){
        final long[] drugCount={0L,0L,0L,0L,0L,0L,0L,0L,0L};
        List<DrugInteraction> interactionList=new ArrayList<>();
        drugList.forEach(drug -> {
            DrugInteraction drugInteraction=new DrugInteraction();
            drugInteraction.setDrugKey(drug.getDrugKey());
            drugInteraction.setDrugId(drug.getDrugId());
            Example<DrugInteraction> drugInteractionExample=Example.of(drugInteraction);
            interactionList.addAll(drugInteractionRepository.findAll(drugInteractionExample));
            String description = drug.getDescription();
            drugCount[0]+=WordCountUtil.count(description);
            String indicationDesc = drug.getStructuredIndicationDesc();
            drugCount[1]+=WordCountUtil.count(indicationDesc);
            String pharmacodynamics = drug.getPharmacodynamics();
            drugCount[2]+=WordCountUtil.count(pharmacodynamics);
            String mechanismOfAction = drug.getMechanismOfAction();
            drugCount[3]+=WordCountUtil.count(mechanismOfAction);
            String routeOfElimination = drug.getRouteOfElimination();
            drugCount[4]+=WordCountUtil.count(routeOfElimination);
            String clearance = drug.getClearance();
            drugCount[5]+=WordCountUtil.count(clearance);
            String absorption = drug.getAbsorption();
            drugCount[6]+=WordCountUtil.count(absorption);
            String toxicity = drug.getToxicity();
            drugCount[7]+=WordCountUtil.count(toxicity);
            String volumeOfDistribution = drug.getVolumeOfDistribution();
            drugCount[8]+=WordCountUtil.count(volumeOfDistribution);
        });
        final long[] interactionDescription = {0L};
        interactionList.forEach(drugInteraction -> interactionDescription[0] +=WordCountUtil.count(drugInteraction.getDescription()));
        logger.info("--drug:");
        logger.info("description"+drugCount[0]);
        logger.info("indicationDesc"+drugCount[1]);
        logger.info("pharmacodynamics"+drugCount[2]);
        logger.info("mechanismOfAction"+drugCount[3]);
        logger.info("routeOfElimination"+drugCount[4]);
        logger.info("clearance"+drugCount[5]);
        logger.info("absorption"+drugCount[6]);
        logger.info("toxicity"+drugCount[7]);
        logger.info("volumeOfDistribution"+drugCount[8]);
        logger.info("interactionDescription"+interactionDescription[0]);
        final long[] total = {interactionDescription[0]};
        Arrays.stream(drugCount).forEach(num-> total[0] +=num);
        logger.info("sub:"+ total[0]);
    }

    @Async
    private void statisticsProtein(List<Protein> proteinList){
        final long[] proteinCount={0L,0L,0L};
        proteinList.forEach(protein -> {
            String functionDescription = protein.getFunctionDescription();
            proteinCount[0]+=WordCountUtil.count(functionDescription);
            String tissueSpecificity = protein.getTissueSpecificity();
            proteinCount[1]+=WordCountUtil.count(tissueSpecificity);
            proteinCount[2]+=WordCountUtil.count(protein.getOtherNames());
        });
        logger.info("--protein:");
        logger.info("functionDescription:"+proteinCount[0]);
        logger.info("tissueSpecificity:"+proteinCount[1]);
        logger.info("synonym:"+proteinCount[2]);
        logger.info("sub:"+(proteinCount[0]+proteinCount[1]+proteinCount[2]));
    }

    @Async
    public void statisticsRegimen(){
        final long[] medicationPlanCount={0L,0L};
        medicationPlanRepository.statistics().forEach(medicationPlan -> {
            String regimenDescription = medicationPlan.getRegimenDescription();
            medicationPlanCount[0]+=WordCountUtil.count(regimenDescription);
            String chemotherapyType = medicationPlan.getChemotherapyType();
            medicationPlanCount[1]+=WordCountUtil.count(chemotherapyType);
        });
        final long[] theText = {0L};
        planInstructionMessageRepository.statistics().forEach(s -> theText[0] +=WordCountUtil.count(s.getTheText()));
        logger.info("--medicationPlan:");
        logger.info("regimenDescription:"+medicationPlanCount[0]);
        logger.info("chemotherapyType:"+medicationPlanCount[1]);
        logger.info("theText:"+theText[0]);
        logger.info("sub:"+(medicationPlanCount[0]+medicationPlanCount[1]+theText[0]));
    }

    @Async
    public void statisticsRegimen(Set<String> drugIds) throws IOException, InterruptedException {
        drugIds.forEach(s -> logger.warn(s));
        CountFunction countFunction=(json,map)->{
            CountService.SimpleCount simpleCount=(key, text)->{
                if (!map.containsKey(key)){
                    map.put(key,0L);
                }
                if (!StringUtils.isEmpty(text)){
                    long count= WordCountUtil.count(text);
                    map.replace(key,map.get(key)+count);
                }
            };
            String chemotherapyDescription=json.getString("chemotherapyDescription");
            simpleCount.count("chemotherapyDescription",chemotherapyDescription);
            String chemotherapyType=json.getString("chemotherapyType");
            simpleCount.count("chemotherapyType",chemotherapyType);
            JSONArray instructions = json.getJSONArray("instructions");
            if (instructions!=null&&instructions.size()>0){
                for (int i=0;i<instructions.size();i++){
                    JSONArray instructionList = instructions.getJSONObject(i).getJSONArray("instructionList");
                    if (instructionList!=null&&instructionList.size()>0){
                        for (int j=0;j<instructionList.size();j++){
                            JSONObject jsonObject = instructionList.getJSONObject(j);
                            if (jsonObject!=null){
                                String text = jsonObject.getString("text");
                                simpleCount.count("text",text);
                            }
                        }
                    }
                }

            }
            return map;
        };
        Map<String, Long> countMap =new HashMap<>();
        Page page = new Page(CPA.REGIMEN.contentUrl, 100, 0);
        CountScan countScan=new CountScan(CPA.REGIMEN,countFunction,page);
        countScan.setJudgeNeedCount((json)->{
            JSONArray drugs = json.getJSONArray("drugs");
            if (drugs!=null&&drugs.size()>0){
                for (int i1 = 0; i1 < drugs.size(); i1++) {
                    String drugId=drugs.getString(i1);
                    if (drugIds.contains(drugId)){
                        System.out.println("judge[true]:"+drugId);
                        return true;
                    }
                }
            }
            System.out.println("judge[false]");
            return false;
        });
        Map<String, Long> map = countScan.scan();
        map.forEach((key, value) ->{
            if (countMap.containsKey(key)){
                countMap.replace(key,value+countMap.get(key));
            }else {
                countMap.put(key,value);
            }
        });
        logger.info("----MedicationPlan----");
        countMap.forEach((key, value) -> logger.info(key + ":" + value));
        logger.info("----MedicationPlan----");
    }

    @Async
    public void statisticsClinicalTrail(){
        final long[] title = {0L};
        clinicalTrailRepository.statistics().forEach(clinicalTrial -> title[0] +=WordCountUtil.count(clinicalTrial.getTheTitle()));
        final long[] outcome={0L,0L};
        clinicalTrialOutcomeRepository.statistics().forEach(clinicalTrialOutcome -> {
            String classification = clinicalTrialOutcome.getClassification();
            outcome[0]+=WordCountUtil.count(classification);
            String outcomeTitle = clinicalTrialOutcome.getTitle();
            outcome[1]+=WordCountUtil.count(outcomeTitle);
        });
        logger.info("--clinicalTrial:");
        logger.info("title:"+title[0]);
        logger.info("classification:"+outcome[0]);
        logger.info("outcomeTitle:"+outcome[1]);
        logger.info("sub:"+(title[0]+outcome[0]+outcome[1]));
    }

    @Async
    public void statisticsClinicalTrail(Set<String>idSet) throws InterruptedException {
        CountFunction countFunction=(json, map)-> {
            CountService.SimpleCount simpleCount = (key, text) -> {
                if (!map.containsKey(key)) {
                    map.put(key, 0L);
                }
                if (!StringUtils.isEmpty(text)) {
                    long count = WordCountUtil.count(text);
                    map.replace(key, map.get(key) + count);
                }
            };
            String title=json.getString("title");
            simpleCount.count("title",title);
            JSONArray outcomes = json.getJSONArray("outcomes");
            if (outcomes!=null&&outcomes.size()>0){
                for (int i=0;i<outcomes.size();i++){
                    String classification = outcomes.getJSONObject(i).getString("classification");
                    simpleCount.count("classification",classification);
                    String outcomesTitle = outcomes.getJSONObject(i).getString("title");
                    simpleCount.count("outcomesTitle",outcomesTitle);
                }
            }
            return map;
        };
        List<String >idList=new ArrayList<>();
        idList.addAll(idSet);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(0,idList.size()/6-1),countFunction);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(idList.size()/6-1,idList.size()/3-1),countFunction);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(idList.size()/3-1,idList.size()/2-1),countFunction);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(idList.size()/2-1,idList.size()/3*2-1),countFunction);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(idList.size()/3*2-1,idList.size()/6*5-1),countFunction);
        cpaStatistics.statisticsByIdList(CPA.CLINICAL_TRIAL,idList.subList(idList.size()/6*5-1,idList.size()-1),countFunction);
    }
}
