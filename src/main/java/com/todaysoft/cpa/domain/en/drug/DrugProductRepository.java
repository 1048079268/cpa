package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface DrugProductRepository extends JpaRepository<DrugProduct,String> {
    List<DrugProduct> findByCreatedWay(Integer createWay);

    DrugProduct findByApprovalNumberAndCreatedWay(String approvalNumber, Integer createdWay);

    DrugProduct findByApprovalNumber(String approvalNumber);

    @Query(value = "SELECT d.approval_number FROM kt_drug_product d GROUP BY d.approval_number HAVING COUNT(d.approval_number) > 1 LIMIT ?1,?2",nativeQuery = true)
    List<String> findDuplicateId(Integer start,Integer limit);

    @Modifying
    @Transactional
    void deleteByApprovalNumberAndCreatedWay(String approvalNumber, Integer createdWay);
}
