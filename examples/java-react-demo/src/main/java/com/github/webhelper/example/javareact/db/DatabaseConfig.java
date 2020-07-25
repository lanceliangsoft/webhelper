package com.github.webhelper.example.javareact.db;

import com.github.webhelper.example.javareact.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories
@Slf4j
public class DatabaseConfig {
    private static final Map<String, String> DIALETS_BY_DRIVER =
        DataUtil.asMap("org.sqlite.JDBC", "com.github.webhelper.example.javareact.db.SQLiteDialect",
            "org.h2.Driver", "org.hibernate.dialect.H2Dialect");

    @Value("${spring.datasource.driver-class:org.h2.Driver}")
    private String jdbcDriver;

    @Value("${spring.datasource.url:jdbc:h2:mem:mydb}")
    private String jdbcUrl;

    @Value("${spring.datasource.username:sa}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    @Value("${spring.datasource.dialet:}")
    private String defaultDialet;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        log.info("dataSource={} url={}", dataSource, dataSource.getUrl());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", DIALETS_BY_DRIVER.getOrDefault(jdbcDriver, defaultDialet));
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "update");

        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setJpaProperties(props);
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("com.github.webhelper.example.javareact.model");
        bean.setJpaVendorAdapter(new org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter());
        return bean;
    }
}
