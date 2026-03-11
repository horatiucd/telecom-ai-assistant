package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VendorMcpServerResolverTest {

    private static final String SERVER_URL = "http://vendor-server:8082/mcp";
    private static final ApiKeyHeader HEADER = new ApiKeyHeader("X-Vendor-Key", "vnd-456");

    @Test
    void resolve_returnsHeader_whenUriMatchesServerUrl() {
        var resolver = new VendorMcpServerResolver(null, SERVER_URL, HEADER);

        Optional<ApiKeyHeader> result = resolver.resolve(URI.create(SERVER_URL));

        assertTrue(result.isPresent());
        assertEquals(HEADER, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenUriDoesNotMatch() {
        var resolver = new VendorMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("http://other-server:9090/mcp")).isEmpty());
    }

    @Test
    void resolve_delegatesToNext_whenCurrentDoesNotMatch() {
        ApiKeyHeader fallbackHeader = new ApiKeyHeader("X-Fallback", "fb-val");
        var fallback = new UrlMcpServerResolver(null, "http://fallback:7070/mcp", fallbackHeader);
        var resolver = new VendorMcpServerResolver(fallback, SERVER_URL, HEADER);

        Optional<ApiKeyHeader> result = resolver.resolve(URI.create("http://fallback:7070/mcp"));

        assertTrue(result.isPresent());
        assertEquals(fallbackHeader, result.get());
    }

    @Test
    void id_returnsVendorMcpServerResolver() {
        var resolver = new VendorMcpServerResolver(null, SERVER_URL, HEADER);

        assertEquals("VendorMcpServerResolver", resolver.id());
    }
}
