package com.hcd.telecomassistant.config.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

abstract class AbstractMcpServerResolver<T> implements McpServerResolver<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMcpServerResolver.class);

    private final McpServerResolver<T> next;

    protected AbstractMcpServerResolver(McpServerResolver<T> next) {
        this.next = next;
    }

    @Override
    public Optional<T> resolve(URI uri) {
        if (uri == null) {
            return Optional.empty();
        }

        log.info("[{}]: Checking request towards {}.", id(), uri);
        Optional<T> result = resolveSpecific(uri);
        if (result.isPresent()) {
            log.info("[{}]: Resolved target endpoint {}.", id(), uri);
            return result;
        }

        if (next == null) {
            log.info("[{}]: No next resolver configured.", id());
            return Optional.empty();
        }
        log.info("[{}]: Target endpoint {} not resolved. Delegating to [{}].", id(), uri, next.id());
        return next.resolve(uri);
    }

    protected abstract Optional<T> resolveSpecific(URI endpoint);
}
