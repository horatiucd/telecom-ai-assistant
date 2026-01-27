package com.hcd.telecomassistant.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatMessageHistory {

    enum Role { USER, ASSISTANT }

    public record ChatMessage(Role role, String content, Instant timestamp) {}

    private final List<ChatMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public List<ChatMessage> messages() {
        synchronized (messages) {
            return List.copyOf(messages);
        }
    }

    public void userMessage(String content) {
        messages.add(new ChatMessage(Role.USER, content, Instant.now()));
    }

    public void assistantMessage(String content) {
        messages.add(new ChatMessage(Role.ASSISTANT, content, Instant.now()));
    }

    public void clear() {
        messages.clear();
    }
}

