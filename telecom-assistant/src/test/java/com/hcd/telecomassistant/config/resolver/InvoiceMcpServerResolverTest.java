package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceMcpServerResolverTest {

    @Test
    void id_returnsInvoiceMcpServerResolver() {
        var resolver = new InvoiceMcpServerResolver(null, "http://invoice-server:8081/mcp",
                new ApiKeyHeader("X-Invoice-Key", "inv-123"));

        assertEquals("InvoiceMcpServerResolver", resolver.id());
    }
}
