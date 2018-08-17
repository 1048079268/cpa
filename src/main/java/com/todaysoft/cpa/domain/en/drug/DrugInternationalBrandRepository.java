package com.todaysoft.cpa.domain.en.drug;

import com.todaysoft.cpa.domain.entity.DrugInternationalBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 10:09
 */
public interface DrugInternationalBrandRepository extends JpaRepository<DrugInternationalBrand,String> {
    List<DrugInternationalBrand> findByDrugKey(String drugKey);
}
