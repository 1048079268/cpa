package com.todaysoft.cpa.utils.dosage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc: 专门处理拆分剂量拆分的工具类
 * @author: 鱼唇的人类
 * @date: 2017/11/20 17:23
 */
public class DosageUtil {
    //解析剂量
    public static Dosage splitDosage(String dosageValue,String dosageForm){
        Dosage dosage=new Dosage();
        dosage.setOriginal(dosageValue);
        dosage.setState(2);
        //预处理
        if (dosageValue==null||dosageValue.length()==0){
            dosage.setState(0);
            return dosage;
        }
        dosageValue=dosageValue.replace('μ','u');
        dosageValue=dosageValue.replaceAll("'"," ");
        if (dosageValue.matches("(^.*)(\\s*[\\(（]{1}[\\s\\u4e00-\\u9fa50-9a-zA-Z.·]*[\\)）]{1}$)")){
            boolean isRemove=false;
            if (!dosageValue.matches("(^.*)([\\(（]{1}[0-9a-zA-Z.]*[\\)）]{1}$)")){
                isRemove=true;
            }
            if (dosageValue.matches("(^.*)([\\(（]{1}I[\\)）]{1}$)")){
                isRemove=true;
            }
            if (isRemove){
                String[] result = extractString("(^.*)([(（]{1}[\\s\\u4e00-\\u9fa50-9a-zA-Z.·]*[)）]{1}$)", dosageValue);
                dosageValue=result[0];
            }
        }
        //测试
//   5ug(0.25 mg/ml, 1.2 ml/支)     "(^[.0-9]+)([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)\\s*[,，]{1}\\s*([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\)）]{1}$"
//        if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)\\s*[,，]{1}\\s*([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\)）]{1}$")){
//            System.out.println(dosageValue);
//        }
        //拆分
        if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)/([.0-9]+$)")){
            //eg:100 mg/1
            String[] result = extractString("(^[.0-9]+)([^.0-9:×%,()（）]+)/([.0-9]+$)", dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue(result[2]);
            dosage.setDosageUnit(acquireUnit(dosageForm));
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9/:×%,()（）]+$)")){
            //eg:1000 mg/mL
            String[] result = extractString("(^[.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9/:×%,()（）]+$)",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageUnit(result[2]);
            dosage.setDosageValue("1");
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)/([.0-9]+)([^.0-9/:×%,()（）]+$)")){
            //eg:100 mg/12mL
            String[] result = extractString("(^[.0-9]+)([^.0-9:×%,()（）]+)/([.0-9]+)([^.0-9/:×%,()（）]+$)",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue(result[2]);
            dosage.setDosageUnit(result[3]);
        }else if (dosageValue.matches("(^[.0-9]+)[\\s]*[%％]{1}$")){
            //eg:1.09%
            dosage.setState(3);
            String[] result = extractString("(^[.0-9]+)[\\s]*[%％]{1}$",dosageValue);
            dosage.setConcentration(result[0]);
        }else if (dosageValue.matches("^([.0-9]+)([^.0-9/:×(（）)%,]*$)")){
            ///eg:0.5 mg
            String[] result = extractString("([.0-9]+)([^.0-9/:×(（）)%,]*$)",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue("1");
            dosage.setDosageUnit(acquireUnit(dosageForm));
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9/:×%,()（）]+)[∶:：\\s]+([.0-9]+)([^.0-9/:×%,()（）]+$)")){
            //eg:100mg:12mL
            String[] result = extractString("(^[.0-9]+)([^.0-9/:×%,()（）]+)[∶:：\\s]+([.0-9]+)([^.0-9/:×%,()（）]+$)",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue(result[2]);
            dosage.setDosageUnit(result[3]);
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9]+)/([.0-9]+)([^.0-9/:×%,()（）]+)/[^.0-9/:×%,()（）]+$")){
            //eg:75mg/10ml/支
            dosageValue=dosageValue.replaceAll("\\(.*\\)","").trim();
            String[] result = extractString("(^[.0-9]+)([^.0-9]+)/([.0-9]+)([^.0-9/:×%,()（）]+)/[^.0-9/:×%,()（）]+$",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue(result[2]);
            dosage.setDosageUnit(result[3]);
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9/:×%,()（）]+)[\\(（]{1}([.0-9]+)[\\s]*[%％]{1}[,，]{1}([.0-9]+)([^.0-9/:×%,()（）]+)[\\)）]{1}/[^.0-9/:×%,()（）]+$")){
            //eg:12.5g(25%,50ml)/袋
            dosage.setState(4);
            String[] result=extractString("(^[.0-9]+)([^.0-9/:×%,()（）]+)[\\(（]{1}([.0-9]+)[\\s]*[%％]{1}[,，]{1}([.0-9]+)([^.0-9/:×%,()（）]+)[\\)）]{1}/[^.0-9/:×%,()（）]+$",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setConcentration(result[2]);
            dosage.setDosageValue(result[3]);
            dosage.setDosageUnit(result[4]);
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)[\\s]*[%％]{1}[\\)）]{1}$")){
            //eg:667mg/ml(50%)
            dosage.setState(4);
            String[] result=extractString("(^[.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)[\\s]*[%％]{1}[\\)）]{1}$",dosageValue);
            dosage.setContentValue(result[0]);
            dosage.setContentUnit(result[1]);
            dosage.setDosageValue("1");
            dosage.setDosageUnit(result[2]);
            dosage.setConcentration(result[3]);
        }else if (dosageValue.matches("(^[.0-9]+)([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)\\s*[,，]{1}\\s*([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\)）]{1}$")){
            //eg:10ug(0.25 mg/ml, 2.4 ml/支)
            String[] result=extractString("(^[.0-9]+)([^.0-9:×%,()（）]+)[\\(（]{1}([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)\\s*[,，]{1}\\s*([.0-9]+)([^.0-9:×%,()（）]+)/([^.0-9:×%,()（）]+)[\\)）]{1}$",dosageValue);
            dosage.setContentUnit(result[3]);
            dosage.setDosageUnit(result[4]);
            dosage.setDosageValue(result[5]);
            double a= Double.parseDouble(result[2]);
            double b= Double.parseDouble(result[5]);
            dosage.setContentValue(String.valueOf(a*b));
        }else {
            dosage.setState(1);
        }
        return dosage;
    }

    /**
     * todo 根据剂型获取单位
     * @param dosageForm
     * @return
     */
    public static String acquireUnit(String dosageForm){
        return "unknown";
    }

    /**
     * 根据正则表达式拆分字符串，并返回拆分结果
     * @param regex
     * @param var
     * @return
     */
    public static String[] extractString(String regex,String var){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(var);
        String[] result=new String[matcher.groupCount()];
        if (!var.matches(regex)){
            return result;
        }
        if (matcher.find()){
            for (int i = 0; i < matcher.groupCount(); i++) {
                result[i]=matcher.group(i+1).trim();
            }
        }
        return result;
    }
}
