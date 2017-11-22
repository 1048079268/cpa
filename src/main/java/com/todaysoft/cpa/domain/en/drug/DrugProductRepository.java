package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:10
 */
public interface DrugProductRepository extends JpaRepository<DrugProduct,String> {
    List<DrugProduct> findByCreatedWay(Integer createWay);
}
