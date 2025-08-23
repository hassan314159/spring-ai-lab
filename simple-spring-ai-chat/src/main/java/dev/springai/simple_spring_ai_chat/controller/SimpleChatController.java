package dev.springai.simple_spring_ai_chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class SimpleChatController {

    private ChatClient chatClient;

    SimpleChatController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam("q") String q) {
        return chatClient.prompt()
                .user(q)
                .call()
                .content();
    }



}
