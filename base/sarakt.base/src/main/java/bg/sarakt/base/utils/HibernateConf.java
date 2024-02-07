/*
 * HibernateConf.java
 *
 * created at 2023-12-06 by Roman Tsonev <roman.tsonev@yandex.ru>
 *
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.base.utils;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.cj.jdbc.MysqlDataSource;

@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties
public class HibernateConf {

    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("bg.sarakt.glossary.entities", "bg.sarakt.storing.hibernate", "bg.sarakt.storing.hibernate.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("bg.sarakt.storing.hibernate.entities");
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setJpaProperties(hibernateProperties());
        return bean;
    }

    @Bean
    public DataSource dataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName("sarakt");
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setUser("sarakt_user");
        return ds;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("spring.jpa.properties.hibernate.check.nullability", "true");
        // properties.setProperty("hibernate.dialect",
        // "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("show_sql", "true");
        properties.setProperty("format_sql", "true");
        return properties;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);

        return transactionManager;
    }
}
