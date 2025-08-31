package dev.springai.simple_spring_rag.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat-rag")
public class SimpleRagChatController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRagChatController.class);


    private ChatClient chatClient;
    private VectorStore vectorStore;
    private final PromptTemplate systemTemplate;

    SimpleRagChatController(ChatClient chatClient, VectorStore vectorStore,
                            PromptTemplate  systemTemplate){
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.systemTemplate = systemTemplate;
    }


    @PostMapping("/load")
    public Map<String, Object> load(@RequestParam("url") String url,
                                    @RequestParam(name = "onlyCode", defaultValue = "false") boolean onlyCode) throws IOException {
        Document bulk = fetchAsBulkDocument(url, onlyCode);
        String text = bulk.getText(); // 1.0.x API
        if (!StringUtils.hasText(text)) {
            return Map.of("url", url, "onlyCode", onlyCode, "message", "No extractable text found");
        }

        var splitter = TokenTextSplitter.builder()
                .withChunkSize(800)           // tokens per chunk
                .withMinChunkSizeChars(200)   // avoid tiny crumbs
                .withMinChunkLengthToEmbed(5) // skip super-short chunks
                .withKeepSeparator(false)
                .build();

        var chunks = splitter.split(List.of(bulk));
        vectorStore.add(chunks);

        return Map.of(
                "url", url,
                "onlyCode", onlyCode,
                "chars_in_bulk", text.length(),
                "chunks_added", chunks.size(),
                "time", Instant.now().toString()
        );
    }

    @PostMapping("/ask")
    public Map<String, Object> ask(@RequestParam("q") String q) {

        // Some Spring AI versions only have similaritySearch(String)
        List<Document> hits = vectorStore.similaritySearch(q);
        if (hits == null) hits = List.of();

        logger.info("Similar Texts: {}", hits);


        String context = hits.stream()
                .map(d -> Optional.ofNullable(d.getText()).orElse(""))
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("\n\n---\n\n"));


        String sys = systemTemplate.render(Map.of("context", context));

        String answer = chatClient.prompt()
                .system(sys)
                .user(q)
                .call()
                .content();

        List<Map<String, Object>> sources = hits.stream().map(d -> Map.of(
                "url", d.getMetadata().getOrDefault("url", ""),
                "chars", Optional.ofNullable(d.getText()).map(String::length).orElse(0)
        )).toList();

        return Map.of("answer", answer, "sources", sources);
    }




    private Document fetchAsBulkDocument(String url, boolean onlyCode) throws IOException {
        var page = Jsoup.connect(url).userAgent("simple-spring-ai-rag").get();

        // Prefer a main/article region; fall back to <body>
        Element root = Optional.ofNullable(page.selectFirst("#mw-content-text .mw-parser-output"))
                .orElseGet(() -> Optional.ofNullable(page.selectFirst("article")).orElse(page.body()));

        final String content;
        if (onlyCode) {
            // collect all code/pre blocks as a single blob
            Elements codes = root.select("pre, code");
            content = codes.stream()
                    .map(Element::text)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining("\n\n"));
        } else {
            // bulk text of the main area
            content = root.text();
        }

        return new Document(
                content == null ? "" : content.trim(),
                Map.of("url", url, "onlyCode", onlyCode)
        );
    }

}
