package com.hcd.vendorserver.config;

//TODO 3 - Add the security configuration
/*
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntity;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.apikey.memory.ApiKeyEntityImpl;
import org.springaicommunity.mcp.security.server.apikey.memory.InMemoryApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.config.McpApiKeyConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableWebSecurity
@Configuration
 */
public class SecurityConfig {
/*
    @Value("${api.key.id}")
    private String apiKeyId;

    @Value("${api.key.secret}")
    private String apiKeySecret;

    @Bean
    ApiKeyEntityRepository<ApiKeyEntity> apiKeyRepository() {
        ApiKeyEntity apiKey = ApiKeyEntityImpl.builder()
                .id(apiKeyId)
                .secret(apiKeySecret)
                .name("API key")
                .build();

        return new InMemoryApiKeyEntityRepository<>(List.of(apiKey));
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .with(McpApiKeyConfigurer.mcpServerApiKey(),
                        apiKeyConfig ->
                                apiKeyConfig.apiKeyRepository(apiKeyRepository())
                                        .headerName("vendor-x-api-key"))
                .build();
    }
*/
}
