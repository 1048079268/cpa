package com.todaysoft.cpa.domain.en.cacer;

import com.todaysoft.cpa.domain.entity.Cancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 11:26
 */
public interface CancerRepository extends JpaRepository<Cancer,String>{
    @Query("select c from Cancer c where c.doid=?1 and c.createdWay=2")
    Cancer findByDoid(String doid);
}
