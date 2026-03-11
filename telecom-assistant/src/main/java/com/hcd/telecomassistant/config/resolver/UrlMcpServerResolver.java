package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

public class UrlMcpServerResolver extends AbstractMcpServerResolver<ApiKeyHeader> {

    private static final Logger log = LoggerFactory.getLogger(UrlMcpServerResolver.class);

    private final URI serverUri;
    private final ApiKeyHeader header;

    public UrlMcpServerResolver(McpServerResolver<ApiKeyHeader> nextResolver,
                                String serverUrl,
                                ApiKeyHeader header) {
        super(nextResolver);
        this.serverUri = URI.create(serverUrl);
        this.header = header;
    }

    @Override
    protected Optional<ApiKeyHeader> resolveSpecific(URI endpoint) {
        if (serverUri.equals(endpoint)) {
            log.debug("[{}]: Target endpoint {} and config URL {} match.", id(), endpoint, serverUri);
            return Optional.of(header);
        }
        log.debug("[{}]: Target endpoint {} and config URL {} don't match.", id(), endpoint, serverUri);
        return Optional.empty();
    }
}
