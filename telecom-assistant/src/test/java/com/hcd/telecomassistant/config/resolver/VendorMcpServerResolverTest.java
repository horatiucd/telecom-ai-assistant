package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendorMcpServerResolverTest {

    @Test
    void id_returnsVendorMcpServerResolver() {
        var resolver = new VendorMcpServerResolver(null, "http://vendor-server:8082/mcp",
                new ApiKeyHeader("X-Vendor-Key", "vnd-456"));

        assertEquals("VendorMcpServerResolver", resolver.id());
    }
}
