package com.todaysoft.cpa.utils.dosage;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 17:45
 */
public class Dosage {
    private int state;//0:表示该剂量为null,1:表示该剂量无法解析，2:表示该剂量是正常剂量，3：表示该剂量是浓度，4：表示既有浓度又有成分
    private String original;
    private String concentration;
    private String dosageValue;
    private String dosageUnit;
    private String contentValue;
    private String contentUnit;

    public String getDosageValue() {
        return dosageValue;
    }

    public void setDosageValue(String dosageValue) {
        this.dosageValue = dosageValue;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }

    public String getContentUnit() {
        return contentUnit;
    }

    public void setContentUnit(String contentUnit) {
        this.contentUnit = contentUnit;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    @Override
    public String toString() {
        return "Dosage：" +
                "\nstate=" + state +
                "\noriginal='" + original + '\'' +
                "\nconcentration='" + concentration + '\'' +
                "\ndosageValue='" + dosageValue + '\'' +
                "\ndosageUnit='" + dosageUnit + '\'' +
                "\ncontentValue='" + contentValue + '\'' +
                "\ncontentUnit='" + contentUnit + '\'' ;
    }
}
