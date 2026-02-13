package com.hcd.telecomassistant.service;

import com.hcd.telecomassistant.controller.ChatMessage;
import com.hcd.telecomassistant.controller.ChatMessage.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.DEFAULT_CONVERSATION_ID;

@Service
public class ChatAssistant {

    private static final Logger log = LoggerFactory.getLogger(ChatAssistant.class);

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final Conversation conversation;

    public ChatAssistant(ChatClient.Builder builder,
                         ToolCallbackProvider toolCallbackProvider,
                         Conversation conversation,
                         ChatMemory chatMemory) {
        this.conversation = conversation;
        this.chatMemory = chatMemory;

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
                .forEach(callback -> log.info("Tool callback available: {}", callback.getToolDefinition()));
    }

    public String ask(String request) {
        log.info("Request:\n {}", request);

        var response = chatClient.prompt()
                .user(request)
                .call()
                .content();

        log.info("Response:\n {}", response);
        return response;
    }

    public List<ChatMessage> conversationMessages() {
        //return conversation.messages();
        return chatMemory.get(DEFAULT_CONVERSATION_ID).stream()
                .filter(msg -> msg.getMessageType() == MessageType.USER ||
                        msg.getMessageType() == MessageType.ASSISTANT)
                .map(msg -> new ChatMessage(msg.getMessageType() == MessageType.USER ? Type.USER : ChatMessage.Type.ASSISTANT,
                        msg.getText()))
                .toList();
    }

    public void storeConversationMessage(Type type, String content) {
        conversation.addMessage(type, content);
    }

    public void clearConversationMessages() {
        conversation.clear();
        chatMemory.clear(DEFAULT_CONVERSATION_ID);
    }
}
