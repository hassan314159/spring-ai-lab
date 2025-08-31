package dev.springai.simple_spring_rag.config;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;

@Configuration
public class ChatConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder baseBuilder) {
        return baseBuilder.
                defaultOptions(ChatOptions
                        .builder()
                        .model("mistral")
                        .temperature(0.3d)
                        .build())
                .build();
    }


    // In-memory vector store for the demo (swap with PgVector/Milvus/etc. in prod)
    @Bean
    SimpleVectorStore.SimpleVectorStoreBuilder simpleVectorStoreBuilder(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel);
    }

    @Bean
    VectorStore vectorStore(SimpleVectorStore.SimpleVectorStoreBuilder builder) {
        return builder.build();
    }

    @Bean
    PromptTemplate systemPromptTemplate(@Value("classpath:prompts/system-wiki-assistant.st") Resource systemPrompt) {
        return PromptTemplate.builder()
                .resource(systemPrompt)   // loads your file text.st
                .build();
    }
}
