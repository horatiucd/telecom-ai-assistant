package com.hcd.vendorserver.server;

import org.springframework.stereotype.Service;

@Service
public class VendorService {

    public String infoByName(String name) {
        if (name == null) {
            name = "";
        }
        return switch (name.toLowerCase()) {
            case "verizon" -> "Leading provider of tech solutions.";
            case "vodafone" -> "Specializes in cloud services.";
            case "orange" -> "Expert in cybersecurity.";
            case "att" -> "Focuses on 5G technology.";
            default -> "No info available";
        };
    }
}
