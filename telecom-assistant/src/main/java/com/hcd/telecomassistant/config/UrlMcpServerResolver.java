package com.hcd.telecomassistant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

public class UrlMcpServerResolver extends McpServerResolver<ApiKeyHeader> {

    private static final Logger log = LoggerFactory.getLogger(UrlMcpServerResolver.class);

    private final String serverUrl;
    private final ApiKeyHeader header;

    public UrlMcpServerResolver(int order,
                                McpServerResolver<ApiKeyHeader> nextResolver,
                                String serverUrl,
                                ApiKeyHeader header) {
        super(order, nextResolver);
        this.serverUrl = serverUrl;
        this.header = header;
    }

    @Override
    protected Optional<ApiKeyHeader> resolveSpecific(URI uri) {
        if (isServerRequest(uri)) {
            log.info("[Resolver {}]: Request URI '{}' is for MCP server at URL '{}'.", order(), uri, serverUrl);
            return Optional.of(header);
        }
        log.info("[Resolver {}]: Request URI '{}' isn't for MCP server at URL '{}'.", order(), uri, serverUrl);
        return Optional.empty();
    }

    boolean isServerRequest(URI uri) {
        if (uri == null) {
            return false;
        }

        URI serverUri;
        try {
            serverUri = URI.create(serverUrl);
        } catch (IllegalArgumentException ex) {
            log.warn("Unreachable MCP server at URL '{}'.", serverUrl, ex);
            return false;
        }

        return isSameOrigin(uri, serverUri);
    }

    /**
     * Match on scheme/host/port/path.
     */
    static boolean isSameOrigin(URI uri1, URI uri2) {
        if (uri1 == null || uri2 == null) {
            return false;
        }

        String host1 = uri1.getHost();
        String host2 = uri2.getHost();
        if (host1 == null || host2 == null) {
            return false;
        }
        if (!host1.equalsIgnoreCase(host2)) {
            return false;
        }

        String scheme1 = uri1.getScheme();
        String scheme2 = uri2.getScheme();
        if (scheme1 != null && scheme2 != null && !scheme1.equalsIgnoreCase(scheme2)) {
            return false;
        }

        int port1 = normalizePort(uri1);
        int port2 = normalizePort(uri2);
        if (port1 != port2) {
            return false;
        }

        String path1 = uri1.getPath();
        String path2 = uri2.getPath();
        if (scheme1 == null && scheme2 != null) {
            return false;
        }
        return path1.equals(path2);
    }

    static int normalizePort(URI uri) {
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
