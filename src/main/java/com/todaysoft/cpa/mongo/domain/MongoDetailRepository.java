package com.todaysoft.cpa.mongo.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author liceyo
 * @version 2018/7/20
 */
public interface MongoDetailRepository extends MongoRepository<MongoDetail,String> {

    MongoDetail findByDataIdAndModuleName(String dataId, String moduleName);

}
