package com.hcd.telecomassistant.config.resolver;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class McpServerResolverTest {

    @Test
    void id_returnsSimpleClassName_forAnonymousImplementation() {
        McpServerResolver<String> resolver = _ -> Optional.empty();

        assertEquals(resolver.getClass().getSimpleName(), resolver.id());
    }

    @Test
    void id_returnsSimpleClassName_forNamedImplementation() {
        class NamedResolver implements McpServerResolver<String> {
            @Override
            public Optional<String> resolve(URI uri) {
                return Optional.empty();
            }
        }

        McpServerResolver<String> resolver = new NamedResolver();

        assertEquals("NamedResolver", resolver.id());
    }
}
