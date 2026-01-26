package com.hcd.invoiceserver.domain;

import com.asentinel.common.orm.mappers.Column;
import com.asentinel.common.orm.mappers.PkColumn;
import com.asentinel.common.orm.mappers.Table;

@Table("ServiceTypes")
public class ServiceType {

    @PkColumn("id")
    private int id;

    @Column("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "ServiceType {" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
