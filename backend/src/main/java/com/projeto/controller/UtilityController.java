package com.projeto.controller;

import com.projeto.service.OpenGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/utils")
@RequiredArgsConstructor
public class UtilityController {

    private final OpenGraphService openGraphService;

    @GetMapping("/extract-image")
    public ResponseEntity<Map<String, String>> extractImage(@RequestParam String url) {
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String imageUrl = openGraphService.extractImage(url);

        if (imageUrl != null) {
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}