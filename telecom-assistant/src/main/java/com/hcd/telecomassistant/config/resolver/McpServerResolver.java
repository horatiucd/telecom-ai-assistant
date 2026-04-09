package com.hcd.telecomassistant.config.resolver;

import java.net.URI;
import java.util.Optional;

//TODO 9: Create the chain of server resolvers
public interface McpServerResolver<T> {

    Optional<T> resolve(URI uri);

    default String id() {
        return getClass().getSimpleName();
    }
}
