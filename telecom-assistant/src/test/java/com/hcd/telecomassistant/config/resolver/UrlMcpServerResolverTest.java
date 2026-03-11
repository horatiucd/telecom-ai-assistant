package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UrlMcpServerResolverTest {

    private static final String SERVER_URL = "http://localhost:8080/api";
    private static final ApiKeyHeader HEADER = new ApiKeyHeader("X-Api-Key", "secret-123");

    private UrlMcpServerResolver singleResolver;

    @BeforeEach
    void setUp() {
        singleResolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);
    }

    @Test
    void constructor_throwsException_whenServerUrlIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new UrlMcpServerResolver(null, "://bad url", HEADER));
    }

    @Test
    void resolve_returnsHeader_whenEndpointMatchesServerUri() {
        var uri = URI.create(SERVER_URL);
        Optional<ApiKeyHeader> result = singleResolver.resolve(uri);

        assertTrue(result.isPresent());
        assertEquals(HEADER, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenHostDiffers() {
        var uri = URI.create("http://other-host:8080/api");

        assertTrue(singleResolver.resolve(uri).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenPortDiffers() {
        var uri = URI.create("http://localhost:9090/api");

        assertTrue(singleResolver.resolve(uri).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenPathDiffers() {
        var uri = URI.create("http://localhost:8080/other");

        assertTrue(singleResolver.resolve(uri).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenSchemeDiffers() {
        var resolver = new UrlMcpServerResolver(null, SERVER_URL, HEADER);

        assertTrue(resolver.resolve(URI.create("https://localhost:8080/api")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenUriIsNull() {
        assertTrue(singleResolver.resolve(null).isEmpty());
    }

    @Test
    void resolve_delegatesToNext_whenCurrentDoesNotMatch() {
        ApiKeyHeader nextHeader = new ApiKeyHeader("X-Next", "val");
        var next = new UrlMcpServerResolver(null, "http://fallback:9090/path", nextHeader);
        var resolver = new UrlMcpServerResolver(next, SERVER_URL, HEADER);

        var uri = URI.create("http://fallback:9090/path");

        Optional<ApiKeyHeader> result = resolver.resolve(uri);

        assertTrue(result.isPresent());
        assertEquals(nextHeader, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenNeitherCurrentNorNextMatch() {
        var next = new UrlMcpServerResolver(null, "http://fallback:9090/path", HEADER);
        var resolver = new UrlMcpServerResolver(next, SERVER_URL, HEADER);

        var uri = URI.create("http://fallback:9090/other");

        assertTrue(resolver.resolve(uri).isEmpty());
    }

    @Test
    void id_returnsUrlMcpServerResolver() {
        assertEquals("UrlMcpServerResolver", singleResolver.id());
    }
}
