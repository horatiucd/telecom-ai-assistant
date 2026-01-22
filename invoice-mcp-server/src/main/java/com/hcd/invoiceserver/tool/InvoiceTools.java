package com.hcd.invoiceserver.tool;

import com.hcd.invoiceserver.domain.Invoice;
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

    @McpTool(name = "get-invoices-by-pattern",
            description = "Retrieves the invoices whose numbers contain the provided pattern")
    public List<Invoice> invoicesBy(@ToolParam(description = "The pattern used for filtering invoices") String pattern) {
        return invoiceService.findByPattern(pattern);
    }
}
