package com.hcd.telecomassistant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ChatAssistant {

    private static final Logger log = LoggerFactory.getLogger(ChatAssistant.class);

    private final ChatClient chatClient;

    public ChatAssistant(ChatClient.Builder builder,
                         ToolCallbackProvider toolCallbackProvider,
                         ChatMemory chatMemory) {
        chatClient = builder
                .defaultSystem("""
                        You are a helpful Telecom AI assistant.
                        Provide short, meaningful answers.
                    """)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).order(0).build(),
                        SimpleLoggerAdvisor.builder().order(1).build())
                .build();

        Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .forEach(toolCallback -> log.info("Tool callback available: {}", toolCallback.getToolDefinition()));
    }

    public String call(String request) {
        log.info("Request:\n {}", request);

        var response = chatClient.prompt()
                .user(request)
                .call()
                .content();

        log.info("Response:\n {}", response);
        return response;
    }
}
