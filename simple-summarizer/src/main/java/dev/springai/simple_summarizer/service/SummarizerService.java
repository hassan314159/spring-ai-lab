package dev.springai.simple_summarizer.service;

import dev.springai.simple_summarizer.client.HuggingFaceClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SummarizerService {

    HuggingFaceClient huggingFaceClient;

    public SummarizerService(HuggingFaceClient huggingFaceClient){
        this.huggingFaceClient = huggingFaceClient;
    }

    public Flux<String> summarize(Flux<String> texts){
        return  huggingFaceClient.summarizeTexts(texts);
    }
}
