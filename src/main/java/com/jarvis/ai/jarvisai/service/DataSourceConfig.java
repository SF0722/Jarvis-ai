package com.jarvis.ai.jarvisai.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
@MapperScan(sqlSessionFactoryRef = "sqlSessionFactory",basePackages =
        {"com.jarvis.ai.jarvisai.dao"})
public class DataSourceConfig{
   private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource" )
    public DataSource primaryDataSource() {
        System.out.println("create dataSource success");
        logger.info("**********dataSource created");
        return new DruidDataSource();
    }

//    @Bean(name = "transactionManager")
//    @Primary
//    public DataSourceTransactionManager masterTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
//        logger.info("**********transactionManager created");
//        System.out.println("**********transactionManager created");
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/mybatis-config.xml"));
        logger.info("**********sqlSessionFactory created....");
        return sessionFactory.getObject();
    }

}
