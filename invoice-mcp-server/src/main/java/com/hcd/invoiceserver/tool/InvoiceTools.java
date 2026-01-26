package com.hcd.invoiceserver.tool;

import com.hcd.invoiceserver.domain.Invoice;
import com.hcd.invoiceserver.domain.InvoiceStatus;
import com.hcd.invoiceserver.service.InvoiceService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceTools {

    private final InvoiceService invoiceService;

    public InvoiceTools(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @McpTool(name = "get-paid-invoices-count",
             description = "Retrieves the number of paid invoices")
    public int countPaidInvoices() {
        return invoiceService.countByStatus(InvoiceStatus.PAID);
    }

    @McpTool(name = "get-paid-invoices-total-amount",
             description = "Retrieves the total amount of all paid invoices")
    public double totalPaidInvoices() {
        return invoiceService.totalByStatus(InvoiceStatus.PAID);
    }

    @McpTool(name = "get-invoices-by-pattern-on-number",
            description = "Retrieves the invoices whose numbers contain the provided pattern")
    public List<Invoice> invoicesBy(@ToolParam(description = "The pattern used for filtering invoices") String pattern) {
        return invoiceService.findByPattern(pattern);
    }
}
