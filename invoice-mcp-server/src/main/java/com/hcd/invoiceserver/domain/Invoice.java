package com.hcd.invoiceserver.domain;

import com.asentinel.common.orm.FetchType;
import com.asentinel.common.orm.mappers.Child;
import com.asentinel.common.orm.mappers.Column;
import com.asentinel.common.orm.mappers.PkColumn;
import com.asentinel.common.orm.mappers.Table;

import java.time.LocalDate;

@Table("Invoices")
public class Invoice {

    public static final String COL_NUMBER = "Number";
    public static final String COL_STATUS = "Status";
    public static final String COL_TOTAL = "Total";

    @PkColumn("Id")
    private int id;

    @Column(value = COL_NUMBER)
    private String number;

    @Column("Date")
    private LocalDate date;

    @Child(fkName = "VendorId", fetchType = FetchType.LAZY)
    private Vendor vendor;

    @Child(fkName = "ServiceTypeId", fetchType = FetchType.LAZY)
    private ServiceType serviceType;

    @Column(value = COL_STATUS)
    private InvoiceStatus status;

    @Column("Total")
    private double total;

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

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Invoice {" +
                "id=" + id +
                ", number=" + number +
                ", date=" + date +
                ", vendor=" + vendor +
                ", serviceType=" + serviceType +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
