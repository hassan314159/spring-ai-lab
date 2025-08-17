package dev.springai.simple_summarizer.controller;

import dev.springai.simple_summarizer.service.SummarizerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/summarize")
public class SummarizerController {

    private final SummarizerService summarizerService;

    public SummarizerController(SummarizerService summarizerService) {
        this.summarizerService = summarizerService;
    }

    // Accept Flux<String> in body, return Flux<String>
    @PostMapping(consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> summarize(@RequestBody Flux<String> texts) {
        return summarizerService.summarize(texts);
    }
}
