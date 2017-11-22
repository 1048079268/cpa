package com.todaysoft.cpa.service.vice;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.domain.cn.drug.CnDrugProductApprovalRepository;
import com.todaysoft.cpa.domain.cn.drug.CnDrugProductRepository;
import com.todaysoft.cpa.domain.cn.drug.CnDrugProductRouteRepository;
import com.todaysoft.cpa.domain.en.drug.DrugProductApprovalRepository;
import com.todaysoft.cpa.domain.en.drug.DrugProductRepository;
import com.todaysoft.cpa.domain.en.drug.DrugProductRouteRepository;
import com.todaysoft.cpa.domain.entity.Drug;
import com.todaysoft.cpa.domain.entity.DrugProduct;
import com.todaysoft.cpa.domain.entity.DrugProductApproval;
import com.todaysoft.cpa.domain.entity.DrugProductRoute;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.utils.DateUtil;
import com.todaysoft.cpa.utils.JsonConverter.JsonObjectConverter;
import com.todaysoft.cpa.utils.MergeException;
import com.todaysoft.cpa.utils.PkGenerator;
import com.todaysoft.cpa.utils.dosage.Dosage;
import com.todaysoft.cpa.utils.dosage.DosageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
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
    DrugProductApprovalRepository drugProductApprovalRepository;
    @Autowired
    CnDrugProductApprovalRepository cnDrugProductApprovalRepository;

    @Async
    public void init(){
        drugProductRepository.findByCreatedWay(2).forEach(drugProduct -> {
            CPA_DRUG_PRODUCT.put(drugProduct.getProductNameEn()+"<-->"+drugProduct.getDosageForm(),drugProduct);
        });
        drugProductRepository.findByCreatedWay(3).forEach(drugProduct -> {
            OLD_DRUG_PRODUCT.put(drugProduct.getProductNameEn(),drugProduct);
        });
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DrugProductApproval save(JSONObject en, JSONObject cn, Drug drug, Map<String,Integer> status){
        String productKey= PkGenerator.generator(DrugProduct.class);
        DrugProduct product = en.toJavaObject(DrugProduct.class);
        String compareItem=product.getProductNameEn()+"<-->"+product.getDosageForm();
        if (OLD_DRUG_PRODUCT.containsKey(product.getProductNameEn())){
            DrugProduct drugProduct = OLD_DRUG_PRODUCT.get(product.getProductNameEn());
            Integer integer = status.get(compareItem);
            if (integer==null||integer==0){
                if (MergeInfo.DRUG_PRODUCT.sign.add(product.getProductNameEn())){
                    List<String> list=new ArrayList<>();
                    list.add(0,String.valueOf(drug.getDrugId()));
                    list.add(1,drug.getNameEn());
                    list.add(2,product.getProductNameEn());
                    list.add(3,product.getDosageForm());
                    list.add(4,drugProduct.getProductKey());
                    list.add(5,drugProduct.getProductNameEn());
                    list.add(6,drugProduct.getDosageForm());
                    MergeInfo.DRUG_PRODUCT.checkList.add(list);
                }
                throw new MergeException("【DrugProduct】等待审核->name="+product.getProductNameEn());
            }else if (integer==1){
                productKey=drugProduct.getProductKey();
            }
        }
        String finalProductKey = productKey;
        JsonObjectConverter<DrugProduct> productConverter=(json)->{
            DrugProduct drugProduct = json.toJavaObject(DrugProduct.class);
            drugProduct.setCheckState(1);
            drugProduct.setCreatedAt(System.currentTimeMillis());
            drugProduct.setCreatedWay(2);
            drugProduct.setCreatedByName("CPA");
            drugProduct.setProductKey(finalProductKey);
            return drugProduct;
        };
        DrugProduct enDrugProduct=productConverter.convert(en);
        DrugProduct cnDrugProduct=productConverter.convert(cn);
        cnDrugProduct.setProductName(cnDrugProduct.getProductNameEn());
        cnDrugProduct.setProductNameEn(enDrugProduct.getProductNameEn());
        DrugProduct drugProduct;
        if (CPA_DRUG_PRODUCT.containsKey(compareItem)){
            drugProduct= CPA_DRUG_PRODUCT.get(compareItem);
        }else {
            drugProduct = drugProductRepository.save(enDrugProduct);
            cnDrugProductRepository.save(cnDrugProduct);
            CPA_DRUG_PRODUCT.put(compareItem,drugProduct);
        }
        //给药方式
        if (!StringUtils.isEmpty(en.getString("route"))&&!StringUtils.isEmpty(cn.getString("route"))){
            DrugProductRoute enRoute=new DrugProductRoute();
            enRoute.setProductKey(drugProduct.getProductKey());
            enRoute.setProductRoute(en.getString("route"));
            DrugProductRoute cnRoute=new DrugProductRoute();
            cnRoute.setProductKey(drugProduct.getProductKey());
            cnRoute.setProductRoute(cn.getString("route"));
            List<DrugProductRoute> routes = drugProductRouteRepository.findAll(Example.of(enRoute));
            if (routes==null||routes.size()==0){
                drugProductRouteRepository.save(enRoute);
                cnDrugProductRouteRepository.save(cnRoute);
            }
        }
        //批准
        JsonObjectConverter<DrugProductApproval> productApprovalConverter=(json)->{
            DrugProductApproval approval=json.toJavaObject(DrugProductApproval.class);
            approval.setProductKey(drugProduct.getProductKey());
            String marketingEnd=json.getString("marketingEnd");
            String marketingStart=json.getString("marketingStart");
            approval.setMarketingEnd(DateUtil.stringToTimestamp(marketingEnd));
            approval.setMarketingStart(DateUtil.stringToTimestamp(marketingStart));
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
                approval.setApprovalNumber(approvalNO);
            }
            String dosageForm = json.getString("dosageForm");
            String dosageStrength = json.getString("dosageStrength");
            Dosage dosage = DosageUtil.splitDosage(dosageStrength, dosageForm);
            if (dosage.getState()==2||dosage.getState()==4){
                approval.setDosageStrengthValue(Double.valueOf(dosage.getDosageValue()));
                approval.setDosageStrengthUnit(dosage.getDosageUnit());
            }
            return approval;
        };
        DrugProductApproval enApproval = productApprovalConverter.convert(en);
        DrugProductApproval cnApproval = productApprovalConverter.convert(cn);
        DrugProductApproval drugProductApproval;
        //先查英文库有没有该批准(药品主键和药品批准号)
        DrugProductApproval checkApprovalEn = drugProductApprovalRepository.findByProductKeyAndApprovalNumber(drugProduct.getProductKey(),enApproval.getApprovalNumber());
        if (checkApprovalEn==null){
            String approvalKey=PkGenerator.generator(DrugProductApproval.class);
            //再查中文库有没有该批准
            DrugProductApproval checkApprovalCn = cnDrugProductApprovalRepository.findByProductKeyAndApprovalNumber(drugProduct.getProductKey(),cnApproval.getApprovalNumber());
            if (checkApprovalCn!=null){
                approvalKey=checkApprovalCn.getApprovalKey();
            }else {
                cnApproval.setApprovalKey(approvalKey);
                cnDrugProductApprovalRepository.save(cnApproval);
            }
            enApproval.setApprovalKey(approvalKey);
            drugProductApproval=drugProductApprovalRepository.save(enApproval);
        }else {
            drugProductApproval=checkApprovalEn;
        }
        return drugProductApproval;
    }
}
