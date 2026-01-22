package com.hcd.telecomassistant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistantController {

    private static final Logger log = LoggerFactory.getLogger(AssistantController.class);

    private final ChatClient chatClient;

    public AssistantController(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        chatClient = builder
                .defaultSystem("You are a helpful Telecom AI assistant, provide short, meaningful answers.")
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @RequestMapping("/ask")
    public String ask(@RequestParam String question) {
        log.info("Question:\n {}", question);

        var response = chatClient.prompt()
                .user(question)
                .call()
                .content();

        log.info("Answer:\n {}", response);
        return response;
    }
}
