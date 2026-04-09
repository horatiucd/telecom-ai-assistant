package com.hcd.telecomassistant.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for MCP servers' API keys.
 *
 * <p>
 * These properties allow configuring the API key details for multiple MCP servers
 * secured as such. Each server is configured with an API key {@code id} and {@code secret}.
 *
 * <p>
 * Example configuration: <pre>
 * mcp.server.api-key:
 *   parameters:
 *     server1:
 *       id: id-1
 *       secret: secret-1
 *     server2:
 *       id: id-1
 *       secret: secret-2
 * </pre>
 *
 * @see ApiKeyParams
 */
//TODO 8.2 - MCP server API keys as properties
//@ConfigurationProperties(McpServerApiKeyProperties.CONFIG_PREFIX)
public class McpServerApiKeyProperties {

    public static final String CONFIG_PREFIX = "mcp.server.api-key";

    private final Map<String, ApiKeyParams> parameters = new HashMap<>();

    public Map<String, ApiKeyParams> getParameters() {
        return parameters;
    }

    public record ApiKeyParams(String id, String secret) {}
}
