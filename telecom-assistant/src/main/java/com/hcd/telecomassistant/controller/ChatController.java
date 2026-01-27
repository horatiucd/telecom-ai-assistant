package com.hcd.telecomassistant.controller;

import com.hcd.telecomassistant.service.ChatAssistant;
import com.hcd.telecomassistant.service.ChatMessageHistory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatAssistant assistant;
    private final ChatMessageHistory history;

    public ChatController(ChatAssistant assistant,
                          ChatMessageHistory history) {
        this.assistant = assistant;
        this.history = history;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("messages", history.messages());
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("question") String question) {
        if (!StringUtils.hasText(question)) {
            return "redirect:/";
        }

        history.userMessage(question);
        var answer = assistant.call(question);
        history.assistantMessage(answer);
        return "redirect:/";
    }

    @PostMapping("/chat/clear")
    public String clear() {
        history.clear();
        return "redirect:/";
    }
}
