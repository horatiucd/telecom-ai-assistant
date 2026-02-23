package com.hcd.telecomassistant.service;

import com.hcd.telecomassistant.advisor.TokenUsageAdvisor;
import com.hcd.telecomassistant.controller.ChatMessage;
import com.hcd.telecomassistant.controller.ChatMessage.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@Service
public class ChatAssistant {

    private static final Logger log = LoggerFactory.getLogger(ChatAssistant.class);

    private final AtomicInteger conversationTokens = new AtomicInteger(0);

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ChatAssistant(ChatClient.Builder builder,
                         ToolCallbackProvider toolCallbackProvider,
                         ChatMemory chatMemory) {
        this.chatMemory = chatMemory;

        chatClient = builder
                .defaultSystem("""
                        You are a helpful Telecom AI assistant.
                        Provide short, meaningful answers.
                    """)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
                                .order(0)
                                .build(),
                        TokenUsageAdvisor.builder()
                                .order(1)
                                .build(),
                        SimpleLoggerAdvisor.builder()
                                .order(2)
                                .build())
                .build();

        Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .forEach(callback -> log.info("Tool callback available: {}", callback.getToolDefinition()));
    }

    public String ask(String question) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                .call()
                .chatResponse();

        String text = Optional.ofNullable(chatResponse)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AbstractMessage::getText)
                .orElse(null);

        int tokens = Optional.ofNullable(chatResponse)
                .map(ChatResponse::getMetadata)
                .map(ChatResponseMetadata::getUsage)
                .map(Usage::getTotalTokens)
                .orElse(0);
        conversationTokens.addAndGet(tokens);

        return text;
    }

    public List<ChatMessage> conversationMessages() {
        return chatMemory.get(DEFAULT_CONVERSATION_ID).stream()
                .filter(msg -> msg.getMessageType() == MessageType.USER ||
                        msg.getMessageType() == MessageType.ASSISTANT)
                .map(msg -> new ChatMessage(msg.getMessageType() == MessageType.USER ? Type.USER : Type.ASSISTANT,
                        msg.getText()))
                .toList();
    }

    public int conversationTokens() {
        return conversationTokens.get();
    }

    public void clearConversation() {
        chatMemory.clear(DEFAULT_CONVERSATION_ID);
        conversationTokens.set(0);
    }
}
