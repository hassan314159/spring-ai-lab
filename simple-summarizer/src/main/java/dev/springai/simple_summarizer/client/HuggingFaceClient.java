package dev.springai.simple_summarizer.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.springai.simple_summarizer.client.config.HuggingFaceConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HuggingFaceClient {

    private static final Map<String, Object> DEFAULT_PARAMS = Map.of(
            "do_sample", false,
            "num_return_sequences", 1,
            "min_length", 20,
            "max_length", 120
    );

    private WebClient webClient;

    public HuggingFaceClient(WebClient.Builder webClientBuilder, HuggingFaceConfig huggingFaceConfig) {
        this.webClient =  webClientBuilder
                .baseUrl(huggingFaceConfig.apiUrl())
                .defaultHeader("Authorization", "Bearer " + huggingFaceConfig.apiKey())
                .build();
    }


    public Flux<String> summarizeTexts(Flux<String> texts) {
        return texts.flatMap(this::summarizeSingleText);
    }

    public Mono<String> summarizeSingleText(String text){
        return webClient.post()
                .bodyValue(Map.of("inputs", text, "parameters", DEFAULT_PARAMS))
                .retrieve()
                .bodyToMono(String.class);
    }

}
