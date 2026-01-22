package com.hcd.invoiceserver.domain;

import com.asentinel.common.orm.mappers.Column;
import com.asentinel.common.orm.mappers.PkColumn;
import com.asentinel.common.orm.mappers.Table;

import java.time.LocalDate;

@Table("Invoices")
public class Invoice {

    public static final String COL_NUMBER = "number";

    @PkColumn("id")
    private int id;

    @Column(value = COL_NUMBER)
    private String number;

    @Column("date")
    private LocalDate date;

    @Column("vendor")
    private Vendor vendor;

    @Column("service")
    private Service service;

    @Column("status")
    private InvoiceStatus status;

    @Column("amount")
    private double amount;

    // needed by the ORM
    protected Invoice() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Invoice {" +
                "id=" + id +
                ", number=" + number +
                ", date=" + date +
                ", vendor=" + vendor +
                ", service=" + service +
                ", status=" + status +
                ", amount=" + amount +
                '}';
    }
}
