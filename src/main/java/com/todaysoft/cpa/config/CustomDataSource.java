package com.todaysoft.cpa.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;

import java.sql.SQLException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/28 14:14
 */
public class CustomDataSource {
    private String url="jdbc:mysql://localhost:3306/test?characterEncoding=utf-8";

    private String username = "root";

    private String password = "root";

    private String driverClassName = "com.mysql.jdbc.Driver";

    private Integer initialSize = 2;

    private Integer minIdle = 1;

    private Integer maxActive = 20;

    private Long maxWait = 60000L;

    private Integer timeBetweenEvictionRunsMillis = 60000;

    private Integer minEvictableIdleTimeMillis = 300000;

    private String validationQuery = "SELECT 'x'";

    private Boolean testWhileIdle = true;

    private Boolean testOnBorrow = false;

    private Boolean testOnReturn = false;

    private Boolean poolPreparedStatements = true;

    private Integer maxPoolPreparedStatementPerConnectionSize = 20;

    private String filters = "stat";

    public static DruidXADataSource toDruidXADataSource(CustomDataSource customDataSource){
        DruidXADataSource druidXADataSource=new DruidXADataSource();
        druidXADataSource.setUrl(customDataSource.getUrl());
        druidXADataSource.setDriverClassName(customDataSource.getDriverClassName());
        druidXADataSource.setUsername(customDataSource.getUsername());
        druidXADataSource.setPassword(customDataSource.getPassword());
        druidXADataSource.setInitialSize(customDataSource.getInitialSize());
        druidXADataSource.setMinIdle(customDataSource.getMinIdle());
        druidXADataSource.setMaxActive(customDataSource.getMaxActive());
        druidXADataSource.setMaxWait(customDataSource.getMaxWait());
        druidXADataSource.setTimeBetweenEvictionRunsMillis(customDataSource.getTimeBetweenEvictionRunsMillis());
        druidXADataSource.setMinEvictableIdleTimeMillis(customDataSource.getMinEvictableIdleTimeMillis());
        druidXADataSource.setValidationQuery(customDataSource.getValidationQuery());
        druidXADataSource.setTestWhileIdle(customDataSource.getTestWhileIdle());
        druidXADataSource.setTestOnBorrow(customDataSource.getTestOnBorrow());
        druidXADataSource.setTestOnReturn(customDataSource.getTestOnReturn());
        druidXADataSource.setPoolPreparedStatements(customDataSource.getPoolPreparedStatements());
        druidXADataSource.setMaxPoolPreparedStatementPerConnectionSize(customDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            druidXADataSource.setFilters(customDataSource.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidXADataSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public Integer getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
