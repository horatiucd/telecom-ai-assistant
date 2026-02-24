package com.hcd.telecomassistant.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tokenizer.JTokkitTokenCountEstimator;
import org.springframework.ai.tokenizer.TokenCountEstimator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenUsageAdvisor implements BaseAdvisor {

    private static final Logger log = LoggerFactory.getLogger(TokenUsageAdvisor.class);

    private final AtomicInteger promptTokenCount = new AtomicInteger(0);
    private final AtomicInteger completionTokenCount = new AtomicInteger(0);
    private final AtomicInteger totalTokenCount = new AtomicInteger(0);

    private final int order;
    private final TokenCountEstimator tokenCountEstimator;

    public TokenUsageAdvisor(int order) {
        this.order = order;
        tokenCountEstimator = new JTokkitTokenCountEstimator();
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        List<Message> messages = chatClientRequest.prompt().getInstructions();

        int tokenCount = messages.stream()
                .mapToInt(msg -> {
                    var text = switch (msg) {
                        case UserMessage userMsg -> userMsg.getText();
                        case AssistantMessage assistantMsg -> assistantMsg.getText();
                        case SystemMessage systemMsg -> systemMsg.getText();
                        default -> "";
                    };
                    return tokenCountEstimator.estimate(text);
                })
                .sum();
        log.debug("Request: {} messages ~ {} estimated tokens.", messages.size(), tokenCount);

        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        Optional.ofNullable(chatClientResponse.chatResponse())
                .map(ChatResponse::getMetadata)
                .map(ChatResponseMetadata::getUsage)
                .ifPresent(usage -> {
                    int currentPrompt = usage.getPromptTokens();
                    int currentCompletion = usage.getCompletionTokens();
                    int currentTotal = usage.getTotalTokens();

                    log.info("Current tokens - \nPrompt: {} Completion: {} Total: {}",
                            currentPrompt, currentCompletion, currentTotal);

                    int accPrompt = promptTokenCount.addAndGet(currentPrompt);
                    int accCompletion = completionTokenCount.addAndGet(currentCompletion);
                    int accTotal = totalTokenCount.addAndGet(currentTotal);

                    log.info("Accumulated tokens - \nPrompt: {} Completion: {} Total: {}",
                            accPrompt, accCompletion, accTotal);
                });

        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public int totalTokens() {
        return totalTokenCount.get();
    }

    public void clearUsage() {
        promptTokenCount.set(0);
        completionTokenCount.set(0);
        totalTokenCount.set(0);
    }
}
