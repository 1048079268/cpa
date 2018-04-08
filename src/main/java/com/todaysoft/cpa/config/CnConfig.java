package com.todaysoft.cpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/28 14:48
 */
@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = "com.todaysoft.cpa.domain.cn", entityManagerFactoryRef = "cnEntityManager")
@EnableConfigurationProperties(CnProperties.class)
public class CnConfig {
    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private CnProperties cnProperties;

    @Bean(name = "cnDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource cnDataSource() throws SQLException {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(CustomDataSource.toDruidXADataSource(cnProperties));
        xaDataSource.setUniqueResourceName("cn");
        xaDataSource.setBorrowConnectionTimeout(90);
        xaDataSource.setMaxPoolSize(20);
        return xaDataSource;

    }

    @Bean(name = "cnEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean entityManager() throws Throwable {
        HashMap<String, Object> properties = new HashMap();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(cnDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("com.todaysoft.cpa.domain.entity");
        entityManager.setPersistenceUnitName("cnPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }
}
