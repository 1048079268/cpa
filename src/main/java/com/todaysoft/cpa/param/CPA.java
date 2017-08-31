package com.todaysoft.cpa.param;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:25
 */
public enum CPA {
    GENE,DRUG,VARIANT,EVIDENCE,PROTEIN,CLINICAL_TRIAL,REGIMEN;
    public Set<String> dbId;
    public String contentUrl;
    public String name;
    CPA() {
        dbId= Collections.synchronizedSet(new HashSet<>());
        contentUrl=null;
        name=null;
    }
}
