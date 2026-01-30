package com.hcd.telecomassistant.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import io.modelcontextprotocol.common.McpTransportContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${mcp.server.invoice.api.key.id}")
    private String invoiceMcpApiKeyId;

    @Value("${mcp.server.invoice.api.key.secret}")
    private String invoiceMcpApiKeySecret;

    @Value("${spring.ai.mcp.client.streamable-http.connections.invoice.url}")
    private String invoiceMcpUrl;

    @Bean
    McpSyncClientCustomizer syncClientCustomizer() {
        return (name, spec) -> spec.transportContextProvider(() ->
            McpTransportContext.create(Map.of("user", "horatiu")));
    }

    @Bean
    McpServerResolver<ApiKeyHeader> serverResolver() {
        return new UrlMcpServerResolver(1,null,
                invoiceMcpUrl,
                new ApiKeyHeader("invoice-x-api-key",
                        String.format("%s.%s", invoiceMcpApiKeyId, invoiceMcpApiKeySecret))
        );
    }

    @Bean
    McpSyncHttpClientRequestCustomizer requestCustomizer() {
        return (builder, method, endpoint, body, context) -> {
            log.info("MCP Client request: method={}, endpoint={}, body={}", method, endpoint, body);

            // this tenant might use multiple MCP servers, at every request,
            // determine: the MCP server name, the MCP server api key id and the MCP server api key secret
            serverResolver()
                    .resolve(endpoint)
                    .ifPresent(apiKeyHeader -> builder.header(apiKeyHeader.name(), apiKeyHeader.value()));
        };
    }
}
