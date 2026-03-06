package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;

public class InvoiceMcpServerResolver extends UrlMcpServerResolver {

    public InvoiceMcpServerResolver(McpServerResolver<ApiKeyHeader> next,
                                    String serverUrl,
                                    ApiKeyHeader header) {
        super(next, serverUrl, header);
    }
}
