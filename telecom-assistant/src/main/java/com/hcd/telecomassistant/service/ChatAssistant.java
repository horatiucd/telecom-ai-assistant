package com.hcd.telecomassistant.service;

import com.hcd.telecomassistant.advisor.MessageLoggerAdvisor;
import com.hcd.telecomassistant.advisor.TokenUsageAdvisor;
import com.hcd.telecomassistant.controller.ChatMessage;
import com.hcd.telecomassistant.controller.ChatMessage.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@Service
public class ChatAssistant {

    private static final Logger log = LoggerFactory.getLogger(ChatAssistant.class);

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    private final TokenUsageAdvisor tokenUsageAdvisor;

    public ChatAssistant(ChatClient.Builder builder,
                         ToolCallbackProvider toolCallbackProvider,
                         ChatMemory chatMemory) {
        this.chatMemory = chatMemory;

        tokenUsageAdvisor = new TokenUsageAdvisor(1);

        chatClient = builder
                .defaultSystem("You are a helpful Telecom AI assistant. Provide short, meaningful answers.")
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        tokenUsageAdvisor,
                        new MessageLoggerAdvisor(2))
                .build();

        log.info("Available tools:\n{}",
                Arrays.stream(toolCallbackProvider.getToolCallbacks())
                        .map(ToolCallback::getToolDefinition)
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
    }

    public String ask(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    public List<ChatMessage> conversationMessages() {
        return chatMemory.get(DEFAULT_CONVERSATION_ID).stream()
                .filter(msg -> msg.getMessageType() == MessageType.USER ||
                        msg.getMessageType() == MessageType.ASSISTANT)
                .map(msg -> new ChatMessage(msg.getMessageType() == MessageType.USER ? Type.USER : Type.ASSISTANT,
                        msg.getText()))
                .toList();
    }

    public void clearConversation() {
        chatMemory.clear(DEFAULT_CONVERSATION_ID);
        tokenUsageAdvisor.clearUsage();
    }

    public int totalTokens() {
        return tokenUsageAdvisor.totalTokens();
    }
}
