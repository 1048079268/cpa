package com.todaysoft.cpa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/28 14:48
 */
@ConfigurationProperties(prefix = "spring.datasource.cn")
public class CnProperties extends CustomDataSource {
}
