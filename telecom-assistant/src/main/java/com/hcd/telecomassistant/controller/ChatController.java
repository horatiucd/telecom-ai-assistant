package com.hcd.telecomassistant.controller;

import com.hcd.telecomassistant.service.ChatAssistant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatAssistant assistant;

    public ChatController(ChatAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("messages", assistant.conversationMessages());
        model.addAttribute("tokens", assistant.lastRequestTotalTokens());
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("question") String question) {
        if (!StringUtils.hasText(question)) {
            return "redirect:/";
        }

        assistant.storeUserMessage(question);
        var answer = assistant.ask(question);
        assistant.storeAssistantMessage(answer);

        return "redirect:/";
    }

    @PostMapping("/chat/clear")
    public String clear() {
        assistant.clearConversationMessages();
        return "redirect:/";
    }
}
