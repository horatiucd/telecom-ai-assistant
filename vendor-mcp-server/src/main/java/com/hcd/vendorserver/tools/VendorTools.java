package com.hcd.vendorserver.tools;

import com.hcd.vendorserver.server.VendorService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

@Component
public class VendorTools {

    private final VendorService vendorService;

    public VendorTools(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @McpTool(name = "get-vendor-information",
             description = "Provides information about the vendor with the provided name")
    public String vendorInfo(String name) {
        return vendorService.infoByName(name);
    }
}
