package com.example.kartik.service;

import com.example.kartik.dto.Analytics;
import com.example.kartik.dto.CustomUrlRequest;
import com.example.kartik.dto.UrlRequest;
import com.example.kartik.dto.UrlResponse;
import com.example.kartik.model.UrlClick;
import com.example.kartik.model.UrlMapping;
import com.example.kartik.model.User;
import com.example.kartik.repo.UrlClickRepository;
import com.example.kartik.repo.UrlRepository;
import com.example.kartik.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlClickRepository urlClickRepository;
    private final UserRepository userRepository;

    public UrlResponse shortenUrl(UrlRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(request.longUrl());
        mapping.setCreatedAt(LocalDateTime.now().withNano(0));

        if (request.expiryDays() != null) {
            mapping.setExpiresAt(LocalDateTime.now().withNano(0).plusDays(request.expiryDays()));
        }

        String shortAlias = UUID.randomUUID().toString().substring(0, 6);
        mapping.setShortAlias(shortAlias);

        mapping.setUser(user);

        urlRepository.save(mapping);

        return new UrlResponse(mapping.getOriginalUrl(), shortAlias);
    }

    public List<UrlMapping> getMyLinks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return urlRepository.findByUser(user);
    }

    public UrlResponse createCustomUrl(CustomUrlRequest request) {
        if (urlRepository.existsByShortAlias(request.customAlias())) {
            throw new RuntimeException("This custom alias is already in use.");
        }

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(request.longUrl());
        mapping.setShortAlias(request.customAlias());
        mapping.setCreatedAt(LocalDateTime.now().withNano(0));

        if (request.expiryDays() != null) {
            mapping.setExpiresAt(LocalDateTime.now().withNano(0).plusDays(request.expiryDays()));
        }

        urlRepository.save(mapping);

        return new UrlResponse(mapping.getOriginalUrl(), request.customAlias());
    }

    public String getOriginalUrl(String shortAlias) {
        UrlMapping mapping = urlRepository.findByShortAlias(shortAlias)
                .orElseThrow(() -> new RuntimeException("URL not found."));

        UrlClick click = new UrlClick(mapping, LocalDateTime.now().withNano(0));
        urlClickRepository.save(click);

        return mapping.getOriginalUrl();
    }

    @Transactional
    public void deleteUrl(String shortAlias) {
        UrlMapping mapping = urlRepository.findByShortAlias(shortAlias)
                .orElseThrow(() -> new RuntimeException("URL not found."));
        urlClickRepository.deleteByUrlMapping(mapping);
        urlRepository.delete(mapping);
    }

    public Analytics getLinkAnalytics(String shortAlias) {
        UrlMapping mapping = urlRepository.findByShortAlias(shortAlias)
                .orElseThrow(() -> new RuntimeException("URL not found."));

        long clickCount = urlClickRepository.countByUrlMappingId(mapping.getId());
        long totalLinks = urlRepository.count();

        return new Analytics(clickCount, totalLinks);
    }
}