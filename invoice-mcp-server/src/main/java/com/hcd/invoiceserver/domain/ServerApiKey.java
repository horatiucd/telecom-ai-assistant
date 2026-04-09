package com.hcd.invoiceserver.domain;

import com.asentinel.common.orm.mappers.Column;
import com.asentinel.common.orm.mappers.PkColumn;
import com.asentinel.common.orm.mappers.Table;

//TODO 5 - Add the API key entity
//@Table("ServerApiKeys")
public class ServerApiKey {

    public static final String COL_SERVER = "Server";
    public static final String COL_KEY_ID = "KeyId";

    public static final String ON_CONFLICT_CLAUSE = String.format("(%s,%s)", COL_SERVER, COL_KEY_ID);

    @PkColumn("Id")
    private int id;

    @Column(COL_SERVER)
    private String server;

    @Column(COL_KEY_ID)
    private String keyId;

    @Column("KeySecret")
    private String keySecret;

    public ServerApiKey() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }
}
