package com.hcd.telecomassistant.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.ToolCallingChatOptions;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO 2: Add a new advisor to log the messages in a particula manner
public class MessageLoggerAdvisor implements BaseAdvisor {

    private static final Logger log = LoggerFactory.getLogger(MessageLoggerAdvisor.class);

    private final int order;

    public MessageLoggerAdvisor(int order) {
        this.order = order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        Prompt prompt = chatClientRequest.prompt();

        Object tools = "N/A";
        if (prompt.getOptions() instanceof ToolCallingChatOptions toolOptions) {
            tools = toolOptions.getToolCallbacks().stream()
                    .map(callback -> callback.getToolDefinition().name())
                    .toList();
        }
        log.info("Tools: {}", tools);

        String messages = prompt.getInstructions().stream()
                .map(ModelOptionsUtils::toJsonString)
                .collect(Collectors.joining("\n"));
        log.info("Request:\n{}", messages);

        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        String messages = Optional.ofNullable(chatClientResponse.chatResponse())
                .map(ChatResponse::getResults)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(gen -> ModelOptionsUtils.toJsonString(gen.getOutput()))
                .collect(Collectors.joining("\n"));

        log.info("Response:\n{}", messages);

        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
