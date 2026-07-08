package com.example.kartik.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CustomUrlRequest(@NotBlank(message = "URL cannot be empty")
                               @URL(message = "Invalid URL format")String longUrl,
                               @NotBlank(message = "Custom alias cannot be empty") String customAlias, Integer expiryDays) {
}
