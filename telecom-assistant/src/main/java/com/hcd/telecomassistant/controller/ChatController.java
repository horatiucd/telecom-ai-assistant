package com.hcd.telecomassistant.controller;

import com.hcd.telecomassistant.service.ChatAssistant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatAssistant chatAssistant;

    public ChatController(ChatAssistant chatAssistant) {
        this.chatAssistant = chatAssistant;
    }

    @RequestMapping("/ask")
    public String ask(@RequestParam String question) {
        return chatAssistant.call(question);
    }
}
