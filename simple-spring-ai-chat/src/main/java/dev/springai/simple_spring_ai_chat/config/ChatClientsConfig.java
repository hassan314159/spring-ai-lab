package dev.springai.simple_spring_ai_chat.config;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class ChatClientsConfig {

    @Bean
    @Qualifier("textClient")
    ChatClient textClient(ChatClient.Builder baseBuilder) {
        return baseBuilder.
                defaultOptions(ChatOptions
                        .builder()
                        .model("mistral")
                        .temperature(0.3d)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("visionClient")
    ChatClient visionClient(ChatClient.Builder baseBuilder) {
        return baseBuilder
                .defaultOptions(ChatOptions.builder()
                        .model("llava")   // multimodal model you pulled
                        .temperature(0.2d)
                        .build())
                .build();
    }
}
