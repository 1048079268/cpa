package com.todaysoft.cpa.param;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @desc: cpa所需的参数
 * @author: 鱼唇的人类
 * @date: 2017/8/7 13:40
 */
@ConfigurationProperties(prefix = "api")
public class CPAProperties {
    private String authorization;
    private String geneUrl;
    private String geneName;
    private String drugUrl;
    private String drugName;
    private String variantUrl;
    private String variantName;
    private String evidenceUrl;
    private String evidenceName;
    private String proteinUrl;
    private String proteinName;
    private String clinicalTrialUrl;
    private String clinicalTrialName;
    private String regimenUrl;
    private String regimenName;
    private int maxThreadNum;
    private int maxCrawlNum;
    private long heartbeat;
    private int maxBlockingNum;
    private int maxFailureBlockingNum;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getGeneUrl() {
        return geneUrl;
    }

    public void setGeneUrl(String geneUrl) {
        this.geneUrl = geneUrl;
    }

    public String getDrugUrl() {
        return drugUrl;
    }

    public void setDrugUrl(String drugUrl) {
        this.drugUrl = drugUrl;
    }

    public String getVariantUrl() {
        return variantUrl;
    }

    public void setVariantUrl(String variantUrl) {
        this.variantUrl = variantUrl;
    }

    public String getEvidenceUrl() {
        return evidenceUrl;
    }

    public void setEvidenceUrl(String evidenceUrl) {
        this.evidenceUrl = evidenceUrl;
    }

    public String getProteinUrl() {
        return proteinUrl;
    }

    public void setProteinUrl(String proteinUrl) {
        this.proteinUrl = proteinUrl;
    }

    public String getClinicalTrialUrl() {
        return clinicalTrialUrl;
    }

    public void setClinicalTrialUrl(String clinicalTrialUrl) {
        this.clinicalTrialUrl = clinicalTrialUrl;
    }

    public String getRegimenUrl() {
        return regimenUrl;
    }

    public void setRegimenUrl(String regimenUrl) {
        this.regimenUrl = regimenUrl;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getMaxCrawlNum() {
        return maxCrawlNum;
    }

    public void setMaxCrawlNum(int maxCrawlNum) {
        this.maxCrawlNum = maxCrawlNum;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getEvidenceName() {
        return evidenceName;
    }

    public void setEvidenceName(String evidenceName) {
        this.evidenceName = evidenceName;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getClinicalTrialName() {
        return clinicalTrialName;
    }

    public void setClinicalTrialName(String clinicalTrialName) {
        this.clinicalTrialName = clinicalTrialName;
    }

    public String getRegimenName() {
        return regimenName;
    }

    public void setRegimenName(String regimenName) {
        this.regimenName = regimenName;
    }

    public long getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(long heartbeat) {
        this.heartbeat = heartbeat*60000L;
    }

    public int getMaxBlockingNum() {
        return maxBlockingNum;
    }

    public void setMaxBlockingNum(int maxBlockingNum) {
        this.maxBlockingNum = maxBlockingNum;
    }

    public int getMaxFailureBlockingNum() {
        return maxFailureBlockingNum;
    }

    public void setMaxFailureBlockingNum(int maxFailureBlockingNum) {
        this.maxFailureBlockingNum = maxFailureBlockingNum;
    }
}
