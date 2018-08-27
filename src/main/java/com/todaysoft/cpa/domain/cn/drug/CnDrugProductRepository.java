package com.todaysoft.cpa.domain.cn.drug;

import com.todaysoft.cpa.domain.entity.DrugProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface CnDrugProductRepository extends JpaRepository<DrugProduct,String> {
    List<DrugProduct> findByCreatedWay(Integer createWay);

    DrugProduct findByApprovalNumber(String approvalNumber);

    @Modifying
    @Transactional
    void deleteByApprovalNumberAndCreatedWay(String approvalNumber, Integer createdWay);
}
