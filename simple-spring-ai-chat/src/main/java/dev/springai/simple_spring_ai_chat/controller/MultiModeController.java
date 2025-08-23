package dev.springai.simple_spring_ai_chat.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class MultiModeController {

    private final ChatClient textClient;
    private final ChatClient visionClient;

    public MultiModeController(@Qualifier("textClient") ChatClient textClient,
                               @Qualifier("visionClient") ChatClient visionClient) {
        this.textClient = textClient;
        this.visionClient = visionClient;
    }

    // Text-only chat (Mistral)
    @PostMapping(path = "/chat/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> chat(@RequestParam("q") String question) {
        String content = textClient
                .prompt()
                .user(question)
                .call()
                .content();
        return Map.of("model", "mistral", "answer", content);
    }

    // Image + (optional) text (LLaVA)
    @PostMapping(
            path = "/vision/describe",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> describe(
            @RequestPart("image") MultipartFile image,
            @RequestPart(name = "prompt", required = false) String extraPrompt) {

        MimeType mime = image.getContentType() != null
                ? MimeType.valueOf(image.getContentType())
                : MimeTypeUtils.IMAGE_PNG;

        final ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(image.getBytes()) {
                @Override public String getFilename() {
                    return image.getOriginalFilename() != null ? image.getOriginalFilename() : "upload";
                }
            };
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read uploaded image", e);
        }

        String prompt = (extraPrompt == null || extraPrompt.isBlank())
                ? "You are an image captioner. Describe this image clearly and concisely."
                : extraPrompt;

        String description = visionClient
                .prompt()
                .user(u -> u
                        .text(prompt)
                        .media(new Media(mime, resource))
                )
                .call()
                .content();

        return Map.of("model", "llava", "description", description);
    }
}
