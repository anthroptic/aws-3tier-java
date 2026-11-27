package com.example.threetier;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Map<String, String> welcome() {
        return Map.of(
            "service", "aws-3tier-java",
            "status", "running"
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/api/messages")
    public List<Message> messages() {
        return repository.findAll();
    }

    @PostMapping("/api/messages")
    public Message createMessage(@Valid @RequestBody Message message) {
        return repository.save(message);
    }
}
