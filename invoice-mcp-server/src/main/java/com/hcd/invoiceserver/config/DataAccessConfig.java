package com.hcd.invoiceserver.config;

import com.asentinel.common.orm.config.EnableAsentinelOrm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
@EnableAsentinelOrm
public class DataAccessConfig {

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") String url,
                                 @Value("${spring.datasource.username}") String username,
                                 @Value("${spring.datasource.password}") String password) {
        return new SingleConnectionDataSource(url, username, password, false);
    }
}
