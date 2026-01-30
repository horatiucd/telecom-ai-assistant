package com.hcd.invoiceserver.config.security;

import com.asentinel.common.jdbc.flavors.postgres.PostgresJdbcFlavor;
import com.asentinel.common.orm.OrmOperations;
import com.hcd.invoiceserver.domain.ServerApiKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DbApiKeyEntityRepositoryTest {

    @Autowired
    private DbApiKeyEntityRepository apiKeyRepository;

    @Autowired
    private OrmOperations orm;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void provisionServerApiKey() {
        ServerApiKey serverApiKey = new ServerApiKey();
        serverApiKey.setServer("invoice-mcp");
        serverApiKey.setKeyId("api-key-id");
        serverApiKey.setKeySecret(passwordEncoder.encode("api-key-secret"));

        orm.upsert(serverApiKey,
                PostgresJdbcFlavor.UPSERT_CONFLICT_PLACEHOLDER, ServerApiKey.ON_CONFLICT_CLAUSE);

        DbApiKeyEntityRepository.InvoiceApiKeyEntity apiKey = apiKeyRepository.findByKeyId(serverApiKey.getKeyId());
        Assertions.assertNotNull(apiKey);
        Assertions.assertEquals(serverApiKey.getKeyId(), apiKey.getId());
        Assertions.assertEquals(serverApiKey.getKeySecret(), apiKey.getSecret());
    }
}
