package com.hcd.telecomassistant.config.resolver;

import com.hcd.telecomassistant.config.ApiKeyHeader;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMcpServerResolverTest {

    private static final ApiKeyHeader HEADER_A = new ApiKeyHeader("X-Key-A", "val-a");
    private static final ApiKeyHeader HEADER_B = new ApiKeyHeader("X-Key-B", "val-b");

    /**
     * Minimal concrete implementation: matches a single URI.
     */
    private static class StubResolver extends AbstractMcpServerResolver<ApiKeyHeader> {

        private final URI targetUri;
        private final ApiKeyHeader header;

        StubResolver(McpServerResolver<ApiKeyHeader> next, URI targetUri, ApiKeyHeader header) {
            super(next);
            this.targetUri = targetUri;
            this.header = header;
        }

        @Override
        protected Optional<ApiKeyHeader> resolveSpecific(URI endpoint) {
            return targetUri.equals(endpoint) ? Optional.of(header) : Optional.empty();
        }
    }

    // --- null URI ---

    @Test
    void resolve_returnsEmpty_whenUriIsNull() {
        var resolver = new StubResolver(null, URI.create("http://a.com"), HEADER_A);

        assertTrue(resolver.resolve(null).isEmpty());
    }

    // --- direct match ---

    @Test
    void resolve_returnsResult_whenResolveSpecificMatches() {
        URI uri = URI.create("http://a.com");
        var resolver = new StubResolver(null, uri, HEADER_A);

        Optional<ApiKeyHeader> result = resolver.resolve(uri);

        assertTrue(result.isPresent());
        assertEquals(HEADER_A, result.get());
    }

    // --- chain delegation ---

    @Test
    void resolve_delegatesToNext_whenCurrentDoesNotMatch() {
        var next = new StubResolver(null, URI.create("http://b.com"), HEADER_B);
        var first = new StubResolver(next, URI.create("http://a.com"), HEADER_A);

        Optional<ApiKeyHeader> result = first.resolve(URI.create("http://b.com"));

        assertTrue(result.isPresent());
        assertEquals(HEADER_B, result.get());
    }

    @Test
    void resolve_returnsEmpty_whenNoNextConfigured_andCurrentDoesNotMatch() {
        var resolver = new StubResolver(null, URI.create("http://a.com"), HEADER_A);

        assertTrue(resolver.resolve(URI.create("http://unknown.com")).isEmpty());
    }

    @Test
    void resolve_returnsEmpty_whenEntireChainExhausted() {
        var last = new StubResolver(null, URI.create("http://b.com"), HEADER_B);
        var first = new StubResolver(last, URI.create("http://a.com"), HEADER_A);

        assertTrue(first.resolve(URI.create("http://unknown.com")).isEmpty());
    }

    @Test
    void resolve_returnsFirstMatch_inChain() {
        var last = new StubResolver(null, URI.create("http://a.com"), HEADER_B);
        var first = new StubResolver(last, URI.create("http://a.com"), HEADER_A);

        // Both match the same URI; the first in the chain wins
        Optional<ApiKeyHeader> result = first.resolve(URI.create("http://a.com"));

        assertTrue(result.isPresent());
        assertEquals(HEADER_A, result.get());
    }

    // --- id ---

    @Test
    void id_returnsConcreteClassName() {
        var resolver = new StubResolver(null, URI.create("http://a.com"), HEADER_A);

        assertEquals("StubResolver", resolver.id());
    }
}
