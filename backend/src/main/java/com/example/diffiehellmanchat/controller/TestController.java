package com.example.diffiehellmanchat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Diffie-Hellman Chat Backend is running!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
