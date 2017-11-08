package com.todaysoft.cpa.statistics;

import com.alibaba.druid.util.StringUtils;
import com.csvreader.CsvReader;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrailRepository;
import com.todaysoft.cpa.domain.en.clinicalTrail.ClinicalTrialOutcomeRepository;
import com.todaysoft.cpa.domain.en.drug.DrugInteractionRepository;
import com.todaysoft.cpa.domain.en.drug.DrugRepository;
import com.todaysoft.cpa.domain.en.gene.GeneOtherNameRepository;
import com.todaysoft.cpa.domain.en.gene.GeneRepository;
import com.todaysoft.cpa.domain.en.medicationPlan.MedicationPlanRepository;
import com.todaysoft.cpa.domain.en.medicationPlan.PlanInstructionMessageRepository;
import com.todaysoft.cpa.domain.en.proteins.ProteinRepository;
import com.todaysoft.cpa.domain.en.proteins.ProteinSynonymRepository;
import com.todaysoft.cpa.domain.entity.ClinicalTrial;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.entity.Gene;
import com.todaysoft.cpa.utils.WordCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/2 15:02
 */
@Service
public class StatisticsService {
    @Autowired
    private GeneRepository geneRepository;
    @Autowired
    private GeneOtherNameRepository geneOtherNameRepository;

    private List<Drug> statisticsDrugList;
    private List<Gene> statisticsGeneList;
    private List<ClinicalTrial> statisticsClinicalTrialList;

    public void init() throws IOException {
        statisticsDrugList=new ArrayList<>();
        CsvReader csvReader=new CsvReader("statistics/oncodrug.csv");
        csvReader.readHeaders();
        while (csvReader.readRecord()){
            Drug drug=new Drug();
            drug.setNameEn(csvReader.get("name"));
            drug.setOncoDrug(Boolean.valueOf(csvReader.get("oncodrug")));
            statisticsDrugList.add(drug);
        }
        System.out.println("drug:"+statisticsDrugList.size());
        statisticsGeneList=new ArrayList<>();
        CsvReader csvReader1=new CsvReader("statistics/cancer+related+genes.csv");
        csvReader1.readHeaders();
        while (csvReader1.readRecord()){
            Gene gene=new Gene();
            gene.setGeneSymbol(csvReader1.get("gene_symbol"));
            gene.setGeneFullName(csvReader1.get("gene_full_name"));
            gene.setCancerGene(csvReader1.get("oncogene"));
            statisticsGeneList.add(gene);
        }
        System.out.println("gene:"+statisticsGeneList.size());
        statisticsClinicalTrialList=new ArrayList<>();
        CsvReader csvReader2=new CsvReader("statistics/cancer_related_trials.csv");
        csvReader2.readHeaders();
        while (csvReader2.readRecord()){
            ClinicalTrial clinicalTrial=new ClinicalTrial();
            clinicalTrial.setClinicalTrialId(csvReader2.get("nct_id"));
            statisticsClinicalTrialList.add(clinicalTrial);
        }
        System.out.println("clinicalTrial:"+statisticsClinicalTrialList.size());
    }

    public void statistics(){

    }

    public void statisticsGene(){
        final long[] entrezGeneSummary = {0L};
        geneRepository.statistics().stream().filter(s-> !StringUtils.isEmpty(s)).forEach(s -> entrezGeneSummary[0] +=WordCountUtil.count(s));
        final long[] otherName = {0L};
        geneOtherNameRepository.statistics().stream().filter(s-> !StringUtils.isEmpty(s)).forEach(s-> otherName[0] +=WordCountUtil.count(s));
        System.out.println("gene:");
        System.out.println("entrezGeneSummary:"+entrezGeneSummary[0]);
        System.out.println("otherName:"+otherName[0]);
        System.out.println("sub:"+(entrezGeneSummary[0]+otherName[0]));
    }
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugInteractionRepository drugInteractionRepository;
    public void statisticsDrug(){
        final long[] drugCount={0L,0L,0L,0L,0L,0L,0L,0L,0L};
        drugRepository.statistics().forEach(drug -> {
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
        drugInteractionRepository.statistics().forEach(drugInteraction -> interactionDescription[0] +=WordCountUtil.count(drugInteraction.getDescription()));
        System.out.println("--drug:");
        System.out.println("description"+drugCount[0]);
        System.out.println("indicationDesc"+drugCount[1]);
        System.out.println("pharmacodynamics"+drugCount[2]);
        System.out.println("mechanismOfAction"+drugCount[3]);
        System.out.println("routeOfElimination"+drugCount[4]);
        System.out.println("clearance"+drugCount[5]);
        System.out.println("absorption"+drugCount[6]);
        System.out.println("toxicity"+drugCount[7]);
        System.out.println("volumeOfDistribution"+drugCount[8]);
        System.out.println("interactionDescription"+interactionDescription[0]);
        final long[] total = {interactionDescription[0]};
        Arrays.stream(drugCount).forEach(num-> total[0] +=num);
        System.out.println("sub:"+ total[0]);
    }

    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private ProteinSynonymRepository proteinSynonymRepository;
    public void statisticsProtein(){
        final long[] proteinCount={0L,0L};
        proteinRepository.statistics().forEach(protein -> {
            String functionDescription = protein.getFunctionDescription();
            proteinCount[0]+=WordCountUtil.count(functionDescription);
            String tissueSpecificity = protein.getTissueSpecificity();
            proteinCount[1]+=WordCountUtil.count(tissueSpecificity);
        });
        final long[] synonym = {0L};
        proteinSynonymRepository.statistics().stream()
                .filter(s -> !StringUtils.isEmpty(s.getSynonym()))
                .forEach(s -> synonym[0] +=WordCountUtil.count(s.getSynonym()));
        System.out.println("--protein:");
        System.out.println("functionDescription:"+proteinCount[0]);
        System.out.println("tissueSpecificity:"+proteinCount[1]);
        System.out.println("synonym:"+synonym[0]);
        System.out.println("sub:"+(proteinCount[0]+proteinCount[1]+synonym[0]));
    }

    @Autowired
    private MedicationPlanRepository medicationPlanRepository;
    @Autowired
    private PlanInstructionMessageRepository planInstructionMessageRepository;
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
        System.out.println("--medicationPlan:");
        System.out.println("regimenDescription:"+medicationPlanCount[0]);
        System.out.println("chemotherapyType:"+medicationPlanCount[1]);
        System.out.println("theText:"+theText[0]);
        System.out.println("sub:"+(medicationPlanCount[0]+medicationPlanCount[1]+theText[0]));
    }

    @Autowired
    private ClinicalTrailRepository clinicalTrailRepository;
    @Autowired
    private ClinicalTrialOutcomeRepository clinicalTrialOutcomeRepository;
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
        System.out.println("--clinicalTrial:");
        System.out.println("title:"+title[0]);
        System.out.println("classification:"+outcome[0]);
        System.out.println("outcomeTitle:"+outcome[1]);
        System.out.println("sub:"+(title[0]+outcome[0]+outcome[1]));
    }
}
