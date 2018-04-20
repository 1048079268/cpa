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
        String productKey= PkGenerator.generator(DrugProduct.class);
        DrugProduct product = en.toJavaObject(DrugProduct.class);
        product.setApprovalNumber(approvalNumber(en));
        String approvalNumber = product.getApprovalNumber();
        DrugProduct oldProduct =OLD_DRUG_PRODUCT.get(approvalNumber); //drugProductRepository.findByApprovalNumberAndCreatedWay(approvalNumber, 3);
        Integer integer = status.get(approvalNumber);
        if (oldProduct!=null){
            if (!MergeInfo.DRUG_PRODUCT.isNeedArtificialCheck){
                productKey=oldProduct.getProductKey();
                integer=1;
            }else if (integer==null||integer==0){
                if (MergeInfo.DRUG_PRODUCT.sign.add(approvalNumber)){
                    List<String> list=new ArrayList<>();
                    list.add(0,String.valueOf(drug.getDrugId()));
                    list.add(1,drug.getNameEn());
                    list.add(2,product.getProductNameEn());
                    list.add(3,product.getApprovalNumber());
                    list.add(4,oldProduct.getProductKey());
                    list.add(5,oldProduct.getProductNameEn());
                    list.add(6,oldProduct.getApprovalNumber());
                    MergeInfo.DRUG_PRODUCT.checkList.add(list);
                }
                throw new MergeException("【DrugProduct】等待审核->name="+product.getProductNameEn()+",no="+approvalNumber);
            }else if (integer==1){
                productKey=oldProduct.getProductKey();
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
        cnDrugProduct.setProductName(cnDrugProduct.getProductNameEn());
        cnDrugProduct.setProductNameEn(enDrugProduct.getProductNameEn());
        DrugProduct drugProduct=CPA_DRUG_PRODUCT.get(approvalNumber);//drugProductRepository.findByApprovalNumberAndCreatedWay(approvalNumber,2);
        if (drugProduct==null){
            //保留老库数据
            if (oldProduct!=null&&integer==1){
                cnDrugProduct.setCheckState(oldProduct.getCheckState());
                cnDrugProduct.setCreatedWay(oldProduct.getCreatedWay());
                cnDrugProduct.setCreatedByName(oldProduct.getCreatedByName());
                enDrugProduct.setCheckState(4);
                cnDrugProduct.setProductName(MergeUtil.cover(oldProduct.getProductName(),cnDrugProduct.getProductName()));
                cnDrugProduct.setProductNameEn(MergeUtil.cover(oldProduct.getProductNameEn(),cnDrugProduct.getProductNameEn()));
                cnDrugProduct.setDosageForm(MergeUtil.cover(oldProduct.getDosageForm(),cnDrugProduct.getDosageForm()));
                cnDrugProduct.setInstructionUrl(MergeUtil.cover(oldProduct.getInstructionUrl(),cnDrugProduct.getInstructionUrl()));
                cnDrugProduct.setDosageStrength(MergeUtil.cover(oldProduct.getDosageStrength(),cnDrugProduct.getDosageStrength()));
                cnDrugProduct.setLabeller(MergeUtil.cover(oldProduct.getLabeller(),cnDrugProduct.getLabeller()));
            }
            drugProduct = drugProductRepository.save(enDrugProduct);
            cnDrugProductRepository.save(cnDrugProduct);
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
            if (cnRoutes==null||cnRoutes.size()==0){
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
