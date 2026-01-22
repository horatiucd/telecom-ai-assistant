package com.hcd.invoiceserver.service;

import com.hcd.invoiceserver.domain.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    void findByPattern() {
        var pattern = "voip";

        List<Invoice> invoices = invoiceService.findByPattern(pattern);
        Assertions.assertTrue(invoices.stream()
                .allMatch(i -> i.getNumber().contains(pattern)));
    }
}
