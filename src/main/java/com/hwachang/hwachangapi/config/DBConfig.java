package com.hwachang.hwachangapi.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig implements TransactionManagementConfigurer {

    @Value("${POSTGRES_DRIVER_CLASS")
    private String driverClassName;

    @Value("${POSTGRES_URL")
    private String url;

    @Value("${POSTGRES_PASSWORD}")
    private String user;

    @Value("${POSTGRES_PASSWORD}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return null;
    }
}
