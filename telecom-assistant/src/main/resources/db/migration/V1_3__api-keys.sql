DROP TABLE IF EXISTS ServerApiKeys CASCADE;
CREATE TABLE IF NOT EXISTS ServerApiKeys (
    Id SERIAL PRIMARY KEY,
    Server VARCHAR NOT NULL,
    KeyId VARCHAR NOT NULL,
    KeySecret VARCHAR NOT NULL,
CONSTRAINT UNQ_ServerApiKeys_Server_KeyId UNIQUE(Server, KeyId));

ALTER TABLE ServerApiKeys DROP CONSTRAINT IF EXISTS CHK_ServerApiKeys_Server;
ALTER TABLE ServerApiKeys
    ADD CONSTRAINT CHK_ServerApiKeys_Server CHECK (Server IN ('invoice-mcp'));

