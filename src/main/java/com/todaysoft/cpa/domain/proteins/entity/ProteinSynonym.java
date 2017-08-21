package com.todaysoft.cpa.domain.proteins.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 14:28
 */
@Entity
@Table(name = "kt_protein_synonym", schema = "project_kb", catalog = "")
public class ProteinSynonym {
    private String proteinSynonymKey;
    private String proteinKey;
    private String proteinId;
    private String synonym;

    @Id
    @Column(name = "protein_synonym_key", nullable = false, length = 64)
    public String getProteinSynonymKey() {
        return proteinSynonymKey;
    }

    public void setProteinSynonymKey(String proteinSynonymKey) {
        this.proteinSynonymKey = proteinSynonymKey;
    }

    @Basic
    @Column(name = "protein_key", nullable = true, length = 64)
    public String getProteinKey() {
        return proteinKey;
    }

    public void setProteinKey(String proteinKey) {
        this.proteinKey = proteinKey;
    }

    @Basic
    @Column(name = "protein_id", nullable = false, length = 11)
    public String getProteinId() {
        return proteinId;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }

    @Basic
    @Column(name = "synonym", nullable = true, length = 200)
    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProteinSynonym that = (ProteinSynonym) o;

        if (proteinSynonymKey != null ? !proteinSynonymKey.equals(that.proteinSynonymKey) : that.proteinSynonymKey != null)
            return false;
        if (proteinKey != null ? !proteinKey.equals(that.proteinKey) : that.proteinKey != null) return false;
        if (proteinId != null ? !proteinId.equals(that.proteinId) : that.proteinId != null) return false;
        if (synonym != null ? !synonym.equals(that.synonym) : that.synonym != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proteinSynonymKey != null ? proteinSynonymKey.hashCode() : 0;
        result = 31 * result + (proteinKey != null ? proteinKey.hashCode() : 0);
        result = 31 * result + (proteinId != null ? proteinId.hashCode() : 0);
        result = 31 * result + (synonym != null ? synonym.hashCode() : 0);
        return result;
    }
}
