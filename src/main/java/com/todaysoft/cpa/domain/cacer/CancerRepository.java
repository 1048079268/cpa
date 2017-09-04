package com.todaysoft.cpa.domain.cacer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:26
 */
public interface CancerRepository extends JpaRepository<Cancer,String>{
    Cancer findByDoid(String doid);
}
