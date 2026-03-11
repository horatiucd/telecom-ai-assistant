package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UrlMcpServerResolverTest {

    private static final String SERVER_URL = "http://localhost:8080/api";
    private static final ApiKeyHeader HEADER = new ApiKeyHeader("X-Api-Key", "secret-123");

    // --- constructor ---

    @Test
    void constructor_parsesServerUrl() {
        UrlMcpServerResolver resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        // Successfully constructed — no exception
        assertNotNull(resolver);
    }

    @Test
    void constructor_throwsException_whenServerUrlIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new UrlMcpServerResolver(null, "://bad url", HEADER));
    }

    // --- resolveSpecific (via resolve) ---

    @Test
    void resolve_returnsHeader_whenEndpointMatchesServerUri() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        Optional<ApiKeyHeader> result = resolver.resolve(URI.create(SERVER_URL));

        assertTrue(result.isPresent());
        assertEquals(HEADER, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenHostDiffers() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("http://other-host:8080/api")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenPortDiffers() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("http://localhost:9090/api")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenPathDiffers() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("http://localhost:8080/other")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenSchemeDiffers() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("https://localhost:8080/api")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenUriIsNull() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(null).isEmpty());
    }

    // --- chain delegation ---

    @Test
    void resolve_delegatesToNext_whenCurrentDoesNotMatch() {
        ApiKeyHeader nextHeader = new ApiKeyHeader("X-Next", "val");
        var next = new UrlMcpServerResolver(null, "http://fallback:9090/path", nextHeader);
        var resolver = new UrlMcpServerResolver(next, SERVER_URL, HEADER);

        Optional<ApiKeyHeader> result = resolver.resolve(URI.create("http://fallback:9090/path"));

        assertTrue(result.isPresent());
        assertEquals(nextHeader, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenNeitherCurrentNorNextMatch() {
        var next = new UrlMcpServerResolver(null, "http://fallback:9090/path", HEADER);
        var resolver = new UrlMcpServerResolver(next, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("http://unknown:1234/x")).isEmpty());
    }

    // --- id ---

    @Test
    void id_returnsUrlMcpServerResolver() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertEquals("UrlMcpServerResolver", resolver.id());
    }
}
