package com.todaysoft.cpa.service.vice;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cn.drug.CnDrugProductRepository;
import com.todaysoft.cpa.domain.cn.drug.CnDrugProductRouteRepository;
import com.todaysoft.cpa.domain.en.drug.DrugProductRepository;
import com.todaysoft.cpa.domain.en.drug.DrugProductRouteRepository;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.entity.DrugProduct;
import com.todaysoft.cpa.domain.entity.DrugProductIngredient;
import com.todaysoft.cpa.domain.entity.DrugProductRoute;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.service.KbUpdateService;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.MergeException;
import com.todaysoft.cpa.utils.MergeUtil;
import com.todaysoft.cpa.utils.PkGenerator;
import com.todaysoft.cpa.utils.dosage.Dosage;
import com.todaysoft.cpa.utils.dosage.DosageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/20 11:46
 */
@Service
public class DrugProductService {
    private static Map<String,DrugProduct> CPA_DRUG_PRODUCT=new HashMap<>();
    private static Map<String,DrugProduct> OLD_DRUG_PRODUCT=new HashMap<>();
    @Autowired
    DrugProductRepository drugProductRepository;
    @Autowired
    CnDrugProductRepository cnDrugProductRepository;
    @Autowired
    DrugProductRouteRepository drugProductRouteRepository;
    @Autowired
    CnDrugProductRouteRepository cnDrugProductRouteRepository;
    @Autowired
    private KbUpdateService kbUpdateService;

