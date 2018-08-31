package com.todaysoft.cpa.mongo;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;
import com.todaysoft.cpa.mongo.domain.*;
import com.todaysoft.cpa.param.CPA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author liceyo
 * @version 2018/7/12
 */
@Service
public class MongoService {
    private static Logger logger= LogManager.getLogger(MongoService.class);
    private final static String UPDATE_SINCE_COLLECTION="updateSince";
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoDetailRepository mongoDetailRepository;
    @Autowired
    private MongoPageRepository mongoPageRepository;


    /**
     * 获取updateSince时间
     * @return 日期
     */
    public String updateSince(){
        Map<String,Object> template = mongoTemplate.findById("updateSince", Map.class, UPDATE_SINCE_COLLECTION);
        if (template==null){
            return "";
        }
        String date = String.valueOf(template.get("date"));
        if (StringUtils.isEmpty(date)){
            return "";
        }
        return date;
    }

    /**
     * 更新updateSince时间
     * @param date 时间
     */
    public void postUpdateSince(String date){
        Query query=Query.query(Criteria.where("_id").is("updateSince"));
        BasicDBObject updateBody = BasicDBObject.parse("{'date':'"+date+"'}");
        Update update = Update.fromDBObject(updateBody);
        mongoTemplate.upsert(query, update, UPDATE_SINCE_COLLECTION);
    }

    /**
     * 查看是否已有该id
     * @param id id
     * @param collection 集合名
     * @return 是否已有该id
     */
    public boolean has(String id,String collection){
        return get(id,collection) != null;
    }

    /**
     * 获取基本信息
     * @param id
     * @param collection
     * @return
     */
    public MongoData get(String id,String collection){
        return mongoTemplate.findById(id, MongoData.class, collection);
    }

    /**
     * 保存数据
     * 采用更新模式
     * @param id 主键
     * @param json 数据
     * @param collection 表名
     * @return id
     */
    public String save(String id, JSONObject json,String collection){
        Query query=Query.query(Criteria.where("_id").is(id));
        BasicDBObject updateBody = BasicDBObject.parse(json.toJSONString());
        Update update = Update.fromDBObject(updateBody);
        WriteResult upsert = mongoTemplate.upsert(query, update, collection);
        return String.valueOf(upsert.getUpsertedId());
    }

    /**
     * 保存详情错误
     * @param detail 错误信息
     */
    public void upsertErrorDetail(MongoDetail detail){
        //1.判断是否已有
        MongoDetail mongoDetail = mongoDetailRepository.findByDataIdAndModuleName(detail.getDataId(), detail.getModuleName());
        if (mongoDetail!=null){
            String updateSince = mongoDetail.getUpdateSince();
            String detailUpdateSince = detail.getUpdateSince();
            //2.判断是否是最新的时间
            int dbHash=StringUtils.isEmpty(updateSince)?0:updateSince.hashCode();
            int hash=StringUtils.isEmpty(detailUpdateSince)?0:detailUpdateSince.hashCode();
            if (hash>dbHash){
                mongoDetailRepository.save(detail);
            }
        }else {
            mongoDetailRepository.insert(detail);
        }
    }

    public void deleteErrorDetail(String id){
        mongoDetailRepository.delete(id);
    }

    /**
     * 保存错误页
     * @param page
     */
    public void upsertErrorPage(MongoPage page){
        page.generateId();
        mongoPageRepository.save(page);
    }

    /**
     * 删除id
     * @param id
     */
    public void deleteErrorPage(String id){
        mongoPageRepository.delete(id);
    }

    public long countModule(CPA cpa){
        return mongoTemplate.count(new Query(),cpa.enDbName());
    }

    public long countModuleByMysql(CPA cpa,boolean ktMysqlStatus){
        Criteria criteria = Criteria.where("ktMysqlSyncStatus").is(ktMysqlStatus);
        return mongoTemplate.count(Query.query(criteria),cpa.enDbName());
    }

    /**
     * 统计知识库需同步数据
     * @param collection 表名
     * @return
     */
    public long countKtData(String  collection){
        return mongoTemplate.count(new Query(), collection);
    }

    /**
     * 更新同步状态
     * @param collection
     * @param id
     * @param status
     */
    public void updateSyncStatus(String collection,String id,boolean status){
        Update update = Update.update("ktMysqlSyncStatus", status);
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.updateFirst(query,update,collection);
    }

    /**
     * 分页（英文）
     * @param collection 表名
     * @param pageable 分页信息
     * @return 分页数据
     */
    public List<MongoData> pageKtData(String collection, Pageable pageable){
        Query query = new Query().with(pageable);
        List<MongoData> mongoData = mongoTemplate.find(query, MongoData.class, collection);
        return mongoData;
    }

    /**
     * 滚动分页
     * @param collection 集合名
     * @param scrollId 滚动id
     * @param limit 限制
     * @return 分页数据
     */
    public List<MongoData> pageScroll(String collection,String scrollId,int limit){
        Query query = Query.query(Criteria.where("_id").gt(scrollId)).limit(limit);
        List<MongoData> mongoData = mongoTemplate.find(query, MongoData.class, collection);
        return mongoData;
    }
}
