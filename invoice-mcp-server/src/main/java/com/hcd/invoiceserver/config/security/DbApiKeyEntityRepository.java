package com.hcd.invoiceserver.config.security;

//TODO 6.1 - Implement the API key entity repository

/*
import com.asentinel.common.orm.OrmOperations;
import com.hcd.invoiceserver.domain.ServerApiKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntity;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntityRepository;
*/

public class DbApiKeyEntityRepository /*implements ApiKeyEntityRepository<DbApiKeyEntityRepository.InvoiceApiKeyEntity>*/ {

    /*
    private final OrmOperations orm;

    public DbApiKeyEntityRepository(OrmOperations orm) {
        this.orm = orm;
    }

    @Override
    public InvoiceApiKeyEntity findByKeyId(@NonNull String keyId) {
        return orm.newSqlBuilder(ServerApiKey.class)
                .select()
                .where()
                    .column(ServerApiKey.COL_SERVER).eq("invoice-mcp").and()
                    .column(ServerApiKey.COL_KEY_ID).eq(keyId)
                .execForOptional()
                .map(serverApiKey ->
                        new InvoiceApiKeyEntity(keyId, serverApiKey.getKeySecret()))
                .orElse(null);
    }

    public static final class InvoiceApiKeyEntity implements ApiKeyEntity {

        private final String id;

        @Nullable
        private String secret;

        private InvoiceApiKeyEntity(String id, @Nullable String secret) {
            this.id = id;
            this.secret = secret;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public @Nullable String getSecret() {
            return secret;
        }

        @Override
        public void eraseCredentials() {
            this.secret = null;
        }

        @Override
        public InvoiceApiKeyEntity copy() {
            return new InvoiceApiKeyEntity(id, secret);
        }
    }
    */
}