    public void init(){
        cnDrugProductRepository.findByCreatedWay(2).forEach(drugProduct -> {
            CPA_DRUG_PRODUCT.put(drugProduct.getApprovalNumber(),drugProduct);
        });
        cnDrugProductRepository.findByCreatedWay(3).forEach(drugProduct -> {
            OLD_DRUG_PRODUCT.put(drugProduct.getApprovalNumber(),drugProduct);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DrugProduct save(JSONObject en, JSONObject cn, Drug drug, Map<String,Integer> status){
        DrugProduct product = en.toJavaObject(DrugProduct.class);
        product.setApprovalNumber(approvalNumber(en));
        String approvalNumber = product.getApprovalNumber();
        DrugProduct oldEn = drugProductRepository.findByApprovalNumber(approvalNumber);
        DrugProduct oldCn = cnDrugProductRepository.findByApprovalNumber(approvalNumber);
        //是否使用老中文库数据状态
        boolean isUseOldCnState=oldCn!=null;
        //是否使用老英文库数据状态
        boolean isUseOldEnState=oldEn!=null;
        //是否保存中文数据
        boolean isSaveCn = oldEn==null&&(oldCn==null||oldCn.getCreatedWay()==3);
        //是否是老库覆盖CPA数据
        boolean isOldBaseData= oldEn==null && oldCn!=null;
        //key
        String productKey=oldCn==null?PkGenerator.generator(DrugProduct.class):oldCn.getProductKey();
        JsonObjectConverter<DrugProduct> productConverter=(json)->{
            DrugProduct drugProduct = json.toJavaObject(DrugProduct.class);
            drugProduct.setCheckState(1);
            drugProduct.setCreatedAt(System.currentTimeMillis());
            drugProduct.setCreatedWay(2);
            drugProduct.setCreatedByName("CPA");
            drugProduct.setProductKey(productKey);
            drugProduct.setMarketingEnd(DateUtil.stringToTimestamp(json.getString("marketingEnd")));
            drugProduct.setMarketingStart(DateUtil.stringToTimestamp(json.getString("marketingStart")));
            drugProduct.setCopy(json.getBoolean("generic"));
            String approvalNO = approvalNumber(json);
            if (approvalNO!=null){
                drugProduct.setApprovalNumber(approvalNO);
            }
            return drugProduct;
        };
        DrugProduct enDrugProduct=productConverter.convert(en);
        DrugProduct cnDrugProduct=productConverter.convert(cn);
        //更新状态
        if (isUseOldCnState){
            cnDrugProduct.setCreatedWay(oldCn.getCreatedWay());
            cnDrugProduct.setCheckState(oldCn.getCheckState());
            cnDrugProduct.setCreatedByName(oldCn.getCreatedByName());
        }
        if (isUseOldEnState){
            enDrugProduct.setCreatedWay(oldEn.getCreatedWay());
            enDrugProduct.setCheckState(oldEn.getCheckState());
            enDrugProduct.setCreatedByName(oldEn.getCreatedByName());
        }
        cnDrugProduct.setProductName(cnDrugProduct.getProductNameEn());
        cnDrugProduct.setProductNameEn(enDrugProduct.getProductNameEn());
        DrugProduct drugProduct=enDrugProduct;
        if (oldEn==null){
            //保留老库数据
            if (isOldBaseData){
                enDrugProduct.setCheckState(4);
                cnDrugProduct.setProductName(MergeUtil.cover(oldCn.getProductName(),cnDrugProduct.getProductName()));
                cnDrugProduct.setProductNameEn(MergeUtil.cover(oldCn.getProductNameEn(),cnDrugProduct.getProductNameEn()));
                cnDrugProduct.setDosageForm(MergeUtil.cover(oldCn.getDosageForm(),cnDrugProduct.getDosageForm()));
                cnDrugProduct.setInstructionUrl(MergeUtil.cover(oldCn.getInstructionUrl(),cnDrugProduct.getInstructionUrl()));
                cnDrugProduct.setDosageStrength(MergeUtil.cover(oldCn.getDosageStrength(),cnDrugProduct.getDosageStrength()));
                cnDrugProduct.setLabeller(MergeUtil.cover(oldCn.getLabeller(),cnDrugProduct.getLabeller()));
            }
            drugProduct = drugProductRepository.save(enDrugProduct);
            if (isSaveCn){
                cnDrugProductRepository.save(cnDrugProduct);
            }
            if (drugProduct.getCheckState()==1){
                kbUpdateService.send("kt_drug_product");
            }
        }
        //给药方式
        if (!StringUtils.isEmpty(en.getString("route"))&&!StringUtils.isEmpty(cn.getString("route"))){
            String generator = PkGenerator.generator(DrugProductRoute.class);
            DrugProductRoute enRoute=new DrugProductRoute();
            enRoute.setProductKey(drugProduct.getProductKey());
            enRoute.setProductRoute(en.getString("route"));
            DrugProductRoute cnRoute=new DrugProductRoute();
            cnRoute.setProductKey(drugProduct.getProductKey());
            cnRoute.setProductRoute(cn.getString("route"));
            List<DrugProductRoute> enRoutes = drugProductRouteRepository.findAll(Example.of(enRoute));
            if (enRoutes==null||enRoutes.size()==0){
                enRoute.setRouteKey(generator);
                drugProductRouteRepository.save(enRoute);
            }
            List<DrugProductRoute> cnRoutes = cnDrugProductRouteRepository.findAll(Example.of(cnRoute));
            if (isSaveCn&&cnRoutes==null||cnRoutes.size()==0){
                cnRoute.setRouteKey(generator);
                cnDrugProductRouteRepository.save(cnRoute);
            }
        }
        return drugProduct;
    }

    private String approvalNumber(JSONObject json){
        JSONObject externalIds = json.getJSONObject("externalIds");
        if (externalIds!=null){
            String approvalNO;
            String dpdId = externalIds.getString("dpd_id");
            String ndcId = externalIds.getString("ndc_id");
            String ndcProductCode = externalIds.getString("ndc_product_code");
            String cfdaId = externalIds.getString("cfda_id");
            approvalNO= StringUtils.isEmpty(dpdId)? null :dpdId;
            approvalNO= StringUtils.isEmpty(ndcId)?approvalNO:ndcId;
            approvalNO= StringUtils.isEmpty(ndcProductCode)?approvalNO:ndcProductCode;
            approvalNO= StringUtils.isEmpty(cfdaId)?approvalNO:cfdaId;
            return approvalNO;
        }
        return null;
    }
}
