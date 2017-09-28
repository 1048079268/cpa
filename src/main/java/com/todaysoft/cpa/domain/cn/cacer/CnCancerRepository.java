package com.todaysoft.cpa.domain.cn.cacer;

import com.todaysoft.cpa.domain.entity.Cancer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:26
 */
public interface CnCancerRepository extends JpaRepository<Cancer,String>{
    Cancer findByDoid(String doid);
}
