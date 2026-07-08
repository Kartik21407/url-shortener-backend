package com.example.kartik.controller;

import com.example.kartik.dto.Analytics;
import com.example.kartik.dto.CustomUrlRequest;
import com.example.kartik.dto.UrlRequest;
import com.example.kartik.dto.UrlResponse;
import com.example.kartik.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @GetMapping("/api/test")
    public String testServer() {
        return "Server is running perfectly";
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody UrlRequest request, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(urlService.shortenUrl(request, email));
    }

    @GetMapping("/api/my-links")
    public ResponseEntity<?> getUserLinks(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(urlService.getMyLinks(email));
    }

    @PostMapping("/api/custom")
    public ResponseEntity<UrlResponse> createCustomUrl(@Valid @RequestBody CustomUrlRequest request) {
        UrlResponse response = urlService.createCustomUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortAlias}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortAlias) {
        String originalUrl = urlService.getOriginalUrl(shortAlias);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @DeleteMapping("/api/delete/{shortAlias}")
    public ResponseEntity<String> deleteUrl(@PathVariable String shortAlias) {
        urlService.deleteUrl(shortAlias);
        return ResponseEntity.ok("URL and its associated clicks are successfully deleted");
    }

    @GetMapping("/api/analytics/{shortAlias}")
    public ResponseEntity<Analytics> getAnalytics(@PathVariable String shortAlias) {
        Analytics analytics = urlService.getLinkAnalytics(shortAlias);
        return ResponseEntity.ok(analytics);
    }
}