package com.hcd.telecomassistant.advisor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.tokenizer.JTokkitTokenCountEstimator;
import org.springframework.ai.tokenizer.TokenCountEstimator;

import java.util.List;

public class TokenUsageAdvisor implements BaseAdvisor {

    private static final Logger log = LoggerFactory.getLogger(TokenUsageAdvisor.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModule(new JavaTimeModule());

    private final int order;
    private final TokenCountEstimator tokenCountEstimator;

    private TokenUsageAdvisor(int order) {
        this.order = order;
        tokenCountEstimator = new JTokkitTokenCountEstimator();
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest,
                                    AdvisorChain advisorChain) {
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
        log.debug("Request: {} messages ~ {} tokens.", messages.size(), tokenCount);

        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse,
                                    AdvisorChain advisorChain) {
        try {
            var json = MAPPER.writeValueAsString(chatClientResponse.chatResponse());
            Integer promptTokens = JsonPath.read(json, "$.metadata.usage.promptTokens");
            Integer completionTokens = JsonPath.read(json, "$.metadata.usage.completionTokens");
            Integer totalTokens = JsonPath.read(json, "$.metadata.usage.totalTokens");
            log.debug("Response: promptTokens = {}, completionTokens = {}, totalTokens = {}.",
                    promptTokens, completionTokens, totalTokens);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int order = Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER;

        private Builder() {}

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public TokenUsageAdvisor build() {
            return new TokenUsageAdvisor(order);
        }
    }
}
