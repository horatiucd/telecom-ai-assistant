package com.hcd.telecomassistant.service;

import com.hcd.telecomassistant.controller.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatHistory {

    private final List<ChatMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public List<ChatMessage> messages() {
        synchronized (messages) {
            return List.copyOf(messages);
        }
    }

    public void addMessage(ChatMessage.Type type, String content) {
        messages.add(new ChatMessage(type, content));
    }

    public void clear() {
        messages.clear();
    }
}

