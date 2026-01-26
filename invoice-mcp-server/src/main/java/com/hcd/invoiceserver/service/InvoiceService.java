package com.hcd.invoiceserver.service;

import com.asentinel.common.orm.AutoEagerLoader;
import com.asentinel.common.orm.EntityDescriptorNodeCallback;
import com.asentinel.common.orm.OrmOperations;
import com.hcd.invoiceserver.domain.Invoice;
import com.hcd.invoiceserver.domain.InvoiceStatus;
import com.hcd.invoiceserver.domain.ServiceType;
import com.hcd.invoiceserver.domain.Vendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final OrmOperations orm;

    public InvoiceService(OrmOperations orm) {
        this.orm = orm;
    }

    @Transactional(readOnly = true)
    public int countByStatus(InvoiceStatus status) {
        log.info("Counting invoices in status '{}'.", status);
        return orm.newSqlBuilder(Invoice.class)
                .selectK().countId()
                .from(EntityDescriptorNodeCallback.rootOnlyQuery())
                .where()
                    .column(Invoice.COL_STATUS).eq(status.name())
                .execForInt();
    }

    @Transactional(readOnly = true)
    public Double totalByStatus(InvoiceStatus status) {
        log.info("Computing the total amount of invoices in status '{}'.", status);
        return orm.newSqlBuilder(Invoice.class)
                .selectK().sql("sum").lp().column(Invoice.COL_TOTAL).rp()
                .from(EntityDescriptorNodeCallback.rootOnlyQuery())
                .where()
                    .column(Invoice.COL_STATUS).eq(status.name())
                .execForObject(Double.class);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findByPattern(String pattern) {
        log.info("Retrieving invoices containing '{}' in their number.", pattern);
        return orm.newSqlBuilder(Invoice.class)
                .select(AutoEagerLoader.forPath(Invoice.class, Vendor.class),
                        AutoEagerLoader.forPath(Invoice.class, ServiceType.class))
                .where()
                    .column(Invoice.COL_NUMBER).like('%' + pattern + '%')
                .exec();
    }
}
