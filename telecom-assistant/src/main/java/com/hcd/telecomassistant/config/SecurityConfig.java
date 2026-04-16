package com.hcd.telecomassistant.config;

import com.hcd.telecomassistant.config.props.McpServerApiKeyProperties;
import com.hcd.telecomassistant.config.resolver.InvoiceMcpServerResolver;
import com.hcd.telecomassistant.config.resolver.McpServerResolver;
import com.hcd.telecomassistant.config.resolver.VendorMcpServerResolver;
import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.client.common.autoconfigure.properties.McpStreamableHttpClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO 11 - Security configuration
//@Configuration
@EnableConfigurationProperties({McpServerApiKeyProperties.class})
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public McpStreamableHttpClientProperties mcpClientProps;
    public McpServerApiKeyProperties mcpServerApiKeys;

    //@Autowired
    public void setMcpClientProps(McpStreamableHttpClientProperties mcpClientProps) {
        this.mcpClientProps = mcpClientProps;
    }

    //@Autowired
    public void setMcpServerApiKeys(McpServerApiKeyProperties mcpServerApiKeys) {
        this.mcpServerApiKeys = mcpServerApiKeys;
    }

    @Bean
    ApiKeyHeader invoiceApiKeyHeader() {
        var apiKey = mcpServerApiKeys.getParameters().get("invoice");
        return new ApiKeyHeader("invoice-x-api-key",
                String.format("%s.%s", apiKey.id(), apiKey.secret()));
    }

    @Bean
    ApiKeyHeader vendorApiKeyHeader() {
        var apiKey = mcpServerApiKeys.getParameters().get("vendor");
        return new ApiKeyHeader("vendor-x-api-key",
                String.format("%s.%s", apiKey.id(), apiKey.secret()));
    }

    @Bean
    McpServerResolver<ApiKeyHeader> serverResolver() {
        var mcpProps = mcpClientProps.getConnections();
        var mcpInvoice = mcpProps.get("invoice");
        var mcpVendor = mcpProps.get("vendor");

        return new VendorMcpServerResolver(new InvoiceMcpServerResolver(null,
                                            String.format("%s%s", mcpInvoice.url(), mcpInvoice.endpoint()),
                                            invoiceApiKeyHeader()),
                String.format("%s%s", mcpVendor.url(), mcpVendor.endpoint()),
                vendorApiKeyHeader()
        );
    }

    @Bean
    McpSyncHttpClientRequestCustomizer requestCustomizer() {
        return (builder, method, endpoint, body, context) -> {
            log.info("MCP Client request: method={}, endpoint={}, body={}", method, endpoint, body);

            serverResolver()
                    .resolve(endpoint)
                    .ifPresent(apiKeyHeader -> builder.header(apiKeyHeader.name(), apiKeyHeader.value()));
        };
    }
}
