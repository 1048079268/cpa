package com.todaysoft.cpa.mongo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.mongo.domain.MongoDetail;
import com.todaysoft.cpa.mongo.domain.MongoDetailRepository;
import com.todaysoft.cpa.mongo.domain.MongoPage;
import com.todaysoft.cpa.mongo.domain.MongoPageRepository;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.param.GlobalVar;
import com.todaysoft.cpa.utils.PkGenerator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 遍历列表
 * @author liceyo
 * @version 2018/7/12
 */
@Service
public class ScanListService {
    private static Logger logger= LoggerFactory.getLogger(ScanListService.class);
    @Autowired
    private MongoService mongoService;
    @Autowired
    private MongoPageRepository mongoPageRepository;
    @Autowired
    private MongoDetailRepository mongoDetailRepository;
    /**
     * 默认页数
     */
    private final static int DEFAULT_LIMIT =20;
    private final static  int DEFAULT_PAGE = 5;
    /**
     * 遍历数据
     * @param cpa 遍历类型
     * @return 主键集合
     */
    public Flux<List<DetailParam>> scan(CPA cpa, String updateSince){
        //获取总页数
        Integer countPage = countPage(cpa,updateSince);
        if (countPage==null){
            return Flux.empty();
        }
        //测试环境固定页数
        if (logger.isDebugEnabled()){
            countPage=DEFAULT_PAGE;
        }
        //根据页数创建流
        return Flux.range(0,countPage)
                //切换为线程执行模式
                .parallel()
                //使用parallel模式运行
                .runOn(Schedulers.parallel())
                //根据页数扫描列表
                .map(page->scanByPage(page,cpa,updateSince))
                .map(array->{
                    List<DetailParam> params=new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        String id = array.getJSONObject(i).getString("id");
                        DetailParam param=new DetailParam(id);
                        param.setModuleName(cpa.name());
                        param.setCpa(cpa);
                        param.setUpdateSince(updateSince);
                        params.add(param);
                    }
                    return params;
                })
                //切换为单线程，主要是为了保证onComplete只有一个
                .sequential();
    }

    /**
     * 扫描错误的列表
     * @return
     */
    public Flux<List<DetailParam>> scanErrorPage(){
        return Flux.fromStream(mongoPageRepository.findAll().stream())
                .parallel()
                .map(page->{
                    CPA cpa = CPA.valueOf(page.getModuleName());
                    String updateSince = page.getUpdateSince();
                    JSONArray array = scanByPage(page.getPage(), cpa, updateSince);
                    //读取成功删除错误记录
                    if (!array.isEmpty()){
                        mongoService.deleteErrorPage(page.getId());
                    }
                    List<DetailParam> params=new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        String id = array.getJSONObject(i).getString("id");
                        DetailParam param=new DetailParam(id);
                        param.setModuleName(page.getModuleName());
                        param.setCpa(cpa);
                        param.setUpdateSince(updateSince);
                        params.add(param);
                    }
                    return params;
                })
                //切换为单线程，主要是为了保证onComplete只有一个
                .sequential();
    }

    /**
     * 扫描错误的页面
     * @return
     */
    public Flux<List<DetailParam>> scanErrorDetail(){
        Pageable pageable= new PageRequest(0,DEFAULT_LIMIT);
        Page<MongoDetail> details = mongoDetailRepository.findAll(pageable);
        return Flux.range(0,details.getTotalPages())
                .map(page->{
                    Pageable pb= new PageRequest(page,DEFAULT_LIMIT);
                    Page<MongoDetail> dl = mongoDetailRepository.findAll(pb);
                    return dl.getContent().stream()
                            .map(detail->{
                                //读取成功后删除错误记录
                                mongoService.deleteErrorDetail(detail.getId());
                                DetailParam param = new DetailParam(detail);
                                param.setCpa(CPA.valueOf(detail.getModuleName()));
                                return param;
                            })
                            .collect(Collectors.toList());
                });
    }

    /**
     * 根据页数扫描列表
     * @param page 页数
     * @param cpa 模块
     * @param updateSince 更新起点
     * @return 列表数据
     */
    private JSONArray scanByPage(Integer page, CPA cpa, String updateSince){
        if (cpa==null){
            return new JSONArray();
        }
        return Mono.just(page)
                //根据页数从接口获取列表数据
                .map(p -> getIds(cpa,page,updateSince))
                //如果异常的话重试三次
                .retry(3)
                //还是异常的话打印错误信息
                .doOnError(e-> logger.error("["+cpa.name()+"]扫描列表错误,page="+page,e))
                //如果异常的话返回空
                .onErrorReturn(new JSONArray())
                .filter(Objects::nonNull)
                //转换id集合
                .block();
    }


    /**
     * 保存突变样本量
     */
    public void scanAndSaveMutationStatistics(){
        CPA cpa = CPA.MUTATION_STATISTICS;
        //获取总页数
        Integer countPage = countPage(cpa,"");
        if (countPage==null){
            return;
        }
        //如果是测试模式
        if (logger.isDebugEnabled()){
            countPage=DEFAULT_PAGE;
        }
        //按页数创建流
        Flux.range(0,countPage)
                //切换为多线程模式
                .parallel()
                //使用parallel模式运行
                .runOn(Schedulers.parallel())
                //根据页数扫描列表
                .map(page->scanByPage(page,cpa,""))
                //切换为单线程模式
                .sequential()
                //过滤空
                .filter(json->!json.isEmpty())
                //如果订阅完成计数器减一
                .toStream()
                //订阅
                .forEach(array->{
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject json=new JSONObject();
                        JSONObject object = array.getJSONObject(i);
                        json.put("data",object);
                        json.put("ktMysqlSyncStatus",false);
                        String jsonString = object.toJSONString();
                        try {
                            //突变样本量使用本身MD5做键
                            String id= PkGenerator.md5(jsonString);
                            //需要再判断mongo已有该数据
                            if (!mongoService.has(id,cpa.name)){
                                //保存入mongodb
                                mongoService.save(id,json,cpa.name);
                                logger.info("["+cpa.name()+"]保存成功，data="+ jsonString);
                            }
                        }catch (Exception e){
                            logger.error("["+cpa.name()+"]保存到mongodb失败，data="+jsonString);
                        }
                    }
                });
    }

    /**
     * 模块总数
     * @param cpa
     * @return
     */
    public long totalElement(CPA cpa){
        try {
            Connection.Response response = Jsoup.connect(cpa.contentUrl)
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .data("offset", "0")
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String json = response.body();
            JSONObject object = JSONObject.parseObject(json);
            if (object!=null){
                JSONObject meta = object.getJSONObject("meta");
                if (meta!=null){
                    return meta.getLongValue("total");
                }
            }
        } catch (IOException e) {
            logger.error(String.format("[%s]获取总数错误",cpa.name()),e);
        }
        return 0;
    }


    /**
     * 获取总页数
     * @param cpa 模块
     * @param updateSince 更新起点
     * @return 总页数
     */
    private Integer countPage(CPA cpa,String updateSince){
        try {
            Connection.Response response = Jsoup.connect(cpa.contentUrl)
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .data("offset", "0")
                    .data("updated-since",updateSince)
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String json = response.body();
            JSONObject object = JSONObject.parseObject(json);
            if (object!=null){
                JSONObject meta = object.getJSONObject("meta");
                if (meta!=null){
                    int total = meta.getIntValue("total");
                    int i = total % DEFAULT_LIMIT;
                    int j = total / DEFAULT_LIMIT;
                    return i==0?j:j+1;
                }
            }
        } catch (IOException e) {
            logger.error(String.format("[%s]获取总数错误",cpa.name()),e);
        }
        return null;
    }

    /**
     * 获取列表
     * @param cpa 模块
     * @param page 页数
     * @param updateSince 更新起点
     * @return 列表数据
     */
    private JSONArray getIds(CPA cpa, int page,String updateSince) {
        try {
            logger.info("["+cpa.name()+"]扫描列表：page="+page+",limit=20");
            Connection.Response response = Jsoup.connect(cpa.contentUrl)
                    .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
                    .data("offset", String.valueOf(page* DEFAULT_LIMIT))
                    .data("limit", String.valueOf(DEFAULT_LIMIT))
                    .data("updated-since",updateSince)
                    .header("Authorization", GlobalVar.getAUTHORIZATION())
                    .header("Accept", "application/test")
                    .ignoreContentType(true)
                    .timeout(12000)// 设置连接超时时间
                    .maxBodySize(0)//设置最大响应长度为0 ，否则太长的返回数据不会完整显示
                    .execute();
            String body = response.body();
            JSONObject object = JSONObject.parseObject(body);
            if (object != null) {
                JSONObject data = object.getJSONObject("data");
                if (data != null) {
                    String name = cpa.name;
                    String key= name.endsWith("s")? name : name +"s";
                    return data.getJSONArray(key);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
