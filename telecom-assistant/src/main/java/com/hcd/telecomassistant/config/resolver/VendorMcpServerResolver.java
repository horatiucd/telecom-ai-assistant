package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;

public class VendorMcpServerResolver extends UrlMcpServerResolver {

    public VendorMcpServerResolver(McpServerResolver<ApiKeyHeader> next,
                                   String serverUrl,
                                   ApiKeyHeader header) {
        super(next, serverUrl, header);
    }
}
