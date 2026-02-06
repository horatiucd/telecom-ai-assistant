package com.hcd.telecomassistant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

public abstract class McpServerResolver<T> {

    private static final Logger log = LoggerFactory.getLogger(McpServerResolver.class);

    private final int order;
    private final McpServerResolver<T> nextResolver;

    protected McpServerResolver(int order,
                                McpServerResolver<T> nextResolver) {
        this.order = order;
        this.nextResolver = nextResolver;
    }

    public Optional<T> resolve(URI uri) {
        if (uri == null) {
            return Optional.empty();
        }

        log.info("[Resolver {}]: Checking request towards {}.", order, uri);
        Optional<T> result = resolveSpecific(uri);
        if (result.isPresent()) {
            log.info("[Resolver {}]: MCP server resolved for URI {}.", order, uri);
            return result;
        }

        if (nextResolver == null) {
            log.info("[Resolver {}]: No next resolver configured.", order);
            return Optional.empty();
        }
        log.info("[Resolver {}]: MCP server not resolved for URI {}. Delegating to [Resolver {}].",
                order, uri, nextResolver.order);
        return nextResolver.resolve(uri);
    }

    protected abstract Optional<T> resolveSpecific(URI endpoint);

    protected int order() {
        return order;
    }
}
