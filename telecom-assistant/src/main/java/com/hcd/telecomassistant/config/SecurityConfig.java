package com.hcd.telecomassistant.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import io.modelcontextprotocol.common.McpTransportContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Map;

@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${invoice.server.api.key.id}")
    private String apiKeyId;

    @Value("${invoice.server.api.key.secret}")
    private String apiKeySecret;

    @Value("${spring.ai.mcp.client.streamable-http.connections.invoice.url}")
    private String invoiceMcpUrl;

    @Bean
    McpSyncClientCustomizer syncClientCustomizer() {
        return (name, spec) -> spec.transportContextProvider(() -> {
            var test = "horatiucd";
            return McpTransportContext.create(Map.of("user-id", test));
        });
    }

    @Bean
    McpSyncHttpClientRequestCustomizer requestCustomizer() {
        return (builder, method, endpoint, body, context) -> {
            log.info("MCP Client request: method={}, endpoint={}, body={}", method, endpoint, body);

            //if (isInvoiceServerRequest(endpoint)) {
                builder.header("telecom-invoice-x-api-key",
                        String.format("%s.%s", apiKeyId, apiKeySecret));
            //}
        };
    }

    private boolean isInvoiceServerRequest(URI endpoint) {
        if (endpoint == null) {
            return false;
        }

        URI invoiceUri;
        try {
            invoiceUri = URI.create(invoiceMcpUrl);
        } catch (IllegalArgumentException ex) {
            log.warn("Wrongly configured invoice MCP server - URL: {}", invoiceMcpUrl, ex);
            return false;
        }

        return sameOrigin(endpoint, invoiceUri);
    }

    private static boolean sameOrigin(URI a, URI b) {
        if (a == null || b == null) {
            return false;
        }
        // match on scheme/host/port
        String aHost = a.getHost();
        String bHost = b.getHost();
        if (aHost == null || bHost == null) {
            return false;
        }
        if (!aHost.equalsIgnoreCase(bHost)) {
            return false;
        }

        String aScheme = a.getScheme();
        String bScheme = b.getScheme();
        if (aScheme != null &&
                bScheme != null &&
                !aScheme.equalsIgnoreCase(bScheme)) {
            return false;
        }

        int aPort = normalizePort(a);
        int bPort = normalizePort(b);
        return aPort == bPort;
    }

    private static int normalizePort(URI uri) {
        int port = uri.getPort();
        if (port != -1) {
            return port;
        }
        String scheme = uri.getScheme();
        if ("https".equalsIgnoreCase(scheme)) {
            return 443;
        }
        if ("http".equalsIgnoreCase(scheme)) {
            return 80;
        }
        return -1;
    }
}
