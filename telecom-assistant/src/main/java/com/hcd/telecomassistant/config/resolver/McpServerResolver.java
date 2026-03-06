package com.hcd.telecomassistant.config.resolver;

import java.net.URI;
import java.util.Optional;

public interface McpServerResolver<T> {

    Optional<T> resolve(URI uri);

    default String id() {
        return getClass().getSimpleName();
    }
}
