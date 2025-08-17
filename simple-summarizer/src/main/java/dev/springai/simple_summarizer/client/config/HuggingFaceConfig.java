package dev.springai.simple_summarizer.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "huggingface")
public record HuggingFaceConfig(String apiUrl, String apiKey) {
}