package com.hcd.invoiceserver.config.security;

//TODO 7 - Add the security configuration

/*
import com.asentinel.common.orm.OrmOperations;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.config.McpApiKeyConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
 */
public class SecurityConfig {

    /*
    private OrmOperations orm;

    @Autowired
    public void setOrm(OrmOperations orm) {
        this.orm = orm;
    }

    @Bean
    ApiKeyEntityRepository<DbApiKeyEntityRepository.InvoiceApiKeyEntity> apiKeyRepository() {
        return new DbApiKeyEntityRepository(orm);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .with(McpApiKeyConfigurer.mcpServerApiKey(),
                        apiKeyConfig ->
                                apiKeyConfig.apiKeyRepository(apiKeyRepository())
                                        .headerName("invoice-x-api-key"))
                .build();
    }
    */
}
